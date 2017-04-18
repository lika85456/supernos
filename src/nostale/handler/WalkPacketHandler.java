package nostale.handler;
import nostale.Nostale;
import nostale.util.Pos;
public class WalkPacketHandler extends Handler{
    public Nostale n;
    
    public WalkPacketHandler(Nostale n)
    {
        this.n = n;
    }
    
    public void Walk(Pos p)
    {
    	n.GameData.Character.IsMoving = true;
        this.Walk(p.x,p.y);
        n.GameData.Character.IsMoving = false;
    }

    private void Walk(int x,int y)
    {
    	
        if(n.GameData.map.canWalkHere(x,y))
        {
           if(Pos.getRange(new Pos(x,y),n.GameData.Character.Pos)>10)
           {
               Pos[] walkTo = Pos.getPath(n.GameData.Character.Pos,new Pos(x,y));
               for(Pos toWalk: walkTo)
               {
            	   double t = (1000*Pos.getRange(n.GameData.Character.Pos,new Pos(x,y))/n.GameData.Character.Speed);
            	   int toSleep = (int)(t);
            	   if(toSleep==0){return;}
            	   n.send("walk "+toWalk.x+" "+toWalk.y+" 0 "+n.GameData.Character.Speed);
            	   try {
					Thread.sleep(toSleep);
					
					n.GameData.Character.Pos = toWalk;
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
    }
 
}
