package nostale.data;

import nostale.resources.Resources;

/*
 * Inventory stores only id's of items, cuz its better, deal with it!
 */
public class Inventory {
    public InventoryItemInstance[] equipment = new InventoryItemInstance[60];
    public InventoryItemInstance[] main = new InventoryItemInstance[60];
    public InventoryItemInstance[] etc = new InventoryItemInstance[60];
    public InventoryItemInstance[] miniland = new InventoryItemInstance[60];
    public InventoryItemInstance[] specialist = new InventoryItemInstance[60];
    public InventoryItemInstance[] costume = new InventoryItemInstance[60];
    public InventoryItemInstance[] wear = new InventoryItemInstance[60];
    public InventoryItemInstance[] basar = new InventoryItemInstance[60];
	public Inventory()
    {
    	
    }
	public void addItem(InventoryItemInstance i)
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

			InventoryItemInstance ti = new InventoryItemInstance(Resources.getItemByVNUM( Integer.parseInt(pp[1])));
			if(ti==null) {System.out.println("Error when loading some of inventory items"); continue;}
			ti.Type = type;
			ti.VNUM = (short)Integer.parseInt(pp[1]);
			ti.InventoryPos = (byte)Integer.parseInt(pp[0]);
			ti.Amount = (byte)Integer.parseInt(pp[2]);
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
	public String toString()
	{
		String toReturn = "";
	    toReturn+="Equipment\n";
	    for(InventoryItemInstance i:this.equipment)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    toReturn+="Main\n";
	    for(InventoryItemInstance i:this.main)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    toReturn+="etc\n";
	    for(InventoryItemInstance i:this.etc)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    toReturn+="miniland\n";
	    for(InventoryItemInstance i:this.miniland)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    toReturn+="specialist\n";
	    for(InventoryItemInstance i:this.specialist)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    toReturn+="costume\n";
	    for(InventoryItemInstance i:this.costume)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    toReturn+="wear\n";
	    for(InventoryItemInstance i:this.wear)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    toReturn+="basar\n";
	    for(InventoryItemInstance i:this.basar)
	    {
	    	if(i==null) continue;
	    	toReturn+="Slot: "+i.InventoryPos+" Name:"+i.Name+" Amount:"+i.Amount+"\n";
	    }
	    
	    return toReturn;
	}
    
}
