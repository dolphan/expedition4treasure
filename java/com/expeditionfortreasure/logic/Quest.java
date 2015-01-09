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
    LatLng treasure;

    private Quest(Location myLocation){
        treasure = randomizeTreasure(myLocation);
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

    private LatLng randomizeTreasure(Location location){
        double lat, lng;
        double angle = (Math.random()*2*Math.PI);
        lat = location.getLatitude() + Math.cos(angle);
        lng = location.getLongitude() + Math.sin(angle);

        return new LatLng(lat,lng);
    }

}
