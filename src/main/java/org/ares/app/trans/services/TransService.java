package org.ares.app.trans.services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ares.app.trans.daos.SandMonthTemperDao;
import org.ares.app.trans.entities.Smonthtemperature;
import org.springframework.stereotype.Service;

@Service
public class TransService {

	public Map<String,Object> getWeatherOfNextDay(){
		Map<String,Object> r=new HashMap<>();
		Map<String,Object> mcur=new HashMap<>();
		mcur.put("temperature", getTemperOfToday());
		mcur.put("date", sdf_date.format(new Date()));
		int days_length=6;
		List<Map<String,?>> rows=new ArrayList<>();
		rows.add(mcur);
		getTemperRangeOfAfterToday(days_length).stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("WData", e[0]);
			m.put("temperature", e[1]);
			rows.add(m);
		});
		r.put("ROWS_DETAIL", rows);
		return r;
	}
	
	List<String[]> getTemperRangeOfAfterToday(int days) {
		List<String[]> r=new ArrayList<>();
		int today_start=getTemperOfToday();
		LocalDate now=LocalDate.now();
		now=now.minusDays(2l);//[yesterday,yesterday+days-1]
		for(int i=0;i<days;i++) {
			String[] min_max=new String[2];
			int start=today_start+randomOfRange(-2,2);
			int end=start+randomOfRange(3,8);
			now=now.plusDays(1l);
			min_max[0]=now+"";
			min_max[1]=start+"~"+end;
			r.add(min_max);
		}
		return r;
	}
	
	/**
	 * 获取给定范围随机数
	 * @param start
	 * @param end
	 */
	int randomOfRange(int start,int end){
		if (start > end){
			int tmp=end;
			end=start;
			start=tmp;
		}
		return (int)(Math.random()*(end-start+1)+start);
	}
	
	/**
	 * 获取给定月份温度范围
	 * @param month
	 */
	int[] getTempeRangeByMonth(int month) {
		if(month>12 ||month<1)
			throw new RuntimeException("Month is invalid");
		Smonthtemperature e=sMonthTemperDao.getOne(month);
		return new int[] {e.getMint(),e.getMaxt()};
	}
	
	int getTemperOfMonthDay(int month,int day) {
		if(month>12||month<1||day<1||day>31)
			throw new RuntimeException("Month or Day is invalid");
		int[] cur_month_temper=getTempeRangeByMonth(month);
		int start=cur_month_temper[0];
		start+=randomOfRange(1,5);
		int end=cur_month_temper[1];
		float adjust=day/30f;
		if(8<=month)
			adjust=(1-adjust);
		int temperOfToday=(int)(adjust*(end-start))+start;
		return temperOfToday;
	}
	
	int getTemperOfToday() {
		LocalDate now=LocalDate.now();
		int month=now.getMonthValue();
		int day=now.getDayOfMonth();
		return getTemperOfMonthDay(month,day);
	}
	
	int[] getTemperRangeOfToday() {
		int[] r=new int[2];
		int start=getTemperOfToday();
		int end=start+randomOfRange(3,8);
		r[0]=start;
		r[1]=end;
		return r;
	}
	
	final SimpleDateFormat sdf_date=new SimpleDateFormat("yyyy-MM-dd");
	@Resource SandMonthTemperDao sMonthTemperDao;
	
}
