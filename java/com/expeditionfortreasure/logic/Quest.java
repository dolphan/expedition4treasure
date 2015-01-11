package com.expeditionfortreasure.logic;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Emil on 2015-01-07.
 */
public class Quest {
    double longitude, lattitude;
    //TODO Change this so that the reward is based on distance to treasure
    public int reward = 500;
    boolean finished;
    LatLng treasure;

    private Quest(Location myLocation, Context context){
        treasure = randomizeTreasure(myLocation, context);
        finished = false;
    }


    /*TODO Make this take your current position so that
     you can randomize a quest from that position
    */
    public static Quest getNewQuest(Location myLocation, Context context){

        return new Quest(myLocation, context);
    }

    public LatLng getTreasure(){
        return treasure;
    }

    private LatLng randomizeTreasure(Location location, Context context){
        // Code that randomize location
        double lat, lng, angle;

        // Add timeout?
        while(true) {
            angle = (Math.random() * 2 * Math.PI);
            lat = location.getLatitude() + Math.cos(angle) / 10;
            lng = location.getLongitude() + Math.sin(angle) / 10;

            String addressName = "";

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            try {
                List<Address> treasureAdress = geocoder.getFromLocation(lat, lng, 1);
                if (treasureAdress != null && treasureAdress.size() > 0) {
                    if (treasureAdress.get(0).getPostalCode() == null) {
                        Log.d("QUEST", "Not a valid address (No PostalCode), Lat: " + lat + " Lng: " + lng);
                    } else {
                        Log.d("QUEST", "Postal Code: " + treasureAdress.get(0).getPostalCode());
                        Log.d("GPS", "The treasue is located on: " + treasureAdress.get(0).getAddressLine(0));
                        // Hardcoded for testing
//                        lat = 59.262837;
//                        lng = 17.981340;
                        return new LatLng(lat, lng);
                    }
                } else {
                    Log.d("GPS", "Not a valid address(No matches)");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCoord(){
        return treasure.latitude + "/" + treasure.longitude;
    }

    public void completeQuest(){
        finished = true;
    }

    public boolean isComplete(){
        return finished;
    }
}
