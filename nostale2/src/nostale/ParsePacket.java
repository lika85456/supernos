package nostale;
import java.util.ArrayList;
import java.util.HashMap;

import nostale.util.*;
import nostale.struct.*;
import nostale.struct.Character;
import nostale.resources.*;
public class ParsePacket {
	public Nostale n;
	public ParsePacket(Nostale n)
	{
	    this.n = n;	
	}
    public void Parse(String po)
    {
    	Packet pac = new Packet(po);
    	switch(pac.type)
    	{
    	case "c_map":
            try {
				this.n.GameData.map = new nostale.resources.Map(pac.getInt(2));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            break;
            
            /*case "sc":
             //sc {type} {weaponUpgrade} {MinHit} {MaxHit} {HitRate} {HitCriticalRate} {HitCritical} {type2} {secondaryUpgrade} {MinDistance} {MaxDistance} {DistanceRate} {DistanceCriticalRate} {DistanceCritical} {armorUpgrade} {Defence} {DefenceRate} {DistanceDefence} {DistanceDefenceRate} {MagicalDefence} {FireResistance} {WaterResistance} {LightResistance} {DarkResistance}
            this.n.GameData.Character.maxDistance = Integer.parseInt(pac[11]);
            
            break;
            */
            
            case "lev":
            //lev {Level} {LevelXp} {JobLevel} {JobXP} {XPLoad()} {JobLoad} {Reput} {GetCP()} {HeroXp} {HeroLevel} {HeroXPLoad()}";
            this.n.GameData.Character.Level = (byte)pac.getInt(1);
            this.n.GameData.Character.LevelXp = pac.getInt(2);
            this.n.GameData.Character.JobLevel = (byte)pac.getInt(3);
            this.n.GameData.Character.JobLevelXp = pac.getInt(4);
            this.n.GameData.Character.LevelMaxXp = pac.getInt(5);
            this.n.GameData.Character.JobLevelMaxXp = pac.getInt(6);
            break;
            
            case "ski":
            ArrayList<Skill> skills = new ArrayList<Skill>();
            for(int i=3;i<pac.splited.length;i++)
            {
               if(pac.splited[i].equals("ski")) continue;
               skills.add(Resources.getSkillByVNUM(Integer.parseInt(pac.splited[i])));
               
            }
            this.n.GameData.Character.skills = skills.toArray(new Skill[0]);
            break;
            
            case "at":
            this.n.GameData.Character.id = pac.getInt(1);
            this.n.GameData.Character.Pos = new Pos(pac.getInt(3),pac.getInt(4));
            break;
            
            case "cond":
            this.n.GameData.Character.Speed = pac.getInt(5);
            break;
           
            case "sc_p_stc":
            this.n.send("npinfo 0");
            break;
            
            case "mv":
            if(pac.getInt(1)==3)
            {
                int id = pac.getInt(2);
                Pos pos = new Pos(pac.getInt(3),pac.getInt(4));
                Mob mob = this.n.GameData.Mobs.get(id);
                if(mob==null || mob.Name==null) 
              	  {
              	      mob = new Mob();
              	      this.n.send("ncif 3 "+id);
              	  }
                if(mob.Name==null) {this.n.send("ncif 3 "+id);}
                mob.id = id;
                mob.Pos = pos;                
                this.n.GameData.Mobs.put(mob.id,mob);
              
            }
            else if(pac.getInt(1)==1)
            {
            	int id = pac.getInt(2);
            	Character ch = this.n.GameData.Characters.get(id);
            	if(ch==null) {ch = new Character();}
            	ch.Pos = new Pos(pac.getInt(3),pac.getInt(4));
            	ch.id = id;
            	this.n.GameData.Characters.put(id, ch);
            }
            break;
            
            case "in":
            if(pac.splited[1].equals("1"))
            {//Character move  
            	//TODO missing hp and mp!!!!
                nostale.struct.Character tChar;
                try{
                   tChar = this.n.GameData.Characters.get(pac.getInt(4));}
              catch(Exception e) {tChar = new nostale.struct.Character();}
                  if(tChar==null) {tChar = new nostale.struct.Character();}
                  tChar.Pos = new Pos(pac.getInt(5),pac.getInt(6));
                  tChar.Direction = (short)pac.getInt(7);
                  tChar.Authority = (byte)pac.getInt(8);
                  tChar.IsSitting = "1"==pac.splited[16];
                  tChar.IsInvisible = "1"==pac.splited[28];
                  tChar.Level = (byte)Integer.parseInt(pac.splited[32]);
                  if(tChar.id!=0)
                  this.n.GameData.Characters.put((int)tChar.id, tChar);

            }
            else if(pac.splited[1].equals("3"))
            {//Mob move                
               Mob t = this.n.GameData.Mobs.get(pac.getInt(3));
               if(t==null) t=new Mob();
               t.Pos = new Pos(pac.getInt(4),pac.getInt(5));
               t.VNUM = (short)pac.getInt(2);
               t.id = pac.getInt(3);
               t.Hp = t.MaxHp*(pac.getInt(7)/100);
               t.Mp = t.MaxMp*(pac.getInt(7)/100);
               Mob mt = Resources.getMobByVNUM(t.VNUM);
               if(mt!=null)
               t.Name = mt.Name;
               this.n.GameData.Mobs.put(t.id,t);

            }
            break;
           
            
            case "stat":
            //stat {Hp} {HPLoad()} {Mp} {MPLoad()} 0 {option}
            this.n.GameData.Character.Hp = pac.getInt(1);
            this.n.GameData.Character.MaxHp = pac.getInt(2);
            this.n.GameData.Character.Mp = pac.getInt(3);
            this.n.GameData.Character.MaxMp = pac.getInt(4);    
            break;
            
            case "drop":
            Item i = new Item();
            i.VNUM = (short)pac.getInt(1);
            i.id = pac.getInt(2);
            i.Pos = new Pos(pac.getInt(3),pac.getInt(4));
            i.Amount = pac.getInt(5);
            i.OwnerID = pac.getInt(7);

            this.n.GameData.Items.put(i.id,i);
            break;
            
            case "get":
            	try{
          	  this.n.GameData.Items.remove(pac.getInt(3));
            	}
            	catch(Exception e){e.printStackTrace();}
          	  break;
          	  
            case "out":
            	try
            	{
            	if(pac.getInt(1)==1)//Player removed
            	{
            		this.n.GameData.Characters.remove(pac.getInt(2));
            		
            	}
            	else if(pac.getInt(1)==9)//Item removed
            	{

          		this.n.GameData.Items.remove(pac.getInt(2));
            	}
            	}
            	catch(Exception e){}
            	  break;
            
            case "st":
            int packetType = pac.getInt(1);
            if(packetType == 1)
            {  
            
               nostale.struct.Character ch = this.n.GameData.Characters.get(pac.getInt(2));
               if(ch==null) ch = new nostale.struct.Character();
               ch.Level = (byte)pac.getInt(3);
               ch.Hp = pac.getInt(7);
               ch.Hp = Integer.parseInt(pac.splited[8]);
               ch.MaxHp = (int)(ch.Hp/pac.getInt(5)*100);
               ch.MaxMp = (int)(ch.Mp/pac.getInt(6)*100);              
               this.n.GameData.Characters.put((int)ch.id, ch);
            }
            else if(packetType == 3)
            {
               Mob m = this.n.GameData.Mobs.get(pac.getInt(2));
               if(m==null) {m=new Mob();}
               m.id = pac.getInt(2);
               m.Level = (short)pac.getInt(3);
               m.Hp = pac.getInt(7);
               m.Mp = Integer.parseInt(pac.splited[8]);
               m.MaxHp = (int)(m.Hp/pac.getInt(5)*100);
               m.MaxMp = (int)(m.Mp/pac.getInt(6)*100);                   
               this.n.GameData.Mobs.put(m.id,m);
            }
            
            break;
            
            case "inv":
          	  this.n.GameData.Character.inventory.parsePacketINV(pac.packet);
            break;
            case "gold":
               this.n.GameData.Character.Gold = pac.getInt(1);
            break;
            
            case "gp":
               Portal p = new Portal();
               p.pos = new Pos(pac.getInt(1),pac.getInt(2));
               p.MapID = pac.getInt(3);
               p.Type = pac.getInt(4);
               p.isEnabled = pac.splited[6]=="1";
               this.n.GameData.Portals.put(p.pos.x*p.pos.y, p);
            break;
            
            case "mapout":
              this.n.send("c_close");
              this.n.send("f_stash_end");
              this.n.GameData.Characters = new HashMap<Integer, Character>();
              this.n.GameData.map = null;
              this.n.GameData.Mobs = new HashMap<Integer, Mob>();
              this.n.GameData.Items = new HashMap<Integer, Item>();
              this.n.GameData.Portals = new HashMap<Integer, Portal>();
            break;
            
            case "rest":
          	  // rest 1 374541 0
          	  if(pac.getInt(2)==this.n.GameData.Character.id)
          	  {
          		  this.n.GameData.Character.IsSitting = pac.splited[3]=="1";
          	  }
          	  break;
          	//Attacking
            //case "sr":
          	//  this.n.GameData.Character.skills[this.n.GameData.Character.getSkillPosByCastID(pac.getInt(1))].IsOnCooldown = false;
          	//  break;
          	  
            case "ct":
            	//ct 1 {Session.Character.CharacterId} 1 {Session.Character.CharacterId} {ski.Skill.CastAnimation} {skillinfo?.Skill.CastEffect ?? ski.Skill.CastEffect} {ski.Skill.SkillVNum}
            	if(pac.getInt(2)==this.n.GameData.Character.id)//Else who cares?
            	{
            		int skillVNUM = pac.getInt(7);
            		for(int ii = 0;ii<this.n.GameData.Character.skills.length;ii++)
            		if(this.n.GameData.Character.skills[ii].VNUM==skillVNUM)this.n.GameData.Character.skills[ii].IsOnCooldown = true;
            	}
            	
            	break;
            
            
            
            case "su":
                if(pac.splited[1]=="3") //Monster attacking someone
                {
              	  if(pac.getInt(4)!=this.n.GameData.Character.id){}//If it isnt me who cares?
              	  if(this.n.GameData.Character.ShouldRest){this.n.MobStoppedResting(pac.getInt(2));}
              	  this.n.GameData.Character.Hp -= Integer.parseInt(pac.splited[13]);  
              	  this.n.send("ncif 3 "+pac.getInt(2));
                }
                else if(pac.splited[1]=="1") //Someone attacking something
                {
              	  //su 1 {hitRequest.Session.Character.CharacterId} 3 {MapMonsterId} {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown} {hitRequest.SkillCombo.Animation} {hitRequest.SkillCombo.Effect} {hitRequest.Session.Character.PositionX} {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)} {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage} {hitmode} {hitRequest.Skill.SkillType - 1}
                  //Mob m = this.n.GameData.Mobs.get(pac.getInt(4));
                  //TODO dodìlat!!!
                  if(pac.getInt(11)==0) //Monster died
                  {
                	  this.n.GameData.Mobs.remove(pac.getInt(4));
                	  if(pac.getInt(2)==this.n.GameData.Character.id)// I killed it
                	  {
                		  if(this.n.target.id == pac.getInt(4))
                		  {
                			  this.n.target = null;
                		  }
                	  }
                  }
                	  
                }
                
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
