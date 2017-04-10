package nostale.handler;

public class BattlePacketHandler extends Handler{
    public Nostale n;
    public Skill lastSkillRequest; //last skill wich was sent to server but we dont know if was sucesfull or not :)
    
    public BattlePacketHandler(Nostale n)
    {
        this.n = n;
    }
    
    public String UseSkill(Skill s,MapMobInstance m)
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
            }
                break;
            
            case "su": //Somebody attacked, get info from that
            //su 1 {hitRequest.Session.Character.CharacterId} 3 {MapMonsterId} {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown} {hitRequest.Skill.AttackAnimation} {hitRequest.SkillEffect} {hitRequest.Session.Character.PositionX} {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)} {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage} {hitmode} {hitRequest.Skill.SkillType - 1}
             
                break;
                
              
            case "sr": //cooldown 
            
                break;
        }
        
    }
}
