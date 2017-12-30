package nostale.packet.receive;

import nostale.packet.Packet;
import nostale.util.Pos;

public class AtPacket extends Packet{
	public int characterId;
	public int mapId;
	public Pos pos;
	public AtPacket(String str) {
		super(str);
		//$"at {CharacterId} {MapInstance.Map.MapId} {PositionX} {PositionY} 2 0 {mapForMusic?.Map.Music ?? 0} -1";
		characterId = getIntParameter(1);
		mapId = getIntParameter(2);
		pos = new Pos(getIntParameter(3),getIntParameter(4));
	}

}
