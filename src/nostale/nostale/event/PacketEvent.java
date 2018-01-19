package nostale.nostale.event;

import nostale.packet.Packet;
import nostale.packet.PacketType;

public class PacketEvent {
	public Packet packet;
	
	public PacketEvent(Packet packet)
	{
		this.packet = packet;
	}
	
	public PacketType getType()
	{
		return packet.packetType;
	}
}
