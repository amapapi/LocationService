package com.amap.apis.locationservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.amap.api.location.LocationProviderProxy;


/**  
 * 地理围栏使用入口
 */
public class GeofenceClient {

	private Context mContext;

	private boolean mIsStarted = false;

	private Messenger mServerMessenger;

	private Messenger mClientMessenger;

	private ClientServiceConnection mServiceConnection;

	private GeoFenceReceiver mGeoFenceReceiver;

	private OnAddGDGeofencesResultListener mOnAddGDGeofenceResultListener;

	private OnGeofenceTriggerListener mOnGeofenceTriggerListener;

	private OnRemoveGDGeofencesResultListener mOnRemoveGeofenceListener;

	private GeofenceHandler mGeofenceHandler;

	private IntentFilter mIntentFilter = new IntentFilter();

	
	/**  
	 * 地理围栏构造函数
	 */  
	public GeofenceClient(Context context) {
		mContext = context;
		if (Looper.myLooper() == null) {
			mGeofenceHandler = new GeofenceHandler(Looper.getMainLooper(), this);
		} else {
			mGeofenceHandler = new GeofenceHandler(this);
		}
		mGeoFenceReceiver = new GeoFenceReceiver(this);
		mServiceConnection = new ClientServiceConnection(this);

		Intent intent = new Intent(mContext, LocationBackGroundService.class);

		mContext.bindService(intent, mServiceConnection,
				Context.BIND_AUTO_CREATE);

		mClientMessenger = new Messenger(mGeofenceHandler);

	}

	 
	/**  
	 * 是否已经开始定位，围栏的扫描服务是否生效
	 */
	public boolean isStarted() {
		return mIsStarted;
	}


	/**
	 * 开启定位，开启围栏扫描服务  
	 */
	public void start() {
		if (!mIsStarted) {
			mIsStarted = true;
			mContext.registerReceiver(mGeoFenceReceiver, mIntentFilter);

		}
		if (mServerMessenger != null) {
			startLocate();
		}

	}


	/** 
	 * 停止定位， 停止围栏扫描服务
	 */
	public void stop() {
		if (mIsStarted) {
			mIsStarted = false;
			Message message = new Message();
			message.what = LocationBackGroundService.STOP_LOCATE;
			try {
				mServerMessenger.send(message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mContext.unbindService(mServiceConnection);
			mContext.unregisterReceiver(mGeoFenceReceiver);
		}
	}
	/**  
	 * 添加地理围栏
	 */
	public void addGDGeofence(GDGeofence geofence,
			GeofenceClient.OnAddGDGeofencesResultListener listener) {
		mIntentFilter
				.addAction(LocationBackGroundService.GEOFENCE_BROADCAST_ACTION
						+ geofence.mGeoFenceId);
		mContext.registerReceiver(mGeoFenceReceiver, mIntentFilter);
		mOnAddGDGeofenceResultListener = listener;
		Message message = new Message();
		Bundle data = new Bundle();
		data.putDouble(LocationBackGroundService.LATITUDE_KEY,
				geofence.mLatitude);
		data.putDouble(LocationBackGroundService.LONGITUDE_KEY,
				geofence.mLongitude);
		data.putFloat(LocationBackGroundService.GEOFENCE_DISTANCE_KEY,
				geofence.mRadius);
		data.putLong(LocationBackGroundService.DURATION_KEY,
				geofence.mIntervalTime);
		data.putString(LocationBackGroundService.GEOFENCEID_KEY,
				geofence.mGeoFenceId);
		message.replyTo = mClientMessenger;
		message.what = LocationBackGroundService.ADD_GEOFENCE;
		message.setData(data);
		try {
			mServerMessenger.send(message);
			// if (mOnAddGDGeofenceResultListener != null) {
			// mOnAddGDGeofenceResultListener.onAddGDGeofencesResult(
			// GDLocationStatusCodes.SUCCESS, "");
			// }
		} catch (RemoteException e) {
			if (mOnAddGDGeofenceResultListener != null) {
				mOnAddGDGeofenceResultListener.onAddGDGeofencesResult(
						GDLocationStatusCodes.ERROR, "");
			}
			e.printStackTrace();

		}
	}

	
		/** 
		 *  删除地理围栏
		 */
		public void removeGDGeofences(
				java.util.List<java.lang.String> geofenceRequestIds,
				OnRemoveGDGeofencesResultListener listener) {
			mOnRemoveGeofenceListener = listener;
			Message message = new Message();
			Bundle data = new Bundle();
			data.putStringArray(
					LocationBackGroundService.GEOFENCEID_KEY,
					geofenceRequestIds.toArray(new String[geofenceRequestIds.size()]));
			message.replyTo = mClientMessenger;
			message.what = LocationBackGroundService.REMOVE_GEOFENCE;
			message.setData(data);
			try {
				mServerMessenger.send(message);
			} catch (RemoteException e) {

				e.printStackTrace();

			}
		}


	/**  
	 * 注册进入围栏的回调接口
	 */
	public void registerGeofenceTriggerListener(
			OnGeofenceTriggerListener onGeofenceTriggerListener) {
		mOnGeofenceTriggerListener = onGeofenceTriggerListener;
	}

	public static abstract interface OnGeofenceTriggerListener {
		public abstract void onGeofenceTrigger(String paramString);

		public abstract void onGeofenceExit(String paramString);
	}

	public static abstract interface OnAddGDGeofencesResultListener {
		public abstract void onAddGDGeofencesResult(int paramInt,
				String paramString);
	}

	
	public static abstract interface OnRemoveGDGeofencesResultListener {
		public abstract void onRemoveGDGeofencesByRequestIdsResult(
				int paramInt, String[] paramArrayOfString);
	}

	public void startGeofenceScann() {
		mContext.registerReceiver(mGeoFenceReceiver, mIntentFilter);
	}
	private void startLocate() {
		Message message = new Message();
		message.what = LocationBackGroundService.START_LOCATE;
		Bundle bundle = message.getData();
		bundle.putString(LocationBackGroundService.TYPE_KEY,
				LocationProviderProxy.AMapNetwork);
		// 可以根据业务需求修改时间长短
		bundle.putLong(LocationBackGroundService.INTERVAL_KEY, 5 * 1000);
		// 距离
		bundle.putFloat(LocationBackGroundService.DISTANCE_KEY, 0);
		// 是否使用gps
		bundle.putBoolean(LocationBackGroundService.GPS_KEY, true);
		message.setData(bundle);

		try {
			mServerMessenger.send(message);

		} catch (RemoteException e) {

			e.printStackTrace();

		}
	}

	
	private static class GeofenceHandler extends Handler {
		GeofenceClient mGeofenceClient;

		private GeofenceHandler(Looper looper, GeofenceClient geofenceClient) {
			super(looper);
			mGeofenceClient = geofenceClient;
			Looper.prepare();
		}

		public GeofenceHandler(GeofenceClient geofenceClient) {
			super();
			mGeofenceClient = geofenceClient;
		}

		public void handleMessage(Message message) {
			switch (message.what) {
			case LocationBackGroundService.ADD_GEOFENCE:
				Bundle data = message.getData();
				String geofenceID = data
						.getString(LocationBackGroundService.GEOFENCEID_KEY);
				boolean isSuccess = data
						.getBoolean(LocationBackGroundService.GEOFENCE_ADDSTATUS_KEY);

				if (mGeofenceClient.mOnAddGDGeofenceResultListener != null) {
					if (isSuccess) {
						mGeofenceClient.mOnAddGDGeofenceResultListener
								.onAddGDGeofencesResult(
										GDLocationStatusCodes.SUCCESS,
										geofenceID);
					} else {
						mGeofenceClient.mOnAddGDGeofenceResultListener
								.onAddGDGeofencesResult(
										GDLocationStatusCodes.ERROR, geofenceID);
					}
				}

				break;
			case LocationBackGroundService.REMOVE_GEOFENCE:

				Bundle removeData = message.getData();
				String[] removeIDs = removeData
						.getStringArray(LocationBackGroundService.GEOFENCEID_KEY);

				if (mGeofenceClient.mOnRemoveGeofenceListener != null
						&& removeIDs != null && removeIDs.length > 0) {
					mGeofenceClient.mOnRemoveGeofenceListener
							.onRemoveGDGeofencesByRequestIdsResult(
									GDLocationStatusCodes.SUCCESS, removeIDs);
				}
				break;

			}

		}
	}

	private static class GeoFenceReceiver extends BroadcastReceiver {

		private GeofenceClient mGeoFenceClient;

		GeoFenceReceiver(GeofenceClient geofenceClient) {
			mGeoFenceClient = geofenceClient;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			// 根据广播的status来确定是在区域内还是在区域外
			int status = bundle.getInt("status");
			String action = intent.getAction();
			String geofenceID = "";
			String[] splitString = action.split(":");
			if (splitString.length == 2) {
				geofenceID = splitString[1];
			}

			if (status == 0) {
				if (mGeoFenceClient.mOnGeofenceTriggerListener != null) {
					mGeoFenceClient.mOnGeofenceTriggerListener
							.onGeofenceExit(geofenceID);
				}
			} else {
				if (mGeoFenceClient.mOnGeofenceTriggerListener != null) {
					mGeoFenceClient.mOnGeofenceTriggerListener
							.onGeofenceTrigger(geofenceID);
				}
			}

		}

	}

	private static class ClientServiceConnection implements ServiceConnection {
		private GeofenceClient mGeofenceClient;

		ClientServiceConnection(GeofenceClient geofenceClient) {
			mGeofenceClient = geofenceClient;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			mGeofenceClient.mServerMessenger = new Messenger(service);
			if (mGeofenceClient.mIsStarted) {
				mGeofenceClient.startLocate();
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	}	

	// -----------------------修改分割线----------------------------

	// 设置围栏提醒的间隔时间
	void setInterval(long interval) {
	}



}
