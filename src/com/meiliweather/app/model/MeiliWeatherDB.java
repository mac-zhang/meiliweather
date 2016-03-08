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
	 * �����캯��˽�л�
	 */
	private MeiliWeatherDB(Context context){
		MeiliWeatherOpenHelper dbHelper = new MeiliWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * ��ȡMeiliWeatherDB��ʵ��
	 */
	public synchronized static MeiliWeatherDB getInstance(Context context){
		if(meiliWeatherDB==null){
			meiliWeatherDB = new MeiliWeatherDB(context);
		}
		return meiliWeatherDB;
	}
	
	/**
	 * ��Provinceʵ���洢�����ݿ�
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
	 * �����ݿ��ȡȫ�����е������Ϣ
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
}
