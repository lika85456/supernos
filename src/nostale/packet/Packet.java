package nostale.packet;


public class Packet {
	public PacketType packetType; // If its received or send
	public String packetString; // Whole packet in string
	public String name; // Name of packet, example - walk x x -> name = walk
	protected String[] parameters;


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
		return parameters[index];
	}

	public int getIntParameter(int index) {
		try {
			return Integer.parseInt(parameters[index]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public int gip(int index)
	{
		return getIntParameter(index);
	}

	@Override
	public String toString() {
		return this.packetString;
	}

}