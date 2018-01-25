package nostale.packet.receive;

import java.util.ArrayList;

import nostale.data.InventoryItemInstance;
import nostale.domain.InventoryType;
import nostale.packet.Packet;

//Inventory packet
public class InvPacket extends Packet{
	public ArrayList<InventoryItemInstance> items;
	public InventoryType inventoryType;
	public InvPacket(String str) {
		super(str);
		items = new ArrayList<InventoryItemInstance>();
		
		int type = Integer.parseInt(parameters[1]);
		this.inventoryType = InventoryType.values()[type];
		for(int i = 2;i < parameters.length;i++)
		{
			String[] pp = parameters[i].split("\\.");

			InventoryItemInstance ti = new InventoryItemInstance();
			ti.ItemVNum = (short) Integer.parseInt(pp[1]);
			ti.Slot = (short) Integer.parseInt(pp[0]);
			ti.Amount = (byte) Integer.parseInt(pp[2]);
			ti.InventoryType = (byte) type;
			items.add(ti);
			}
	}

}
