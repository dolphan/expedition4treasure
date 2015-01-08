package com.expeditionfortreasure.logic.building;

import java.io.Serializable;

public class Farm extends Building implements Serializable{
	
	public Farm (){
		super(50);
	}
	
	@Override
	public Type getType(){
		return Type.FARM;
	}
}
