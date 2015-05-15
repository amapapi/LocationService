# LocationService
使用高德地图android定位sdk进行封装，以remote service方式提供定位功能
##配置工程
- 高德lbs官网申请key [申请地址](http://lbs.amap.com/console/)
- 在mainfest进行一下配置  

1：配置定位所需权限  

 ``` java  
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_CONFIGURATION" />
    <uses-permission android:name="android.permission.WAKE_LOCK" /> 
    <uses-permission android:name="android.permission.WRITE_SETTINGS" /> 
```    

  2：配置所需key  

  ``` java   
     <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="您申请的key" />
```    
  
  3：配置定位service  

  ``` java     
     <service 
            android:process=":remote"
            android:name="com.amap.apis.locationservice.LocationBackGroundService">
```    
  完成以上操作后可以进行定位的开发			

##定位功能

* 实例化定位Client

``` java
mLocationClient = new LocationClient(getApplicationContext());
``` 

* 设置定位参数

``` java
	    LocationClientOption locationOption = new LocationClientOption();
		//设置定位间隔
		locationOption.setScanSpan(2000);
		//设置定位模式，其他模式见LocationMode
		locationOption.setLocationMode(LocationMode.Battery_Saving);	
		mLocationClient.setLocOption(locationOption);
``` 

* 设置定位监听
``` java
	GDLocationListener gdLocationListener = new GDLocationListener() {

			@Override
			public void onReceiveLocation(GDLocation location) {
				Log.i("location",
						 
						
						location.getAddrStr() + "  " + location.getCity()
								+ "  " + location.getCityCode() + "  "
								+ location.getDistrict() + "  "
								+ location.getLatitude() + "  "
								+ location.getLongitude()+"  "
								+location.getStreet()+"   "
								+location.getRadius()+"  "
								+location.getProvince()+"  "
								+location.getSpeed()
				);

			}
		};

		mLocationClient.registerLocationListener(gdLocationListener);
``` 	
* 开始定位
``` java
	//开启定位
	mLocationClient.start();	
``` 
* 停止定位
``` java
mLocationClient.stop();	
``` 
##地理围栏功能

* 实例化地理围栏Client
``` java
mGeofenceClient=new GeofenceClient(getApplicationContext());
```

* 设置地理围栏监听
``` java
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
```

* 添加地理围栏
``` java
	//创建地理围栏，设置地理围栏id,范围和有效时间
			GDGeofence gdGeofence=new GDGeofence.Builder().
			setGeofenceId("testGeofence")
			.setCircularRegion(116.480829, 39.989614, 1000).setExpirationDruation(1000*60)
			.build();
			//添加地理围栏，注意同一个地理围栏id只能添加一次
			mGeofenceClient.addGDGeofence(gdGeofence, new OnAddGDGeofencesResultListener() {
```

* 开启地理围栏扫描

``` java
mGeofenceClient.start();
```
* 停止地理围栏扫描
``` java
mGeofenceClient.stop();
```
##注意事项

- LocationClient与GeofenceClient的start()方法会启动remote service，因此注意在合适的业务场景和生命周期中调用相对的stop()方法停止后台的定位进程  
- 设置LocationClient的LocationClientOption时，最终定位会以第一个最先设置的参数生效  
- 目前地理围栏支持单个listener的回调，即已设置的最后一个listener进行回调，如果有类似定位LocationClient多个listener回调需求，后续会增加此功能的支持  

