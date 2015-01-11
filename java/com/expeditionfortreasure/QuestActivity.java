package com.expeditionfortreasure;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.expeditionfortreasure.logic.GameLogic;


public class QuestActivity extends ActionBarActivity implements LocationListener{
    GameLogic gl;
    Location loc;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        // Retrieve the button to generate new quests, disable it
        Button btn = (Button) findViewById(R.id.newquest);
        btn.setEnabled(false);


        gl = GameLogic.getInstance();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quest, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();

        // We might want to change this so that it uses both (if available)
        // Inform user if none available

        if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }

        if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
        updateButtons();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newQuest(View view){
        // Problem: Sometimes we don't have a location yet, should fix this (Waiting for location?)

        if(loc != null) {
            // We don't want to generate a quest unless our position is relatively accurate
            if(loc.getAccuracy() < 50) {
                Log.d("Treasure", "My Location" + loc.getLatitude() + "/" + loc.getLongitude());

                gl.newQuest(loc, this);

                Log.d("Treasure", "trs loc " + gl.getCurrentQuest().getTreasure().latitude + "/" + gl.getCurrentQuest().getTreasure().longitude);
            }
        }
        else {
            // Should notify user that he could not be located (toast?)
            Log.d("Treasure", "No location");
        }
        updateButtons();
       // int number = gl.getCurrentQuest().number;
       // Log.v("Quest", "New quest " + number);
    }

    public void abortQuest(View view){

        gl.abortQuest();
        updateButtons();
    }

    public void readQuest(View view){
        Intent intent = new Intent(this, QuestLogActivity.class);

        startActivity(intent);
        updateButtons();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Retrieve button and enable it when we have a new location
        if(location.getAccuracy() < 50) {
            Log.d("QUEST", "New location in QuestListener");
            Button btn = (Button) findViewById(R.id.newquest);
            btn.setEnabled(true);

            // Save location
            loc = location;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("GPS", "Turning off GPS");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    public void updateButtons(){
        Button newbtn, abortbtn;
        newbtn = (Button) findViewById(R.id.newquest);
        abortbtn = (Button) findViewById(R.id.abortquest);

        if(gl.getCurrentQuest() == null){
            newbtn.setEnabled(true);
            abortbtn.setEnabled(false);
        }else {
            newbtn.setEnabled(false);
            abortbtn.setEnabled(true);
        }
    }
}
