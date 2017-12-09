package nostale.net;

import java.util.HashMap;

import nostale.packet.Packet;

public class SmartConnection extends Connection {
	private HashMap<String, Long> PacketTypesSend; // this is array for storing
													// when what packettype was
													// send

	public SmartConnection() {
	}

	public void Send(Packet p) {
		if (p.timeToWait > 0) {
			if (PacketTypesSend.containsKey(p.name)) {
				long lastTime = PacketTypesSend.get(p.name);
			} else {
				try {
					send(p.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				PacketTypesSend.put(p.name, System.currentTimeMillis());
			}
		} else {
			try {
				send(p.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}