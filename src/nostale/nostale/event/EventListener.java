package nostale.nostale.event;

import nostale.gameobject.Player;

public class EventListener implements IEventListener{
	public Player player;
	public EventListener(Player p)
	{
		this.player = p;
	}
	public void call()
	{
		
	}
}
