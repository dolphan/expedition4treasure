package com.expeditionfortreasure;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.expeditionfortreasure.file.FileHandling;
import com.expeditionfortreasure.logic.GameLogic;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


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
    boolean initGPS;

    Location currentBestLocation;
    private static final int MINUTE = 1000 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Get the map-fragment from layout-file
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Fetch the games logic (and state)
        gameLogic = gameLogic.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Indicate that the application is starting
        Log.d("GPS", "Turning on GPS");
        starting = true;
        initGPS = true;
        myLocationMarker = null;

        // Fetch the our last location to get a faster fix on our new location

        if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {

            // Check which providers are available and request updates if they are

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                // Using NETWORK_PROVIDER for a fast fix and early location updates
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                myLocationCoordinates = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                // Add the marker, removes old marker if we resumed the activity
                myLocationMarker = map.addMarker(new MarkerOptions()
                        .position(myLocationCoordinates)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.myloc)));

                // Move the camera to the last known location (this will make it seem that the camera starts nearby)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationCoordinates, 15));

                // Start Location updates with high frequency of updates to get a faster fix
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            }

            // If GPS is enabled, start location updates
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }

        }else{
            Log.d("GPS", "No Provider enabled");
            // Should inform user that no provider was found
            Toast questCompleteToast = Toast.makeText(this, "No Service Provider could be found, please enable your network provider or GPS", Toast.LENGTH_LONG);
            questCompleteToast.show();
        }

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
        if(myLocationMarker != null)
            myLocationMarker.remove();
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocationCoordinates = new LatLng(location.getLatitude(), location.getLongitude());

        if(map != null){
            if(location.getProvider().equals(locationManager.GPS_PROVIDER)){

                // When the gps has established a good fix on our location, update less frequent
                if(location.getAccuracy() < 25 && initGPS){
                    Log.i("GPS", "GPS accuracy goal achieved");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
                    initGPS = false;
                }

            }

            if(location.getProvider().equals(locationManager.NETWORK_PROVIDER)){

                // When the Network provider has established a good fix on our location
                // update less frequent
                if(location.getAccuracy() < 50 && starting) {
                    Log.i("GPS", "Changing update frequency to once every five seconds");

                    // When we have an accurate enough fix, request updates less frequent
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

                    // We  adjust the camera when the app was started and when we have a fixed position
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationCoordinates, 15));

                    // We now have a accurate fix on our position, request position less often
                    starting = false;
                }

            }

            // Check if the new location is better
            if(isBetterLocation(location)){
                // If it is, set as new best location
                currentBestLocation = location;

                // We want a minimum accuracy of 50 meters
                if(location.getAccuracy() < 50){

                    // Change the position of the marker representing our location
                    myLocationMarker.setPosition(myLocationCoordinates);

                    // If we have a quest active, calculate distance to it
                    if(gameLogic.getCurrentQuest() != null){

                        // Get active quests coordinates
                        LatLng treasure = new LatLng(gameLogic.getCurrentQuest().getTreasure().latitude, gameLogic.getCurrentQuest().getTreasure().longitude);

                        Log.d("QUEST", "Calculating distance");

                        // If we are closer than 20 meters, complete quest
                        if(calculateDistance(myLocationCoordinates, treasure) <= 0.020){
                            Toast questCompleteToast = Toast.makeText(this, "Quest Complete, rewarded " + gameLogic.getCurrentQuest().reward + " gold", Toast.LENGTH_LONG);
                            questCompleteToast.show();
                            gameLogic.completeQuest();
                            FileHandling.saveFile(this, gameLogic);
                        }
                    }
                }
            }
        }
    }

    public boolean isBetterLocation(Location loc){
        if(currentBestLocation == null)
            return true;

        // Check whether the new location fix is newer or older
        long timeDelta = loc.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > MINUTE;
        boolean isSignificantlyOlder = timeDelta < -MINUTE;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (loc.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        // Max reduction in accuracy
        boolean isSignificantlyLessAccurate = accuracyDelta > 50;

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate) {
            return true;
        }
        return false;
    }

    public double calculateDistance(LatLng startCoordinate, LatLng destinationCoordinate) {

        // Mean earth radius in km (Does not take location into consideration, earth is not a perfect sphere)
        // According to Wikipedia, this results in an error margin of 0.5%

        int Radius = 6371;

        // Get lat/long
        double lat1 = startCoordinate.latitude;
        double lon1 = startCoordinate.longitude;
        double lat2 = destinationCoordinate.latitude;
        double lon2 = destinationCoordinate.longitude;

        // Calculate difference
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);

        // Great Circle Distance (Computational formula (Wikipedia)
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        // Angle
        double c = 2 * Math.asin(Math.sqrt(a));

        // Distance
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
