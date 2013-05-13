package com.example.heartdroid.workshop;

import android.text.TextUtils;

public class SimpleMessageParser {

	static final String THERMOSTAT_SETTINGS = "thermostat_settings";
	
	public float getTemperature(String message){
		int indexStart = TextUtils.indexOf(message, THERMOSTAT_SETTINGS)+THERMOSTAT_SETTINGS.length()+1;// 1 for comma
		int indexEnd = TextUtils.indexOf(message, "]" ,indexStart);
		String temp = TextUtils.substring(message, indexStart, indexEnd);
		float result = Float.parseFloat(temp);
		return result;
	}

}
