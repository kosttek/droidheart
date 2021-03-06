package com.example.heartdroid.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.heartdroid.R;
import com.example.heartdroid.services.ConnectionService;
import com.example.heartdroid.services.ConnectionService.ConnectionBinder;
import com.example.heartdroid.services.ConnectionService.ConnectionListener;
import com.example.heartdroid.services.ConnectionService.ResultListener;
import com.example.heartdroid.workshop.ReqestCreator;
import com.example.heartdroid.workshop.SimpleMessageParser;

public class MainActivity extends Activity {

	ConnectionService mService;
	boolean mBound = false;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_request:
				goToPickDateActivity();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void goToPickDateActivity(){
		Intent intent = new Intent(this, PickDateActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to ConnectionService
		Intent intent = new Intent(this, ConnectionService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to ConnectionService, cast the IBinder and get
			// ConnectionService instance
			ConnectionBinder binder = (ConnectionBinder) service;
			mService = binder.getService();
			mBound = true;
			mService.startConnection("localhost", 8090, new ConnectionMainListener());
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};


	class ConnectionMainListener implements ConnectionListener {

		@Override
		public void onSuccess() {
			ReqestCreator reqestCreator = new ReqestCreator();
			String request = reqestCreator.temperatureRequest();
			
			mService.send(request, new ResultMainListener());

		}

		@Override
		public void onFailure() {
			textView.setText("couldn't connect");

		}

	}
	
	class ResultMainListener implements ResultListener{

		@Override
		public void onSuccess(String message) {
			// TODO Auto-generated method stub
			SimpleMessageParser simpleMessageParser = new SimpleMessageParser();
			textView.setText(simpleMessageParser.getTemperature(message)+" oC");
		}

		@Override
		public void onFailure() {
			// TODO Auto-generated method stub
			textView.setText("fail");
		}
		
	}
}
