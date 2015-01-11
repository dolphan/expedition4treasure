package com.expeditionfortreasure;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.expeditionfortreasure.file.FileHandling;
import com.expeditionfortreasure.logic.*;
import com.expeditionfortreasure.logic.building.*;


public class CityActivity extends ActionBarActivity {
    private GameLogic gl;
    private TextView gold;
    private TextView farmprice, tavernprice, libraryprice, barracksprice;
    private Button farmbutton, tavernbutton, librarybutton, barracksbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        gold = (TextView) findViewById(R.id.cityGold);
        farmbutton = (Button) findViewById(R.id.farmbuilding);
        tavernbutton = (Button) findViewById(R.id.tavernbuilding);
        librarybutton = (Button) findViewById(R.id.librarybuilding);
        barracksbutton = (Button) findViewById(R.id.barracksbuilding);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city, menu);
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();

        Log.v("File", "1");
        gl = GameLogic.getInstance(getApplicationContext());
        Log.v("File", "Got the intance");


        updateAll();
    }


    public void levelUpFarm(View view) {
        //If you can't build show toast
        if (!gl.buyBuilding(Building.Type.FARM)) {
            Context context = getApplicationContext();
            CharSequence text = "Couldn't afford a farm";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            updateAll();
        }
    }

    public void levelUpTavern(View view) {
        //If you can't build show toast
        if (!gl.buyBuilding(Building.Type.TAVERN)) {
            Context context = getApplicationContext();
            CharSequence text = "Couldn't afford a tavern";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            updateAll();
        }
    }

    public void levelUpLibrary(View view) {
        //If you can't build show toast
        if (!gl.buyBuilding(Building.Type.LIBRARY)) {
            Context context = getApplicationContext();
            CharSequence text = "Couldn't afford a Library";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            updateAll();
        }


    }

    public void levelUpBarracks(View view) {
        //If you can't build show toast
        if (!gl.buyBuilding(Building.Type.BARRACKS)) {
            Context context = getApplicationContext();
            CharSequence text = "Couldn't afford a barrack";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            updateAll();
        }
    }


    public void updateAll(){
        updateFarm();
        updateTavern();
        updateLibrary();
        updateBarracks();
        FileHandling.saveFile(this, gl);
    }

    public void updateFarm() {
//        gold.setText("Current $: " + gl.getGold());
        farmbutton.setText("Farm level " + gl.getBuildingLevel(Building.Type.FARM) + "\n" + getResources().getString(R.string.nextlevel) + " $" + gl.getBuildingPrice(Building.Type.FARM));
    }



    public void updateTavern() {
  //      gold.setText("Current $: " + gl.getGold());
        tavernbutton.setText("Tavern level: " + gl.getBuildingLevel(Building.Type.TAVERN) + "\n" + getResources().getString(R.string.nextlevel) + " $" + gl.getBuildingPrice(Building.Type.TAVERN));

    }


    public void updateLibrary(){
        //gold.setText("Current $: " + gl.getGold());
        librarybutton.setText("Library level: " + gl.getBuildingLevel(Building.Type.LIBRARY) +"\n" + getResources().getString(R.string.nextlevel) + " $" + gl.getBuildingPrice(Building.Type.LIBRARY));
    }

    public void updateBarracks(){
        //gold.setText("Current $: " + gl.getGold());
        barracksbutton.setText("Barracks level: " + gl.getBuildingLevel(Building.Type.BARRACKS) +"\n" + getResources().getString(R.string.nextlevel) + " $" + gl.getBuildingPrice(Building.Type.BARRACKS));

    }
}
