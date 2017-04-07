package nostale.struct;
/*
 * Inventory stores only id's of items, cuz its better, deal with it!
 */
public class Inventory {
    public Item[] equipment = new Item[60];
    public Item[] main = new Item[60];
    public Item[] etc = new Item[60];
    public Item[] miniland = new Item[60];
    public Item[] specialist = new Item[60];
    public Item[] costume = new Item[60];
    public Item[] wear = new Item[60];
    public Item[] basar = new Item[60];
	public Inventory()
    {
    	
    }
	public void addItem(Item i)
	{
		switch(i.Type)
		{
	    case Equipment:
	    	equipment[i.InventoryPos] = i;
	        break;
		case Bazaar:
			basar[i.InventoryPos] = i;
			break;
		case Costume:
			costume[i.InventoryPos] = i;
			break;
		case Etc:
			etc[i.InventoryPos] = i;
			break;
		case Main:
			main[i.InventoryPos] = i;
			break;
		case Miniland:
			miniland[i.InventoryPos] = i;
			break;
		case Specialist:
			specialist[i.InventoryPos] = i;
			break;
		case Wear:
			wear[i.InventoryPos] = i;
			break;
		default:
			break;
		
		
		}
	}
	public void parsePacketINV(String p)
	{
		String[] pac = p.split(" ");
		InventoryType type = GetInventoryType(Integer.parseInt(pac[1]));
		for(int i = 2;i < pac.length;i++)
		{
			String[] pp = pac[i].split("\\.");

			Item ti = new Item();
			ti.NameID = Integer.parseInt(pp[1]);
			ti.InventoryPos = Integer.parseInt(pp[0]);
			ti.Amount = Integer.parseInt(pp[2]);
			ti.Type = type;
			addItem(ti);
		}
	}
    
	public InventoryType GetInventoryType(int i)
	{
		switch(i)
		{
		case 0:
		    return InventoryType.Equipment;
		case 1:
	        return InventoryType.Main;
		case 2:
			return InventoryType.Etc;
		case 3:
			return InventoryType.Miniland;
		case 6:
			return InventoryType.Specialist;
		case 7:
			return InventoryType.Costume;
		case 8:
			return InventoryType.Wear;
		case 9:
			return InventoryType.Bazaar;
	    default:
	    	return InventoryType.Equipment;
		}
	
	}
    
}
