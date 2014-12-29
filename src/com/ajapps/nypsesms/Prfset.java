package com.ajapps.nypsesms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Prfset extends PreferenceActivity {
	
	SharedPreferences sharedpreferences;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.setpref);
		Preference pp = findPreference("counts");

	
		sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
		int i = sharedpreferences.getInt("count", 0);

		

		pp.setTitle("SMS Count : " + i);

		
	

	}
}
