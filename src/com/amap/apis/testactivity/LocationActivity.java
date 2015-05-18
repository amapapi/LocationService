package com.amap.apis.testactivity;

 

import android.app.Activity;
import android.os.Bundle;
 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	private RadioGroup selectMode;
	private EditText frequence;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private GDLocationListener mGDLocationListener=new GDLocationListener() {
		
		@Override
		public void onReceiveLocation(GDLocation location) {
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nloctype : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == location.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				 
			} else if (location.getLocType() == location.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			 LocationResult.setText(sb.toString());
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
	 
		mLocationClient=new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(mGDLocationListener);
		LocationResult = (TextView) findViewById(R.id.textView1);
		ModeInfor = (TextView) findViewById(R.id.modeinfor);
		ModeInfor.setText(getString(R.string.hight_accuracy_desc));
		frequence = (EditText) findViewById(R.id.frequence);
	 
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
				
				} else {
					 
					mLocationClient.stop();
					startLocation.setText(getString(R.string.startlocation));
				}

			}
		});
		selectMode = (RadioGroup) findViewById(R.id.selectMode);
		 
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
	 
	}

	@Override
	protected void onStop() {		
	 
		mLocationClient.stop();

		super.onStop();
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		 
		int span = 1000;
		try {
			span = Integer.valueOf(frequence.getText().toString());
		} catch (Exception e) {
	 
		}
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
 
	 	mLocationClient.setLocOption(option);
	}
}
