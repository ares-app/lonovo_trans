package org.ares.app.common.dog;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import Aladdin.Hasp;
import Aladdin.HaspStatus;

@Component
public class Dog1801 implements HDog {

	public boolean hasDog() {

		boolean r = false;
		long feature = 2;

		String vendorCode = "52RDn7zc4rCLnPbtKhAbht1rDJiyHGLMuJZMasbdmon6LHSKZIZUxPWDIeTG/D5R3F0lQ4SredAyKgtQ"
				+ "tPw2Os2FkhyrvMVz6pk/hZ8vvqEsS430RadifDKqUPW08SHb6krPC6Ms5B3FdqGm3JZzSlczgAs/9YEh"
				+ "0EuvRRxQkqfecfCknRFWoBlN52sEbos1C84BDSRUTVfUAIRAKFFjZX79v3WbcCNgs/cZMChA8vCcuNV4"
				+ "9ROavAqla8YECUhO7RMwAM0WzXPptjN1C/htXyhli+OAVn46XZNvr5kMHOQ142xqhDprqK1i3lv28zsk"
				+ "pm30+RHylWQ2qu9vcCHZNsBCr/QFKz/q10k1SGQenWydw+5tDNO72taOVQbClayEoSTZvCb++EmCsiY0"
				+ "w3hojAMFewMEbWsHUudPnOlAZ+IB0RBLqrrS1DmwEUlFUi+n+Et1aLCWeXQk15tg5eSvLMXHsN6j5gqn"
				+ "PxLxUmHESOFmWKrjzS/QVRabv+RrYp/Nqcyh5RBOL7F6mXO2JOL1QMkWLmS6V0kpFWYuzjJx/qtdFgoZ"
				+ "6aG9SanpFBw4o+4ukm8xIfQWiJlrxF5dx9OSzMrOkhlD2KhqkYvw97aIPntnMAshmoyk1quuhcSoCMbr"
				+ "27K1fc4NAlWRa4wDBVrQEGf+v8OrlNAW4CX2R0LDo+fcgKxDSFhEXMe2W80ovPhexB3fQEdTPzG/2JkE"
				+ "rSqc6UV4rPDbxJmkMhL0gQEw5fK0juPhMVtgYC3C9Wt0UuSXfyUI6q9ea/Nw1VI62W8Amvhicq8KZhCe"
				+ "OAAGRXV95E/Dsa34p+YaN5Q1390Hi38v4qqpEgZBRLtloGxRlU2X7toVCSZU/M8virS/QhRS+7MgL7Ds"
				+ "EXmU8Pd+KRC4sAh2WWBo6yp6uqsClxaAeAOBIYgtGY9XaeZdHJWJ9qqRib9HYvez/VeHi1rwTQBxhPHI"
				+ "5awfdEFvhwZMoDPUaxD8Jw==";

		Hasp hasp = new Hasp(feature);

		hasp.login(vendorCode);
		int status = hasp.getLastError();

		if (HaspStatus.HASP_STATUS_OK != status){
			//System.out.println("[error,dog status is]--"+status);
			return false;
		}
		byte[] membuffer = new byte[32];
		hasp.read(Hasp.HASP_FILEID_RW, 0, membuffer);
		hasp.decrypt(membuffer);
		String ck = new String(membuffer, Charset.forName("utf-8"));
		String ok=null;
		try {
			ok = new String(Base64.getDecoder().decode("YXJlc0AyMDE4X0xlbm92byFXZWxjb21lVHJhbnNBcHA="),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		r = ok.equals(ck);
		return r;
	}
	
	final Logger log = LoggerFactory.getLogger(this.getClass());
	
}
