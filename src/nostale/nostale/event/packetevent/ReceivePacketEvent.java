package nostale.nostale.event.packetevent;

import nostale.nostale.event.PacketEvent;
import nostale.packet.Packet;

public class ReceivePacketEvent extends PacketEvent{

	public ReceivePacketEvent(Packet packet) {
		super(packet);
	}
	
}
