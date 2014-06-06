package com.mygdx.game.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver
{
      
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	String notificationMessage = intent.getStringExtra("notification_message_string");
    	long timeInMillis = intent.getExtras().getLong("notification_fire_time");
    	
    	Intent service1 = new Intent(context, NotificationService.class);
    	
    	service1.putExtra("notification_message_string", notificationMessage);
    	service1.putExtra("notification_fire_time", timeInMillis);
    	
    	context.startService(service1);
    }   
}
