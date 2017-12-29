package nostale.handler;

import nostale.gameobject.Player;
import nostale.handler.interfaces.IHandler;
import nostale.packet.Packet;

public class Handler implements IHandler{
	protected Player player;

	@Override
	public void parsePacket(Packet p)
	{
		
	}

	public Handler(Player p) {
		player = p;
		p.addHandler(this);
	}

}
