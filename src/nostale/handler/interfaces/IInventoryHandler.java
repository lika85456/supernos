package nostale.handler.interfaces;

import nostale.data.InventoryItemInstance;
import nostale.domain.ItemType;

public interface IInventoryHandler {
	public void dropItem();
	public InventoryItemInstance[] getItemsByType(ItemType it);
}
