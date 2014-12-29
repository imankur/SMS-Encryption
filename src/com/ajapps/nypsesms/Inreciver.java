package com.ajapps.nypsesms;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class Inreciver extends BroadcastReceiver {

	NotificationCompat.Builder ncm;
	NotificationManager nm;
	long[] d = { 0, 500, 300, 500 };

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent pi = new Intent(context, MainActivity.class);
		ncm = new Builder(context);
		nm = (NotificationManager) context.getSystemService("notification");
		ncm.setSmallIcon(R.drawable.newsms).setContentTitle("New SMS")
				.setContentText("You've New Message..!!")
				.setWhen(System.currentTimeMillis())
				.setTicker("Check In!! New Message.!!").setAutoCancel(true)
				.setVibrate(d)
				.setContentIntent(PendingIntent.getActivity(context, 0, pi, 0));

		nm.notify(22, ncm.build());
	}

}
