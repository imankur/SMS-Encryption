package com.ajapps.nypsesms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ListView smslist;
	Cursor cursor;
	Context context;
	SimpleCursorAdapter mcursor;
	inad dd;
	SharedPreferences sharedpreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		smslist = (ListView) findViewById(R.id.listView1);
		context = getApplicationContext();

		context.getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, new rd(context));
		registerForContextMenu(smslist);
		
		
		
		
		
		
		new thrd().execute();
		smslist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Cursor theCursor = ((SimpleCursorAdapter) ((ListView) arg0)
						.getAdapter()).getCursor();
				String td = theCursor.getString(theCursor
						.getColumnIndex("thread_id"));
				String snkln = theCursor.getString(theCursor
						.getColumnIndex("address"));
				TextView kk = (TextView) arg1.findViewById(R.id.textView1);
				String k = kk.getText().toString();
				Intent intt = new Intent(context, TdView.class);
				intt.putExtra("no", k);
				intt.putExtra("thrd", td);
				intt.putExtra("scnd", snkln);
				startActivity(intt);
			}
		});
		
		PackageInfo info;
		try {
		    info = getPackageManager().getPackageInfo("com.ajapps.nypsesms", PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md;
		        md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        String something = new String(Base64.encode(md.digest(), 0));
		        //String something = new String(Base64.encodeBytes(md.digest()));
		        Log.e("hash key", something);
		    }
		} catch (NameNotFoundException e1) {
		    Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
		    Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
		    Log.e("exception", e.toString());
		}
		
		
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mab, menu);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.welcome, menu);
	}

	class thrd extends AsyncTask<Void, Void, Cursor> {
		String k;

		@Override
		protected Cursor doInBackground(Void... params) {
			Uri smsuri = Uri.parse("content://sms/");
			cursor = getContentResolver().query(
					smsuri,
					new String[] { "_id", "thread_id", "address", "person",
							"date", "body", "read" },
					"_id) GROUP BY (thread_id", null, "date DESC");
			
			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			dd = new inad(context, cursor);
			smslist.setAdapter(dd);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.item1) {
			Intent intt1 = new Intent(context, Nmg.class);
			startActivity(intt1);
			return true;
		} else if (itemId == R.id.item2) {
			
			return true;
		} else if (itemId == R.id.item3) {
			Intent intt = new Intent(context, Prfset.class);
			startActivity(intt);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int v = info.position;
		int itemId = item.getItemId();
		if (itemId == R.id.item2) {
			deletethread(v);
			return true;
		} else if (itemId == R.id.item1) {
			call(v);
			;
			return true;
		} else {
			return super.onContextItemSelected(item);
		}
	}

	void deletethread(int v) {
		cursor.moveToPosition(v);
		String h = cursor.getString(cursor.getColumnIndex("thread_id"));
		
		getContentResolver().delete(Uri.parse("content://sms/"), "thread_id=?",
				new String[] { h });
		Toast.makeText(context, "Deleted..", Toast.LENGTH_SHORT).show();
	}

	void call(int v) {
		cursor.moveToPosition(v);
		String h = cursor.getString(cursor.getColumnIndex("address"));

		try {
			Intent localIntent = new Intent("android.intent.action.CALL");
			localIntent.setData(Uri.parse("tel:" + h));
			startActivity(localIntent);

		} catch (ActivityNotFoundException localActivityNotFoundException) {
			
		}
	}

}
