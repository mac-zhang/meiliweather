package com.meiliweather.app.model;

public class City {
	private int id;
	private String CityName;
	private String CityCode;
	private int ProvinceId;
	
	public int GetId(){
		return id;
	}
	
	public void SetId(int id){
		this.id	= id;
	}
	
	public String GetCityName(){
		return CityName;
	}
	
	public void SetCityName(String CityName){
		this.CityName = CityName;
	}
	
	public String GetCityCode(){
		return CityCode;
	}
	
	public void SetCityCode(String CityCode){
		this.CityCode = CityCode;
	}
	
	public int GetProvinceId(){
		return ProvinceId;
	}
	
	public void SetProvinceId(int ProvinceId){
		this.ProvinceId = ProvinceId;
	}
}
