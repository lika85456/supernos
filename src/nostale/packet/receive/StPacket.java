package nostale.packet.receive;

import nostale.domain.UserType;
import nostale.packet.Packet;

//NamedCharacterInformation
public class StPacket extends Packet{
	/*$"st 1 {CharacterId} {Level} {HeroLevel} 
	 * {(int)(Hp / (float)HPLoad() * 100)} {(int)(Mp / (float)MPLoad() * 100)} {Hp} {Mp}
	 * {Buff.Replace(s => !s.StaticBuff).Aggregate(string.Empty, (current, buff) => current + $" {buff.Card.CardId}")}"
	$"st 3 {ncifPacket.TargetId} {monsterinfo.Level} {monsterinfo.HeroLevel} 
	{(int)((float)monster.CurrentHp / (float)monster.Monster.MaxHP * 100)} 
	{(int)((float)monster.CurrentMp / (float)monster.Monster.MaxMP * 100)} 
	{monster.CurrentHp} {monster.CurrentMp}
	{monster.Buff.Replace(s => !s.StaticBuff).Aggregate(string.Empty, (current, buff) => current + $" {buff.Card.CardId}.{buff.Level}")}"
	*/
	public UserType userType;
	public int userId;
	public int level;
	public int heroLevel;
	public int hpPerc;
	public int mpPerc;
	public int hp;
	public int mp;
	
	public StPacket(String str) {
		super(str);
		byte userTypeByte = (byte)getIntParameter(1);
		if(userTypeByte==1) userType = UserType.Player;
		if(userTypeByte==2) userType = UserType.Npc;
		if(userTypeByte==3) userType = UserType.Monster;
		if(userTypeByte==9) userType = UserType.Object;
		userId = gip(2);
		level = gip(3);
		heroLevel = gip(4);
		hpPerc = gip(5);
		mpPerc = gip(6);
		hp = gip(7);
		mp = gip(8);
	}
	
}
