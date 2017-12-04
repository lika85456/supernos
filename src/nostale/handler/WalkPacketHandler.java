package nostale.handler;
import nostale.data.Player;
import nostale.util.Pos;
public class WalkPacketHandler extends Handler{

    
    public WalkPacketHandler(Player player)
    {
    	super(player);
    }
    
    public void Walk(Pos p)
    {
    	
        this.Walk(Pos.getPath(player.gameData.map[player.gameDataMapID], player.pos, p),0);
    }

    private void Walk(Pos[] path,int index)
    {
    	if(index>=path.length)
    	{
    		player.IsMoving = false;
    		return;
    	}

        	player.IsMoving = true;
        	player.send("walk "+path[index].x+" "+path[index].y+" 0 "+player.Speed);
        	index++;
        	final int i = index;
        	int timeToWait = (1000/player.Speed);
        	new java.util.Timer().schedule( 
        	        new java.util.TimerTask() {
        	            @Override
        	            public void run() {
        	                Walk(path,i);
        	            }
        	        }, 
        	        timeToWait 
        	);

    	
/*    	if(player.IsMoving==true) return;
        if(player.gameData.map[player.gameDataMapID].canWalkHere(x,y))
        {
           if(Pos.getRange(new Pos(x,y),player.pos)>10)
           {
               Pos[] walkTo = Pos.getPath(player.gameData.map[player.gameDataMapID],player.pos,new Pos(x,y));
               for(Pos toWalk: walkTo)
               {
            	   double t = (1000*Pos.getRange(player.pos,new Pos(x,y))/player.Speed);
            	   int toSleep = (int)(t);
            	   if(toSleep==0){return;}
            	   player.send("walk "+toWalk.x+" "+toWalk.y+" 0 "+player.Speed);
            	   try {
					player.pos = toWalk;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               }
               //TODO walk to.. best with waiting (not sleep) -> Character.IsMoving = true;
           }
           else
           {
        	   double t = (1000*Pos.getRange(n.GameData.Character.Pos,new Pos(x,y))/n.GameData.Character.Speed);
        	   int toSleep = (int)(t);
        	   if(toSleep==0){return;}
        	   n.send("walk "+x+" "+y+" 0 "+n.GameData.Character.Speed);
        	   try {
				Thread.sleep(toSleep);
				n.GameData.Character.Pos = new Pos(x,y);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
           }
        }
        //player.IsMoving = false;
         */
    }
 
}
