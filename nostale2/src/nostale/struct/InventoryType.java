package nostale.struct;

public enum InventoryType
{
    Equipment(0),
    Main(1),
    Etc(2),
    Miniland(3),
    Specialist(6),
    Costume(7),
    Wear(8),
    Bazaar(9);
	public final int id;
	InventoryType(int id)
	{
		this.id = id;
	}
    public static InventoryType get(int id)
    {
    	for(InventoryType c:InventoryType.values())
    	{
    		if(c.id==id) return c;
    	}
    	return null;
    }
}