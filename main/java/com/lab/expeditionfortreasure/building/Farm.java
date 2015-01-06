package com.lab.expeditionfortreasure.building;

public class Farm extends Building {
	
	public Farm (){
		super(50);
		
	}
	
	@Override
	public Type getType(){
		return Type.FARM;
	}
}
