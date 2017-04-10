package nostale.data;

public enum ItemType
{
    Weapon(0),
    Armor(1),
    Fashion(2),
    Jewelery(3),
    Specialist(4),
    Box(5),
    Shell(6),
    Main(10),
    Upgrade(11),
    Production(12),
    Map(13),
    Special(14),
    Potion(15),
    Event(16),
    Quest1(18),
    Sell(20),
    Food(21),
    Snack(22),
    Magical(24),
    Part(25),
    Teacher(26),
    Ammo(27),
    Quest2(28);
	public final int id;
	ItemType(int id)
	{
		this.id = id;
	}
	
    public static ItemType get(int id)
    {
    	for(ItemType c:ItemType.values())
    	{
    		if(c.id==id) return c;
    	}
    	return null;
    }
}
