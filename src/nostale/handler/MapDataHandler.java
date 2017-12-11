package nostale.handler;

import java.util.ArrayList;

import nostale.gameobject.Player;
import nostale.packet.Packet;

public class MapDataHandler extends Handler implements IHandler{
	public MapDataHandler(Player p)
	{
		super(p);
	}
	
	public void parse()
	{
		ArrayList<String> toParse = player.received;
	}

}
