package com.scan.ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertManual extends Activity {
	EditText edtTicketCode;
	Button verifyBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_manual);
		
		edtTicketCode = (EditText) findViewById (R.id.edit_ticket);
		edtTicketCode.setText("");
		verifyBtn = (Button) findViewById(R.id.verify_btn);
		verifyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edtTicketCode.getText().toString().equals("")){
					Toast msg = Toast.makeText(InsertManual.this, "Please type your ticket code!", Toast.LENGTH_LONG);
					msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
					msg.show();
				}else{
					GetResult.back = true;
					Intent intent = new Intent (InsertManual.this,GetResult.class);
					intent.putExtra("barcode", edtTicketCode.getText().toString());
					startActivity(intent);
				}
			}
		});
		
	}
}