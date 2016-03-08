package com.meiliweather.app.model;

public class Province {
	private int id;
	private String ProvinceName;
	private String ProvinceCode;
	
	public int GetId(){
		return id;
	}
	
	public void SetId(int id){
		this.id	= id;
	}
	
	public String GetProvinceName(){
		return ProvinceName;
	}
	
	public void SetProvinceName(String ProvinceName){
		this.ProvinceName = ProvinceName;
	}
	
	public String GetProvinceCode(){
		return ProvinceCode;
	}
	
	public void SetProvinceCode(String ProvinceCode){
		this.ProvinceCode = ProvinceCode;
	}
}
