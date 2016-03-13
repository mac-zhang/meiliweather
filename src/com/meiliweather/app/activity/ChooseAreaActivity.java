package com.meiliweather.app.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.meiliweather.app.model.City;
import com.meiliweather.app.model.Country;
import com.meiliweather.app.model.MeiliWeatherDB;
import com.meiliweather.app.model.Province;
import com.meiliweather.app.util.HttpCallbackListener;
import com.meiliweather.app.util.HttpUtil;
import com.meiliweather.app.util.Utility;

import android.R;
import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	public static final int PROVINCE_LEVEL	= 0;
	public static final int CITY_LEVEL		= 1;
	public static final int COUNTRY_LEVEL	= 2;
	
	private ProgressDialog			progressDialog;
	private TextView				textView;
	private ListView				listView;
	private ArrayAdapter<String> 	adapter;
	private MeiliWeatherDB			meiliWeatherDB;
	private List<String> 			dataList	= new ArrayList<String>();
	
	private List<Province> 			provinceList;
	private List<City> 				cityList;
	private List<Country>  			countryList;
	private Province				selectedProvince;
	private City					selectedCity;
	private int						currentLevel;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean("city_selected", false)){
			Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.meiliweather.app.R.layout.choose_area);
		listView = (ListView)findViewById(com.meiliweather.app.R.id.list_view);
		textView = (TextView)findViewById(com.meiliweather.app.R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		meiliWeatherDB = MeiliWeatherDB.getInstance(this);
		
		listView.setOnClickListener(new OnItemClickListener()){
			public void OnItemClickListener(AdapterView<?>arg0, View view, int index, long arg3){
				if(currentLevel == PROVINCE_LEVEL){
					selectedProvince 	= provinceList.get(index);
					queryCities();
				}else if(currentLevel == CITY_LEVEL){
					selectedCity		= cityList.get(index);
					queryCountries();
				}else if(currentLevel == COUNTRY_LEVEL){
					String countryCode = countryList.get(index).GetCountryCode();
					Intent intent		= new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("country_code", countryCode);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();
	}
	
	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryProvinces(){
		provinceList = meiliWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province : provinceList){
				dataList.add(province.GetProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel	= PROVINCE_LEVEL;
		}else{
			queryFromServer(null, "province");
		}
	}
	
	/**
	 * 查询选中省内的城市，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCities(){
		cityList = meiliWeatherDB.loadCity(selectedProvince.GetId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city : cityList){
				dataList.add(city.GetCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.GetProvinceName());
			currentLevel	= CITY_LEVEL;
		}else{
			queryFromServer(selectedProvince.GetProvinceCode(), "city");
		}
	}
	
	/**
	 * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCountries(){
		countryList = meiliWeatherDB.loadCountries(selectedCity.GetId());
		if(countryList.size()>0){
			dataList.clear();
			for(Country country : countryList){
				dataList.add(country.GetCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.GetCityName());
			currentLevel	= CONTEXT_RESTRICTED;
		}else{
			queryFromServer(selectedCity.GetCityCode(), "country");
		}
	}
	
	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 */
	private void queryFromServer(final String code, final String type){
		String	address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response){
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvincesResponse(meiliWeatherDB, response);
				}else if("city".equals(type)){
					result = Utility.handleCityResponse(meiliWeatherDB, response, selectedProvince.GetId());
				}else if("country".equals(type)){
					result = Utility.handleCountryResponse(meiliWeatherDB, response, selectedCity.GetId());
				}
			}
			
			if(result){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						if("province".equals(type)){
							queryProvinces();
						}else if("city".equals(type)){
							queryCities();
						}else if("country".equals(type)){
							queryCountries();
						}
						
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
						
					}
				});				
			}
		});
	}
	
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("网络正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
			
		}
		progressDialog.show();
	}
	
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	/**
	 * 捕获mBack键，根据当前的级别来判断
	 */
	@Override
	public void onBackPressed() {
		if(currentLevel == COUNTRY_LEVEL){
			queryCities();
		}else if(currentLevel == CITY_LEVEL){
			queryProvinces();
		}else{
			finish();
		}
	}
	
	
}
