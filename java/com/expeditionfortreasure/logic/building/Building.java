package com.expeditionfortreasure.logic.building;

import java.io.Serializable;

public abstract class Building implements Serializable{
	protected int basePrice;
	private int level;
	private int currentPrice;
    private Type buildingType;
	
	//TODO Vettig konstruktor
	protected Building (int basePrice){
		this.basePrice = basePrice;
		level = 1;
		currentPrice = basePrice;
	}

	public boolean buyOneLevel(){
		if(level < 99) {
            level++;
            calculateNewPrice();
            return true;
        }
       return  false;
	}
	

	protected void calculateNewPrice(){
		currentPrice += 2*level;
	}
	

	public Type getType(){
		return null;
	}
	
	public int getCurrentPrice(){
		return currentPrice;
	}
	
	public int getLevel(){
		return level;
	}
	
	
	//F�r att identifiera typ av byggnad
	public enum Type{
		FARM, TAVERN, LIBRARY, BARRACKS;
	}

    public String toString(){
        return getType() + ":" + basePrice + ":" + currentPrice + ":" + level;
    }
	
}
