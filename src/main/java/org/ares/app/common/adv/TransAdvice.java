package org.ares.app.common.adv;

import static org.ares.app.common.cfg.Params.ERR_MSG_USER_NOT_AUTH;

import javax.annotation.Resource;

import org.ares.app.common.danger.SysCP;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransAdvice {
	
	@Around("execution (* org.ares.app.trans..*Dao*.*(..))")
	public Object serviceExceptionHandle(ProceedingJoinPoint pjp) throws RuntimeException{
		Object r=null;
		try {
			if(dg.danger())
				return null;
			r=pjp.proceed();
		} catch (Throwable e) {
			throw new RuntimeException(ERR_MSG_USER_NOT_AUTH);
		}
		return r;
	}
	
	@Resource SysCP dg;
}
