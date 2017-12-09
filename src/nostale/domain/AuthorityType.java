package nostale.domain;

public enum AuthorityType {
	Closed(-3), Banned(-2), Unconfirmed(-1), User(0), Moderator(1), GameMaster(2);

	private byte value;

	private AuthorityType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}