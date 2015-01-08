package com.expeditionfortreasure.logic.building;

import java.io.Serializable;

public class Library extends Building implements Serializable{

	public Library() {
		super(350);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Type getType(){
		return Type.LIBRARY;
	}

}
