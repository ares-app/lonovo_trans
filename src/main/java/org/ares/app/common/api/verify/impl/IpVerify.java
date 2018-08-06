package org.ares.app.common.api.verify.impl;

import static org.ares.app.common.cfg.Params.CLIENT_REQUEST_IP;

import java.util.Map;

import org.ares.app.common.api.verify.ApiVerify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("singleIpVerify")
public class IpVerify implements ApiVerify {

	@Override
	public boolean verify(Map<String, ?> param) {
		boolean r=false;
		String request_ip=param.get(CLIENT_REQUEST_IP)+"";
		r=client_ip.equals(request_ip);
		return r;
	}
	
	@Value("${client.ip}")
	String client_ip;

}
