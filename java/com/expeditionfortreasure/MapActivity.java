package com.expeditionfortreasure;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import com.expeditionfortreasure.logic.GameLogic;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


public class MapActivity extends ActionBarActivity implements LocationListener{

    // Currently using NETWORK_PROVIDER, We might want to change to GPS or a combination
    final int GPS_ACURRACY = 25;
    final int NETWORK_ACURRACY = 50;

    // The map
    private GoogleMap map;
    // Manager, to control the listener
    private LocationManager locationManager;
    // The applications logic
    private GameLogic gameLogic;
    // Markers
    Marker myLocationMarker;
    Marker treasure;
    // Coordinates
    LatLng myLocationCoordinates;
    // Flag signaling if we are on startup
    boolean starting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Get the map-fragment from layout-file
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Fetch the games logic (and state)
        gameLogic = gameLogic.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Indicate that the application is starting
        Log.d("GPS", "Turning on GPS");
        starting = true;

        // Fetch the our last location to get a faster fix on our new location
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        myLocationCoordinates = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        myLocationMarker = map.addMarker(new MarkerOptions().position(myLocationCoordinates).title("You are here"));

        // Move the camera to the last known location (this will make it seem that the camera starts nearby)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationCoordinates, 15));

        // Start Location updates with high frequency of updates to get a faster fix
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

        // Add a marker for our quest (destination)
        if(gameLogic.getCurrentQuest() != null){
            Log.i("MAP", "Treasure found, adding marker to map");
            LatLng treasureLocation = new LatLng(gameLogic.getCurrentQuest().getTreasure().latitude, gameLogic.getCurrentQuest().getTreasure().longitude);
            treasure = map.addMarker(new MarkerOptions()
                    .position(treasureLocation)
                    .title("Treasure")
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.treasureicon)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        Log.d("GPS", "Turning off GPS");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocationCoordinates = new LatLng(location.getLatitude(), location.getLongitude());

        Log.d("GPS", "Placing marker");

        if(map != null){

            Log.d("GPS","Acc: " + location.getAccuracy());
            // When we got a location accurate enough, request location updates less frequent
            if(location.getAccuracy() < 50 && starting) {
                Log.i("GPS", "GPS accuracy goal achieved");
                Log.i("GPS", "Changing update frequency to once every five seconds");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

                // We  adjust the camera when the app was started and when we have a fixed position
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationCoordinates, 15));

                // We now have a accurate fix on our position, request position less often
                starting = false;
            }

            Log.d("GPS", "Setting marker");

            // Change the position of the marker representing our location
            myLocationMarker.setPosition(myLocationCoordinates);

            // Should fetch coordinates from the game logic
            LatLng treasure = new LatLng(59.323678,18.047787); // Sjön
            // Testing adding

            String addressName = "";

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                    addressName = listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(location.getAccuracy() < 50){

                // We actually have a quest
                if(gameLogic.getCurrentQuest() != null){
                    treasure = new LatLng(gameLogic.getCurrentQuest().getTreasure().latitude, gameLogic.getCurrentQuest().getTreasure().longitude);
                    Log.d("QUEST", "Distance - Formula: " + CalculationByDistance(myLocationCoordinates, treasure));
                    Log.d("QUEST", "My coordinates " + location.getLatitude() + ", " + location.getLongitude());
                    Log.d("QUEST", "Quest coordinates " + gameLogic.getCurrentQuest().getTreasure().latitude + ", " + gameLogic.getCurrentQuest().getTreasure().longitude);
                }
            }
//            try {
//                List<Address> treasureAdress = geocoder.getFromLocation(treasure.latitude, treasure.longitude, 1);
//                if (treasureAdress != null && treasureAdress.size() > 0){
//                    if (treasureAdress.get(0).getPostalCode() == null) {
//                        Log.d("GPS", "Not a valid address (No PostalCode)");
//                    } else {
//                        Log.d("GPS", treasureAdress.get(0).getPostalCode());
//                        if (treasureAdress != null && treasureAdress.size() > 0) {
//                            Log.d("GPS", "The treasue is located on: " + treasureAdress.get(0).getAddressLine(0));
//                            Marker treasuerMarker = map.addMarker(new MarkerOptions().position(treasure).title(treasureAdress.get(0).getAddressLine(0)));
//                        }
//                    }
//                }else{
//                    Log.d("GPS", "Not a valid address(No matches)");
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//
//            Log.d("GPS","The address is: " + addressName);
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
//        Log.i("QUEST",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);

        return Radius * c;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
