package nostale.handler;

import nostale.gameobject.Player;
import nostale.handler.interfaces.ITalkHandler;
import nostale.packet.Packet;

public class TalkHandler extends Handler implements ITalkHandler{

	public TalkHandler(Player p) {
		super(p);
	}
	
	@Override
	public void parsePacket(Packet p)
	{
		switch(p.name)
		{
		//spk 1 351883 5 Helèa258 ssssss

		case "spk":
			onPM(Long.parseLong(p.getParameter(2)),p.getParameter(4),p.getParameter(5));
			break;
		}
	}
	
	public void say(String content)
	{
		player.send(new Packet("say "+content));
	}
	
	public void pm(String name,String content)
	{
		player.send(new Packet("/"+name+" "+content));
	}
	
	@Override
	public void onPM(long playerID,String name,String content)
	{
		
	}

}
