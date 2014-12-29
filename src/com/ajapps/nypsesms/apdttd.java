package com.ajapps.nypsesms;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class apdttd extends SimpleCursorAdapter {
	static String[] a = { "date", "body" };
	static int[] b = { R.id.textView1, R.id.textView2 };
	String res;
	int i ;
	Time time = new Time();
	String who;
	Context cc;
	public apdttd(Context paramContext, Cursor paramCursor, String paramString) {
		super(paramContext, R.layout.tdrow, paramCursor, a, b, 0);
		res = paramString;
	}

	
	
	public void bindView(View paramView, Context paramContext,
			Cursor paramCursor) {
		super.bindView(paramView, paramContext, paramCursor);
		LayoutInflater inflater = (LayoutInflater) paramContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView localTextView1 = (TextView) paramView
				.findViewById(R.id.textView1);
		TextView localTextView2 = (TextView) paramView
				.findViewById(R.id.textView2);
		 i = paramCursor.getInt(paramCursor.getColumnIndex("type"));
		//int kk = paramCursor.getInt(paramCursor.getColumnIndex("_id"));
		
		String msg = paramCursor.getString(paramCursor.getColumnIndex("body"));

		if (i == 1) {
			who = "   " + res + ":  " + "    ";
			
			inflater.inflate(R.layout.tdrow, null);
			localTextView1.setTextColor(paramContext.getResources().getColor(
					R.color.white));
			localTextView2.setTextColor(paramContext.getResources().getColor(
					R.color.white));
			localTextView2.setText(msg);
			
		} else  {
			who = " Me:" + "       ";
			inflater.inflate(R.layout.tdrow1, null);
			
			localTextView1.setTextColor(paramContext.getResources().getColor(
					R.color.white));
			localTextView2.setTextColor(paramContext.getResources().getColor(
					R.color.white));
			localTextView2.setText(msg);
		} 
		long l;
		int j;
		int k;
		int m;

		l = paramCursor.getLong(paramCursor.getColumnIndex("date"));
		time.set(l);
		j = time.year;
		k = time.month;
		m = time.getWeekNumber();
		time.set(System.currentTimeMillis());
		if (DateUtils.isToday(l)) {

			localTextView1.setText(DateUtils.getRelativeTimeSpanString(
					paramContext, l, false) + " " + who);
		} else if ((j == time.year) && (k == time.month)
				&& (m == time.getWeekNumber())) {
			localTextView1.setText(DateUtils.formatDateTime(paramContext, l,
					DateUtils.FORMAT_SHOW_WEEKDAY).substring(0, 3)
					+ " "
					+ DateUtils.formatDateTime(paramContext, l,
							DateUtils.FORMAT_SHOW_TIME) + " " + who);
		} else {
			localTextView1.setText(DateUtils.formatDateTime(paramContext, l,
					DateUtils.FORMAT_NO_YEAR)
					+ " "
					+ DateUtils.formatDateTime(paramContext, l,
							DateUtils.FORMAT_SHOW_TIME) + " " + who);
		}
	}

	/*@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
Log.v("type", ""+i);
		if (i == 1) {
			
			
			return inflater.inflate(R.layout.tdrow, null);
		} else{
			return inflater.inflate(R.layout.tdrow1, null);
		}
	}*/

}
