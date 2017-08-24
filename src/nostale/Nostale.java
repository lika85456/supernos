package nostale;

import nostale.net.Connection;
import nostale.net.Crypto;
import nostale.resources.LoadItems;
import nostale.resources.LoadMobs;
import nostale.resources.LoadSkills;
import nostale.resources.Resources;
import nostale.util.Pos;
import nostale.data.MapCharacterInstance;
import nostale.data.GameData;
import nostale.data.InventoryItemInstance;
import nostale.data.MapItemInstance;
import nostale.data.MapMobInstance;
import nostale.data.Mob;
import nostale.data.Portal;
import nostale.data.Skill;
import nostale.handler.BattlePacketHandler;
import nostale.handler.WalkPacketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import nostale.Game;
import nostale.GameServer;

public class Nostale {
    public Login Login;
    public Game Game;
    public Connection c;
    public GameData GameData;
    public MapMobInstance target;
    public BattlePacketHandler BattleHandler;
    public WalkPacketHandler WalkHandler;

    
    public Nostale()
    {
	//Handler initialisation
	BattleHandler = new BattlePacketHandler(this);
	WalkHandler = new WalkPacketHandler(this);
	    
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
    		System.out.println("Error: "+this.Login.sessionState);
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

    public void SelectCharacter(MapCharacterInstance ch)
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
    public void sendAfterWait(String packet,int miliseconds)
    {
    	try{

    		Game.sendAfterWait(packet,miliseconds);
    	}
    	catch(Exception e){e.printStackTrace();}    	
    }
    
    public MapMobInstance GetNearestMob()
    {
    	int range = 100000;
    	MapMobInstance m = null;
        for(Entry<Integer, MapMobInstance> entry : GameData.Mobs.entrySet()) {
        	//Integer key = entry.getKey();
        	MapMobInstance mob = entry.getValue();
        	if(Pos.getRange(mob.Pos, GameData.Character.Pos)<range)
        	{
        		m = mob;
        		range = Pos.getRange(mob.Pos, GameData.Character.Pos);
        	}
        }
        return m;
    }
    
    public MapItemInstance[] GetPickupableItems()
    {
    	ArrayList<MapItemInstance> it = new ArrayList<MapItemInstance>();
    	MapItemInstance item = null;
        for(Entry<Integer, MapItemInstance> entry : GameData.Items.entrySet()) {
        	//Integer key = entry.getKey();
        	item = entry.getValue();
        	if(item.OwnerID==0 || item.OwnerID==GameData.Character.id)
        	{
        		it.add(item);
        	}
        }    	
        return it.toArray(new MapItemInstance[0]);
    }
    
    public void PickUpItem(int tId)
    {
    	MapItemInstance item = GameData.Items.get(tId);
    	if(!(item.OwnerID==0 || item.OwnerID==GameData.Character.id)) {return;} //item cannot be pickuped
    	WalkHandler.Walk(Pos.getShortestPosInRange(1, item.Pos, GameData.Character.Pos));
    	
    	send("get 1 "+GameData.Character.id+" "+item.id);
    	try {
			Thread.sleep(75);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void MobAttackedMe(int MobId) 
    {
    	//Mob attacked me!
    	if(GameData.Character.ShouldRest == true) //AND I WAS RESTING!!!!!!!!
    	{
    		//kill that shit
    		System.out.println("Hp: "+GameData.Character.Hp);
    	}
    }
    
    public void UseItem(InventoryItemInstance i)
    {
    	send("u_i 1 "+GameData.Character.id+" "+i.Type+" "+i.InventoryPos+" 0 0");
    }
    
    public void WearSP()
    {
    	send("sl 0");
    }
    public void SPDown()
    {
    	send("sl 0");
    }
    
    public void ReceiveAndParse()
    {
    	String[] received = this.c.GetReceived();
    	for(String p:received)
    	{
    		Packet pac = new Packet(p);
    		BattleHandler.onReceive(p);
    		switch(pac.type)
        	{
        	case "c_map":
                try {
    				GameData.map = new nostale.resources.Map(pac.getInt(2));
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
                break;
                
                /*case "sc":
                 //sc {type} {weaponUpgrade} {MinHit} {MaxHit} {HitRate} {HitCriticalRate} {HitCritical} {type2} {secondaryUpgrade} {MinDistance} {MaxDistance} {DistanceRate} {DistanceCriticalRate} {DistanceCritical} {armorUpgrade} {Defence} {DefenceRate} {DistanceDefence} {DistanceDefenceRate} {MagicalDefence} {FireResistance} {WaterResistance} {LightResistance} {DarkResistance}
                GameData.Character.maxDistance = Integer.parseInt(pac[11]);
                
                break;
                */
                
                case "lev":
                //lev {Level} {LevelXp} {JobLevel} {JobXP} {XPLoad()} {JobLoad} {Reput} {GetCP()} {HeroXp} {HeroLevel} {HeroXPLoad()}";
                GameData.Character.Level = (byte)pac.getInt(1);
                GameData.Character.LevelXp = pac.getInt(2);
                GameData.Character.JobLevel = (byte)pac.getInt(3);
                GameData.Character.JobLevelXp = pac.getInt(4);
                GameData.Character.LevelMaxXp = pac.getInt(5);
                GameData.Character.JobLevelMaxXp = pac.getInt(6);
                break;
                
                case "ski":
                ArrayList<Skill> skills = new ArrayList<Skill>();
                for(int i=3;i<13;i++)
                {
                   if(pac.splited[i].equals("ski")) continue;
                   skills.add(Resources.getSkillByVNUM(Integer.parseInt(pac.splited[i])));
                   
                }
                GameData.Character.skills = skills.toArray(new Skill[0]);
                break;
                
                case "at":
                GameData.Character.id = pac.getInt(1);
                GameData.Character.Pos = new Pos(pac.getInt(3),pac.getInt(4));
                break;
                
                case "cond":
                GameData.Character.Speed = pac.getInt(5);
                break;
               
                case "sc_p_stc":
                send("npinfo 0");
                break;
                
                case "mv":
                if(pac.getInt(1)==3)
                {
                    int id = pac.getInt(2);
                    Pos pos = new Pos(pac.getInt(3),pac.getInt(4));
                    MapMobInstance mob = GameData.Mobs.get(id);
                    if(mob!=null && mob.Name==null)
                    {
                    	send("ncif 3 "+id);
                    }
                    if(mob==null)
                    {
                    	mob = new MapMobInstance();
                    }
                    mob.id = id;
                    mob.Pos = pos;                
                    GameData.Mobs.put(mob.id,mob);
                  
                }
                else if(pac.getInt(1)==1)
                {
                	int id = pac.getInt(2);
                	MapCharacterInstance ch = GameData.Characters.get(id);
                	if(ch==null) {ch = new MapCharacterInstance();}
                	ch.Pos = new Pos(pac.getInt(3),pac.getInt(4));
                	ch.id = id;
                	GameData.Characters.put(id, ch);
                }
                break;
                
                case "in":
                if(pac.splited[1].equals("1"))
                {//Character move  
                	//TODO missing hp and mp!!!!
                    MapCharacterInstance tChar;
                    try{
                       tChar = GameData.Characters.get(pac.getInt(4));}
                  catch(Exception e) {tChar = new MapCharacterInstance();}
                      if(tChar==null) {tChar = new MapCharacterInstance();}
                      tChar.Name = pac.get(2);
                      tChar.Pos = new Pos(pac.getInt(5),pac.getInt(6));
                      tChar.Direction = (short)pac.getInt(7);
                      tChar.Authority = (byte)pac.getInt(8);
                      tChar.IsSitting = "1"==pac.splited[16];
                      tChar.IsInvisible = "1"==pac.splited[28];
                      tChar.Level = (byte)Integer.parseInt(pac.splited[32]);
                      tChar.id = pac.getInt(4);
                      if(tChar.id!=0)
                      GameData.Characters.put((int)tChar.id, tChar);

                }
                else if(pac.splited[1].equals("3"))
                {//Mob move                
                   MapMobInstance t = GameData.Mobs.get(pac.getInt(3));
                   if(t==null) t=new MapMobInstance();
                   t.Pos = new Pos(pac.getInt(4),pac.getInt(5));
                   t.VNUM = (short)pac.getInt(2);
                   t.id = pac.getInt(3);
                   t.Hp = t.MaxHp*(pac.getInt(7)/100);
                   t.Mp = t.MaxMp*(pac.getInt(7)/100);
                   Mob mt = Resources.getMobByVNUM(t.VNUM);
                   if(mt!=null)
                   t.Name = mt.Name;
                   GameData.Mobs.put(t.id,t);

                }
                break;
               
                
                case "stat":
                //stat {Hp} {HPLoad()} {Mp} {MPLoad()} 0 {option}
                GameData.Character.Hp = pac.getInt(1);
                GameData.Character.MaxHp = pac.getInt(2);
                GameData.Character.Mp = pac.getInt(3);
                GameData.Character.MaxMp = pac.getInt(4);    
                break;
                
                case "drop":
                MapItemInstance i = new MapItemInstance();
                i.VNUM = (short)pac.getInt(1);
                i.id = pac.getInt(2);
                i.Pos = new Pos(pac.getInt(3),pac.getInt(4));
                i.Amount = pac.getInt(5);
                i.OwnerID = pac.getInt(7);

                GameData.Items.put(i.id,i);
                break;
                
                case "get":
                	try{
              	  GameData.Items.remove(pac.getInt(3));
                	}
                	catch(Exception e){e.printStackTrace();}
              	  break;
              	  
                case "out":
                	try
                	{
                	if(pac.getInt(1)==1)//Player removed
                	{
                		GameData.Characters.remove(pac.getInt(2));
                		
                	}
                	else if(pac.getInt(1)==9)//Item removed
                	{

              		GameData.Items.remove(pac.getInt(2));
                	}
                	}
                	catch(Exception e){}
                	  break;
                
                case "st":
                int packetType = pac.getInt(1);
                if(packetType == 1)
                {  
                
                   MapCharacterInstance ch = GameData.Characters.get(pac.getInt(2));
                   if(ch==null) ch = new MapCharacterInstance();
                   ch.Level = (byte)pac.getInt(3);
                   ch.Hp = pac.getInt(7);
                   ch.Hp = Integer.parseInt(pac.splited[8]);
                   ch.MaxHp = (int)(ch.Hp/pac.getInt(5)*100);
                   ch.MaxMp = (int)(ch.Mp/pac.getInt(6)*100);              
                   GameData.Characters.put((int)ch.id, ch);
                }
                else if(packetType == 3)
                {
                   MapMobInstance m = GameData.Mobs.get(pac.getInt(2));
                   if(m==null) {m=new MapMobInstance();}
                   m.id = pac.getInt(2);
                   m.Level = (short)pac.getInt(3);
                   m.Hp = pac.getInt(7);
                   m.Mp = Integer.parseInt(pac.splited[8]);
                   m.MaxHp = (int)(m.Hp/pac.getInt(5)*100);
                   m.MaxMp = (int)(m.Mp/pac.getInt(6)*100);                   
                   GameData.Mobs.put(m.id,m);
                }
                
                break;
                
                case "inv":
              	  GameData.Character.inventory.parsePacketINV(pac.packet);
                break;
                case "gold":
                   GameData.Character.Gold = pac.getInt(1);
                break;
                
                case "gp":
                   Portal pop = new Portal();
                   pop.pos = new Pos(pac.getInt(1),pac.getInt(2));
                   pop.MapID = pac.getInt(3);
                   pop.Type = pac.getInt(4);
                   pop.isEnabled = pac.splited[6]=="1";
                   GameData.Portals.put(pop.pos.x*pop.pos.y, pop);
                break;
                
                case "mapout":
                  send("c_close");
                  send("f_stash_end");
                  GameData.Characters = new HashMap<Integer, MapCharacterInstance>();
                  GameData.map = null;
                  GameData.Mobs = new HashMap<Integer, MapMobInstance>();
                  GameData.Items = new HashMap<Integer, MapItemInstance>();
                  GameData.Portals = new HashMap<Integer, Portal>();
                break;
                
                case "rest":
              	  // rest 1 374541 0
              	  if(pac.getInt(2)==GameData.Character.id)
              	  {
              		  GameData.Character.IsSitting = pac.splited[3]=="1";
              	  }
              	  break;
				
		case "dir":
		//dir 1 {CharacterId} {Direction}
		GameData.Characters.get(pac.getInt(2)).Direction = (short)pac.getInt(3);
	  	  break;	
				
		case "rc":
		//rc 1 {CharacterId} {characterHealth} 0
		GameData.Characters.get(pac.getInt(2)).Hp = pac.getInt(3);
		  break;		

		case "eff_ob":
		//eff_ob  -1 -1 0 4269 - when i die
		
        break;
        
		case "delay":
			//delay 5000 3 #sl^1 
			sendAfterWait(pac.get(3),pac.getInt(1));
			break;
                
                
                
                //Nosbasar..
                /*case "rc_blist":
                   this.nb.parse(packet);
                break;
                case "rc_buy":
                   this.nb.parse(packet);
                break;
                */
        	}
        	
    	}

    }
}
