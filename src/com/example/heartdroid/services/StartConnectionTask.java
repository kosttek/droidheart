package com.example.heartdroid.services;

import android.os.AsyncTask;

import com.example.heartdroid.services.ConnectionService.ConnectionListener;
import com.example.heartdroid.services.ConnectionService.ResultListener;

public class StartConnectionTask extends AsyncTask<Object , Void, Boolean> {
	
	ConnectionService connectionService;
	ConnectionListener connectionListener;
	
	public StartConnectionTask(ConnectionService connectionService/*,ConnectionListener listener*/) {
//		this.connectionListener = listener;
		this.connectionService = connectionService;
	}
	
	
	
	@Override
	protected Boolean doInBackground(Object... params) {
		this.connectionListener= (ConnectionListener)params[0] ;
		try{
		connectionService.startConnection();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(connectionListener == null)
			return;
		if(result){
			connectionListener.onSuccess();
		}else{
			connectionListener.onFailure();
		}
	}
	

}


