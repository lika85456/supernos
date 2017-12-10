package nostale.packet;

public class Packet {
	public PacketType packetType; // If its received or send
	public String packetString; // Whole packet in string
	public String name; // Name of packet, example - walk x x -> name = walk
	private String[] parameters;

	// Packet restrictors for time
	public int timeToWait = 0; // in miliseconds time between send delay

	/**
	 * New packet from string
	 * 
	 * @param str
	 */
	public Packet(String str) {
		this.packetString = str;
		this.parameters = str.split(" ");
		this.name = parameters[0];
	}

	public String getParameter(int index) {
		return parameters[index + 1];
	}

	public Integer getIntParameter(int index) {
		try {
			return Integer.parseInt(parameters[index + 1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String toString() {
		return this.packetString;
	}

}