package nostale.handler;

import java.util.ArrayList;

import nostale.gameobject.Player;
import nostale.handler.interfaces.IHandler;
import nostale.packet.Packet;

public class Handler implements IHandler{
	protected Player player;

	public void parsePacket(Packet p)
	{
		
	}

	public Handler(Player p) {
		player = p;
		p.addHandler(this);
	}

}
