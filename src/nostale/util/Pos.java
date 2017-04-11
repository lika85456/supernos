package nostale.util;

import nostale.resources.Map;

public class Pos
{
    public short x;
    public short y;
    
    public Pos(Pos x)
    {
    	this.x = x.x;
    	this.y = x.y;
    }
    
    public Pos(int x, int y)
    {
        this.x = (short)x;
        this.y = (short)y;
    }

    public static int getRange(Pos x,Pos y)
    {
    	int difX = x.x - y.x;
        int difY = x.y - y.y;
        if(difX==0) difX = 1;
        if(difY==0) difY = 1;
        difX -= difX/Math.abs(difX);
        difY -= difY/Math.abs(difY);
    	return (int)Math.ceil(Math.sqrt((double)Math.pow(x.x - y.x, 2) + (double)Math.pow(x.y - y.y, 2)));
    }
    
    public void add(Pos x)
    {
    	this.x +=x.x;
    	this.y +=x.y;
    }
       
    public static Pos getShortestMovablePos(Map m,Pos p)
    {
    	for(int i = 1;i<2000;i++)
    	{
    		for(int x1=p.x-i;x1<x1+i;x1++)
    		{
    			if(m.canWalkHere(x1, p.y+i))
    			{
    				return new Pos(x1,p.y+1);
    			}
    			if(m.canWalkHere(x1, p.y-i))
    			{
    				return new Pos(x1,p.y-1);
    			}
    		}
    		for(int y1=p.y-i;y1<y1+i;y1++)
    		{
    			if(m.canWalkHere(p.x+i, y1))
    			{
    				return new Pos(p.x+i,y1);
    			}
    			if(m.canWalkHere(p.x-i,y1))
    			{
    				return new Pos(p.x-i,y1);
    			}
    		}
    	}
    	return null;
    }
    
    public static Pos getShortestPosInRange(int range,Pos p1, Pos p2)
    {
    	int r = getRange(p1,p2);
    	if(r<range) {return p2;}
    	return new Pos(p2.x+((int)((p1.x-p2.x)*(r-range)/r)),p2.y+((int)((p1.y-p2.y)*(r-range)/r)));
    }

    public static Pos[] getPath(Pos p1, Pos p2)
    {
        ArrayList<Pos> p = new ArrayList<Pos>();
        int range = Pos.getRange(p1,p2);
        while(true)
        {
           p.add(Pos.getShortestPosInRange(range-8,p2,p1));
           range -= 8;
           if(range<0) return p.toArray(new Pos[0]);
        }
    }
    
    
    @Override
    public String toString()
    {
    	return this.x+"|"+this.y;
    }
}
