package com.meiliweather.app.util;

import android.text.TextUtils;

import com.meiliweather.app.model.City;
import com.meiliweather.app.model.Country;
import com.meiliweather.app.model.MeiliWeatherDB;
import com.meiliweather.app.model.Province;

public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
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
					// 将解析出来的数据存储到Province表
					meiliWeatherDB.saveProvince(province);
				}
				return	true;
			}
		}
		return	false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
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
					// 将解析出来的数据存储到Province表
					meiliWeatherDB.saveCity(city);
				}
				return	true;
			}
		}
		return	false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
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
					// 将解析出来的数据存储到Province表
					meiliWeatherDB.saveCountry(country);
				}
				return	true;
			}
		}
		return	false;
	}
	
}
