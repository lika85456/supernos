package nostale.packet.receive;

import nostale.domain.UserType;
import nostale.packet.Packet;

//Skill casted
public class CtPacket extends Packet {
	public UserType attackerType;
	public int attackerId;
	public UserType attackedType;
	public int attackedId;
	public short castAnimation;
	public short castEffect;
	public int skillVNum;

	public CtPacket(String str) {
		super(str);
		if (parameters[1].equals("1"))
			attackerType = UserType.Player;
		if (parameters[1].equals("2"))
			attackerType = UserType.Npc;
		if (parameters[1].equals("3"))
			attackerType = UserType.Monster;
		attackerId = Integer.parseInt(parameters[2]);
		if (parameters[3].equals("1"))
			attackedType = UserType.Player;
		if (parameters[3].equals("2"))
			attackedType = UserType.Npc;
		if (parameters[3].equals("3"))
			attackedType = UserType.Monster;
		attackerId = Integer.parseInt(parameters[2]);
		attackedId = Integer.parseInt(parameters[4]);
		castAnimation = Short.parseShort(parameters[5]);
		castEffect = Short.parseShort(parameters[6]);
		skillVNum = Integer.parseInt(parameters[7]);
	}
	/*
	 * ct 1 {Session.Character.CharacterId} 1 {Session.Character.CharacterId}
	 * {ski.Skill.CastAnimation} {skillinfo?.Skill.CastEffect ??
	 * ski.Skill.CastEffect} {ski.Skill.SkillVNum}
	 */
}
