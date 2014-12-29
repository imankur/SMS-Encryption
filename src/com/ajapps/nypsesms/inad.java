package com.ajapps.nypsesms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class inad extends SimpleCursorAdapter {

	static String[] a = { "address", "body", "date" };
	static int[] b = { R.id.textView1, R.id.textView2, R.id.textView4 };
	Time time = new Time();

	public inad(Context context, Cursor c) {
		super(context, R.layout.row, c, a, b, 0);

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		super.bindView(view, context, cursor);
		String addr = cursor.getString(cursor.getColumnIndex("address"));
		if (addr == null) {
			view.setVisibility(View.GONE);
		}
		int a = cursor.getInt(cursor.getColumnIndex("read"));

		if (a == 0) {
			if (addr.length() > 2) {
				view.setBackgroundResource(R.color.gy);
			}
		} else {
			view.setBackgroundResource(R.color.black);
		}

		long tim = cursor.getLong(cursor.getColumnIndex("date"));
		time.set(tim);
		TextView b = (TextView) view.findViewById(R.id.textView1);
		TextView tt = (TextView) view.findViewById(R.id.textView4);

		String fn = findNameByAddress(context, addr);
		b.setText(fn);
		String h = DateUtils.formatDateTime(context, tim,
				DateUtils.FORMAT_ABBREV_MONTH);
		/*
		 * if (h.indexOf(" ")==1){ //Log.v("index", ""+h.indexOf("")); String j=
		 * h.substring(0, 4); tt.setText(h); } else{
		 */

		tt.setText(h);
	}

	// }

	public String findNameByAddress(Context ct, String addr) {
		try {
			Uri myPerson = Uri
					.withAppendedPath(
							android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
							Uri.encode(addr));
			String[] projection = new String[] { "display_name", "_id",
					"photo_id" };
			Cursor cursor = ct.getContentResolver().query(myPerson, projection,
					null, null, null);
			if (cursor.moveToFirst()) {
				String name = cursor.getString(cursor
						.getColumnIndex("display_name"));
				cursor.close();
				return name;
			}
			cursor.close();
			// return addr;
		} catch (IllegalArgumentException c) {
			// return addr;
		}
		return addr;

	}
}
