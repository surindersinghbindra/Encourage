package com.nearnia.encouragement;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;

public class GcmIntentService extends IntentService {

	public static int NOTIFICATION_ID = 1;
	int numMessages = 0;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	String TAG = "gcm";

	public GcmIntentService() {
		super(MainActivity2.SENDER_ID);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				Log.e("EXTRAS_EXTRAS1", extras.toString());

				if (!TextUtils.isEmpty(extras.getString("message"))
						&& !TextUtils.isEmpty(extras.getString("type_status"))
						&& !TextUtils.isEmpty(extras.getString("fullname"))
						&& !TextUtils.isEmpty(extras.getString("categoryid"))
						&& !TextUtils.isEmpty(extras.getString("quote_id"))) {

					sendNotification(extras.getString("message"), extras.getString("type_status"),
							extras.getString("fullname"), extras.getString("user_picture"),
							extras.getString("categoryid"), extras.getString("quote_id"));
				} else {

				}

				// sendNotification(extras.getString("message"));

			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		WakefulBroadcastReceiver.completeWakefulIntent(intent);

	}

	private void sendNotification(String msg, String type_status, String fullname, String user_picture,
			String categoryid, String quote_id) {

		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent i = new Intent(this, AfterNotificationRecieved.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("message", msg);
		i.putExtra("type_status", type_status);
		i.putExtra("fullname", fullname);
		i.putExtra("user_picture", user_picture);
		i.putExtra("categoryid", categoryid);
		i.putExtra("quote_id", quote_id);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.lsmallogo)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
				.setContentTitle("-@ " + fullname).setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true)
				.setColor(Color.parseColor("#26A69A"));

		mBuilder.setContentIntent(pendingIntent);
		mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
		// NOTIFICATION_ID++;
		mNotificationManager.notify(1, mBuilder.build());

	}

	private void sendNotification(String msg) {

		Intent i = new Intent(this, AfterNotificationRecieved.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.logo)
				.setContentTitle(getResources().getString(R.string.app_name))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg)
				.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).setColor(Color.parseColor("#26A69A"));

		mBuilder.setContentIntent(pendingIntent);
		mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(numMessages, mBuilder.build());

		VolleySingleton.notificationMessage = null;
		VolleySingleton.notificationMessage = msg;
		VolleySingleton.profileIdRecieved = null;

	}

}
