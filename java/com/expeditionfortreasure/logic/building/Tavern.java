package com.expeditionfortreasure.logic.building;

import java.io.Serializable;

public class Tavern extends Building implements Serializable{
	public Tavern(){
		super(120);
	
	}
	
	@Override
	public Type getType(){
		return Type.TAVERN;
	}
	
	
}
