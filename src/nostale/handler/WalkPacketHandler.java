package nostale.handler;
import nostale.util.Pos;
public class WalkPacketHandler extends Handler{
    public Nostale n;
    
    public WalkPacketHandler(Nostale n)
    {
        this.n = n;
    }
    
    public void Walk(Pos p)
    {
        Walk(p.x,p.y)
    }

    public void Walk(int x,int y)
    {
        if(n.GameData.map.canWalkHere(x,y))
        {
           if(Pos.getRange(new Pos(x,y),n.GameData.Character.Pos)>10)
           {
               Pos[] walkTo = Pos.getPath(n.GameData.Character.Pos,new Pos(x,y));
               //TODO walk to.. best with waiting (not sleep) -> Character.IsMoving = true;
           }
        }
    }
 
}
