package nostale.struct;

public enum ClassType
{
    Adventurer(0),
    Swordman(1),
    Archer(2),
    Magician(3);
    
    public final int id;
    ClassType(int id)
	{
		this.id = id;
	}
    public static ClassType get(int id)
    {
    	for(ClassType c:ClassType.values())
    	{
    		if(c.id==id) return c;
    	}
    	return null;
    }
}
