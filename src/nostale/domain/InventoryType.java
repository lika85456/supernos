package nostale.domain;

public enum InventoryType {

	Equipment(0), Main(1), Etc(2), Miniland(3), Specialist(6), Costume(7), Wear(8), Bazaar(9), Warehouse(
			10), FamilyWareHouse(11), PetWarehouse(
					12), FirstPartnerInventory(13), SecondPartnerInventory(14), ThirdPartnerInventory(15);
	private byte value;

	private InventoryType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}
