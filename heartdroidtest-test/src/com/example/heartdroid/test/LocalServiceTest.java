package com.example.heartdroid.test;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.mock.MockApplication;
import android.text.style.SuperscriptSpan;
import android.util.Log;

import com.example.heartdroid.services.LocalService;

public class LocalServiceTest extends ServiceTestCase<LocalService> {

	private static final String TAG = "LocalServiceTest";
	
	public LocalServiceTest() {
		super(LocalService.class);
	}
	
	public LocalServiceTest(Class<LocalService> serviceClass) {
		super( serviceClass);// ?
	}
	
	

	public void setUp(){
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "start test");
		Intent intent = new Intent(getSystemContext(),LocalService.class);
		IBinder binder = bindService(intent);
		assertNotNull(binder);
	}

	public void testOne(){
		LocalService localService = getService();
		int staticNumber = localService.getStaticNumber();
		assertEquals(2, staticNumber);
	}
}
