package com.mygdx.game.android;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.util.Notification;

public class AndroidLauncher extends AndroidApplication 
{
	private PendingIntent pendingIntent;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	      
		initialize(new MyGdxGame(new AndroidNotification(this)), config);
	}
}

class AndroidNotification implements Notification
{
	AndroidLauncher launcher;
	Calendar calendar;
	Intent myIntent;
	PendingIntent pendingIntent;
	
	public AndroidNotification(AndroidLauncher a)
	{
		launcher = a;
	}

	public void deployNotification(int s, int m, int h, int d, String msg)
	{    
		//make sure to let the calendar reset here
		calendar = Calendar.getInstance();
		
		long timeToAddInMillis = (s * 1000) + ((m * 60) * 1000) + (((h * 60) * 60) * 1000) + ((((d * 24) * 60) * 60) * 1000);
		Gdx.app.log("DEBUG", "AndroidLauncher.deployNotification - timeToAddInMillis = " + timeToAddInMillis);
		
        long currentTimeInMillis = calendar.getTimeInMillis();
        Gdx.app.log("DEBUG", "AndroidLauncher.deployNotification - currentTimeInMillis = " + currentTimeInMillis);
        
        long notificationTimeInMillis = timeToAddInMillis + currentTimeInMillis;
        Gdx.app.log("DEBUG", "AndroidLauncher.deployNotification - notificationTimeInMillis = " + notificationTimeInMillis);
     
        myIntent = new Intent(launcher, MyReceiver.class);
        myIntent.putExtra("notification_string_message", msg);
        myIntent.putExtra("notification_fire_time", notificationTimeInMillis);
        pendingIntent = PendingIntent.getBroadcast(launcher, 0, myIntent,0);
     
        AlarmManager alarmManager = (AlarmManager)launcher.getSystemService(Context.ALARM_SERVICE);
        
        //THIS IS THE MONEY RIGHT HERE. CALL THIS TO SCHEDULE NOTIFICATION
        alarmManager.set(AlarmManager.RTC, notificationTimeInMillis, pendingIntent);
	}
	
	public void clearNotifications()
	{
		NotificationManager nM = (NotificationManager) 
				launcher.getApplicationContext().getSystemService(launcher.getApplicationContext().NOTIFICATION_SERVICE);
		
		nM.cancelAll();
	}
}
