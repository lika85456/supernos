package nostale.packet.receive;

import nostale.domain.UserType;
import nostale.packet.Packet;
import nostale.util.Pos;

//Probably some gameobject enters into the map (not exactly enters, he is just there)
public class InPacket extends Packet{
	/*in 1 {(Authority == AuthorityType.Moderator ? 
	 * $"[{Language.Instance.GetMessageFromKey("SUPPORT")}]" + name : name)}
	 *  - 
	 *  {CharacterId} 
	 *  {PositionX} {PositionY} {Direction}
	 *   {(Undercover ? (byte)AuthorityType.User : Authority < AuthorityType.User ? (byte)AuthorityType.User : (byte)Authority)} 
	 *   {(byte)Gender} {(byte)HairStyle} {color} {(byte)Class} {GenerateEqListForPacket()} 
	 *   {Math.Ceiling(Hp / HPLoad() * 100)} {Math.Ceiling(Mp / MPLoad() * 100)} 
	 *   {(IsSitting ? 1 : 0)} {(Group?.GroupType == GroupType.Group ? (long)Group?.GroupId : -1)} 
	 *   {(fairy != null ? 4 : 0)} {fairy?.Item.Element ?? 0} 0 {fairy?.Item.Morph ?? 0} 0 
	 *   {(UseSp || IsVehicled ? Morph : 0)} {GenerateEqRareUpgradeForPacket()} 
	 *   {(foe ? -1 : Family?.FamilyId ?? -1)} {(foe ? name : Family?.Name ?? "-")} 
	 *   {(GetDignityIco() == 1 ? GetReputIco() : -GetDignityIco())} {(Invisible ? 1 : 0)} 
	 *   {(UseSp ? MorphUpgrade : 0)} {faction} {(UseSp ? MorphUpgrade2 : 0)} {Level} 
	 *   {Family?.FamilyLevel ?? 0} {ArenaWinner} {(Authority == AuthorityType.Moderator ? 500 : Compliment)} 
	 *   {Size} {HeroLevel}";
	 */
	
	
	/*
	 * in 3 {MonsterVNum} {MapMonsterId} {MapX} {MapY} {Position} 
	 * {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} 
	 * {(int)((float)CurrentMp / (float)Monster.MaxMP * 100)} 0 0 0 -1 
	 * {(Monster.NoAggresiveIcon ? (byte)InRespawnType.NoEffect : (byte)InRespawnType.TeleportationEffect)} 
	 * 0 -1 - 0 -1 0 0 0 0 0 0 0 0
	 */

	public UserType userType;
	public int userId;
	public int monsterVNum;
	public Pos pos;
	public byte hpPercent;
	public byte mpPercent;
	public String name;
	public byte authority;
	public byte level;
	
	public byte direction;
	public boolean isSitting;
	public boolean isInvisible;
	//TODO make more info from the packet
	public InPacket(String str) {
		super(str);
		userType = parameters[1].equals("1") ? UserType.Player : UserType.Monster; //TODO rly
		if(userType==UserType.Monster)
		{
			userId = Integer.parseInt(parameters[3]);
			monsterVNum = Integer.parseInt(parameters[2]);
			pos = new Pos(Integer.parseInt(parameters[4]),Integer.parseInt(parameters[5]));
			hpPercent = Byte.parseByte(parameters[7]);
			mpPercent = Byte.parseByte(parameters[8]);
		}
		else if(userType==UserType.Player)
		{
			name = parameters[2];
			pos = new Pos(Integer.parseInt(parameters[5]),Integer.parseInt(parameters[6]));
			direction = Byte.parseByte(parameters[7]);
			authority = Byte.parseByte(parameters[8]);
			isSitting = "1".equals(parameters[16]);
			isInvisible = "1".equals(parameters[28]);
			level = Byte.parseByte(parameters[32]);
			userId = Integer.parseInt(parameters[4]);
		}
	}

}
