package nostale.domain;

public enum ClassType {

	Adventurer(0), Swordman(1), Archer(2), Magician(3);

	private byte value;

	private ClassType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}
