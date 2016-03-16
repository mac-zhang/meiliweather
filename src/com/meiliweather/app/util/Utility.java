package com.meiliweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.meiliweather.app.model.City;
import com.meiliweather.app.model.Country;
import com.meiliweather.app.model.MeiliWeatherDB;
import com.meiliweather.app.model.Province;

public class Utility {
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(MeiliWeatherDB meiliWeatherDB, String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length>0){
				for(String p : allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.SetProvinceCode(array[0]);
					province.SetProvinceName(array[1]);
					// ���������������ݴ洢��Province��
					meiliWeatherDB.saveProvince(province);
				}
				return	true;
			}
		}
		return	false;
	}
	
	/**
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCityResponse(MeiliWeatherDB meiliWeatherDB, String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length>0){
				for(String c : allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.SetCityCode(array[0]);
					city.SetCityName(array[1]);
					city.SetProvinceId(provinceId);
					// ���������������ݴ洢��Province��
					meiliWeatherDB.saveCity(city);
				}
				return	true;
			}
		}
		return	false;
	}
	
	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	public synchronized static boolean handleCountryResponse(MeiliWeatherDB meiliWeatherDB, String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCountrys = response.split(",");
			if(allCountrys != null && allCountrys.length>0){
				for(String p : allCountrys){
					String[] array = p.split("\\|");
					Country country = new Country();
					country.SetCountryCode(array[0]);
					country.SetCountryName(array[1]);
					country.SetCityId(cityId);
					// ���������������ݴ洢��Province��
					meiliWeatherDB.saveCountry(country);
				}
				return	true;
			}
		}
		return	false;
	}
	
		
	/**
	 * �������������ص�JSON���ݣ��������ݴ洢������
	 */
	public static void handleWeatherRespon(Context context, String response){
		try{
			JSONObject jsonObject 	= new JSONObject(response);
			JSONObject weatherInfo 	= jsonObject.getJSONObject("weatherinfo");
			String cityName 		= weatherInfo.getString("city");
			String weatherCode 		= weatherInfo.getString("cityid");
			String temp1 			= weatherInfo.getString("temp1");
			String temp2	 		= weatherInfo.getString("temp2");
			String weatherDesp 		= weatherInfo.getString("weather");
			String publishTime 		= weatherInfo.getString("ptime");
//			Log.d("w1", cityName);
//			Log.d("w1", temp1);
//			Log.d("w1", weatherDesp);
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		}catch (JSONException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ�
	 */
	public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2,
			String weatherdesp, String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		SharedPreferences.Editor editor	= PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherdesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
