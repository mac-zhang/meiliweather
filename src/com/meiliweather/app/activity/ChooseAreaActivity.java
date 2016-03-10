package com.meiliweather.app.activity;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.meiliweather.app.model.City;
import com.meiliweather.app.model.Country;
import com.meiliweather.app.model.MeiliWeatherDB;
import com.meiliweather.app.model.Province;

import android.R;
import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView)findViewById(R.id.list_view);
		textView = (TextView)findViewById(R.id.title_text);
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
					queryCountry();
				}
			}
		});
		quetyProvinces();
	}
	
}
