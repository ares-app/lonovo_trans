package org.ares.app.common.danger;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.ares.app.common.api.verify.impl.DateVerify;
import org.ares.app.common.dog.HDog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SysCP {

	/**
	 * 如果返回true，则功能受限
	 * 如果返回false，则无限制
	 * 屏蔽狗，注释下列语句
	 * 	if(!cdk.hasDog()) {
			System.out.println("please check that dog!");
			return true;
		}
		屏蔽日期，修改p.put("date", "2038-04-15");中日期字符串
		命令打包命令目录包含pom.xml文件,mvn clean package
	 * @return
	 */
	public boolean danger(){
		boolean r=false;
		if(!cdk.hasDog()) {
			System.out.println("please check that dog!");
			return true;
		}
		Map<String,String> p=new HashMap<String,String>();
		p.put("date", "2038-04-15");
		r=dv.verify(p);
		return r;
	}
	
	final Logger log = LoggerFactory.getLogger(getClass());
	@Resource HDog cdk;
	@Resource DateVerify dv;
}
