package nostale.gameobject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import nostale.data.AccountData;
import nostale.data.MapCharacterInstance;
import nostale.data.MapInstance;
import nostale.event.GameEvent;
import nostale.event.GameEventListener;
import nostale.event.PacketEvent;
import nostale.event.PacketEventListener;
import nostale.handler.ConnectionHandler;
import nostale.handler.LoginHandler;
import nostale.packet.Packet;
import nostale.packet.PacketType;

public class Player extends MapCharacterInstance{
	public boolean debug = true;
	public AccountData accData;
	public int session;
	public int mapId;
	public MapInstance map;

	public ArrayList<GameEvent> gameEvents;
	public ArrayList<PacketEvent> packetEvents;
	public ArrayList<GameEventListener> gameEventListeners;
	public ArrayList<PacketEventListener> packetEventListeners; 
	public ConnectionHandler connectionHandler;
 	public Player(AccountData ccData)
	{
 		this.accData = ccData;
		this.gameEvents = new ArrayList<GameEvent>();
		this.packetEvents = new ArrayList<PacketEvent>();
		this.gameEventListeners = new ArrayList<GameEventListener>();
		this.packetEventListeners = new ArrayList<PacketEventListener>();
		LoginHandler loginHandler = new LoginHandler(this);
		connectionHandler = new ConnectionHandler(this,loginHandler);
	}
 	
 	public void addGameEventListener(GameEventListener listener)
 	{
 		gameEventListeners.add(listener);
 	}
 	public void addPacketEventListener(PacketEventListener listener)
 	{
 		packetEventListeners.add(listener);
 	}
 	
 	public void addGameEvent(GameEvent event)
 	{
 		gameEvents.add(event);
 	}

 	public void addPacketEvent(PacketEvent event)
 	{
 		packetEvents.add(event);
 	}
 	public void loop()
 	{

 		connectionHandler.packetReceiveListener.call();
 		for(int i = 0;i<packetEventListeners.size();i++)
 		{
 			PacketEventListener pel = packetEventListeners.get(i);
 			pel.call();
 		}

 		for(int i = 0;i<gameEventListeners.size();i++)
 		{
 			GameEventListener gel = gameEventListeners.get(i);
 			gel.call();
 		}

 		connectionHandler.packetSendListener.call();
 		gameEvents.clear();
 		packetEvents.clear();

 	}
 	
 	public void sendPacket(Packet p)
 	{
 		p.packetType = PacketType.SEND;
 		PacketEvent pa = new PacketEvent(p);
 		addPacketEvent(pa);
 	}
 	public void sendPacket(String packet)
 	{
 		sendPacket(new Packet(packet));
 	}
 	
 	public void log(String tag,String content)
 	{
 		if(debug)
 		{
 			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 			System.out.println(sdf.format(new Date())+" "+tag.toUpperCase()+": "+content);
 		}
 	}

}
