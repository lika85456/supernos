package nostale.handler;
import nostale.event.packetEventListener.MapDataPacketEventListener;
import nostale.event.packetEventListener.PlayerDataPacketEventListener;
import nostale.gameobject.Player;


public class MapHandler extends Handler{
	public MapHandler(Player p) {
		super(p);
		p.addPacketEventListener(new PlayerDataPacketEventListener(p));
		p.addPacketEventListener(new MapDataPacketEventListener(p));
	}

}
