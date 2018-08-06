package org.ares.app.common.api.verify.impl;

import java.time.LocalDate;
import java.util.Map;

import org.ares.app.common.api.verify.ApiVerify;
import org.springframework.stereotype.Component;

@Component
public class DateVerify implements ApiVerify {

	@Override
	public boolean verify(Map<String, ?> param) {
		boolean r=false;
		String p=param.get("date")+"";
		if(LocalDate.parse(p).compareTo(LocalDate.now())<0)
			r=true;
		return r;
	}

}
