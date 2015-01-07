package com.lab.expeditionfortreasure.logic.building;

import java.io.Serializable;

public abstract class Building implements Serializable{
	protected int basePrice;
	private int level;
	private int currentPrice;
	
	//TODO Vettig konstruktor
	protected Building (int basePrice){
		this.basePrice = basePrice;
		level = 1;
		currentPrice = basePrice;
	}
	
	
	//TODO Antagligen inte void
	//Vettiga argument
	public void buyOneLevel(){
		level++;
		calculateNewPrice();
	}
	
	//TODO Hitta vettig formel f�r att ber�kna nytt pris
	protected void calculateNewPrice(){
		currentPrice += level;
	}
	
	
	//TODO Os�ker p� vad den ska returnera
	//Klura ut smart l�sning f�r att returnera vad f�r bonus
	//Kanske kan l�sas med returtyp som sen kollas i respektive
	//logikdel om den �verensst�mmer
	//Dvs case KORREKTRETURTYP: G�r r�tt
	
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
	
}
