package nostale.data;

public enum MonsterType
{
    Unknown(0),
    Partner(1),
    NPC(2),
    Well(3),
    Portal(4),
    Boss(5),
    Elite(6),
    Peapod(7),
    Special(8),
    GemSpaceTime(9);
	public final int id;
	MonsterType(int id)
	{
		this.id = id;
	}
    public static MonsterType get(int id)
    {
    	for(MonsterType c:MonsterType.values())
    	{
    		if(c.id==id) return c;
    	}
    	return null;
    }
}