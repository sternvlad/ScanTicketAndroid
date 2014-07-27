package com.scan.ticket;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

	SharedPreferences prefs;
	Context context;

	public static AppSettings get(Context context) {
		AppSettings sett = new AppSettings(context);
		sett.context = context;
		return sett;
	}

	private AppSettings(Context context) {
		prefs = context.getApplicationContext().getSharedPreferences(
				"AppSettings", Context.MODE_PRIVATE);
	}

	
	public String getUrl() {
		return prefs.getString("Url", "http://lrkventures.com/mrsamerica2013/admin/post/verify_ticket.php");
	}
	
	public void updateUrl(String value) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("Url", value);
		edit.commit();
	}

}
