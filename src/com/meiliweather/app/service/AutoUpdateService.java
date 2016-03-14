package com.meiliweather.app.service;

import com.meiliweather.app.recevier.AutoUpdateReciver;
import com.meiliweather.app.util.HttpCallbackListener;
import com.meiliweather.app.util.HttpUtil;
import com.meiliweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service{
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				updateWeather();
				
			}
		}).start();
		
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 7*60*60*1000;
		long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
		Intent i = new Intent(this, AutoUpdateReciver.class);
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
			
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void updateWeather(){
		SharedPreferences prfes = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prfes.getString("weather_code", "");
		String address 	= "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Utility.handleWeatherRespon(AutoUpdateService.this, response);
				
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				
			}
		});
	}
}
