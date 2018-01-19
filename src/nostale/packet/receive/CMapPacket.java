package nostale.packet.receive;

import nostale.packet.Packet;

public class CMapPacket extends Packet{

	public int mapId;

	
	public CMapPacket(String str) {
		super(str);
		mapId = gip(2);
		//TODO add MapInstaceType
		//c_map 0 {MapInstance.Map.MapId} {(MapInstance.MapInstanceType != MapInstanceType.BaseMapInstance ? 1 : 0)}
	}

}
