package nostale.struct;

public class Packet {
     public String[] splited;
     public String packet;
     public String type;
     public Packet(String packet)
     {
    	 this.packet = packet;
    	 this.splited = packet.split(" ");
    	 this.type = splited[0];
     }
     public String get(int id)
     {
    	 return splited[id];
     }
     public int getInt(int id)
     {
    	 try
    	 {
    	 return Integer.parseInt(splited[id]);
    	 }
    	 catch(Exception e){e.printStackTrace();}
    	 return 0;
     }
}
