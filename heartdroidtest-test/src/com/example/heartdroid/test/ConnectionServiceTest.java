package com.example.heartdroid.test;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.mock.MockApplication;
import android.text.style.SuperscriptSpan;
import android.util.Log;

import com.example.heartdroid.services.ConnectionService;

/**
 * To test this case there should be running heart on 192.168.1.103 port 8090 
 *  I know it is not in unit test convention but f the system :) 
 * I will set to ignore all of that test
 * @author kosttek
 *
 */

public class ConnectionServiceTest extends ServiceTestCase<ConnectionService> {

	private static final String TAG = "ConnectionServiceTest";
	
	public ConnectionServiceTest() {
		super(ConnectionService.class);
	}
	
	public ConnectionServiceTest(Class<ConnectionService> serviceClass) {
		super( serviceClass);
	}
	
	

	public void setUp(){
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "start test");
		Intent intent = new Intent(getSystemContext(),ConnectionService.class);
		IBinder binder = bindService(intent);
		assertNotNull(binder);
	}

	final static String host = "192.168.1.103"; 
	public void testSending(){
		ConnectionService connectionService = getService();
		connectionService.startConnection(host, 8090);
		String result = connectionService.send("[model,getlist].");
		assertTrue(result != null);
		Log.i(TAG, result);
	}
	
	public void testConnection(){
		ConnectionService connectionService = getService();
		connectionService.startConnection(host, 8090);
		
		assertNotNull(connectionService.getEchoSocket());
		assertNotNull(connectionService.getIn());
		assertNotNull(connectionService.getOut());
		
	}
}
