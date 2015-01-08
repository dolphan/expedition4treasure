package com.expeditionfortreasure.logic.building;

import java.io.Serializable;

public class Barrack extends Building implements Serializable{
	
	public Barrack(){
		super(500);
	}
	
	@Override
	public Type getType(){
		return Type.BARRACKS;
	}
	
}
