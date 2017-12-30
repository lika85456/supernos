package nostale.packet.receive;

import nostale.packet.Packet;
import nostale.util.Pos;

public class DropPacket extends Packet{
	//$"drop {droppedItem.ItemVNum} {droppedItem.TransportId} {droppedItem.PositionX} {droppedItem.PositionY} {droppedItem.Amount} 0 -1
	public int itemVNum;
	public int transportId;
	public Pos pos;
	public int amount;
	public DropPacket(String str) {
		super(str);
		itemVNum = gip(1);
		transportId = gip(2);
		pos = new Pos(gip(3),gip(4));
		amount = gip(5);
	}

}
