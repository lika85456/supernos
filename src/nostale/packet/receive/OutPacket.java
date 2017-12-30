package nostale.packet.receive;

import nostale.packet.Packet;

public class OutPacket extends Packet{
	public int characterId;
	public OutPacket(String str) {
		super(str);
		characterId = gip(2);
	}

}
