package nostale.handler;

public class BattlePacketHandler extends Handler{
    public Nostale n;
    public Skill lastSkillRequest; //last skill wich was sent to server but we dont know if was sucesfull or not :)
    
    public BattlePacketHandler(Nostale n)
    {
        this.n = n;
    }
    
    public void UseSkill(Skill s,MapMobInstance m)
    {
        //{CastId} {UserType} {MapMonsterId} {MapX} {MapY}
        this.lastSkillRequest = s;
        n.send("u_s "+s.CastId+" 3 "+m.id+" "+n.GameData.Character.Pos.x+" "+n.GameData.Character.Pos.y);
    }
    
    public void onReceive(String packet)
    {
        Packet pac = new Packet(packet);
        switch(pac.type)
        {
            case "cancel": //casting spell canceled (lastSkillRequest)
                break;
        
            case "ct": //Somebody started attacking
            //ct 1 {Session.Character.CharacterId} 3 {monsterToAttack.MapMonsterId} {ski.Skill.CastAnimation} {characterSkillInfo?.Skill.CastEffect ?? ski.Skill.CastEffect} {ski.Skill.SkillVNum}
            if(pac.getInt(2)==n.GameData.Character.id)
            {
               //TODO Parse cooldowns etc..
               n.GameData.Character.Skills.get(); //get by VNUM
            }
                break;

            
            case "su": //Somebody attacked, get info from that
            //su 1 {hitRequest.Session.Character.CharacterId} 3 {MapMonsterId} {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown} {hitRequest.Skill.AttackAnimation} {hitRequest.SkillEffect} {hitRequest.Session.Character.PositionX} {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)} {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage} {hitmode} {hitRequest.Skill.SkillType - 1}
                    if(pac.splited[1]=="3") //Monster attacking someone
                    {
                  	  if(pac.getInt(4)!=GameData.Character.id){}//If it isnt me who cares?
                  	  MobAttackedMe(pac.getInt(2));
                  	  GameData.Character.Hp -= Integer.parseInt(pac.splited[13]);  
                  	  send("ncif 3 "+pac.getInt(2));
                    }
                    else if(pac.splited[1]=="1") //Someone attacking something
                    {
                  	  //su 1 {hitRequest.Session.Character.CharacterId} 3 {MapMonsterId} {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown} {hitRequest.SkillCombo.Animation} {hitRequest.SkillCombo.Effect} {hitRequest.Session.Character.PositionX} {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)} {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage} {hitmode} {hitRequest.Skill.SkillType - 1}
                      //MapMobInstance m = n.GameData.get(pac.getInt(4));
                      //TODO dodìlat!!!
                      if(pac.getInt(11)==0) //Monster died
                      {
                    	  GameData.Mobs.remove(pac.getInt(4));
                    	  if(pac.getInt(2)==GameData.Character.id)// I killed it
                    	  {
                    		  if(target.id == pac.getInt(4))
                    		  {
                                  //if i killed target, null it
                    			  target = null;
                    		  }
                    	  }
                      }
                    	  
                    }
                break;
                
              
            case "sr": //cooldown 
            
                break;
        }
        
    }
}
