package nostale.gameobject;

import nostale.net.Connection;
import nostale.net.Crypto;
import nostale.packet.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import nostale.data.AccountData;
import nostale.data.MapCharacterInstance;
import nostale.data.Skill;
import nostale.handler.Handler;

public class Player extends MapCharacterInstance{
	public AccountData accData;
	public Timer t;
	public TimerTask tt;
	public int packetId = 247;
	public Connection c;
	public int session;
	public int mapId;
	public HashMap<Integer,Skill> skills;
	public ArrayList<Handler> handlers;
	public Player()
	{
		skills = new HashMap<Integer,Skill>();
		this.handlers = new ArrayList<Handler>();
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
	
	
	public void addHandler(Handler h)
	{
		handlers.add(h);
	}
	
	public void receiveAndParse()
	{
		String[] rece = c.getReceived();
		for(String rec:rece)
		{
			for(Handler handler:handlers)
			{
				handler.parsePacket(new Packet(rec));
			}
		}
	}

}
