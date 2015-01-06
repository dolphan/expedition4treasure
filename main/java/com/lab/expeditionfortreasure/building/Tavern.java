package com.lab.expeditionfortreasure.building;

public class Tavern extends Building {
	public Tavern(){
		super(120);
	
	}
	
	@Override
	public Type getType(){
		return Type.TAVERN;
	}
	
	
}
