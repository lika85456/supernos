package nostale.gameobject;

import nostale.net.Connection;
import nostale.net.Crypto;
import nostale.packet.Packet;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nostale.data.AccountData;
import nostale.data.MapCharacterInstance;

public class Player extends MapCharacterInstance{
	public AccountData accData;
	public Timer t;
	public TimerTask tt;
	public int packetId = 247;
	public Connection c;
	public int session;
	public int mapId;
	
	public ArrayList<String> received;
	public Player()
	{
		received = new ArrayList<String>();
	}
	
	public void send(Packet p)
	{
		try {
			packetId++;
			c.send(Crypto.EncryptGamePacket(packetId+" "+p.toString(), session, false));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receive()
	{
		String[] rece = c.getReceived();
		for(String rec:rece)
		{
			received.add(rec);
		}
	}
	public void clear()
	{
		received = null;
		received = new ArrayList<String>();
	}
}
