package com.example.heartdroid.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ConnectionService extends Service {

	
	
	IBinder mBinder = new ConnectionBinder();
	String hostname ;
	int port;
	

	

	private Socket echoSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class ConnectionBinder extends Binder {
		public ConnectionService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return ConnectionService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public void setHostnameAndPort(String hostname, int port){
		this.hostname = hostname;
		this.port = port;
	}
	
	public void startConnection(String hostname, int port,ConnectionListener listener) {
		setHostnameAndPort(hostname, port);
		StartConnectionTask startConnectionTask = new StartConnectionTask(this);
		startConnectionTask.execute(listener);
		
	}
	
	
	
	public void startConnection() throws UnknownHostException, IOException {
		try {
			closeConnection();
			
			setEchoSocket(new Socket(hostname, port));
			setOut(new PrintWriter(getEchoSocket().getOutputStream(), true));
			setIn(new BufferedReader(new InputStreamReader(
					getEchoSocket().getInputStream())));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + hostname);
			throw e;

		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection to:"
					+ hostname);
			throw e;
		}
	}

	public void closeConnection() {
		try {
			if(getOut()!=null ){
				getOut().close();
			}
			if(getIn() != null){
				getIn().close();
			}
			if(getEchoSocket() != null){
				getEchoSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		closeConnection();
	}
	
	public void send(String message , ResultListener listener){
		Object [] params = {listener,message};
		SendRequestTask sendRequestTask = new SendRequestTask(this);
		sendRequestTask.execute(params);
	}
	
	
	public String send(String message) {
		
		getOut().println(message);
		getOut().flush();

		try {
			return getIn().readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

	public Socket getEchoSocket() {
		return echoSocket;
	}

	public void setEchoSocket(Socket echoSocket) {
		this.echoSocket = echoSocket;
	}
	
	public interface ConnectionListener{
		public void onSuccess();
		public void onFailure();
	}
	
	public interface ResultListener{
		public void onSuccess(String message);
		public void onFailure();
	}

}
