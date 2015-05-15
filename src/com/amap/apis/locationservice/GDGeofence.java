package com.amap.apis.locationservice;

public class GDGeofence {
	public static final int RADIUS_TYPE_SMALL = 1;
	double mLatitude;
	double mLongitude;
	int mRadius;
	long mIntervalTime;
	String mGeoFenceId;

	public String getGeofenceId() {
		return mGeoFenceId;
	}

	private GDGeofence(Builder builder) {
		mLatitude = builder.mLatitude;
		mLongitude =  builder.mLongitude;
		mRadius =  builder.mRadius;
		mIntervalTime = builder.mIntervalTime;
		mGeoFenceId = builder.mGeofenceId;
	}

	public static class Builder {
		private double mLongitude;

		private double mLatitude;
		// 半径
		private int mRadius;

		private long mIntervalTime;

		private String mGeofenceId;

		
		/**
		 * 创建围栏  
		 */
		public GDGeofence build() {
			return new GDGeofence(this);
		}

		/**
		 * 设置围栏的中心点坐标和半径
		 */
	public	GDGeofence.Builder setCircularRegion(double longitude, double latitude,
				int radius) {
			mLongitude = longitude;
			mLatitude = latitude;
			mRadius = radius;
			return this;
		}
 
	/**
	 * 设置围栏的有效时间
	 */
	public GDGeofence.Builder setExpirationDruation(long intervalTime) {
			mIntervalTime = intervalTime;
			return this;
		}

	/**
	 * 设置围栏唯一id
	 */
		public GDGeofence.Builder setGeofenceId(java.lang.String geofenceId) {
			mGeofenceId = geofenceId;
			return this;
		}
	}
}
