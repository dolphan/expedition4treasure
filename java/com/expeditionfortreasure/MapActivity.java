package com.expeditionfortreasure;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends ActionBarActivity {

    // Currently using NETWORK_PROVIDER, We might want to change to GPS or a combination

    private GoogleMap map;
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private LocationManager locationManager;
    private MyLocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Create our LocationListener
        locationListener = new MyLocationListener(getFragmentManager(), this);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Turning on GPS (Starting to request location)
        Log.d("GPS", "Turning on GPS");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void makeUseOfNewLocation(Location location) {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        final LatLng MyLocation = new LatLng(location.getLatitude(), location.getLongitude());

        Log.d("GPS", "Trying to place marker");

        if(map != null){
            Marker hamburg  = map.addMarker(new MarkerOptions().position(MyLocation).title("You are here"));
        }

//        Log.i("GPS", "Acc: " + location.getAccuracy());
//        Log.i("GPS", "Current Location: " + "N (Latitude) " + location.getLatitude() + " W (Longitude) " + location.getLongitude());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("GPS","Turning off GPS");
        locationManager.removeUpdates(locationListener);
        locationListener.setStarting(true);
    }
}
