package nostale.connection;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import nostale.LogType;
import nostale.Logger;
/*
 * @author lika85456
 * @description
 * Class for communicating with nostale server
 * 
 */
public class Connection{ 
    
 
   public Socket clientSocket; 
   public DataOutputStream outToServer; 
   public BufferedReader inFromServer;
   public DataInputStream in;

   //("79.110.84.75", 4006)
   /*
    * Connect to nostale server
    * @param String ip - ip of the server
    * @param int port - port of the server
    * 
    */
   public void Connect(String ip,int port) throws Exception{

      clientSocket = new Socket(ip,port);
     // if(!clientSocket.getInetAddress().isReachable(500))
      //{
      //throw new java.net.ConnectException();
      //}
      outToServer = new DataOutputStream(clientSocket.getOutputStream());
      inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      this.in = new DataInputStream(clientSocket.getInputStream());

    }
    
    public String[] GetReceived()
    {
    	String[] received = Crypto.DecryptGamePacket(getReceived()).toArray(new String[0]);
    	for(String p: received)
    	Logger.log(LogType.Receive, p);
        return  received;
    }
    public ArrayList<Integer> getReceived()
    {
       ArrayList<Integer> received = new ArrayList<Integer>();
        try 
        {
            while (in.available() > 0) {
					received.add(in.readUnsignedByte());
				}

	    }
        catch (Exception e) {
			e.printStackTrace();
		}
        return received;
    }
    
    /*
     * Sends RAW packet to server
     * @param String packet - RAW packet to send
     * 
     */
    public void Send(String packet) throws Exception
    {     
    	
          outToServer.writeBytes(packet);
    }

    /*
     * Close the connection, optional but recommended
     * 
     */
    public void Close() throws Exception{
      clientSocket.close();    
    }
    
    
}