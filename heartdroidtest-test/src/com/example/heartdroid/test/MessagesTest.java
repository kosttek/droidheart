package com.example.heartdroid.test;



import java.util.Calendar;
import java.util.Date;

import com.example.heartdroid.workshop.ReqestCreator;
import com.example.heartdroid.workshop.SimpleMessageParser;

import android.test.AndroidTestCase;
import android.util.Log;


public class MessagesTest  extends AndroidTestCase{

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}
	
	public void testGetTemperatureFromMessage(){
		String message = "[true,[[day,1.0],[hour,13.0],[month,3.0],[season,1.0],[today,1.0],[operation,1.0],[thermostat_settings,20.0]],[ms,2,dt,1,th,4,os,4]]";
		
		SimpleMessageParser simpleMessageParser = new SimpleMessageParser();
		float temperature = simpleMessageParser.getTemperature(message);
		assertEquals((float)20.0, temperature);
		
	}
	
	public void testCreateTemperatureRequest(){
		String day = "mon";
		int month = 3;
		int hour = 13;
		String requestExpected = "[model,run,'thermostat','user2',ddi,[ms,dt,th,os],[[day,"+day+"],[hour,"+hour+"],[month,"+month+"]]].";
		ReqestCreator reqestCreator = new ReqestCreator();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY,13);
		calendar.set(Calendar.MONTH,3);
		Date date = new Date(calendar.getTimeInMillis());
		
		String request = reqestCreator.temperatureRequest(date);
		assertEquals(requestExpected, request);
				
	}
	
	public void testDayOfWeek(){
		ReqestCreator reqestCreator = new ReqestCreator();
		assertEquals(reqestCreator.dayOfWeek(Calendar.MONDAY), "mon");
		assertEquals(reqestCreator.dayOfWeek(Calendar.TUESDAY), "tue");
		assertEquals(reqestCreator.dayOfWeek(Calendar.WEDNESDAY), "wed");
		assertEquals(reqestCreator.dayOfWeek(Calendar.THURSDAY), "thr");
		assertEquals(reqestCreator.dayOfWeek(Calendar.FRIDAY), "fri");
		assertEquals(reqestCreator.dayOfWeek(Calendar.SATURDAY), "sat");
		assertEquals(reqestCreator.dayOfWeek(Calendar.SUNDAY), "sun");
		
		
	}

}
