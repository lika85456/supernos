package nostale.handler;

import nostale.gameobject.Player;
import nostale.packet.Packet;

public class SpecialistHandler extends Handler{
	private Boolean isOn = false;
	private Long lastTime = (long) 0;
	public SpecialistHandler(Player p)
	{
		super(p);
	}
	public Boolean putOn()
	{
		if(System.currentTimeMillis()<lastTime+30000)
		{
			return false;
		}
		player.send(new Packet("sl 0"));
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.send(new Packet("#sl^1"));
		isOn=true;
		return true;
	}
	public void putOff()
	{
		player.send(new Packet("sl 0"));
		isOn=false;
		
	}
	public Boolean isOn()
	{
		return isOn;
	}
	public void parsePacket(Packet p)
	{
		
	}
}
