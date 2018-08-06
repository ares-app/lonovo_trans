package org.ares.app.test;

import java.time.LocalDate;
import java.util.Base64;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAppTests {

	/*@Test
    public void encryptPwd() {
        String plaintext="root";  
        String ciphertext=jse.encrypt(plaintext);  
        System.out.println(plaintext + " : " + ciphertext);
        System.out.println(jse.decrypt(ciphertext));
    }
    
    @Resource
    StringEncryptor jse;*/
	
	@Test
	public void t() {
	}
	
    
    public static void main(String[] args){
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();  
        config.setAlgorithm("PBEWithMD5AndDES");  
        config.setPassword("ares@2017");  
        encryptor.setConfig(config);
    	String plaintext="trans";  
        String ciphertext=encryptor.encrypt(plaintext);  
        System.out.println(plaintext + " : " + ciphertext);
        System.out.println(encryptor.decrypt(ciphertext));
        
        System.out.println(Base64.getEncoder().encodeToString("2018-04-15".getBytes()));;
        
        String ed=new String(Base64.getDecoder().decode("MjAxOC0wNC0xNQ=="));
        System.out.println(ed);
		if(LocalDate.parse(ed).compareTo(LocalDate.now())<0)
			System.out.println("ok");
    }

}
