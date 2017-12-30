package nostale.packet.receive;

import nostale.packet.Packet;

public class StatPacket extends Packet{
	// stat {Hp} {HPLoad()} {Mp} {MPLoad()} 0 {option}
	public int hp;
	public int hpLoad;
	public int mp;
	public int mpLoad;
	public StatPacket(String str) {
		super(str);
		hp = gip(1);
		hpLoad = gip(2);
		mp = gip(3);
		mpLoad = gip(4);
	}

}
