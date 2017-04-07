package nostale;
import nostale.login.*;
import nostale.struct.*;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import nostale.connection.*;
import nostale.resources.*;
import nostale.util.*;
public class Nostale {
    public Login Login;
    public Game Game;
    public Connection c;
    public GameData GameData;
    public ParsePacket parser;
    
    public Mob target;
    
    public Nostale()
    {
    	this.parser = new ParsePacket(this);
    	try
    	{
        System.out.println("Loading resources");

        Resources.skills = LoadSkills.loadSkills();
        Resources.items = LoadItems.loadItems();
        Resources.mobs = LoadMobs.loadMobs(Resources.items);
        System.out.println("Loading done!");
    	}
    	catch(Exception e){e.printStackTrace();}
    }
    
    public void Login(String nickname,String password,CServer s)
    {
    	this.Login = new Login(nickname,password,s);
    	if(this.Login.sessionState!=SessionEnum.NOTHING)
    		System.out.println("Error in Nostale 10: "+this.Login.sessionState);
    	//TODO Loading resources by country
    	
    	
    }
        
    public void SelectChannel(GameServer s)
    {
    	try
    	{
    	Game = new Game();
    	Game.GameData.id = Login.id;
    	Game.GameData.pw = Login.pw;
    	Game.GameData.session = Login.session;
    	Game.Connect(s);
    	this.c = Game.c;
    	this.GameData = Game.GameData;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void SelectCharacter(nostale.struct.Character ch)
    {
    	try{
    		Game.selectChar(ch);	
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    }
    
    public void send(String packet)
    {

    	try{
    		Game.send(packet);
    	}
    	catch(Exception e){e.printStackTrace();}
    	
    }
    
    public void MobStoppedResting(int id)
    {
    	
    }
    
    

    
    public void Walk(Pos p)
    {
    	Walk(p.x,p.y);
    }
    public void Walk(int x,int y)
    {
    	if(GameData.Character.Moving) return;
    	if(Pos.getRange(new Pos(x,y),GameData.Character.Pos)<3) return;
    	if(Pos.getRange(new Pos(x,y), GameData.Character.Pos)>9){
    		Walk(Pos.getShortestPosInRange(9, GameData.Character.Pos, new Pos(x,y)));
    	}
    	
    	if(GameData.map.canWalkHere(x, y))
    	{
    		send("walk "+x+" "+y+" 0 "+GameData.Character.Speed);
    	    GameData.Character.Moving = true;	
    		int timeToWait = (int)(Pos.getRange(new Pos(x,y), GameData.Character.Pos)/GameData.Character.Speed)*1000+100;
    		send("ncif 1 "+GameData.Character.id);
    		new Timer().schedule(new TimerTask() {          
    		    @Override
    		    public void run() {
    		        GameData.Character.Pos = new Pos(x,y);  
    		        GameData.Character.Moving = false;
    		    }
    		}, timeToWait-100);
    		try {
    			System.out.println("Walking to" +new Pos(x,y)+" Waiting: +"+timeToWait);
				Thread.sleep(timeToWait);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	else
    	{
    		Walk(Pos.getShortestMovablePos(GameData.map, new Pos(x,y)));
    	}
    }
    
    public Mob GetNearestMob()
    {
    	int range = 100000;
    	Mob m = null;
        for(Map.Entry<Integer, Mob> entry : GameData.Mobs.entrySet()) {
        	//Integer key = entry.getKey();
        	Mob mob = entry.getValue();
        	if(Pos.getRange(mob.Pos, GameData.Character.Pos)<range)
        	{
        		m = mob;
        		range = Pos.getRange(mob.Pos, GameData.Character.Pos);
        	}
        }
        return m;
    }
    
    public void Attack(Mob m,Skill s)
    {
    	if(m==null || s==null) return;
    	if(s.IsOnCooldown) return;
    	if(s.Range>=Pos.getRange(m.Pos,GameData.Character.Pos))
    	{
    		send("u_s "+s.CastId+" 3 "+m.id);
    		System.out.println("Attacking to mob:"+m.id+"|"+m.Name+" + Waiting: "+s.CastTime/2);
    		try {
				Thread.sleep(s.CastTime/2*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            //s.isOnCooldown = true;
    	}
    		
    	else
    	{
    		Pos p = Pos.getShortestPosInRange(s.Range, m.Pos, GameData.Character.Pos);
    		Walk(p);


    	}

    	
    }
    
    
    
    
    
    public void ReceiveAndParse()
    {
    	String[] received = this.c.GetReceived();
    	for(String p:received)
    	{
        	parser.Parse(p);
        	
    	}

    }
}
