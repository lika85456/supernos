package nostale.handler;

import nostale.data.Player;

public abstract class Handler {
	public Player player;
	public Handler(Player p)
	{
		this.player = p;
	}

}
