package nostale.domain;

public enum BasarType {
	OnSale(1), Solded(2), DelayExpired(3);

	private byte value;

	private BasarType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}
