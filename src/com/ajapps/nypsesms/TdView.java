package com.ajapps.nypsesms;

import java.io.IOException;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TdView extends Activity implements OnClickListener {
	ListView mlist;
	TextView contr;
	EditText comp;
	Button send;
	Handler h;
	Intent intent;
	String threadid;
	Context f;
	int lnth;
	
	Cursor cursorin;
	apdttd ggj;
	SimpleCursorAdapter mcursor;
	String who, di, ssap, scnd, scndk, here;

	SharedPreferences sp;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_td_view);
		getActionBar().hide();

		mlist = (ListView) findViewById(R.id.listView1);
		contr = (TextView) findViewById(R.id.textView1);
		comp = (EditText) findViewById(R.id.editText1);
		send = (Button) findViewById(R.id.button1);
		f = getApplicationContext();
		h = new Handler();

		intent = getIntent();
		Bundle bundle = intent.getExtras();
		threadid = bundle.getString("thrd");
		who = bundle.getString("no");
		scnd = bundle.getString("scnd");

		registerForContextMenu(mlist);
		a();
		f.getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, new rd(f));
		send.setOnClickListener(this);
		if (scnd.length() > 10) {
			scndk = scnd.substring(3);
		} else {
			scndk = scnd;
		}

		new thrd().execute();
		comp.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {

				int count = s.length();
				if (count == 0) {
					contr.setText("0/140");
				} else if (count <= 140) {
					contr.setText(count + "/134");

				} else {
					contr.setText("140/140");

				}

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}
		});
mlist.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView kk = (TextView) arg1.findViewById(R.id.textView2);
		String k = kk.getText().toString();
		byte b[]=k.getBytes();
		for(int i=0;i<b.length;i++)
			b[i]=(byte) (b[i]-3);
		String finl= new String(b);
		
		Toast.makeText(f,finl, Toast.LENGTH_LONG).show();
		//arg0.getContext().toString();
	}
});
	}

	public void a() {

		ContentValues contentvalues;

		contentvalues = new ContentValues(1);
		contentvalues.put("read", Integer.valueOf(1));
		f.getContentResolver().update(
				Uri.parse("content://sms"),
				contentvalues,
				(new StringBuilder("thread_id=")).append(threadid)
						.append(" AND read=0").toString(), null);

	}

	class thrd extends AsyncTask<Void, Void, Cursor> {
		String k;
		String cluase = "thread_id=?";
		String[] g = { "" };

		@Override
		protected Cursor doInBackground(Void... args) {
			g[0] = threadid;
			cursorin = getContentResolver()
					.query(Uri.parse("content://sms/"),
							new String[] { "_id", "type", "thread_id", "date",
									"body" }, cluase, g, "date ASC");
			return cursorin;
		}

		@Override
		protected void onPostExecute(Cursor result) {

			ggj = new apdttd(f, result, who);
			mlist.setAdapter(ggj);

		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tdmn, menu);
	}

	class rd extends ContentObserver {

		Context c;

		public rd(Context context) {
			super(null);
			c = context;
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			new thrd().execute();
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			super.onChange(selfChange, uri);
			new thrd().execute();
		}

	}

	@Override
	public void onClick(View v) {
		
		final String s1, s2;
		s1 = scndk;
		s2 = comp.getText().toString();
	
		if (s2.length() == 0) {
			Toast.makeText(f, "Ooops, Blank Message, Kuch To Likho..!!", Toast.LENGTH_LONG).show();
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
							contentvalues.put("address", s1);
							contentvalues.put("date",
									Long.valueOf((new Date()).getTime()));
							contentvalues.put("read", Integer.valueOf(1));
							contentvalues.put("status", Integer.valueOf(-1));
							contentvalues.put("type", Integer.valueOf(2));
							contentvalues.put("body", s2);
							f.getContentResolver().insert(
									Uri.parse("content://sms"), contentvalues);
							int i = sp.getInt("count", 0);
							SharedPreferences.Editor set = sp.edit();
							i++;
							set.putInt("count", i);
							set.commit();

						
						
				
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int jj = info.position;
		int itemId = item.getItemId();
		if (itemId == R.id.item2) {
			String id = info.id + "";
			getContentResolver().delete(Uri.parse("content://sms"), "_id=?",
					new String[] { id });
			Toast.makeText(f, "Deleted", Toast.LENGTH_SHORT).show();
			return true;
		} else if (itemId == R.id.item1) {
			cursorin.moveToPosition(jj);
			String ftxt = cursorin.getString(cursorin.getColumnIndex("body"));
			Intent intent = new Intent(f, Nmg.class);
			intent.putExtra("gettx", ftxt);
			startActivity(intent);
			return true;
		} else {
			return super.onContextItemSelected(item);
		}
	}

	
}