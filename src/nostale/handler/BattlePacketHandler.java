package nostale.handler;

import nostale.Packet;
import nostale.data.MapMobInstance;
import nostale.data.Player;
import nostale.data.Skill;
import nostale.util.Pos;

public class BattlePacketHandler extends Handler{
    public Skill lastSkillRequest; //last skill wich was sent to server but we dont know if was sucesfull or not :)
    public long canUse = 0;
    public BattlePacketHandler(Player player)
    {
        super(player);
    }
    
    public void UseSkill(Skill s,MapMobInstance m)
    {
        //{CastId} {UserType} {MapMonsterId} {MapX} {MapY}
    	if(canUse>System.currentTimeMillis()){return;}
    	this.canUse = System.currentTimeMillis();
        this.lastSkillRequest = s;
        player.walkHandler.Walk(Pos.getShortestPosInRange(1,m.Pos,player.pos));
        player.send("u_s "+s.CastId+" 3 "+m.id+" "+player.pos.x+" "+player.pos.y);
        canUse += s.CastTime*1000-100;
    }
    
    public void onReceive(String packet)
    {
        Packet pac = new Packet(packet);
        switch(pac.type)
        {
            case "cancel": //casting spell canceled (lastSkillRequest)
                break;
        
            //case "ct": //Somebody started attacking
            //ct 1 {Session.Character.CharacterId} 3 {monsterToAttack.MapMonsterId} {ski.Skill.CastAnimation} {characterSkillInfo?.Skill.CastEffect ?? ski.Skill.CastEffect} {ski.Skill.SkillVNum}
            //if(pac.getInt(2)==n.GameData.Character.id)
            //{
               //TODO Parse cooldowns etc..
            //   n.GameData.Character.getSkillByVNUM(pac.getInt(7)); //get by VNUM
            //}
             //   break;


            
            case "su": //Somebody attacked, get info from that
            //su 1 {hitRequest.Session.Character.CharacterId} 3 {MapMonsterId} {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown} {hitRequest.Skill.AttackAnimation} {hitRequest.SkillEffect} {hitRequest.Session.Character.PositionX} {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)} {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage} {hitmode} {hitRequest.Skill.SkillType - 1}   
            	if(pac.splited[1].equals("3")) //Monster attacking someone
                    {
                  	  if(pac.getInt(4)!=player.id){return;}//If it isnt me who cares?
                  	  player.MobAttackedMe(pac.getInt(2));
                  	  player.Hp -= Integer.parseInt(pac.splited[13]);  
                      player.send("ncif 3 "+pac.getInt(2));
                    }
                    else if(pac.splited[1].equals("1")) //Someone attacking something
                    {
                      if(pac.getInt(2)==player.id)
                      {
                    	  player.getSkillByVNUM(pac.getInt(5)).IsOnCooldown = true;
                      }
                  	  //su 1 {hitRequest.Session.Character.CharacterId} 3 {MapMonsterId} {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown} {hitRequest.SkillCombo.Animation} {hitRequest.SkillCombo.Effect} {hitRequest.Session.Character.PositionX} {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)} {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage} {hitmode} {hitRequest.Skill.SkillType - 1}
                      //MapMobInstance m = n.GameData.get(pac.getInt(4));
                      //TODO dodìlat!!!
                      if(pac.getInt(11)==0) //Monster died
                      {
                    	  player.gameData.map[player.gameDataMapID].Mobs.remove(pac.getInt(4));
                    	  if(pac.getInt(2)==player.id)// I killed it
                    	  {
                    		  if(player.target!=null && player.target.id == pac.getInt(4))
                    		  {
                                  //if i killed target, null it
                    			  player.target = null;
                    		  }
                    	  }
                      }
                    	  
                    }
                break;
                
              
            case "sr": //cooldown 
            player.getSkillByCastID(pac.getInt(1)).IsOnCooldown = false;
                break;
        }
        
    }
}

