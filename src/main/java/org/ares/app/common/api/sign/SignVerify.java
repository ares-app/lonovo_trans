package org.ares.app.common.api.sign;

import java.util.Map;

public interface SignVerify {

	boolean verify(Map<String,?> param);
	
}
