package org.ares.app.common.cfg;

import org.ares.app.common.mvc.SysInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SysWebAppCfg extends WebMvcConfigurerAdapter {

	@Bean
	public SysInterceptor demoInterceptor(){
		return new SysInterceptor();
	}
	
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(demoInterceptor()).addPathPatterns(Params.OTHER_API_URL).addPathPatterns(Params.API_URL).excludePathPatterns(Params.REGISGER_URL);
		super.addInterceptors(registry);
	}

	final static Logger log = LoggerFactory.getLogger(SysWebAppCfg.class);
}
