package nostale.packet.receive;

import nostale.domain.InventoryType;
import nostale.packet.Packet;

//if something changes in inventory
public class IvnPacket extends Packet{
	public InventoryType inventoryType;
	public int slot;
	public int itemVNum;
	public short amount;
	public byte rare;
	public byte upgrade;
	public byte spUpgrade;
	public IvnPacket(String str) {
		super(str);
		//ivn 1 1.1012.17.0
		inventoryType = InventoryType.values()[Integer.parseInt(parameters[1])];
		String[] itemSplited = parameters[2].split(".");
		slot = Integer.parseInt(itemSplited[0]);
		itemVNum = Integer.parseInt(itemSplited[1]);
		
        switch (inventoryType)
        {
            case Equipment:
                //return $"ivn 0 {Slot}.{ItemVNum}.{Rare}.{(Item.IsColored ? Design : Upgrade)}.0";
            	rare = Byte.parseByte(itemSplited[2]);
            	upgrade = Byte.parseByte(itemSplited[3]);
                break;
            case Main:
                //return $"ivn 1 {Slot}.{ItemVNum}.{Amount}.0";
            	amount = Short.parseShort(itemSplited[2]);
                break;
            case Etc:
                //return $"ivn 2 {Slot}.{ItemVNum}.{Amount}.0";
            	amount = Short.parseShort(itemSplited[2]);
                break;
            case Miniland:
                //return $"ivn 3 {Slot}.{ItemVNum}.{Amount}";
            	amount = Short.parseShort(itemSplited[2]);
                break;
            case Specialist:
                //return $"ivn 6 {Slot}.{ItemVNum}.{Rare}.{Upgrade}.{(this as SpecialistInstance)?.SpStoneUpgrade}";
            	rare = Byte.parseByte(itemSplited[2]);
            	upgrade = Byte.parseByte(itemSplited[3]);
            	spUpgrade = Byte.parseByte(itemSplited[4]);
            	break;
            case Costume:
                //return $"ivn 7 {Slot}.{ItemVNum}.{Rare}.{Upgrade}.0";
            	rare = Byte.parseByte(itemSplited[2]);
            	upgrade = Byte.parseByte(itemSplited[3]);
                break;
}
	}

}
