package com.expeditionfortreasure;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Should have some form of Accuracy control (Should also add last known location for faster fix)
// We could/should use both Network and GPS provider.

// We should also adjust the speed at which we check our position, after we have a certain accuracy
// we should request locationUpdates less frequent (Ex. 0 to 5 sec & 0 to 25 meters)

// We must also deregister this listener

// Should setPosition of marker if it is already created

// Geocoding is apparently resource intensive

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


            //59.323678, 18.047787

            // Should fetch coordinates from the game logic
            LatLng treasure = new LatLng(59.323678,18.047787); // Sjön
//            LatLng treasure = new LatLng(myCoordinates.latitude+11,myCoordinates.longitude);

            // Testing adding

            String addressName = "";

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                    addressName = listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                List<Address> treasureAdress = geocoder.getFromLocation(treasure.latitude, treasure.longitude, 1);
                if (treasureAdress != null && treasureAdress.size() > 0){
                    if (treasureAdress.get(0).getPostalCode() == null) {
                        Log.d("GPS", "Not a valid address (No PostalCode)");
                    } else {
                        Log.d("GPS", treasureAdress.get(0).getPostalCode());
                        if (treasureAdress != null && treasureAdress.size() > 0) {
                            Log.d("GPS", "The treasue is located on: " + treasureAdress.get(0).getAddressLine(0));
                            Marker treasuerMarker = map.addMarker(new MarkerOptions().position(treasure).title(treasureAdress.get(0).getAddressLine(0)));
                        }
                    }
                }else{
                    Log.d("GPS", "Not a valid address(No matches)");
                }
            }catch (IOException e){
                e.printStackTrace();
            }

            Log.d("GPS","The address is: " + addressName);
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
