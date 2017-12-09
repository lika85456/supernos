package nostale.domain;

public enum RequestExchangeType {
	Unknown(0), Requested(1), List(2), Confirmed(3), Cancelled(4), Declined(5);

	private byte value;

	private RequestExchangeType(int v) {
		this.value = (byte) v;
	}

	public int getValue() {
		return value;
	}
}
