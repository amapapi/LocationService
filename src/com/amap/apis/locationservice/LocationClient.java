package com.amap.apis.locationservice;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

/**  
 */
public class LocationClient {

	private Context mContext;
	// 默认的定位参数
	private LocationClientOption mLocationClientOption = new LocationClientOption();

	private ClientHandler mClientHandler;

	private Messenger mServerMessenger;

	private Messenger mClientMessenger;

	private ServiceConnection mServiceConnection;

	private volatile boolean mIsStarted = false;

	private ArrayList<GDLocationListener> mlocationListeners = new ArrayList<GDLocationListener>();

	/**
	 * 构造函数
	 */
	public LocationClient(Context context) {
		mContext = context;
		if (Looper.myLooper() == null) {
			mClientHandler = new ClientHandler(Looper.getMainLooper(), this);
		} else {
			mClientHandler = new ClientHandler(this);
		}
		mClientMessenger = new Messenger(mClientHandler);
	}

	/**
	 * 设置 LocationClientOption，主要用于设置定位的一些基本参数
	 */
	public void setLocOption(LocationClientOption locationOption) {
		mLocationClientOption = locationOption;
	}

	/**
	 * client是否started,用于判断是否已经开启了定位
	 */
	public boolean isStarted() {
		return mIsStarted;
	}

	/**
	 * 启动定位
	 */
	public void start() {
		if (!mIsStarted) {
			mIsStarted = true;
			mServiceConnection = new ClientServiceConnection(this);
			Intent intent = new Intent(mContext,
					LocationBackGroundService.class);
			mContext.bindService(intent, mServiceConnection,
					Context.BIND_AUTO_CREATE);
		} else {
			if (mServerMessenger != null) {
				// 调用后台service的定位，传递定位的参数
				startLocate();
			}
		}
	}

	/**
	 * 停止定位，会unbinder后台运行的service
	 */
	public void stop() {
		if (mIsStarted) {
			mIsStarted = false;
			Message message = Message.obtain();
			message.what = LocationBackGroundService.STOP_LOCATE;
			message.replyTo = mClientMessenger;
			try {
				mServerMessenger.send(message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			mContext.unbindService(mServiceConnection);
		}
	}

	/**
	 * 获取定位设置的参数
	 */
	public LocationClientOption getLocOption() {
		return mLocationClientOption;
	}

	/**
	 * 获取定位sdk版本信息
	 */
	public String getVersion() {
		return LocationManagerProxy.getVersion();
	}

	/**
	 * 注册定位监听函数
	 */
	public synchronized void registerLocationListener(
			GDLocationListener gdLocationListener) {
		if (!mlocationListeners.contains(gdLocationListener)) {
			mlocationListeners.add(gdLocationListener);
		}

	}

	/**
	 * 取消之前注册的定位监听函数，可以根据自己的业务需求自行决定是否unbinder后台的service
	 */
	public synchronized void unRegisterLocationListener(
			GDLocationListener listener) {
		mlocationListeners.remove(listener);

	}

	/**
	 * 返回最近一次的定位结果，如果app从未进行定位或定位成功，返回null
	 * 
	 */
	public GDLocation getLastKnownLocation() {
		AMapLocation gpsLocation = LocationManagerProxy.getInstance(mContext)
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		AMapLocation netWorkLocation = LocationManagerProxy.getInstance(
				mContext)
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		AMapLocation amapLocation = LocationManagerProxy.getInstance(mContext)
				.getLastKnownLocation(LocationProviderProxy.AMapNetwork);
		AMapLocation lastLocation = gpsLocation;
		if ((lastLocation == null)
				|| (netWorkLocation != null && netWorkLocation.getTime() > lastLocation
						.getTime())) {
			lastLocation = netWorkLocation;
		}
		if ((lastLocation == null)
				|| (amapLocation != null && amapLocation.getTime() > lastLocation
						.getTime())) {
			lastLocation = amapLocation;
		}
		if (lastLocation == null) {
			return null;
		}
		GDLocation gdLocation = new GDLocation();
		gdLocation.setAMapLocaion(lastLocation);

		return gdLocation;
	}

	private static class ClientServiceConnection implements ServiceConnection {
		private LocationClient mLocationClient;

		ClientServiceConnection(LocationClient locationClient) {
			mLocationClient = locationClient;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			mLocationClient.mServerMessenger = new Messenger(service);
			mLocationClient.startLocate();

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	}

	/**
	 * 处理后台service的回调
	 */
	private static class ClientHandler extends Handler {
		LocationClient mLocationClient;

		private ClientHandler(Looper looper, LocationClient locationClient) {
			super(looper);
			mLocationClient = locationClient;
			Looper.prepare();
		}

		public ClientHandler(LocationClient locationClient) {
			super();
			mLocationClient = locationClient;
		}

		public void handleMessage(Message message) {

			switch (message.what) {
			case LocationBackGroundService.ON_LOCATION:
				Bundle data = message.getData();
				data.setClassLoader(GDLocation.class.getClassLoader());
				Parcelable parcel = data
						.getParcelable(LocationBackGroundService.LOCATION_KEY);
				GDLocation gdLocation = (GDLocation) parcel;
				mLocationClient.onLocation(gdLocation);
				break;
			case LocationBackGroundService.ON_STOP:
				break;
			}
		}

	}

	private void startLocate() {
		// 第一次binder之后，开启定位
		Message message =  Message.obtain();
		message.what = LocationBackGroundService.START_LOCATE;
		Bundle bundle = message.getData();
		bundle.putString(LocationBackGroundService.TYPE_KEY,
				mLocationClientOption.getlocationType());

		bundle.putLong(LocationBackGroundService.INTERVAL_KEY,
				mLocationClientOption.getScanSpan());
		bundle.putFloat(LocationBackGroundService.DISTANCE_KEY, 0);

		bundle.putBoolean(LocationBackGroundService.GPS_KEY,
				mLocationClientOption.isOpenGps());
		bundle.putBoolean(LocationBackGroundService.KILL_PROCESS_KEY, 
				mLocationClientOption.mIsKillProcess
				);
		
		message.setData(bundle);
		message.replyTo = mClientMessenger;
		try {
			mServerMessenger.send(message);
		} catch (RemoteException e) {

			e.printStackTrace();

		}
	}

	// TODO 需要仔细考虑下是不是 需要同步
	private synchronized void onLocation(GDLocation gdLocation) {
		for (GDLocationListener gdLocationListener : mlocationListeners) {
			gdLocationListener.onReceiveLocation(gdLocation);
		}
	}

	
}
