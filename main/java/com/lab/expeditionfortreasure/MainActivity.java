package com.lab.expeditionfortreasure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lab.expeditionfortreasure.logic.GameLogic;
import com.lab.expeditionfortreasure.logic.building.Building;


public class MainActivity extends ActionBarActivity {
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    private GameLogic gl;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO READ FROM SQL DATABASE
        gl = GameLogic.getInstance();

    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();

    }



    public void goToCity(View view){
        Intent intent = new Intent(this, CityActivity.class);

        startActivity(intent);

    }

    public void goToQuest(View view){
        Intent intent = new Intent(this, QuestActivity.class);

        startActivity(intent);

    }

    public void goToTreasure(View view){
        Intent intent = new Intent(this, TreasureActivity.class);

        startActivity(intent);

    }

    public void goToMap(View view){
        Intent intent = new Intent(this, MapActivity.class);

        startActivity(intent);

    }

}
