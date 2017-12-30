package nostale.packet.receive;

import nostale.packet.Packet;

public class CondPacket extends Packet{
	public int speed;
	public CondPacket(String str) {
		super(str);
		//$"cond 1 {CharacterId} {(NoAttack ? 1 : 0)} {(NoMove ? 1 : 0)} {Speed}";
		speed = getIntParameter(5);
	}

}
