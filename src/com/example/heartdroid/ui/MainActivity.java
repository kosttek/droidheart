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
import android.widget.TextView;

import com.example.heartdroid.R;
import com.example.heartdroid.services.ConnectionService;
import com.example.heartdroid.services.ConnectionService.ConnectionBinder;
import com.example.heartdroid.services.ConnectionService.ConnectionListener;
import com.example.heartdroid.services.ConnectionService.ResultListener;

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

	// class TelnetTask extends AsyncTask<Void, Void, String>{
	//
	// @Override
	// protected String doInBackground(Void... params) {
	// mService.startConnection("localhost", 8090);
	// return mService.send("[model,getlist].")+"";
	// }
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// textView.setText(result);
	// }
	// }
	class ConnectionMainListener implements ConnectionListener {

		@Override
		public void onSuccess() {
			mService.send("[model,getlist].", new ResultMainListener());

		}

		@Override
		public void onFailure() {
			// TODO Auto-generated method stub

		}

	}
	
	class ResultMainListener implements ResultListener{

		@Override
		public void onSuccess(String message) {
			// TODO Auto-generated method stub
			textView.setText(message);
		}

		@Override
		public void onFailure() {
			// TODO Auto-generated method stub
			textView.setText("fail");
		}
		
	}
}
