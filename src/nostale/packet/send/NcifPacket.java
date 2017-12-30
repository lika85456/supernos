package nostale.packet.send;

import nostale.data.MapCharacterInstance;
import nostale.data.NpcMonsterInstance;
import nostale.packet.Packet;

//Information packet
public class NcifPacket extends Packet{

	public NcifPacket(MapCharacterInstance s)
	{
		super("ncif 1 "+s.id);
	}
	public NcifPacket(NpcMonsterInstance s)
	{
		super("ncif 3 "+s.id);
	}
	
	public NcifPacket(byte type,int id) {
		super("ncif "+type+" "+id);
	}

}
