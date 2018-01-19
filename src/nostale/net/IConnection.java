package nostale.net;

import nostale.packet.Packet;

public interface IConnection {
	public void onReceive(Packet packet);
	public void onSend(Packet packet);
}
