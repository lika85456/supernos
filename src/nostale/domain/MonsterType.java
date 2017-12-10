package nostale.domain;

public enum MonsterType {
	Unknown(0), Partner(1), NPC(2), Well(3), Portal(4), Boss(5), Elite(6), Peapod(7), Special(8), GemSpaceTime(9);

	private byte value;

	private MonsterType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}
