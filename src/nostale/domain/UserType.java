package nostale.domain;

public enum UserType {
	Player(1), Npc(2), Monster(3), Object(9);

	private byte value;

	private UserType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}
