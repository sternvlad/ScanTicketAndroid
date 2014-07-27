package com.scan.ticket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class GetResult extends Activity {
	Button scanAgain,validateBtn;
	TextView tvResult,tvInvalid,tvName,tvType;
	Boolean status=null;
	String result;
	Ticket ticket;
	public static boolean back;
	String barcode;
	ProgressDialog dlg;
	AppSettings sett;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_result);
		
		scanAgain = (Button) findViewById(R.id.home_btn);
		tvResult = (TextView) findViewById (R.id.ticket_id);
		sett = AppSettings.get(this);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		ticket = new Ticket();
		tvResult.setText("Ticket");
		tvInvalid = (TextView) findViewById(R.id.tv_invalid);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvType = (TextView) findViewById(R.id.tv_type);
		scanAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (GetResult.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		validateBtn = (Button) findViewById(R.id.validate_btn);
		validateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isOnline())
				{
					errorDialog(1);
				}
				else
				{
					new Validate().execute(null,null,null);
				}
			}
		});
		
		new Verify().execute(null,null,null);
	}
	
	public void onBackPressed ()
	{
		if (!back){
			Intent intent = new Intent (GetResult.this,CameraScan.class);
			startActivity(intent);
		}else{
			super.onBackPressed();
		}
	}

	
	private void errorDialog (int tag) { //functie pop up
	     final Dialog dialog=new Dialog(GetResult.this);
	     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     dialog.setContentView(R.layout.custom_dialog);
	     dialog.setCancelable(false);
	     Button btnYes = (Button) dialog.findViewById(R.id.button_yes);
	     Button btnNo = (Button) dialog.findViewById(R.id.button_no);
	     TextView tvError = (TextView) dialog.findViewById(R.id.error_textView);
	     
	     if (tag == 1){//not connected to internet
	    	 btnYes.setBackgroundResource(R.drawable.ok_btn);
	    	 btnYes.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
	    	 
	    	 btnNo.setVisibility(View.GONE);
	     }else if (tag == 2){//not a ticket
	    	 btnYes.setBackgroundResource(R.drawable.ok_btn);
	    	 tvError.setText("This is not a valid ticket!");
	    	 btnYes.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
					dialog.dismiss();
				}
			});
	    	 
	    	 btnNo.setVisibility(View.GONE);
	     }else if (tag == 3){//error validating,no status
	    	 btnYes.setBackgroundResource(R.drawable.ok_btn);
	    	 tvError.setText("There was an error validating your ticket,please try again!");
	    	 btnYes.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
	    	 
	    	 btnNo.setVisibility(View.GONE);
	     }else if (tag == 4){//status succes
	    	 btnYes.setBackgroundResource(R.drawable.ok_btn);
	    	 tvError.setText("Your ticket was validated with succes!");
	    	 btnYes.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
					dialog.dismiss();
				}
			});
	    	 
	    	 btnNo.setVisibility(View.GONE);
	     }else if (tag == 5){//status error
	    	 btnYes.setBackgroundResource(R.drawable.ok_btn);
	    	 tvError.setText("There was an error validating your ticket,please try again!");
	    	 btnYes.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
	    	 
	    	 btnNo.setVisibility(View.GONE);
	     }
	     dialog.show();
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	    	
	        return true;
	    }
	    return false;
	}

	private class Verify extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute(){  
			dlg = new ProgressDialog(GetResult.this);
			dlg.setMessage("verifying...");
			dlg.setCancelable(false);
			dlg.show();    
		}
		@Override
		protected Void doInBackground(Void... arg0) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
         
        /* login.php returns true if username and password is equal to saranga */
 
        HttpURLConnection connection;
        OutputStreamWriter request = null;

             URL url = null;   
             String response = null;         
             String parameters = "verify="+barcode;   

             try
             {
                 url = new URL(sett.getUrl());
                 connection = (HttpURLConnection) url.openConnection();
                 connection.setDoOutput(true);
                 connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                 connection.setRequestMethod("POST");

                 request = new OutputStreamWriter(connection.getOutputStream());
                 request.write(parameters);
                 request.flush();
                 request.close();            
                 String line = "";               
                 InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                 BufferedReader reader = new BufferedReader(isr);
                 StringBuilder sb = new StringBuilder();
                 while ((line = reader.readLine()) != null)
                 {
                     sb.append(line);
                 }
                 // Response from server after login process will be stored in response variable.                
                 response = sb.toString();
                 result = response;
                 // You can perform UI operations here
                 //Toast.makeText(this,"Message from Server: \n"+ response, 0).show(); 
                 JSONObject mainObject;
                 try {
          			mainObject = new JSONObject(result);
          			ticket.validate = mainObject.getString("validated");
          			ticket.type = mainObject.getString("type");
          			ticket.name = mainObject.getString("name");
          			ticket.valid = (mainObject.getString("valid").equals("true")? true:false);
          		}catch (Exception e){
          			ticket = null;
          		}
                 
                 isr.close();
                 reader.close();

             }
             catch(IOException e)
             {
                 dlg.dismiss();
             }
        return null;
    }
		
		@Override
		protected void onPostExecute(final Void unused){
			if (ticket != null)
			{
				if (ticket.type.equals("2"))
					tvType.setText("Discounted ticket");
				else tvType.setText("Normal ticket");
				tvName.setText(ticket.name);
				if (ticket.valid){
					tvInvalid.setVisibility(View.VISIBLE);
					validateBtn.setVisibility(View.GONE);
				}else {
					tvInvalid.setVisibility(View.GONE);
					validateBtn.setVisibility(View.VISIBLE);
				}
				dlg.dismiss();
			}else {
				tvType.setText("");
				tvName.setText("");
				tvInvalid.setVisibility(View.GONE);
				validateBtn.setVisibility(View.GONE);
				errorDialog(2);
				dlg.dismiss();
			}
		}
	}

	private class Validate extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute(){  
			dlg = new ProgressDialog(GetResult.this);
			dlg.setMessage("validating...");
			dlg.setCancelable(false);
			dlg.show();    
		}
		@Override
		protected Void doInBackground(Void... arg0) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
         
        /* login.php returns true if username and password is equal to saranga */
 
        HttpURLConnection connection;
        OutputStreamWriter request = null;

             URL url = null;   
             String response = null;         
             String parameters = "validate="+barcode;   

             try
             {
                 url = new URL(sett.getUrl());
                 connection = (HttpURLConnection) url.openConnection();
                 connection.setDoOutput(true);
                 connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                 connection.setRequestMethod("POST");

                 request = new OutputStreamWriter(connection.getOutputStream());
                 request.write(parameters);
                 request.flush();
                 request.close();            
                 String line = "";               
                 InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                 BufferedReader reader = new BufferedReader(isr);
                 StringBuilder sb = new StringBuilder();
                 while ((line = reader.readLine()) != null)
                 {
                     sb.append(line);
                 }
                 // Response from server after login process will be stored in response variable.                
                 response = sb.toString();
                 result = response;
                 // You can perform UI operations here
                 //Toast.makeText(this,"Message from Server: \n"+ response, 0).show(); 
                 JSONObject mainObject;
                 try {
          			mainObject = new JSONObject(result);
          			status = (mainObject.getString("status").equals("success")? true : false);
          		}catch (Exception e){
          			status = null;
          		}
                 
                 isr.close();
                 reader.close();

             }
             catch(IOException e)
             {
                 dlg.dismiss();
             }
        return null;
    }
		
		@Override
		protected void onPostExecute(final Void unused){
			if (status != null)
			{
				if (status){
					errorDialog(4);
				}else {
					errorDialog(5);
				}
				dlg.dismiss();
			}else {
				errorDialog(3);
				dlg.dismiss();
			}
		}
}

}


