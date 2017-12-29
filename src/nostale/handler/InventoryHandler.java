package nostale.handler;

import java.util.ArrayList;

import nostale.data.InventoryItemInstance;
import nostale.domain.ItemType;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IInventoryHandler;
import nostale.packet.Packet;
import nostale.resources.Resources;

public class InventoryHandler extends Handler implements IInventoryHandler{
	public ArrayList<InventoryItemInstance> inventory;
	//TODO add wearable instance of weapon, armor etc.
	private boolean sortopenSent = false;
	public InventoryHandler(Player p)
	{
		super(p);
		inventory = new ArrayList<InventoryItemInstance>();
	}
	
	@Override
	public void parsePacket(Packet p)
	{
		switch(p.name)
		{
		case "inv":
			//TODO
			if(sortopenSent==false)
			{
				sortopenSent=true;
				player.send(new Packet("sortopen"));
			}
			break;
		}
	}


	@Override
	public void dropItem() {
		// TODO Auto-generated method stub
	}

	@Override
	public InventoryItemInstance[] getItemsByType(ItemType it) {
		ArrayList<InventoryItemInstance> tList = new ArrayList<InventoryItemInstance>();
		for(InventoryItemInstance temp:inventory)
		{
			if(Resources.getItem((int)temp.ItemVNum).ItemType==it)
			{
				tList.add(temp);
			}
		}
		return tList.toArray(new InventoryItemInstance[0]);
	}
	public void useItem(InventoryItemInstance item)
	{
		//u_i 1 351883 2 1 0 0 
		//player.send(new Packet("u_i"));
	}
}
