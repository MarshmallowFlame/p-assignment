package com.frigebigrow;

import java.util.Random;
import java.util.ArrayList;

public class Main {
	static ArrayList<Person> queue = new ArrayList<Person>();
	static int totalQtime=0;
	static int time = 0;
	static int endTime=540;
	static int spawnChance=200; //the percentage*10 chance that a customer spawns
	static int dspawnChance=0;
	static int people=0;
	static boolean busy=false;
	static int rdyTime= -1;
	static boolean print;
	static String outString;
	static boolean robbed=false;
	static ArrayList<String> nameList= new ArrayList<String>();

	public static void main(String[] args){
		while(time<=endTime||busy){
			
			outString = "The time is "+conTime(time); 
			print=false;
			
			if(spawnChance!=200){
				spawnChance+=dspawnChance;
			}else{
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
				print=true;
				queue.remove(0);
				busy=false;
			}
			
			if(!busy && !queue.isEmpty()){
					int e=queue.get(0).getErrands();
					outString+=" and person "+queue.get(0).getId()+" is being helped ("+e+")";
					rdyTime=time+e*2;
					busy=true;
					totalQtime+= time-queue.get(0).getToa();
			}
			
			if(rand(spawnChance)&&time<endTime){
				if(rand(1)){
					robbery();
				}else{
					spawn();
				}
			}
			
			if(print){
				System.out.println(outString);
			}
			
			time+=1;
		}
		System.out.println();
		System.out.printf("Total customers: "+people+" Total waiting time: "+totalQtime+" Average waiting time: %.2f minutes",((float)totalQtime/(float)people));
		
		if(robbed){
			System.out.println();
			System.out.println("The post office was attacked!");
		}else{
			System.out.println();
			System.out.println("It was an ordinary day");
		}
	}
	
	public static int errands(){
		Random randGen = new Random();
		float p=randGen.nextFloat();
		int count=(int)(-(Math.log(p)/Math.log(2))+1);
		return count;
	}
	
	public static boolean rand(int nr){
		Random randGen = new Random();
		int roll= randGen.nextInt(999);
		if(roll<nr){
			return true;
		}else{
			return false;
		}
	}
	
	public static String conTime(int t){
		int m = t%60;
		int h = (t-m)/60;
		String hour=Integer.toString(9+h);
		String minute;
		
		if(m<10){
			minute="0"+Integer.toString(m);
		}else{
			minute=Integer.toString(m);
		}
		
		String time = hour+":"+minute;
		return time;
	}
	
	public static void robbery(){
		Random randGen = new Random();
		
		robbed=true;
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
		print=true;
		queue.clear();
		rdyTime=-1;
		busy=false;
	}

	public static void spawn(){
		Random randGen = new Random();
		
		people+=1;
		
		String name= nameList.get(randGen.nextInt((nameList.size()-1)));
		Person person = new Person(people,name,time);
		queue.add(person);
		
		outString+=" and person "+queue.get(queue.size()-1).getId();
		print=true;
		
		int e=errands();
		queue.get(queue.size()-1).setErrands(e);
		
		if(busy){
			outString+=" entered queue as nr "+Integer.toString(queue.size());
		}else{
			outString+=" entered and is being helped immediately ("+e+")";			
			rdyTime=time+e*2;
			busy=true;
		}
	}
}

