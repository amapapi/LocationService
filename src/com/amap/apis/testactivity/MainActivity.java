package com.amap.apis.testactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amap.location.demo.R;

public class MainActivity extends Activity implements OnClickListener {

	private Button mLocationButton;
	private Button mServiceButton;	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationButton = (Button) findViewById(R.id.start_button);
		mServiceButton = (Button) findViewById(R.id.geofence_button);
		mLocationButton.setOnClickListener(this);
		mServiceButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_button:
			Intent intent = new Intent(MainActivity.this,
					LocationActivity.class);
			startActivity(intent);
			break;
		case R.id.geofence_button:
			Intent geofenceIntent = new Intent(MainActivity.this,
					GeoFenceActivity.class);
			startActivity(geofenceIntent);
			break;

		}

	}

}
