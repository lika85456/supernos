package nostale.handler;

import java.util.ArrayList;
import java.util.HashMap;

import nostale.data.InventoryItemInstance;
import nostale.domain.InventoryType;
import nostale.domain.ItemType;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IInventoryHandler;
import nostale.packet.Packet;
import nostale.packet.receive.InvPacket;
import nostale.packet.receive.IvnPacket;
import nostale.packet.send.UseItemPacket;
import nostale.resources.Resources;

public class InventoryHandler extends Handler implements IInventoryHandler{
	//byte = inventoryType short = slot
	public HashMap<Byte,HashMap<Short,InventoryItemInstance>> inventory;
	//TODO add wearable instance of weapon, armor etc.
	private boolean sortopenSent = false;
	public InventoryHandler(Player p)
	{
		super(p);
		inventory = new HashMap<Byte,HashMap<Short,InventoryItemInstance>>();
	}
	
	@Override
	public void parsePacket(Packet p)
	{
		switch(p.name)
		{
		case "inv":
			//TODO
			InvPacket invPacket = new InvPacket(p.packetString);
			for(InventoryItemInstance tI:invPacket.items)
			{
				if(inventory.get(tI.InventoryType)==null) inventory.put(tI.InventoryType, new HashMap<Short,InventoryItemInstance>());
				inventory.get(tI.InventoryType).put(tI.Slot, tI);
			}
			if(sortopenSent==false)
			{
				sortopenSent=true;
				player.send(new Packet("sortopen"));
			}
			break;
		case "ivn":
			IvnPacket ivnPacket = new IvnPacket(p.packetString);
			//if(inventory.get(tI.InventoryType)==null) inventory.put(tI.InventoryType, new HashMap<Short,InventoryItemInstance>());
			if(ivnPacket.isDelete)
			{
				try
				{
					inventory.get(ivnPacket.inventoryType).remove(ivnPacket.slot);
				}
				catch(Exception e)
				{
					
				}
			}
			else
			{
				try
				{
					InventoryItemInstance tii = new InventoryItemInstance();
					tii.Amount = ivnPacket.amount;
					tii.InventoryType = (byte)ivnPacket.inventoryType.getValue();
					tii.ItemVNum = ivnPacket.itemVNum;
					tii.Rare = ivnPacket.rare;
					tii.Upgrade = ivnPacket.upgrade;
					inventory.get(ivnPacket.inventoryType).put(ivnPacket.slot,tii);
				}
				catch(Exception e)
				{
					
				}
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
		inventory.forEach((k,v) -> {
			v.forEach((kk,vv)->{
				if(Resources.getItem((int)vv.ItemVNum).ItemType==it)
					tList.add(vv);
			});
		});
		return tList.toArray(new InventoryItemInstance[0]);
	}
	public void useItem(InventoryItemInstance item)
	{
		player.send(new UseItemPacket(item,player));
	}
}
