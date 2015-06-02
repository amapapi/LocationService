package com.amap.apis.locationservice;

import android.location.LocationManager;



import com.amap.api.location.LocationProviderProxy;

//TODO 设置默认状态
public class LocationClientOption {
	private static final String battery_saving = "Battery_Saving";
	private static final String hight_accuracy = "Hight_Accuracy";
	private static final String device_sensors = "Device_Sensors";

	private boolean mIsOpenGPS = false;
	
	private LocationMode mLocationMode = LocationMode.Battery_Saving;

	private int mTimeInterval = 2000;
	
	boolean mIsKillProcess=true;

	/**  
	 * 是否打开gps进行定位
	 */
	public void setOpenGps(boolean openGPS) {
		mIsOpenGPS = openGPS;
	}
 
	/**  
	 * 是否打开gps进行定位
	 */
	public boolean isOpenGps() {

		return mIsOpenGPS;
	}

	/**  
	 * 设置定位模式
	 */
	public void setLocationMode(LocationMode locationMode) {
		mLocationMode = locationMode;
	}

	LocationClientOption.LocationMode getLocationMode() {

		return mLocationMode;
	}

	/**  
	 * 设置定位时间间隔，单位是毫秒
	 */
	public void setScanSpan(int timeInterval) {
		mTimeInterval = timeInterval;
	}

	/**  
	 * 获取定位时间间隔，单位是毫秒
	 */
	public int getScanSpan() {
		return mTimeInterval;
	}

	/**  
	 * Battery_Saving：省电模式，主要使用基站和网络定位
	 * Device_Sensors：仅使用系统的gps设备进行定位
	 * Hight_Accuracy：高精度模式会采用gps加基站网络定位方式进行混合定位，优先选取精度高的gps>wifi>基站进行定位
	 */
	public static enum LocationMode {

		Battery_Saving(battery_saving), Device_Sensors(device_sensors), Hight_Accuracy(
				hight_accuracy);
		private String mMode;

		private String getMode() {
			return this.mMode;
		}

		private LocationMode(String mode) {
			this.mMode = mode;
		}

	}

	 /**
	  * 设置是否退出定位进程
	  * */
	 public void setIgnoreKillProcess(boolean isKillProcess)
	  {
	    this.mIsKillProcess = isKillProcess;
	  }
	String getlocationType() {
		String locationType = LocationProviderProxy.AMapNetwork;
		switch (mLocationMode) {
		case Hight_Accuracy:
			locationType = LocationProviderProxy.AMapNetwork;
			mIsOpenGPS = true;
			break;
		case Battery_Saving:
			locationType = LocationProviderProxy.AMapNetwork;
			mIsOpenGPS = false;
			break;
		case Device_Sensors:
			locationType = LocationManager.GPS_PROVIDER;
			mIsOpenGPS = true;
			break;
		}
		return locationType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (mIsOpenGPS ? 1231 : 1237);
		result = prime * result
				+ ((mLocationMode == null) ? 0 : mLocationMode.hashCode());
		result = prime * result + mTimeInterval;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationClientOption other = (LocationClientOption) obj;
		if (mIsOpenGPS != other.mIsOpenGPS)
			return false;
		if (mLocationMode != other.mLocationMode)
			return false;
		if (mTimeInterval != other.mTimeInterval)
			return false;
		return true;
	}
	 
	
 

}
