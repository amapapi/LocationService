# LocationService
使用高德地图android定位sdk进行封装，以remote service方式提供定位功能
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