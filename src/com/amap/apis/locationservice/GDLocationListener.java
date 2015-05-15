package com.amap.apis.locationservice;

/**  
 * 定位回调监听接口
 */
public interface GDLocationListener {
	
	/**  
	 * 定位回调接口
	 */
	public void onReceiveLocation(GDLocation location);
}
