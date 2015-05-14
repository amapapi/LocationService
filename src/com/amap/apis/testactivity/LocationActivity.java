package com.amap.apis.testactivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amap.apis.locationservice.GDLocation;
import com.amap.apis.locationservice.GDLocationListener;
import com.amap.apis.locationservice.LocationClient;
import com.amap.apis.locationservice.LocationClientOption;
import com.amap.apis.locationservice.LocationClientOption.LocationMode;
import com.amap.location.demo.R;

public class LocationActivity extends Activity {
	private LocationClient mLocationClient;
	private TextView LocationResult, ModeInfor;
	private Button startLocation;
	private RadioGroup selectMode, selectCoordinates;
	private EditText frequence;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private CheckBox checkGeoLocation;

	private LocationClient mLocationClient1;

	private LocationClient mLocationClient2;

	private boolean mIsStart = true;

	private ExecutorService threadPools;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		mLocationClient = ((LocationApplication) getApplication()).mLocationClient;

		mLocationClient1 = new LocationClient(getApplicationContext());

		mLocationClient2 = new LocationClient(getApplicationContext());
		threadPools = Executors.newFixedThreadPool(5);
	
		LocationClientOption option1 = new LocationClientOption();
		option1.setScanSpan(2000);
		option1.setLocationMode(LocationMode.Battery_Saving);
		mLocationClient1.setLocOption(option1);

		mLocationClient1.registerLocationListener(new GDLocationListener() {

			@Override
			public void onReceiveLocation(GDLocation location) {

				//Log.i("yiyi.qi", "location 1 is" + location.getAddrStr());

			}
		});

		mLocationClient2.registerLocationListener(new GDLocationListener() {

			@Override
			public void onReceiveLocation(GDLocation location) {

			//	Log.i("yiyi.qi", "location 2 is" + location.getAddrStr());

			}
		});

		mLocationClient1.start();
		mLocationClient2.start();

		LocationClientOption option2 = new LocationClientOption();
		option2.setScanSpan(2000);
		option2.setLocationMode(LocationMode.Battery_Saving);
		mLocationClient2.setLocOption(option2);

		LocationResult = (TextView) findViewById(R.id.textView1);
		ModeInfor = (TextView) findViewById(R.id.modeinfor);
		ModeInfor.setText(getString(R.string.hight_accuracy_desc));
		((LocationApplication) getApplication()).mLocationResult = LocationResult;

		frequence = (EditText) findViewById(R.id.frequence);
		checkGeoLocation = (CheckBox) findViewById(R.id.geolocation);
		startLocation = (Button) findViewById(R.id.addfence);
		startLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitLocation();

				if (startLocation.getText().equals(
						getString(R.string.startlocation))) {
					mLocationClient.start();
					startLocation.setText(getString(R.string.stoplocation));

					new Thread() {
						public void run() {
							while (mIsStart) {
								threadPools.submit(new Runnable() {
									@Override
									public void run() {
										for(int i=0;i<3;i++){
										int randomInt = (int) (Math.random() * 4);
										switch (randomInt) {
										case 0:
											mLocationClient1.start();
											break;
										case 1:
											mLocationClient2.start();
											break;
										case 2:
											mLocationClient1.stop();
											break;
										case 3:
											mLocationClient2.stop();
											break;

										}
										}
										
									}

								}

								);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									  
									// TODO Auto-generated catch block  
									e.printStackTrace();  
									
								}
							}
							

						}

					}.start();
				
				} else {
					mIsStart = false;
					mLocationClient.stop();
					startLocation.setText(getString(R.string.startlocation));
				}

			}
		});
		selectMode = (RadioGroup) findViewById(R.id.selectMode);
		selectCoordinates = (RadioGroup) findViewById(R.id.selectCoordinates);
		selectMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				String ModeInformation = null;
				switch (checkedId) {
				case R.id.radio_hight:
					tempMode = LocationMode.Hight_Accuracy;
					ModeInformation = getString(R.string.hight_accuracy_desc);
					break;
				case R.id.radio_low:
					tempMode = LocationMode.Battery_Saving;
					ModeInformation = getString(R.string.saving_battery_desc);
					break;
				case R.id.radio_device:
					tempMode = LocationMode.Device_Sensors;
					ModeInformation = getString(R.string.device_sensor_desc);
					break;
				default:
					break;
				}
				ModeInfor.setText(ModeInformation);
			}
		});
		selectCoordinates
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.radio_gcj02:
							tempcoor = "gcj02";
							break;
						case R.id.radio_bd09ll:
							tempcoor = "bd09ll";
							break;
						case R.id.radio_bd09:
							tempcoor = "bd09";
							break;
						default:
							break;
						}
					}
				});
	}

	@Override
	protected void onStop() {		
		mLocationClient1.stop();
		mLocationClient2.stop();
		mLocationClient.stop();

		super.onStop();
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		// option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		try {
			span = Integer.valueOf(frequence.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
	//	option.setIsNeedAddress(checkGeoLocation.isChecked());
	 	mLocationClient.setLocOption(option);
	}
}
