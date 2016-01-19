package com.frigebigrow;

public class Person {
	private int id;
	private int toa;
	private int errands;
	
	public Person(int ID, String nm,int aTime){
		setId(ID);
		setToa(aTime);
	}
	public Person(int ID,int aTime){
		id=ID;
		toa=aTime;
	}
	public Person(){
		id=-1;
		toa=-1;	
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getToa() {
		return toa;
	}
	public void setToa(int toa) {
		this.toa = toa;
	}
	public int getErrands() {
		return errands;
	}
	public void setErrands(int errands) {
		this.errands = errands;
	}
	
	
}
