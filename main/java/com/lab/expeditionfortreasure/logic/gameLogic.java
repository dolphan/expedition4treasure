package com.lab.expeditionfortreasure.logic;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

import com.lab.expeditionfortreasure.logic.building.*;

public class GameLogic implements Serializable{
	private int gold;
	private HashMap<Building.Type, Building> city;
    private static GameLogic instance = null;

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

		if(gold >= city.get(building).getCurrentPrice() ){
			gold -= city.get(building).getCurrentPrice();
			city.get(building).buyOneLevel();
            Log.d("GameLogic", "Bought a level " + getBuildingLevel(building));
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
		return city.get(building).getCurrentPrice();
	}

    public static GameLogic getInstance() {
        if(instance == null ) {
            instance = new GameLogic();
        }

        return instance;
    }
	
	
}
