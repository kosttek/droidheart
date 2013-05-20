package com.example.heartdroid.ui;

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.heartdroid.R;
import com.example.heartdroid.services.ConnectionService;
import com.example.heartdroid.services.ConnectionService.ConnectionBinder;
import com.example.heartdroid.services.ConnectionService.ConnectionListener;
import com.example.heartdroid.services.ConnectionService.ResultListener;
import com.example.heartdroid.ui.PickDateActivity.TimeListner;
import com.example.heartdroid.workshop.ReqestCreator;
import com.example.heartdroid.workshop.RequestData;
import com.example.heartdroid.workshop.SimpleMessageParser;

public class PickDateActivity extends FragmentActivity {

	ConnectionService mService;
	boolean mBound = false;
	
	private RequestData requestData = new RequestData();
	
	private Spinner monthSpinner;
	private Spinner daySpinner;
	private Button  timeButton;
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_date_activity);
		setSpinners();
		setTimeButton();
		setTextView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// Bind to ConnectionService
		Intent intent = new Intent(this, ConnectionService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void setSpinners() {
		monthSpinner = (Spinner) findViewById(R.id.month_spinner);
		daySpinner   = (Spinner) findViewById(R.id.day_spinner);

		ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ReqestCreator.months);
		
		adapterMonths
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		monthSpinner.setAdapter(adapterMonths);

		ArrayAdapter<String> adapterDays = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ReqestCreator.days);
		
		adapterMonths
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		daySpinner.setAdapter(adapterDays);
		
		SpinerSelect spinerSelect = new SpinerSelect();
		
		monthSpinner.setOnItemSelectedListener(spinerSelect);
		daySpinner.setOnItemSelectedListener(spinerSelect);
		
		
	}
	
	private void setTimeButton(){
		timeButton = (Button) findViewById(R.id.time_button);
	} 
	private void setTextView(){
		text = (TextView) findViewById(R.id.temp);
	}

	/**
	 * on Click button
	 * @param v
	 */
	public void showTimeDialog(View v){
		TimePickerFragment timePicker = new TimePickerFragment(new TimeListner());
		timePicker.show(this.getSupportFragmentManager(), "timePicker");
	}
	
	/**
	 * on Click button
	 * @param v
	 */
	public void sendRequest(View v){
		//TODO
//		text.setText(requestData.month+"m "+requestData.day+"d "+requestData.hour+"h ");
//		ReqestCreator reqestCreator = new ReqestCreator();
		
		//TODO check if connected
//		mService.send(reqestCreator.temperatureRequest(requestData), new CustomRequestResultListener());
//		mService.send(reqestCreator.temperatureRequest(), new CustomRequestResultListener());
		
		mService.startConnection("localhost", 8090, new CustomConnectionResultListener());
	}	
	
	class TimeListner implements TimePickerDialog.OnTimeSetListener{
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			timeButton.setText(hourOfDay+"h "+minute+"min");
			requestData.hour = hourOfDay;
		}
		
	}
	
	class SpinerSelect implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> spinner, View arg1, int pos,
				long arg3) {
			
			if(spinner == monthSpinner){
				requestData.month = pos;
			}else if(spinner == daySpinner){
				requestData.day = pos+1;// TODO check and remember
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}	
	}
	
	class CustomConnectionResultListener implements ConnectionListener{

		@Override
		public void onSuccess() {
			ReqestCreator reqestCreator = new ReqestCreator();
			mService.send(reqestCreator.temperatureRequest(requestData), new CustomRequestResultListener());
			
		}

		@Override
		public void onFailure() {
			// TODO Auto-generated method stub
			text.setText("connection fail");
		}
		
	}
	
	class CustomRequestResultListener implements ResultListener{

		@Override
		public void onSuccess(String message) {
			
			SimpleMessageParser smp = new SimpleMessageParser();
			try{
			text.setText(smp.getStringTemperatureOutput(message)+"oC");
			}catch(NullPointerException e){
				e.printStackTrace();
				text.setText("ERROR: "+message);
			}		
		}

		@Override
		public void onFailure() {
			text.setText("request fail");
		}
		
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to ConnectionService, cast the IBinder and get
			// ConnectionService instance
			ConnectionBinder binder = (ConnectionBinder) service;
			mService = binder.getService();
			mBound = true;
		
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
	
}

 class TimePickerFragment extends DialogFragment  {

	private TimeListner timeListner;
	
	public TimePickerFragment(TimeListner timeListner) {
		this.timeListner= timeListner;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), timeListner, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

}