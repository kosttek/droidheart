package com.example.heartdroid.services;

import android.os.AsyncTask;
import android.util.Pair;

import com.example.heartdroid.services.ConnectionService.ResultListener;

public class SendRequestTask extends AsyncTask<Object , Void, Pair<Boolean,String>> {
	
	ConnectionService connectionService;
	ResultListener resultListener;
	
	public SendRequestTask(ConnectionService connectionService/*,ConnectionListener listener*/) {
//		this.connectionListener = listener;
		this.connectionService = connectionService;
	}
	
	
	
	@Override
	protected Pair<Boolean,String> doInBackground(Object... params) {
		
		this.resultListener = (ResultListener)params[0] ;
		String message = (String)params[1];
		
		String resultRequest ;
		Pair<Boolean,String> result;
		
		try{
			resultRequest = connectionService.send(message);
		}catch(Exception e){
			e.printStackTrace();
			result = Pair.create(new Boolean(false), null);
			return result;
		}
		result = Pair.create(new Boolean(true), resultRequest);
		return result;
	}
	
	

	@Override
	protected void onPostExecute(Pair<Boolean,String> result) {
		super.onPostExecute(result);
		if(result.first){
			resultListener.onSuccess(result.second);
		}else{
			resultListener.onFailure();
		}
	}
	
	

}


