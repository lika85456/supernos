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
    public Nosbasar Nosbasar = new Nosbasar();
    
    public Nostale()
    {
	//Handler initialization
	BattleHandler = new BattlePacketHandler(this);
	WalkHandler = new WalkPacketHandler(this);
	 this.Nosbasar.n = this;
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
    	//System.out.println("SENT:"+packet);
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
    		//System.out.println("RECEIVED:"+p);
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
                

               
                
                case "at":
                GameData.Character.id = pac.getInt(1);
                break;
               
               
                case "sc_p_stc":
                send("npinfo 0");
                break;
                
                
                
                
               
                
                
                
              	  
                
                
                
                case "inv":
              	  GameData.Character.inventory.parsePacketINV(pac.packet);
                break;
                case "gold":
                   GameData.Character.Gold = pac.getInt(1);
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
                
               

		case "eff_ob":
		//eff_ob  -1 -1 0 4269 - when i die
		
        break;
        
		case "delay":
			//delay 5000 3 #sl^1 
			sendAfterWait(pac.get(3),pac.getInt(1));
			break;
                
                
                
                //Nosbasar..
                case "rc_blist":
                   this.Nosbasar.parse(pac);
                break;
                case "rc_buy":
                   this.Nosbasar.parse(pac);
                break;
                case "rc_slist":
                   this.Nosbasar.parse(pac);
                break;
                
        	}
        	
    	}

    }
}
