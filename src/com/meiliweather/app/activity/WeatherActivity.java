package com.meiliweather.app.activity;

import com.meiliweather.app.util.HttpCallbackListener;
import com.meiliweather.app.util.HttpUtil;
import com.meiliweather.app.util.Utility;

import android.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity{
	private LinearLayout weatherInfoLayout;
	private TextView	 cityNameText;
	private TextView	publishText;
	private TextView	weatherDespText;
	private TextView	temp1Text;
	private TextView	temp2Text;
	private TextView	currentDateText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.meiliweather.app.R.layout.weather_layout);
		// 初始化控件
		weatherInfoLayout = (LinearLayout) findViewById(com.meiliweather.app.R.id.weather_info_layout);
		cityNameText	= (TextView) findViewById(com.meiliweather.app.R.id.city_name);
		publishText	= (TextView) findViewById(com.meiliweather.app.R.id.publish_text);
		weatherDespText	= (TextView) findViewById(com.meiliweather.app.R.id.weather_desp);
		temp1Text	= (TextView) findViewById(com.meiliweather.app.R.id.temp1);
		temp2Text	= (TextView) findViewById(com.meiliweather.app.R.id.temp2);
		currentDateText	= (TextView) findViewById(com.meiliweather.app.R.id.current_date);
		String countryCode = getIntent().getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			publishText.setText("天气数据同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}else{
			showWeather();
		}
	}
	
	/**
	 * 查询县级代号所对应的天气代号
	 * @param countryCode
	 */
	private void queryWeatherCode(String countryCode){
		String address = "http://www.weather.com.cn/data/list3/city"+countryCode+".html";
		queryFromSever(address, "countryCode");
	}
	
	/**
	 * 查询天气代号对应的天气
	 */
	private void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromSever(address, "weatherCode");
	}
	
	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	 */
	private void queryFromSever(final String address, final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if("countryCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Utility.handleWeatherRespon(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
							
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						publishText.setText("同步失败！");
						
					}
				});
				
			}
		});
	}
	
	/**
	 * 从SharedPreferences文件中读取存储的天气信息，并显示到界面
	 */
	private void showWeather(){
		SharedPreferences prefs	= PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}
