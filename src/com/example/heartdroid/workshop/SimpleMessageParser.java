package com.example.heartdroid.workshop;

import android.text.TextUtils;

public class SimpleMessageParser {

	static final String THERMOSTAT_SETTINGS = "thermostat_settings";

	public float getTemperature(String message)  {
			
				
			String temp;
			try {
				temp = getStringTemperature(message);
			
			float result = Float.parseFloat(temp);
			return result;
			} catch (NoStringContainsException e) {
				
				e.printStackTrace();
				return -1000;
			}
	}

	public String getStringTemperature(String message) throws NoStringContainsException{
		if (message.contains(THERMOSTAT_SETTINGS)) {
			int indexStart = TextUtils.indexOf(message, THERMOSTAT_SETTINGS)
					+ THERMOSTAT_SETTINGS.length() + 1;// 1 for comma
			int indexEnd = TextUtils.indexOf(message, "]", indexStart);
			String temp = TextUtils.substring(message, indexStart, indexEnd);
			return temp;
		}else{
			throw new NoStringContainsException();
		}
	}
	
	public String getStringTemperatureOutput(String message){
		try {
			return getStringTemperature(message);
		} catch (NoStringContainsException e) {
		return "no temperature setted ";
			
		}
	}
	
	class NoStringContainsException extends Exception{
		
	}
}
