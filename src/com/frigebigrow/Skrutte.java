package com.frigebigrow;

import java.util.Random;
import java.util.ArrayList;

public class Skrutte {
	ArrayList<Person> queue = new ArrayList<Person>();
	int totalQtime=0;
	int time = 0;
	int endTime;
	int spawnChance; //the per mille chance that a customer spawns
	int dspawnChance; //the change of spawn chance
	int rdyTime= -1;
	int people=0;
	int errTime;
	int openH;
	boolean busy=false;
	boolean print;
	boolean robbed=false;
	String outString;
	
	public Skrutte(int et, int sc, int errt){
		endTime=et;
		spawnChance=sc;
		dspawnChance=0;
		errTime=errt;
	}
	public void simulate(){
		
		//Main loop
		while(time<=endTime||busy){
			outString = "The time is "+conTime(time); 
			print=false;
			
			if(spawnChance!=200){
				spawnChance+=dspawnChance;
			}else if(dspawnChance!=0){
				dspawnChance=0;
			}
			
			if(time==0){
				outString+=" and the office opens";
				print=true;
			}else if(time==endTime){
				outString+=" and the door is closed";
				print=true;
			}
			
			if(time==rdyTime){
				outString+=" and person "+queue.get(0).getId()+" leaves";
				queue.remove(0);
				busy=false;
				print=true;
			}
			
			if(!busy && !queue.isEmpty()){
					int e=queue.get(0).getErrands();
					outString+=" and person "+queue.get(0).getId()+" is being helped ("+e+")";
					rdyTime=time+e*errTime;
					busy=true;
					totalQtime+= time-queue.get(0).getToa();
					print=true;
			}
			
			if(rand(spawnChance)&&time<endTime){
				if(rand(999)){
					spawn();
				}else{
					robbery();
				}
			}
			
			if(print){
				System.out.println(outString);
			}
			time+=1;
		}
		System.out.println();
		System.out.printf("Total customers: "+people+" Total waiting time: "+totalQtime+" minute Average waiting time: %.2f minutes",((float)totalQtime/(float)people));
		System.out.println();

		if(robbed){
			System.out.println("The post office was attacked!");
		}else{
			System.out.println("It was an ordinary day");
		}
	}
	
	
	public void robbery(){
		//robbery
		Random randGen = new Random();
		outString = "The time is "+conTime(time)+" and the office was ROBBED, ";
		int killed=0;
		if(!queue.isEmpty()){
			killed=randGen.nextInt(queue.size());
		}
		outString+=killed+" ppl is kill";
		if(rand(100)){
			spawnChance=50;
			dspawnChance=10;
			outString+=" and she did not fight it off!";
		}else{
			spawnChance=500;
			dspawnChance=-10;
			outString+=" but she fought off the attacker!";
		}
		outString+="\n";
		queue.clear();
		rdyTime=-1;
		busy=false;
		robbed=true;
		print=true;
		
	}
	
	public void spawn(){
		//spawning of person
		people+=1;
		Person person = new Person(people,time);
		queue.add(person);
		outString+=" and person "+queue.get(queue.size()-1).getId();
		int e=errands();
		queue.get(queue.size()-1).setErrands(e);
		
		if(busy){
			outString+=" entered queue as nr "+Integer.toString(queue.size());
		}else{
			outString+=" entered and is being helped immediately ("+e+")";			
			rdyTime=time+e*errTime;
			busy=true;
		}
		print=true;
		
		
	}
	
	//generates number of errands
	public static int errands(){
		Random randGen = new Random();
		float p=randGen.nextFloat();
		int count=(int)(-(Math.log(p)/Math.log(2))+1);
		return count;
	}
	
	//chance of returning true determined by input probability
	public static boolean rand(int nr){
		Random randGen = new Random();
		int roll= randGen.nextInt(1000);
		if(roll<nr){
			return true;
		}else{
			return false;
		}
	}
	
	//converting from number of minutes since start to HH:MM string
	public static String conTime(int t){
		int m = t%60;
		int h = (t-m)/60;
		h+=9;
		h=h%24;
		String hour=Integer.toString(h);
		String minute;
		
		if(m<10){
			minute="0"+Integer.toString(m);
		}else{
			minute=Integer.toString(m);
		}
		
		String time = hour+":"+minute;
		return time;
	}
}