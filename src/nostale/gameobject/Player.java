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
import nostale.handler.ConnectionHandler;
import nostale.handler.Handler;

public class Player extends MapCharacterInstance{
	public AccountData accData;
	public int session;
	public int mapId;
	public MapInstance map;
	
	public ArrayList<GameEvent> gameEvents;
	public ArrayList<PacketEvent> packetEvents;
	public ArrayList<GameEventListener> gameEventListeners;
	public ArrayList<PacketEventListener> packetEventListeners; 
	public ConnectionHandler connectionHandler;
 	public Player()
	{
		this.gameEvents = new ArrayList<GameEvent>();
		this.packetEvents = new ArrayList<PacketEvent>();
		this.gameEventListeners = new ArrayList<GameEventListener>();
		this.packetEventListeners = new ArrayList<PacketEventListener>();
		connectionHandler = new ConnectionHandler(this);
	}

 	public void loop()
 	{
 		for(PacketEventListener pel:packetEventListeners)
 		{
 			pel.call();
 		}
 		for(GameEventListener gel:gameEventListeners)
 		{
 			gel.call();
 		}
 		
 		gameEvents.clear();
 		packetEvents.clear();
 	}

}
