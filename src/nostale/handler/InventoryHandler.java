package nostale.handler;

import java.util.ArrayList;
import java.util.HashMap;

import nostale.data.InventoryItemInstance;
import nostale.domain.ItemType;
import nostale.event.packetEventListener.InventoryPacketEventListener;
import nostale.gameobject.Player;
import nostale.packet.send.UseItemPacket;
import nostale.resources.Resources;

public class InventoryHandler extends Handler{
	//byte = inventoryType short = slot
	public HashMap<Byte,HashMap<Short,InventoryItemInstance>> inventory;
	//TODO add wearable instance of weapon, armor etc.
	public boolean sortopenSent = false;
	public InventoryHandler(Player p)
	{
		super(p);
		inventory = new HashMap<Byte,HashMap<Short,InventoryItemInstance>>();
		p.addPacketEventListener(new InventoryPacketEventListener(p,this));
	}

	public void printInventory()
	{
		inventory.forEach((k,v) -> {
			v.forEach((kk,vv)->{
				System.out.println(Resources.getItem((int)vv.ItemVNum).Name+" "+vv.Amount+" "+Resources.getItem((int)vv.ItemVNum).ItemType+" VNUM:"+vv.ItemVNum);
			});
		});
	}

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
		player.sendPacket(new UseItemPacket(item,player));
	}
}
