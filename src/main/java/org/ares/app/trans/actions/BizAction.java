package org.ares.app.trans.actions;

import static org.ares.app.common.cfg.Params.ROLE_ADMIN_LABEL;
import static org.ares.app.common.cfg.Params.ROLE_AUSER_LABEL;
import static org.ares.app.common.cfg.Params.ROLE_NUSER_LABEL;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.ares.app.trans.daos.ParamDao;
import org.ares.app.trans.daos.UserDao;
import org.ares.app.trans.entities.SParam;
import org.ares.app.trans.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BizAction {
	
	@RequestMapping("/user/query_all")
	public String user_query(Model m){
		m.addAttribute("users", userdao.findAll());
		return "setup";
	}
	
	@RequestMapping("/user/set_adv")
	public String user_setadv(UserModel m){
		String username=m.getUsername();
		if(!StringUtils.isEmpty(username))
			userdao.setAdvUser(username);
		String url=URL_USER;
		return "redirect:"+url;
	}
	
	@RequestMapping("/user/set_nor")
	public String user_setnor(UserModel m){
		String username=m.getUsername();
		if(!StringUtils.isEmpty(username))
			userdao.setNorUser(username);
		String url=URL_USER;
		return "redirect:"+url;
	}
	
	@RequestMapping("/user/disable")
	public String user_dis(UserModel m){
		String username=m.getUsername();
		if(!StringUtils.isEmpty(username))
			userdao.disableUser(username);
		String url=URL_USER;
		return "redirect:"+url;
	}
	
	@RequestMapping("/param/setip")
	public String prm_setip(@NotNull String sand_ip){
		SParam p=prmdao.findOne("sand_ip");
		p.setVal(sand_ip);
		prmdao.save(p);
		String url=URL_SETIP;
		return "redirect:"+url;
	}
	
	//-----------------------------------------------------------------------------------------------
	@RequestMapping("/usermgr")
	public String usermgr(Model m){
		Map<String,String> roles=new HashMap<>();
		roles.put("admin", ROLE_ADMIN_LABEL);
		roles.put("adv_user", ROLE_AUSER_LABEL);
		roles.put("nor_user", ROLE_NUSER_LABEL);
		/*Sort sort = new Sort(Direction.ASC, "username");*/
		int curpage=0;
		int pagesize=10;
		Pageable page=new PageRequest(curpage,pagesize);
		m.addAttribute("users", userdao.queryAll(page));
		m.addAttribute("roles", roles);
		return "usermgr";
	}
	
	@RequestMapping("/sandset")
	public String sandset(Model m){
		m.addAttribute("sand_ip", prmdao.findOne("sand_ip").getVal());
		return "sandset";
	}
	
	@RequestMapping("/logo")
	public String logo(Model m){
		return "logo";
	}
	
	final static Logger log = LoggerFactory.getLogger(BizAction.class);
	final static String URL_USER="/usermgr";
	final static String URL_SETIP="/sandset";
	final static String URL_OLD="/";
	@Resource UserDao userdao;
	@Resource ParamDao prmdao;
}
