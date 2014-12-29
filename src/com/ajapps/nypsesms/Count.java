package com.ajapps.nypsesms;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Count extends DialogPreference {
	EditText us;
	Context a;

	public Count(Context context, AttributeSet attrs) {
		super(context, attrs);
		a = context;
		setDialogLayoutResource(R.layout.count);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBindDialogView(View view) {
		// TODO Auto-generated method stub
		super.onBindDialogView(view);

		us = (EditText) view.findViewById(R.id.editText1);

	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		SharedPreferences sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		if (positiveResult) {
			sharedpreferences.edit()
					.putString("signature", "\n\n-" + us.getText().toString())
					.commit();
			Toast.makeText(a, "Saved..", Toast.LENGTH_SHORT).show();
		}
	}

}
