package com.amap.apis.locationservice;

import android.location.LocationManager;



import com.amap.api.location.LocationProviderProxy;

//TODO 设置默认状态
public class LocationClientOption {
	private static final String battery_saving = "Battery_Saving";
	private static final String hight_accuracy = "Hight_Accuracy";
	private static final String device_sensors = "Device_Sensors";

	private boolean mIsOpenGPS = false;

//	private boolean mIsNeedAddress = false;
//
//	private boolean mIsNeedDeviceDirect = false;

	private LocationMode mLocationMode = LocationMode.Battery_Saving;

	private int mTimeInterval = 2000;

	// 是否打开gps进行定位
	public void setOpenGps(boolean openGPS) {
		mIsOpenGPS = openGPS;
	}

	// 是否打开gps进行定位
	public boolean isOpenGps() {

		return mIsOpenGPS;
	}

	
	

	// 设置定位模式
	public void setLocationMode(LocationMode locationMode) {
		mLocationMode = locationMode;
	}

	LocationClientOption.LocationMode getLocationMode() {

		return mLocationMode;
	}

	

	// 设置定位时间间隔，单位是毫秒
	public void setScanSpan(int timeInterval) {
		mTimeInterval = timeInterval;
	}

	// 获取定位时间间隔，单位是毫秒
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
	 
	
	// ------------------------------------修改分割线---------------------------------

//	 void setIsNeedAddress(boolean isNeed) {
//		mIsNeedAddress = isNeed;
//	}
//	// 在网络定位时，是否需要设备方向
//		public void setNeedDeviceDirect(boolean isNeedDeviceDirect) {
//			mIsNeedDeviceDirect = isNeedDeviceDirect;
//		}




	public String getAddrType() {
		return null;
	}

	public boolean isLocationNotify() {
		return false;
	}

	// 设置是否进行异常捕捉
	  void setIgnoreCacheException(boolean cacheException) {

	}

	// 设置是否退出定位进程
	  void setIgnoreKillProcess(boolean killProcess) {

	}

	  void setLocationNotify(boolean notify) {

	}

	//
	// public int getTimeOut() {
	//
	// return 0;
	// }
	// //
	// public void setTimeOut(int timeOut) {
	//
	// }

	// 不同坐标系不用考虑

	//
	// java.lang.String getCoorType() {
	//
	// return null;
	// }
	//
	// // 设置坐标类型
	// public void setCoorType(String coorType) {
	//
	// }

	// // 设置Prod字段值
	// public void setProdName(java.lang.String prodName) {
	//
	// }
	// public String getProdName() {
	// return null;
	// }

}
