package nostale.handler;

import nostale.gameobject.Player;
import nostale.packet.Packet;
import nostale.util.Pos;

public class WalkHandler extends Handler{
	public WalkHandler(Player p)
	{
		super(p);
	}
	public void Walk(Pos p)
	{
		this.Walk(Pos.getPath(player.map, player.pos, p),0);
	}
	public void Walk(int x,int y)
	{
		this.Walk(new Pos(x,y));
	}
	private void Walk(Pos[] path,int index)
    {
    	if(index>=path.length-1)
    	{
    		player.IsMoving = false;
    		return;
    	}

        	player.IsMoving = true;
        	player.sendPacket(new Packet("walk "+path[index].x+" "+path[index].y+" 0 "+player.Speed));
        	index++;
        	final int i = index;
        	int timeToWait = (1/player.Speed*1000);
        	new java.util.Timer().schedule( 
        	        new java.util.TimerTask() {
        	            @Override
        	            public void run() {
        	                Walk(path,i);
        	            }
        	        }, 
        	        timeToWait 
        			);
    }
}
