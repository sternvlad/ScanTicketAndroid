package com.scan.ticket;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button scanBtn,insertBtn;
	AppSettings sett;
	Button settingsBtn;
	//suma numere
	Dialog dialog;
	Button sumaNumere;
	ProgressDialog dlg;
	String result;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sett = AppSettings.get(MainActivity.this);
		
		scanBtn = (Button) findViewById(R.id.scan_btn);
		scanBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isOnline())
				{
					errorDialog(1);
				}else
				{
					Intent intent = new Intent (MainActivity.this,CameraScan.class);
					startActivity(intent);
				}
			}
		});
			
			settingsBtn = (Button) findViewById(R.id.button_settings);
			settingsBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setYourUrlDialog();
				}
			});
			
			insertBtn = (Button) findViewById(R.id.insert_manual_btn);
			insertBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!isOnline())
					{
						errorDialog(1);
					}else
					{
					Intent intent = new Intent (MainActivity.this,InsertManual.class);
					startActivity(intent);
					}
				}
			});
	}

	private void setYourUrlDialog () { //functie pop up
	     dialog=new Dialog(MainActivity.this);
	     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     dialog.setContentView(R.layout.dialog);
	     dialog.setCancelable(false);
	     
	     final EditText edtUrl = (EditText) dialog.findViewById(R.id.editText_url);
	     edtUrl.setText(sett.getUrl());

	     Button btn_ok=(Button)dialog.findViewById(R.id.button_ok);
	     btn_ok.setOnClickListener(new OnClickListener() {
	     @Override
	     public void onClick(View v) {
	        dialog.dismiss();
	        sett.updateUrl(edtUrl.getText().toString());
	     	}
	     });
	     dialog.show();
	}
	
	private void errorDialog (int tag) { //functie pop up
	     dialog=new Dialog(MainActivity.this);
	     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     dialog.setContentView(R.layout.custom_dialog);
	     dialog.setCancelable(false);
	     Button btnYes = (Button) dialog.findViewById(R.id.button_yes);
	     Button btnNo = (Button) dialog.findViewById(R.id.button_no);
	     TextView tvError = (TextView) dialog.findViewById(R.id.error_textView);
	     
	     if (tag == 1){
	    	 btnYes.setBackgroundResource(R.drawable.ok_btn);
	    	 btnYes.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
	    	 
	    	 btnNo.setVisibility(View.GONE);
	     }else if (tag == 2){
	    	 tvError.setText("Are you sure you want to exit?");
	    	 btnYes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_MAIN);
	        		intent.addCategory(Intent.CATEGORY_HOME);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		startActivity(intent);
	        		dialog.dismiss();
				}
			});
	    	 btnNo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
	     }
	     dialog.show();
	}
	
	@Override
	public void onBackPressed ()
	{
		errorDialog(2);
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
   
    
}