package org.ares.app.trans.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthService {

	public boolean auth(String role,String url){
		boolean r=false;
		if(StringUtils.isEmpty(role))
			return false;
		if(ROLE_ADMIN.equals(role))
			return true;
		if(m_auth.get(role)==null)
			return false;
		r=m_auth.get(role).contains(url);
		return r;
	}
	
	@PostConstruct
	public void init(){
		for(String a:nor_1){
			role_nuser.add(a);
			role_auser.add(a);
		}
		for(String a:nor_2){
			role_nuser.add(a);
			role_auser.add(a);
		}
		for(String a:adv_1)
			role_auser.add(a);
		for(String a:adv_2)
			role_auser.add(a);
		
		m_auth.put(ROLE_AUSER, role_auser);
		m_auth.put(ROLE_NUSER, role_nuser);
	}
	
	String[] adv_2={"get_trafficlight_now_status","set_trafficlight_now_status","get_trafficlight_config",
			"set_trafficlight_config","get_roadlight_status","set_roadlight_status","set_roadlight_control_mode",
			"set_light_sense_valve","get_all_car_peccancy","get_car_info","get_all_user_info"};
	
	String[] nor_2={"GetCarSpeed","set_car_move","get_car_account_balance","set_car_account_recharge","GetCarRechargeRecord",
			"get_all_sense","get_sense_by_name","GetLightSenseValve","get_bus_station_info","get_bus_capacity","get_road_status",
			"get_peccancy_type","get_car_peccancy","get_weather","user_login","get_weather_xxxxx"};
	
	String[] adv_1={"GetTrafficLightNowStatus","SetTrafficLightNowStatus","GetTrafficLightConfigAction",
			"SetTrafficLightConfig","GetRoadLightStatus","SetRoadLightStatusAction","SetRoadLightControlMode",
			"SetLightSenseValve","GetAllCarPeccancy","GetCarInfo","GetSUserInfo",
			"SetEtcRate","SetParkRate"};
	
	String[] nor_1={"GetCarSpeed","SetCarSpeed","SetCarMove","GetCarAccountBalance","SetCarAccountRecharge","GetCarRechargeRecord",
			"GetAllSense","GetSenseByName","GetLightSenseValve","GetBusStationInfo","GetBusCapacity","GetRoadStatus",
			"GetPeccancyType","GetCarPeccancy","GetWeather","user_login",
			"GetEtcRate","GetParkRate"};
	
	List<String> role_auser=new ArrayList<>();
	List<String> role_nuser=new ArrayList<>();
	Map<String,List<String>> m_auth=new HashMap<>();
	
	static final String ROLE_ADMIN="admin";
	static final String ROLE_AUSER="adv_user";
	static final String ROLE_NUSER="nor_user";
	
}
