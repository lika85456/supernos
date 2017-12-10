package nostale.gameobject;

import nostale.net.Connection;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nostale.data.AccountData;
import nostale.handler.LoginHandler;

public class Player {
	public AccountData accData;
	public Timer t;
	public TimerTask tt;
	public int packetId = 247;
	public Connection c;
	public int session;
	
	public ArrayList<String> received;
	public Player()
	{
		received = new ArrayList<String>();
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