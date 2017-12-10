package nostale.domain;

public enum BasarListType {
	Default(0), Weapon(1), Armor(2), Equipment(3), Jewelery(4), Specialist(5), Pet(6), Npc(7), Shell(8), Main(
			9), Usable(10), Other(11), Vehicle(12);

	private byte value;

	private BasarListType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}