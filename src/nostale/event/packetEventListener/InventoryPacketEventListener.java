package nostale.event.packetEventListener;

import java.util.HashMap;

import nostale.data.InventoryItemInstance;
import nostale.event.PacketEventListener;
import nostale.gameobject.Player;
import nostale.handler.InventoryHandler;
import nostale.packet.Packet;
import nostale.packet.receive.InvPacket;
import nostale.packet.receive.IvnPacket;

public class InventoryPacketEventListener extends PacketEventListener{
	public InventoryHandler inventoryHandler;
	public InventoryPacketEventListener(Player p,InventoryHandler inventoryHandler) {
		super(p);
		this.inventoryHandler = inventoryHandler;
	}
	
	public void packetCall(Packet p)
	{
		switch(p.name)
		{
		case "inv":
			//TODO
			InvPacket invPacket = new InvPacket(p.packetString);
			for(InventoryItemInstance tI:invPacket.items)
			{
				if(inventoryHandler.inventory.get(tI.InventoryType)==null) inventoryHandler.inventory.put(tI.InventoryType, new HashMap<Short,InventoryItemInstance>());
				inventoryHandler.inventory.get(tI.InventoryType).put(tI.Slot, tI);
			}
			if(inventoryHandler.sortopenSent==false)
			{
				inventoryHandler.sortopenSent=true;
				player.sendPacket(new Packet("sortopen"));
			}
			break;
		case "ivn":
			IvnPacket ivnPacket = new IvnPacket(p.packetString);
			//if(inventory.get(tI.InventoryType)==null) inventory.put(tI.InventoryType, new HashMap<Short,InventoryItemInstance>());
			if(ivnPacket.isDelete)
			{
				try
				{
					inventoryHandler.inventory.get(ivnPacket.inventoryType).remove(ivnPacket.slot);
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
					inventoryHandler.inventory.get(ivnPacket.inventoryType).put(ivnPacket.slot,tii);
				}
				catch(Exception e)
				{
					
				}
			}
			inventoryHandler.printInventory();
			break;
		}
	}

}
