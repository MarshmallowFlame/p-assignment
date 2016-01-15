import java.util.Random;
import java.util.ArrayList;

public class Main {
	static ArrayList<Person> queue = new ArrayList<Person>();
	static int totalQ=0; //total waiting time
	static int time = 0; //current time
	static int endTime=540; //closing time
	static int fame=200; //the percentage chance that a customer spawns
	static int dFame=0; //how fame changes
	static int nop=0; //number of people in existence
	static boolean busy=false;
	static int rdyTime= -1; //when current customer is finished
	static boolean pr; //is something should be written that day
	static String out; //the string that will be printed if something happens at that minute
	static boolean robbed=false;
	
	public static void main(String[] args){
		//Main loop, is run until 18.00, and beyond if she is busy still
		while(time<=endTime||busy){
			if(fame!=200){
				fame+=dFame;
			}else{
				dFame=0;
			}
			
			out = "The time is "+conTime(time); 
			pr=false;
			if(time==0){
				out=out+" The office opens";
				pr=true;
			}
			if(time==endTime){
				out=out+" The door is closed";
				pr=true;
			}
			
			if(time==rdyTime){
				out=out+" and person "+queue.get(0).getId()+" leaves";
				pr=true;
				
				queue.remove(0);
				busy=false;
			}
			
			//is she is not busy and there is someone waiting to be served
			if(!busy && !queue.isEmpty()){
				if(queue.get(0).getToa()!=time-1){
					int e=errands();
					out=out+" and person "+queue.get(0).getId()+" is being helped";
					rdyTime=time+e*2;
					busy=true;
					totalQ+= time-queue.get(0).getToa();
				}
			}
			
			//if the test passes and it is before closing, a new customer spawns
			if(rand(fame)&&time<=endTime){
				if(rand(1)){
					robbery();
					
				}else{
					spawn();
				}
			}
			if(pr){
				System.out.println(out);
			}
			time+=1;
		}
		System.out.printf("Total customers: "+nop+" Total waiting time: "+totalQ+" Average waiting time: %.2f minutes",((float)totalQ/(float)nop));
		if(robbed){
			System.out.println();
			System.out.println("Was robbed!");
		}
	}
	
	public static int errands(){
		Random randGen = new Random();
		
		float p=randGen.nextFloat();
		boolean test=true;
		float n=2;
		int count =1;
		while(test){
			if(p<(1/n)){
				n=2*n;
				count+=1;
			}else{
				test=false;
			}
		}
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
		robbed=true;
		out = "The time is "+conTime(time);
		out+=" and the office was ROBBED, ";
		
		Random randGen = new Random();
		int kill=0;
		if(!queue.isEmpty()){
			kill=randGen.nextInt(queue.size());
		}
		out+=kill+" ppl is kill";
		
		if(rand(10)){
			fame=50;
			dFame=10;
			out+=" and she did not fight it off!";
		}else{
			fame=500;
			dFame=-10;
			out+=" and she fought it off!";
		}
		
		pr=true;
		queue.clear();
		rdyTime=-1;
		busy=false;
	}

	public static void spawn(){
		nop+=1; //nr of people in total
		Person person = new Person(nop,"Lars",time); //new ppl object
		//add person to queue
		queue.add(person);
		
		out=out+" and person "+queue.get(queue.size()-1).getId();
		pr=true;
	
		if(busy){
			out=out+" entered queue as nr "+Integer.toString(queue.size()-1);
		}else{
			out=out+" entered and is being helped immediately";
			int e=errands();
			rdyTime=time+e*2;
			busy=true;
			totalQ+= time-1-queue.get(0).getToa();
			
		}
		queue.get(0).setToa(time);
	}

}

