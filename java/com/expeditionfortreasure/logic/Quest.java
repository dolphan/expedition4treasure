package com.expeditionfortreasure.logic;

import android.util.Log;

/**
 * Created by Emil on 2015-01-07.
 */
public class Quest {
    double longitude, lattitude;
    //TODO Change this so that the reward is based on distance to treasure
    public int reward = 500;
    boolean finished;
    public int number;

    private Quest(){
        number = (int) (Math.random()*1000);
        Log.d("Quest", "#" + number);
    }


    /*TODO Make this take your current position so that
     you can randomize a quest from that position
    */
    public static Quest getNewQuest(){
        return new Quest();
    }

}
