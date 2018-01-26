package nostale.event.gameEventListener;

import java.util.ArrayList;

import nostale.event.GameEventListener;
import nostale.event.PacketEvent;
import nostale.gameobject.Player;
import nostale.handler.ConnectionHandler;
import nostale.net.Crypto;
import nostale.packet.GamePacket;
import nostale.packet.PacketType;

public class PacketReceiveListener extends GameEventListener{
	public ConnectionHandler connectionHandler;
	public PacketReceiveListener(Player p,ConnectionHandler c) {
		super(p);
		this.connectionHandler = c;
	}

	@Override
	public void call()
	{
		ArrayList<String> received = Crypto.DecryptGamePacket(connectionHandler.connection.getReceived());
		//long time = System.currentTimeMillis();
		for(String packet:received)
		{
			if(!(packet.contains("mv")) && !(packet.contains("stat")) && !(packet.contains("in")))
			player.log("Packet received", packet);
			GamePacket tPacket = new GamePacket(packet);
			tPacket.packetType = PacketType.RECEIVE;
			PacketEvent pEvent = new PacketEvent(tPacket);
			//pEvent.time = time;
			player.addPacketEvent(pEvent);
		}
	}
}
