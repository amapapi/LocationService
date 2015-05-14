package com.amap.apis.locationservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amap.apis.locationservice.GeofenceClient.OnGeofenceTriggerListener;
import com.amap.apis.testactivity.GeoFenceActivity;
import com.amap.apis.testactivity.LocationActivity;
import com.amap.location.demo.R;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private LocationClient mLocationClient;

	private Button mStartButton;
	private Button mStopButton;
	
	
	private GeofenceClient mGeofenceClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mStartButton = (Button) findViewById(R.id.start_button);
		mStopButton = (Button) findViewById(R.id.stop_button);
		mStartButton.setOnClickListener(this);
		mStopButton.setOnClickListener(this);

//		mGeofenceClient=new GeofenceClient(getApplicationContext());
//		
		//设置地理围栏的监听
		mGeofenceClient.registerGeofenceTriggerListener(new OnGeofenceTriggerListener() {
			
			@Override
			public void onGeofenceTrigger(String geofenceID) {
				
			 Log.i("location", "in the geofence"+geofenceID);
				
			}
			
			@Override
			public void onGeofenceExit(String geofenceID) {
				
				 Log.i("location", "out of the geofence"+geofenceID); 
				
			}
		});
		
		mGeofenceClient.start();
//		
//		mLocationClient = new LocationClient(getApplicationContext());
//		GDLocationListener gdLocationListener = new GDLocationListener() {
//
//			@Override
//			public void onReceiveLocation(GDLocation location) {
//				Log.i("location",
//						 
//						
//						location.getAddrStr() + "  " + location.getCity()
//								+ "  " + location.getCityCode() + "  "
//								+ location.getDistrict() + "  "
//								+ location.getLatitude() + "  "
//								+ location.getLongitude()+"  "
//								+location.getStreet()+"   "
//								+location.getRadius()+"  "
//								+location.getProvince()+"  "
//								+location.getSpeed()
//				);
//
//			}
//		};
//
//		mLocationClient.registerLocationListener(gdLocationListener);
//
//		LocationClientOption locationOption = new LocationClientOption();
//		//设置定位间隔
//		locationOption.setScanSpan(2000);
//		//设置定位模式，其他模式见LocationMode
//		locationOption.setLocationMode(LocationMode.Battery_Saving);
//		
//		mLocationClient.setLocOption(locationOption);
//
	}

	protected void onStop() {
		//mLocationClient.stop();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_button:

	 
			//创建地理围栏，设置地理围栏id,范围和有效时间
			GDGeofence gdGeofence=new GDGeofence.Builder().
			setGeofenceId("testGeofence")
			.setCircularRegion(116.480829, 39.989614, 1000).setExpirationDruation(1000*60)
			.build();
			//添加地理围栏，注意同一个地理围栏id只能添加一次
			mGeofenceClient.addGDGeofence(gdGeofence, new OnAddGDGeofencesResultListener() {
//				
//				@Override
//				public void onAddGDGeofencesResult(int statusCode, String geofenceID) {
//					Log.i("yiyi.qi", statusCode+"   "+geofenceID);
//					 
//					
//				}
//			});
//			
//			
//			GDGeofence gdGeofence1=new GDGeofence.Builder().
//					setGeofenceId("testGeofence1")
//					.setCircularRegion(116.480829, 39.989614, 6000).setExpirationDruation(1000*60)
//					.build();
//					mGeofenceClient.addGDGeofence(gdGeofence1, new OnAddGDGeofencesResultListener() {
//						
//						@Override
//						public void onAddGDGeofencesResult(int statusCode, String geofenceID) {
//							Log.i("yiyi.qi", statusCode+"   "+geofenceID);
//							 
//							
//						}
//					});
//			
//					
//					GDGeofence gdGeofence2=new GDGeofence.Builder().
//							setGeofenceId("testGeofence2")
//							.setCircularRegion(115.480829, 39.189614, 6000).setExpirationDruation(1000*60)
//							.build();
//							mGeofenceClient.addGDGeofence(gdGeofence2, new OnAddGDGeofencesResultListener() {
//								
//								@Override
//								public void onAddGDGeofencesResult(int statusCode, String geofenceID) {
//									Log.i("yiyi.qi", statusCode+"   "+geofenceID);
//									 
//									
//								}
//							});	
					

			//开启定位
			mLocationClient.start();
			Intent intent=new Intent(MainActivity.this,LocationActivity.class);
			startActivity(intent);
			
			break;
		case R.id.stop_button:
			Intent geofenceIntent=new Intent(MainActivity.this,GeoFenceActivity.class);
			startActivity(geofenceIntent);
		//	mGeofenceClient.stop();
			//mLocationClient.stop();
			break;

		}

	}
	
	
 
}
