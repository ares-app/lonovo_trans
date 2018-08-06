package org.ares.app.common.handle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AjaxFormFailedHandle extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,AuthenticationException exception) throws IOException, ServletException {
		if(isAjaxRequest(request)) {
			response.getWriter().print(LoginJson(false,null));
			response.getWriter().flush();
		}else
			super.onAuthenticationFailure(request, response, exception);
	}
	
	boolean isAjaxRequest(HttpServletRequest request) {
		boolean r=false;
		String requestType = request.getHeader("X-Requested-With");
		log.info("requestType  is:"+requestType);
		if(requestType!=null)
			r=true;
		return r;
	}
	
	String LoginJson(boolean success,String userid){
		Map<String,Object> m=new HashMap<>();
		Integer code=new Integer(999);
		if(success){
			code=new Integer(1);
			m.put("data", userid);
		}
		m.put("code", code);
		String r=null;
		try {
			r = mapper.writeValueAsString(m);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	final Logger log = LoggerFactory.getLogger(this.getClass());
	ObjectMapper mapper = new ObjectMapper();
}
