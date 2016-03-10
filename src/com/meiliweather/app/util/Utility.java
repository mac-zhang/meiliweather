package com.meiliweather.app.util;

import android.text.TextUtils;

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
	
}
