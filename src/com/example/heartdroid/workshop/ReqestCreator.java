package com.example.heartdroid.workshop;

import java.util.Calendar;
import java.util.Date;

public class ReqestCreator {
	
	static public final String [] days = {"sun","mon","tue","wed","thr","fri","sat"};
	static public final String [] months = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
	
	
	public String temperatureRequest(){
		return temperatureRequest(new Date());
	}
	
	public String temperatureRequest(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String day = dayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
		int month = calendar.get(Calendar.MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		String request = "[model,run,'thermostat','user2',ddi,[ms,dt,th,os],[[day,"+day+"],[hour,"+hour+"],[month,"+month+"]]].";
		return request;
	}
	
	
	
	public String dayOfWeek(int calendarDay){		
		return days[calendarDay-1];
	}
	
}
