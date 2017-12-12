package nostale.handler;

import java.util.ArrayList;

import nostale.gameobject.Player;
import nostale.packet.Packet;

public class Handler{
	protected Player player;

	public void parsePacket(Packet p)
	{
		
	}

	public Handler(Player p) {
		player = p;
	}

	public void parse() {
		ArrayList<String> toParse = player.received;
		for (String po : toParse) {
		parsePacket(new Packet(po));
		}
	}
}
