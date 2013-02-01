package com.example.googlemaptest;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MainActivity extends MapActivity implements LocationListener {

	private LocationManager mLocationManager;
	MapView map;
	MapController mc;
	ArrayList<PinItemizedOverlay> pinItemList = new ArrayList<PinItemizedOverlay>();
	Drawable pin;
    PinItemizedOverlay pinOverlay;
    
	//シミュレーション用変数
	double lat = 35.681390;
	double lon = 139.766040;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		setContentView(R.layout.activity_main);
		map = (MapView)findViewById(R.id.mapview);
		mc = map.getController();
		// タッチ操作を可能にする
		map.setEnabled(true);
        map.setClickable(true);
        // ズーム操作を可能にする
        map.setBuiltInZoomControls(true);
		GeoPoint point = new GeoPoint(35681099, 139767084);
	    mc.setCenter(point);
	    mc.setZoom(15);
	    
	 // 画像を地図上に配置するオーバーレイ
//	    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//	    MyOverlay overlay = new MyOverlay(bmp, new GeoPoint(35681099, 139767084));
//	    List<Overlay> list = map.getOverlays();
//	    list.add(overlay);
	    
	    pin = getResources().getDrawable( R.drawable.pin);
        pinOverlay = new PinItemizedOverlay( pin);
        
	    map.getOverlays().add( pinOverlay);
        
        //pinOverlayの管理
        pinItemList.add(pinOverlay);
        
        //pinを指す
//        GeoPoint tokyo = new GeoPoint( 35681396, 139766049);
//        addPin(tokyo);
       //pinを消す
//        clearPin(pinOverlay);
//        LocationListLoader lll = new LocationListLoader(this, "1","35,681396","139,766049");
//        LocationListLoader lll = new LocationListLoader(this, "2","35.781396","139.566049");
//        lll.forceLoad();
        
	}
	
	public void addPin(GeoPoint point){
        map.getOverlays().add( pinOverlay);
        pinOverlay.addPoint( point);
	}
	
	@Override
	protected void onResume() {
		if (mLocationManager != null) {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					// LocationManager.NETWORK_PROVIDER,
					2000, 1000, this);
		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(this);
		}

		super.onPause();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v("----------", "----------");
		Log.v("Latitude", String.valueOf(location.getLatitude()));
		Log.v("Longitude", String.valueOf(location.getLongitude()));
		Log.v("Accuracy", String.valueOf(location.getAccuracy()));
		Log.v("Altitude", String.valueOf(location.getAltitude()));
		Log.v("Time", String.valueOf(location.getTime()));
		Log.v("Speed", String.valueOf(location.getSpeed()));
		Log.v("Bearing", String.valueOf(location.getBearing()));
		lat += 0.01;
		lon += 0.01;
		map.getOverlays().clear();
		LocationListLoader lll = new LocationListLoader(this, "2",String.valueOf(lat),String.valueOf(lon));
        lll.forceLoad();
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.AVAILABLE:
			Log.v("Status", "AVAILABLE");
			break;
		case LocationProvider.OUT_OF_SERVICE:
			Log.v("Status", "OUT_OF_SERVICE");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v("Status", "TEMPORARILY_UNAVAILABLE");
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
}
