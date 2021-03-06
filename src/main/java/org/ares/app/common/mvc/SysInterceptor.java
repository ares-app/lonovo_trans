package org.ares.app.common.mvc;

import static org.ares.app.common.cfg.Params.CLIENT_REQUEST_IP;
import static org.ares.app.common.cfg.Params.ERR_MSG_REQUEST_ADDR;
import static org.ares.app.common.cfg.Params.KEY_OF_AUTH_USER;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ares.app.common.api.verify.ApiVerify;
import org.ares.app.common.exception.SignException;
import org.ares.app.common.mvc.filter.DemoRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SysInterceptor extends HandlerInterceptorAdapter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		ObjectMapper maper = new ObjectMapper();
		ServletRequest req= new DemoRequestWrapper((HttpServletRequest)request);
		Map m=null;
		
		m=new HashMap();
		String ip=req.getRemoteAddr();
		m.put(CLIENT_REQUEST_IP, ip);
		log.info("request ip is:"+ip);
		if(!multiIpVerify.verify(m)&&check_ip)
			throw new RuntimeException(ERR_MSG_REQUEST_ADDR);
		
		if(req.getInputStream().available()!=0){
			m=maper.readValue(req.getInputStream(), Map.class);
			log.debug("preHandle: "+m);
		}
		if(namepassVerify.verify(m)||namepassVerify.verify(request.getParameterMap())){
			log.debug("first verify is ok");
			return super.preHandle(request, response, handler);
		}else{
			log.info("no parameter:"+KEY_OF_AUTH_USER);
			throw new SignException();
		}
	}
	
	boolean check_ip=true;
	@Resource ApiVerify multiIpVerify;
	//@Resource ApiVerify noIpVerify;
	@Resource ApiVerify namepassVerify;
	
	final static Logger log = LoggerFactory.getLogger(SysInterceptor.class);
	
}
