package org.ares.app.trans.actions;


import static org.ares.app.common.cfg.Params.CODE_FAILED_OF_SRV;
import static org.ares.app.common.cfg.Params.CODE_SUCCESS_OF_SRV;
import static org.ares.app.common.cfg.Params.ERR_MSG_ACCESS_SANDTABLE_FAILED;
import static org.ares.app.common.cfg.Params.ERR_MSG_REQUEST_BODY_EMPTY;
import static org.ares.app.common.cfg.Params.ERR_MSG_REQUEST_PARAM_LOSE;
import static org.ares.app.common.cfg.Params.ERR_MSG_SANDTABLE_NULL;
import static org.ares.app.common.cfg.Params.ERR_MSG_USERNAME_INVALID;
import static org.ares.app.common.cfg.Params.ERR_MSG_USER_NOT_APPROVE;
import static org.ares.app.common.cfg.Params.ERR_MSG_USER_NOT_AUTH;
import static org.ares.app.common.cfg.Params.KEY_ERRMSG_OF_SRV;
import static org.ares.app.common.cfg.Params.KEY_OF_AUTH_USER;
import static org.ares.app.common.cfg.Params.KEY_RESULT_OF_SAND;
import static org.ares.app.common.cfg.Params.KEY_RESULT_OF_SRV;
import static org.ares.app.common.cfg.Params.KEY_ROWS_OF_SRV;
import static org.ares.app.common.cfg.Params.MSG_FAILED_OF_SAND;
import static org.ares.app.common.cfg.Params.MSG_FAILED_OF_SRV;
import static org.ares.app.common.cfg.Params.MSG_SUCCESS_OF_SAND;
import static org.ares.app.common.cfg.Params.MSG_SUCCESS_OF_SRV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.ares.app.common.danger.SysCP;
import org.ares.app.trans.daos.CarDao;
import org.ares.app.trans.daos.CaruserDao;
import org.ares.app.trans.daos.LoginDao;
import org.ares.app.trans.daos.ParamDao;
import org.ares.app.trans.daos.PeccancyDao;
import org.ares.app.trans.daos.PectypeDao;
import org.ares.app.trans.daos.UserDao;
import org.ares.app.trans.entities.Loginuser;
import org.ares.app.trans.entities.SUser;
import org.ares.app.trans.models.UserModel;
import org.ares.app.trans.services.AuthService;
import org.ares.app.trans.services.TransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@SuppressWarnings({"unchecked","rawtypes"})
public class BizApiAction {
	
	@RequestMapping({"/api/user/register","/action/user/register"})
	public Map<String, Object> register(@RequestBody UserModel model) {
		Map<String, Object> r = new HashMap<>();
		SUser u = new SUser();
		BeanUtils.copyProperties(model, u);
		userdao.save(u);
		r.put("msg", "success");
		return r;
	}

	@RequestMapping({"/api/transport/{app_req_url}","/action/{app_req_url}"})
	public Map<String, Object> transport(@PathVariable String app_req_url,HttpServletRequest request){
		Map model=null;
		try{
			//ObjectMapper maper = new ObjectMapper();
			model=mapper.readValue(request.getInputStream(), Map.class);
			log.info("[app request params]--"+model);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(ERR_MSG_REQUEST_BODY_EMPTY);
		}
		String username=model.get(KEY_OF_AUTH_USER)+"";
		SUser u=userdao.findByUsername(username);
		if(u==null)
			throw new RuntimeException(ERR_MSG_USERNAME_INVALID);
		if(StringUtils.isEmpty(u.getRole()))
			throw new RuntimeException(ERR_MSG_USER_NOT_APPROVE);
		if(!auth.auth(u.getRole(), app_req_url))
			throw new RuntimeException(ERR_MSG_USER_NOT_AUTH);
		
		Map<String, Object> c=new HashMap<>();
		boolean query=false;
		switch(app_req_url){
		case"GetSUserInfo":
			c.put(KEY_ROWS_OF_SRV,caruserdao.findAll());
			query=true;
			break;
		case"GetCarPeccancy":
			String carnumber=""+model.get("carnumber");
			if(StringUtils.isEmpty(model.get("carnumber")))
				throw new RuntimeException("Param carnumber is must ");
			c.put(KEY_ROWS_OF_SRV, peccancydao.findByCarnumber(carnumber));
			query=true;
			break;
		case"GetAllCarPeccancy":
			c.put(KEY_ROWS_OF_SRV, peccancydao.findAll());
			query=true;
			break;
		case"GetCarInfo":
			c.put(KEY_ROWS_OF_SRV, cardao.findAll());
			query=true;
			break;
		case"GetPeccancyType":
			c.put(KEY_ROWS_OF_SRV, pecdao.findAll());
			query=true;
			break;
		case"GetWeather":
			c.put(KEY_ROWS_OF_SRV, trans.getWeatherOfNextDay().get(KEY_ROWS_OF_SRV));
			query=true;
			break;	
		case"user_login":
			String upwd=model.get("UserPwd")+"";
			if(StringUtils.isEmpty(model.get("UserPwd")))
				throw new RuntimeException("Param UserPwd is must ");
			Loginuser usr=logindao.findByUsernameAndUserpwd(username, upwd);
			if(usr==null)
				throw new RuntimeException("UserPwd is invalid");
			c.put("UserRole",usr.getUserrole());
			query=true;
			break;
		}
		if(query){
			if(dg.danger())
				c.remove(KEY_ROWS_OF_SRV);
			c.put(KEY_RESULT_OF_SRV, CODE_SUCCESS_OF_SRV);
			c.put(KEY_ERRMSG_OF_SRV, MSG_SUCCESS_OF_SRV);
			return c;
		}
		
		String ip = paramdao.findOne("sand_ip").getVal();
		String port = paramdao.findOne("sand_port").getVal();
		String real_sand_url=beforeSpecProcess(model,app_req_url);
		String url = "http://" + ip + ":" + port + prefix + real_sand_url;
		model.remove(KEY_OF_AUTH_USER);
		log.info("[to sand table url]--"+url);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity param = new HttpEntity(model, headers);
		log.info("[to sand table params]--"+model);
		Map<String, Object> r=null;
		try{
			r=getSandTableData(real_sand_url,url,param);
		}catch(Exception e){
			throw new RuntimeException(ERR_MSG_ACCESS_SANDTABLE_FAILED);
		}
		afterSpecProcess(r,app_req_url);
		buildResultOfSand(r);
		return r;
	}
	
	/**
	 * GetWeather
	 * 		old mode simu 6 day + sand current day
	 * 		cur mode simu 6 day + simu current day
	 * 		Disable GetWeather => disGetWeather
	 * 		in method [transport] add [case"GetWeather":]
	 */
	String beforeSpecProcess(Map m,String sand_url){
		String url=sand_url;
		if("SetTrafficLightConfig".equals(sand_url)){
			Object YellowTime=m.get("YellowTime");
			Object GreenTime=m.get("GreenTime");
			if(YellowTime==null||GreenTime==null)
				throw new RuntimeException(ERR_MSG_REQUEST_PARAM_LOSE);
			m.put("YellowTime", GreenTime);
			m.put("GreenTime", YellowTime);
		}
		if("disGetWeather".equals(sand_url)){
			url="GetSenseByName";
			m.put("SenseName", "temperature");
		}
		return url;
	}
	
	Map<String,Object> getSandTableData(String sand_url,String full_url,HttpEntity param) throws Exception{
		Map<String,Object> r=new HashMap<>();
		if("GetBusStationInfo".equals(sand_url)){
			String s = restTemplate.postForObject(full_url, param, String.class);
			log.info("[from sand table data]--"+s);
			List<Map> datas = mapper.readValue(s,new TypeReference<List<Map>>(){});
			r.put(KEY_ROWS_OF_SRV, datas);
			return r;
		}
		r = restTemplate.postForObject(full_url, param, Map.class);
		log.info("[from sand table data]--"+r);
		return r;
	}
	

	/**
	 * GetWeather
	 * 		old mode simu 6 day + sand current day
	 * 		cur mode simu 6 day + simu current day
	 * 		Disable GetWeather => disGetWeather 	
	 */
	void afterSpecProcess(Map m,String sand_url){
		if("disGetWeather".equals(sand_url)){
			if(m==null||!m.containsKey("temperature"))
				throw new RuntimeException(ERR_MSG_ACCESS_SANDTABLE_FAILED);
			m.put("WCurrent",m.get("temperature"));
			m.remove("temperature");
			m.put(KEY_ROWS_OF_SRV, trans.getWeatherOfNextDay().get(KEY_ROWS_OF_SRV));
		}
	}
	
	void buildResultOfSand(Map<String,Object> m){
		if(m==null)
			throw new RuntimeException(ERR_MSG_SANDTABLE_NULL);
		String r_sand=null;
		if(m.containsKey(KEY_RESULT_OF_SAND)){
			r_sand=m.get(KEY_RESULT_OF_SAND)+"";
			if(MSG_SUCCESS_OF_SAND.equals(r_sand)){
				m.put(KEY_RESULT_OF_SRV, CODE_SUCCESS_OF_SRV);
				m.put(KEY_ERRMSG_OF_SRV, MSG_SUCCESS_OF_SRV);
			}
			if(MSG_FAILED_OF_SAND.equals(r_sand)){
				m.put(KEY_RESULT_OF_SRV, CODE_FAILED_OF_SRV);
				m.put(KEY_ERRMSG_OF_SRV, MSG_FAILED_OF_SRV);
			}
			m.remove(KEY_RESULT_OF_SAND);
			return;
		}
		m.put(KEY_RESULT_OF_SRV, CODE_SUCCESS_OF_SRV);
		m.put(KEY_ERRMSG_OF_SRV, MSG_SUCCESS_OF_SRV);
	}

	final static String prefix = "/type/jason/action/";
	final static Logger log = LoggerFactory.getLogger(BizApiAction.class);
	final ObjectMapper mapper = new ObjectMapper();
	
	@Resource AuthService auth;
	@Resource UserDao userdao;
	@Resource ParamDao paramdao;
	@Resource PeccancyDao peccancydao;
	@Resource CarDao cardao;
	@Resource CaruserDao caruserdao;
	@Resource LoginDao logindao;
	@Resource PectypeDao pecdao;
	@Resource RestTemplate restTemplate;
	@Resource TransService trans;
	@Resource SysCP dg;
}
