package com.lab.expeditionfortreasure.building;

public class Barrack extends Building{
	
	public Barrack(){
		super(500);
	}
	
	@Override
	public Type getType(){
		return Type.BARRACKS;
	}
	
}
