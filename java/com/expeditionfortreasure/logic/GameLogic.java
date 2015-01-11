package com.expeditionfortreasure.logic;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.expeditionfortreasure.QuestActivity;
import com.expeditionfortreasure.file.FileHandling;
import com.expeditionfortreasure.logic.building.Building;
import com.expeditionfortreasure.logic.building.*;

/**
 * Singleton logic that handles all player progress
 */

public class  GameLogic implements Serializable{

	private GameLogic() {
        init();
    }
	
	//Initiate one of each kind
	private void init(){
		city = new HashMap<Building.Type, Building>();
		city.put(Building.Type.FARM, new Farm());
		city.put(Building.Type.TAVERN, new Tavern());
		city.put(Building.Type.LIBRARY, new Library());
		city.put(Building.Type.BARRACKS, new Barrack());


        //TODO Ändra start guld
		gold = 5000;
	}
	
	public boolean buyBuilding(Building.Type building){
        int tmpBasePrice = getBuildingPrice(building);
		if(gold >= tmpBasePrice ){
			gold -= tmpBasePrice;
			city.get(building).buyOneLevel();
            return true;
		}
		else 
			return false;
	}
	
	public int getBuildingLevel(Building.Type building){
		return city.get(building).getLevel();
	}

    public int getGold() { return gold; }

	public int getBuildingPrice(Building.Type building){
        int discount = ((city.get(Building.Type.FARM).getLevel()/100) * city.get(building).getCurrentPrice());
        return city.get(building).getCurrentPrice() - discount; }

    public void newQuest(Location myLocation, Context context){
        if(currentQuest == null) {
            Log.d("Quest","Getting new quest");

            currentQuest = Quest.getNewQuest(myLocation, context);
            Log.d("Quest","Got quest");
            //TODO CHANGE, ONLY FOR DEBUG
            completedQuests.add(currentQuest);
        }
    }

    public void abortQuest(){
        for(Quest q : completedQuests){
            if(currentQuest == q)
                completedQuests.remove(q);
        }

        currentQuest = null;
    }

    public ArrayList<Quest> readQuest(){
        //TODO SUMTHINGSUMTHING ARAYLIST
        return completedQuests;
    }

    public Quest getCurrentQuest(){
        return currentQuest;
    }

    public void completeQuest(){
        //Make proper methods
        if(currentQuest != null) {
            int bonus = (city.get(Building.Type.TAVERN).getLevel() / 100) * currentQuest.reward;
            gold += currentQuest.reward + bonus;
            currentQuest.completeQuest(bonus);
            currentQuest = null;

        }
    }

    public void readQuestLog(){
        for(Quest q : completedQuests){
        }
    }


    //Checks if there is a file to load logic from
    public static GameLogic getInstance(Context context) {
        Log.v("File", "2");
        if(instance == null ) {
            Log.v("File", "3");
            GameLogic tmp = FileHandling.loadFile(context);
            if(tmp != null){
                Log.v("File", "no file");
                instance = tmp;
            }else {
                Log.v("File", "created new logic");
                instance = new GameLogic();
            }
            //jhfjh
        }

        Log.v("File", "returning instance");
        return instance;
    }

    private static GameLogic instance = null;
    private int gold;
    private HashMap<Building.Type, Building> city;
    private Quest currentQuest = null;
    private ArrayList<Quest> completedQuests = new ArrayList<Quest>();



}
