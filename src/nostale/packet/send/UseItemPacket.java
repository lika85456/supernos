package nostale.packet.send;

import nostale.data.InventoryItemInstance;
import nostale.gameobject.Player;
import nostale.packet.Packet;

public class UseItemPacket extends Packet{

	public UseItemPacket(InventoryItemInstance item,Player player) {
		super("u_i 1 "+player.id+" "+item.InventoryType+" "+item.Slot+" 0 0");
		//u_i 1 351883 2 4 0 0 
		//u_i {UserType} {UserId} {Item.InventoryType} {Item.Slot} {Unknwon} {Unknown}
	}

}
