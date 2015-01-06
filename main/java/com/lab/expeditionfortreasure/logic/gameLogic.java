package com.lab.expeditionfortreasure.logic;

import java.util.HashMap;

import gamelogic.building.*;
import gamelogic.building.Building.Type;

public class gameLogic {
	private int gold;
	private HashMap<Building.Type, Building> city;
	
	public gameLogic() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	//Initiate one of each kind
	public void init(){
		city = new HashMap<Building.Type, Building>();
		city.put(Type.FARM, new Farm());
		city.put(Type.TAVERN, new Tavern());
		city.put(Type.LIBRARY, new Library());
		city.put(Type.BARRACKS, new Barrack());
		
		gold = 0;
	}
	
	public boolean buyBuilding(Building.Type building){
		if(gold >= city.get(building).getCurrentPrice() ){
			gold -= city.get(building).getCurrentPrice();
			city.get(building).buyOneLevel();
			return true;
		}
		else 
			return false;
	}
	
	public int getBuildingLevel(Building.Type building){
		return city.get(building).getLevel();
	}
	
	public int getBuildingPrice(Building.Type building){
		return city.get(building).getCurrentPrice();
	}
	
	
}
