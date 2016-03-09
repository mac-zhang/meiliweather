package com.meiliweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.meiliweather.app.db.MeiliWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MeiliWeatherDB {
	/**
	 * DataBase Name
	 */
	public static final String DB_NAME	= "meili_weather";
	
	/**
	 * DataBase Version
	 */
	public static final int VERSION = 1;
	private static MeiliWeatherDB meiliWeatherDB;
	private SQLiteDatabase db;
	
	/**
	 * 将构造函数私有化
	 */
	private MeiliWeatherDB(Context context){
		MeiliWeatherOpenHelper dbHelper = new MeiliWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 获取MeiliWeatherDB的实例
	 */
	public synchronized static MeiliWeatherDB getInstance(Context context){
		if(meiliWeatherDB==null){
			meiliWeatherDB = new MeiliWeatherDB(context);
		}
		return meiliWeatherDB;
	}
	
	/**
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.GetProvinceName());
			values.put("province_code", province.GetProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	/**
	 * 从数据库读取全国所有的省份信息
	 */
	public List<Province> loadProvinces(){
		List<Province> list	= new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province	= new Province();
				province.SetId(cursor.getInt(cursor.getColumnIndex("id")));
				province.SetProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.SetProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor != null)
			cursor.close();
		return list;
		
	}
	
	/**
	 * 将City实例存储到数据库
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.GetCityName());
			values.put("city_code", city.GetCityCode());
			values.put("province_id", city.GetProvinceId());
			db.insert("City", null, values);
		}
	}
	
	/**
	 * 从数据库读取某省下所有城市的信息
	 */
	public List<City> loadCity(int provinceId){
		List<City> list	= new ArrayList<City>();
		Cursor cursor = db
				.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city	= new City();
				city.SetId(cursor.getInt(cursor.getColumnIndex("id")));
				city.SetCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.SetCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.SetProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor != null)
			cursor.close();
		return list;
		
	}
	
	/**
	 * 将Country实例存储到数据库
	 */
	public void saveCountry(Country country){
		if(country != null){
			ContentValues values = new ContentValues();
			values.put("country_name", country.GetCountryName());
			values.put("country_code", country.GetCountryCode());
			values.put("city_id", country.GetCityId());
			db.insert("Country", null, values);
		}
	}
	
	/**
	 * 从数据库读取某城市下所有县的信息
	 */
	public List<Country> loadCountries(int cityId){
		List<Country> list	= new ArrayList<Country>();
		Cursor cursor = db
				.query("Country", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country	= new Country();
				country.SetId(cursor.getInt(cursor.getColumnIndex("id")));
				country.SetCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.SetCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.SetCityId(cityId);
				list.add(country);
			}while(cursor.moveToNext());
		}
		if(cursor != null)
			cursor.close();
		return list;
		
	}
	
}
