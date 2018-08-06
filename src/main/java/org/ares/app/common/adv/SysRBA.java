package org.ares.app.common.adv;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

public class SysRBA implements ResponseBodyAdvice<Map<String,Object>> {

	@Override
	public Map<String, Object> beforeBodyWrite(Map<String, Object> body, MethodParameter returnType, MediaType cntType,
			Class<? extends HttpMessageConverter<?>> conv, ServerHttpRequest requst, ServerHttpResponse response) {
		if(MediaType.APPLICATION_JSON.equals(cntType)){
			
		}
		return body;
	}

	@Override
	public boolean supports(MethodParameter mp, Class<? extends HttpMessageConverter<?>> conv) {
		return false;
	}

	

}
