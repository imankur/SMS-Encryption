package com.ajapps.nypsesms;

import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Nmg extends Activity {
	Button cont, send, clr;
	EditText et, to;
	TextView conter, ctx;
	
	String di;
	Context f;
	String ssap;
	Handler h;
	SharedPreferences sp;
	String cno, cnm;
	static final int PICK_CONTACT = 1;

	public static final String PREFS_NAME = "Prefs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nmg);
		getActionBar().hide();

		cont = (Button) findViewById(R.id.button1);
		send = (Button) findViewById(R.id.button2);
		clr = (Button) findViewById(R.id.button3);
		et = (EditText) findViewById(R.id.editText2);
		to = (EditText) findViewById(R.id.editText1);
		conter = (TextView) findViewById(R.id.textView1);
		ctx = (TextView) findViewById(R.id.textView2);
		clr.setVisibility(View.GONE);
	
		h = new Handler();
		f = getApplicationContext();
		sp = PreferenceManager.getDefaultSharedPreferences(f);
		di = sp.getString("username", "");
		ssap = sp.getString("password", "");
		
		if (sp.getInt("mse", 0) == 1) {
			et.setText(sp.getString("signature", "pp"));
			conter.setText(sp.getString("signature", "").length()+"/140");
		}
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int count = s.length();
				
				if (count == 0) {
					conter.setText("0/140");
				} else if (count <= 140) {
					conter.setText(count + "/140");
				}else{
					conter.setText("140/140");
					
				}

			}

		});
		clr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				to.setText(null);
				ctx.setText("To :");
				cont.setEnabled(true);
				to.setEnabled(true);
				clr.setVisibility(View.GONE);

			}
		});
		cont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);

			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				final String s1, s2;
				s1 = to.getText().toString();
				s2 = et.getText().toString();
				 if (s1.length() < 9) {
					Toast.makeText(f, "Ooops, Invalid Number Boss", Toast.LENGTH_LONG)
							.show();
				} else if (s2.length() == 0) {
					Toast.makeText(f, "Ooops, Blank Message, Kuch To Likho..!!", Toast.LENGTH_LONG)
							.show();
				} else {
									int ii;
									
									byte b[]=s2.getBytes();
									for(ii=0;ii<b.length;ii++)
										b[ii]=(byte) (b[ii]+3);
									String finl= new String(b);
									SmsManager sms= SmsManager.getDefault();
									sms.sendTextMessage(s1, null, finl, null, null);
									Toast.makeText(f, "Cipher : "+finl, Toast.LENGTH_LONG).show();

									ContentValues contentvalues = new ContentValues();
									contentvalues.put("afddress", s1);
									contentvalues.put("date", Long
											.valueOf((new Date()).getTime()));
									contentvalues.put("read",
											Integer.valueOf(1));
									contentvalues.put("status",
											Integer.valueOf(-1));
									contentvalues.put("type",
											Integer.valueOf(2));
									contentvalues.put("body", s2);
									f.getContentResolver().insert(
											Uri.parse("content://sms"),
											contentvalues);
									int i = sp.getInt("count", 0);
									SharedPreferences.Editor set = sp.edit();
									i++;
									set.putInt("count", i);
									set.commit();
								}	
				}
			
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = getContentResolver().query(contactData, null, null,
						null, null);
				if (c.moveToFirst()) {
					String id = c
							.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

					String hasPhone = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					if (hasPhone.equalsIgnoreCase("1")) {
						Cursor phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						phones.moveToFirst();
						cno = phones.getString(phones.getColumnIndex("data1"));
						cnm = c.getString(c
								.getColumnIndex(StructuredPostal.DISPLAY_NAME));
						String dc = null;
						cno.replace("-", "");
						if (cno == null) {
							cno = cnm;
						}
						if (cno.length() > 10) {
							dc = cno.substring(3);
						} else {
							dc = cno;
						}
						to.setText(dc);
						ctx.setText("To : " + cnm);
						cont.setEnabled(false);
						to.setEnabled(false);
						clr.setVisibility(View.VISIBLE);
						Toast.makeText(
								this,to.getText().toString()
										+ "\n" + 
										"Contact Info : " +cnm +".", Toast.LENGTH_LONG).show();

					}

				}
			}
		}
	}

}
