package nostale.packet.receive;

import nostale.domain.UserType;
import nostale.packet.Packet;

// Skill used packet. received when someone/something attacks somebody/something
public class SuPacket extends Packet{
	public UserType attackerType;
	public int attackerId;
	public UserType attackedType;
	public int attackedId;
	public short skillVNum;
	public short skillCooldown;
	public short attackAnimation;
	public short skillEffect;
	public short x;
	public short y;
	public boolean isAlive;
	public byte targetPercHP;
	public int damage;
	public short hitmode;
	public short skillType;
	// su {attackerType - usertype} {hitRequest.Session.Character.CharacterId} 
	//{attackedType - usertype} {MapMonsterId}
	// {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown}
	// {hitRequest.Skill.AttackAnimation} {hitRequest.SkillEffect}
	// {hitRequest.Session.Character.PositionX}
	// {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)}
	// {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage}
	// {hitmode} {hitRequest.Skill.SkillType - 1}
	public SuPacket(String str) {
		super(str);
		try
		{
			if(parameters[1].equals("1"))
			attackerType = UserType.Player;
			if(parameters[1].equals("2"))
			attackerType = UserType.Npc;
			if(parameters[1].equals("3"))
			attackerType = UserType.Monster;
			attackerId = Integer.parseInt(parameters[2]);
			if(parameters[3].equals("1"))
			attackedType = UserType.Player;
			if(parameters[3].equals("2"))
			attackedType = UserType.Npc;
			if(parameters[3].equals("3"))
			attackedType = UserType.Monster;
			attackedId = Integer.parseInt(parameters[4]);
			skillVNum = Short.parseShort(parameters[5]);
			skillCooldown = Short.parseShort(parameters[6]);
			attackAnimation = Short.parseShort(parameters[7]);
			skillEffect = Short.parseShort(parameters[8]);
			x = Short.parseShort(parameters[9]);
			y = Short.parseShort(parameters[10]);
			isAlive = parameters[11].equals("1");
			targetPercHP = Byte.parseByte(parameters[12]);
			damage = Integer.parseInt(parameters[13]);
			hitmode = Short.parseShort(parameters[14]);
			skillType = Short.parseShort(parameters[15]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
