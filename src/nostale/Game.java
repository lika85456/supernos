package nostale;
import java.util.*;


import nostale.net.*;
import nostale.data.MapCharacterInstance;
import nostale.data.Character;
import nostale.data.ClassType;
import nostale.data.GameData;
public class Game
{
    public Connection c;
    public int packetId = 240;
    public Boolean parse = false;
    public int time = 0;
    public GameData GameData;
    public Game() throws Exception
    {
    	this.GameData = new GameData();
    }
    public void Connect(GameServer s) throws Exception
    {
        c = new Connection();
        c.Connect(s.ip, s.port);
        c.Send(Crypto.EncryptGamePacket(packetId() + Integer.toString(this.GameData.session),this.GameData.session,true));
        Thread.sleep(200);
        c.Send(Crypto.EncryptGamePacket(packetId() + this.GameData.id,this.GameData.session,false) + Crypto.EncryptGamePacket(packetId() + this.GameData.pw,this.GameData.session,false));
        parseChars();
        Timer timer = new Timer();

        timer.schedule( new TimerTask() {
            public void run() {
                time+=60;
                try {
					send("pulse "+time+" 0");
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
         }, 60*1000, 60*1000);
    }
    
    public void selectChar(MapCharacterInstance ch) throws Exception
    {
        this.GameData.Character = ch;
        send("select "+ch.Slot);
        parse = true;
        Boolean s = true;
        while(s==true)
        {
             String[] received = c.GetReceived();
             for(String rec:received)
             {

                if(rec.contains("OK"))
                {
                  send("game_start");
                  send("lbs 0");
                  s=false;
                }
              }
             Thread.sleep(5);      
        }

    
    }
    
    private void parseChars() throws Exception
    {
        String lastPacket = "";
        ArrayList<MapCharacterInstance> tempChars = new ArrayList<MapCharacterInstance>();
        while(!lastPacket.contains("clist_end"))
        {
          String[] received = c.GetReceived();
          for(String rec:received)
          {
             lastPacket = rec;

             //System.out.println(":"+parsePacket(rec)[0]+":"+(parsePacket(rec)[0]=="clist"));
             //System.out.println(":"+parsePacket(rec)[0]+":");
             if(parsePacket(rec)[0].equals("clist"))
             {
                tempChars.add(parseChar(rec));
             }
          }
        } 
        send("c_close");
        send("f_stash_end");
        this.GameData.characters = tempChars.toArray(new MapCharacterInstance[0]);
    
    }
        
    private MapCharacterInstance parseChar(String pa)
    {
       MapCharacterInstance chara = new MapCharacterInstance();
       String[] p = parsePacket(pa);
       chara.Slot = (byte)Integer.parseInt(p[1]);
       chara.Name = p[2];
       chara.Class = ClassType.get(Integer.parseInt(p[8]));
       chara.Level = (byte)Integer.parseInt(p[9]);
       chara.JobLevel = (byte)Integer.parseInt(p[12]);

       return chara;
    }
    
    private String[] parsePacket(String packet)
    {
       return packet.split(" ");
    }
    
    public void send(String s) throws Exception
    {
    	c.Send(Crypto.EncryptGamePacket(packetId()+s,this.GameData.session,false));
    }
    public void sendAfterWait(String s,int time) throws Exception
    {
    	GameData gd = this.GameData;
    	new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                	try {
						c.Send(Crypto.EncryptGamePacket(packetId()+s,gd.session,false));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }, 
            time 
    );
    
    }
    
    
    
    
    private String packetId()
    {
      this.packetId++;
      return this.packetId+" ";
    }
}

