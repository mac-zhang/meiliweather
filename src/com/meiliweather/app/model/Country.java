package com.meiliweather.app.model;

public class Country {
	private int id;
	private String CountryName;
	private String CountryCode;
	private int CityId;
	
	public int GetId(){
		return id;
	}
	
	public void SetId(int id){
		this.id	= id;
	}
	
	public String GetCountryName(){
		return CountryName;
	}
	
	public void SetCountryName(String CountryName){
		this.CountryName = CountryName;
	}
	
	public String GetCountryCode(){
		return CountryCode;
	}
	
	public void SetCountryCode(String CountryCode){
		this.CountryCode = CountryCode;
	}
	
	public int GetCityId(){
		return CityId;
	}
	
	public void SetCityId(int CityId){
		this.CityId = CityId;
	}
}
