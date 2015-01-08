package com.expeditionfortreasure;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// Should have some form of Accurracy control (Should also add last known location for faster fix)
// We could/should use both Network and GPS provider.

// We should also adjust the speed at which we check our position, after we have a certain accuracy
// we should request locationUpdates less frequent (Ex. 0 to 5 sec & 0 to 25 meters)

// We must also deregister this listener

public class MyLocationListener implements LocationListener {

    FragmentManager manager;
    Marker myLocation;
    private GoogleMap map;
    Context context;

    boolean starting;

    MyLocationListener(FragmentManager manager, Context test){
        this.manager = manager;
        this.context = test;
        this.starting = true;
    }

    public void setStarting(boolean startup){
        this.starting = startup;
    }

    protected void makeUseOfNewLocation(Location location) {
        map = ((MapFragment) manager.findFragmentById(R.id.map)).getMap();

        final LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());

        Log.d("GPS", "Placing marker");

        if(map != null){
            if(myLocation != null){
                myLocation.remove();
            }

            Log.d("GPS","Acc: " + location.getAccuracy());
            // When we got a location accurate enough, request location updates less frequent
            if(location.getAccuracy() < 50 && starting) {
                Log.d("GPS", "Changing update frequency to once every five seconds");
                LocationManager locationManager =
                        (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

                // We  adjust the camera when the app was started and when we have a fixed position
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, 15));
                map.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
                map.animateCamera(CameraUpdateFactory.zoomTo(15));

                starting = false;
            }

            // Always add a marker representing the users position
            myLocation = map.addMarker(new MarkerOptions().position(myCoordinates).title("You are here"));
        }
    }

	@Override
	public void onLocationChanged(Location location) {

        // When our location has changed, replace marker
        makeUseOfNewLocation(location);

        // If we want to print long and lat
//		Log.i("GPS", "Acc: " + location.getAccuracy());
//		Log.i("GPS","Long: " + location.getLongitude() + ", Lati: " + location.getLatitude());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}
