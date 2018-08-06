package org.ares.app.common.api.sign;

import static org.ares.app.common.cfg.Params.KEY_OF_AUTH_USER;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UnamePassSignVerify implements SignVerify {

	@Override
	public boolean verify(Map<String, ?> param) {
		if(param==null)
			return false;
		String k_username=KEY_OF_AUTH_USER;
		if(!param.containsKey(k_username)||StringUtils.isEmpty(param.get(k_username)))
			return false;
		return true;
	}

}
