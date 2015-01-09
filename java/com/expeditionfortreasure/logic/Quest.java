package com.expeditionfortreasure.logic;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Emil on 2015-01-07.
 */
public class Quest {
    double longitude, lattitude;
    //TODO Change this so that the reward is based on distance to treasure
    public int reward = 500;
    boolean finished;
    public int number;
    LatLng treasure;

    private Quest(Location myLocation){
        number = (int) (Math.random()/20.0);
        treasure = new LatLng(myLocation.getLatitude() + (Math.random()/20.0), myLocation.getLongitude() + (Math.random()/20.0) );
        Log.d("Quest", "#" + number);
    }


    /*TODO Make this take your current position so that
     you can randomize a quest from that position
    */
    public static Quest getNewQuest(Location myLocation){

        return new Quest(myLocation);
    }

    public LatLng getTreasure(){
        return treasure;
    }

}
