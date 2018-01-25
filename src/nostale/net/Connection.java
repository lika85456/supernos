package nostale.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/*
 * @author lika85456
 * @description
 * Class for communicating with nostale server
 * 
 */
public class Connection {

	public Socket clientSocket;
	public DataOutputStream outToServer;
	public BufferedReader inFromServer;
	public DataInputStream in;

	// ("79.110.84.75", 4006)
	/*
	 * Connect to nostale server
	 * 
	 * @param String ip - ip of the server
	 * 
	 * @param int port - port of the server
	 * 
	 */
	public void Connect(String ip, int port) throws Exception {

		clientSocket = new Socket(ip, port);
		// if(!clientSocket.getInetAddress().isReachable(500))
		// {
		// throw new java.net.ConnectException();
		// }
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		this.in = new DataInputStream(clientSocket.getInputStream());

	}


	public ArrayList<Integer> getReceived() {
		ArrayList<Integer> received = new ArrayList<Integer>();
		try {
			while (in.available() > 0) {
				received.add(in.readUnsignedByte());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return received;
	}

	/*
	 * Sends RAW packet to server
	 * 
	 * @param String packet - RAW packet to send
	 * 
	 */
	public void send(String packet){

		// outToServer.writeBytes(packet);
		for (int i = 0; i < packet.length(); i++) {
			char c = packet.charAt(i);
			//byte first = (byte) (c >> 8 << 8);
			byte second = (byte) (c << 8 >> 8);
			try {
				outToServer.writeByte(second);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println("First: "+first+" Second: "+second);

		}

	}

	/*
	 * Close the connection, optional but recommended
	 * 
	 */
	public void Close() throws Exception {
		clientSocket.close();
	}

}