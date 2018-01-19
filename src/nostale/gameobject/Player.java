package nostale.gameobject;

import nostale.net.Connection;
import nostale.net.Crypto;
import nostale.nostale.event.GameEvent;
import nostale.nostale.event.GameEventListener;
import nostale.nostale.event.PacketEvent;
import nostale.nostale.event.PacketEventListener;
import nostale.packet.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import nostale.data.AccountData;
import nostale.data.MapCharacterInstance;
import nostale.data.MapInstance;
import nostale.data.SkillData;
import nostale.handler.Handler;

public class Player extends MapCharacterInstance{
	public AccountData accData;
	public int session;
	public int mapId;
	public MapInstance map;
	
	public ArrayList<GameEvent> gameEvent;
	public ArrayList<PacketEvent> packetEvent;
	public ArrayList<GameEventListener> gameEventListener;
	public ArrayList<PacketEventListener> packetEventListener; 
	
 	public Player()
	{
		this.gameEvent = new ArrayList<GameEvent>();
		this.packetEvent = new ArrayList<PacketEvent>();
		this.gameEventListener = new ArrayList<GameEventListener>();
		this.packetEventListener = new ArrayList<PacketEventListener>();
	}

 	public void loop()
 	{
 		//New thread
 	}

}
