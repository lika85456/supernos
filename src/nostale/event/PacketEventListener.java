package nostale.event;

import java.util.Iterator;

import nostale.gameobject.Player;
import nostale.packet.Packet;
import nostale.packet.PacketType;

public class PacketEventListener extends EventListener{

	public PacketEventListener(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void call()
	{
		for(int i =0;i<player.packetEvents.size();i++)
		{
		    PacketEvent pEvent = player.packetEvents.get(i);
			if(pEvent.getType()==PacketType.RECEIVE)
			{
				packetCall(pEvent.packet);
			}
		}

	}
	
	public void packetCall(Packet p)
	{
		
	}

}