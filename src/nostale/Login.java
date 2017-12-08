package nostale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import nostale.data.Character;
import nostale.data.ClassType;
import nostale.net.Connection;
import nostale.net.Crypto;
/**
 * Write a description of class Login here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Login
{
    //md5(nostalex.dat)+md5(nostale.dat)
    public String HASH = "034D977451B01B474FF737EAA89E84E80A85BC5DBE6D49576D327A30E71A1460";
    //Version
    public String version = "0.9.3.3080";
    public SessionEnum sessionState = SessionEnum.NOTHING;
    //NOTHING
    //NO_INTERNET_CONNECTION
    //FAIL
    public int session = 0;
    public GameServer[] channels; //channels
    public String id = "";
    public String pw = "";
    public Connection c;
    public int packetId = 240;
    public Boolean parse = false;
    public int time = 0;
    public Character[] characters;
    public Timer t;
    public TimerTask tt;
    /***
     * First of all we need to connect to server with some data
     * @param nickname
     * @param password
     * @param se
     */
    public Login(String nickname, String password, CServer se)
    {
        this.id = nickname;
        this.pw = password;
        try
        {
            Server s = new Server(se);
            Connection c = new Connection();
            c.Connect(s.ip, s.port);
            c.Send(Crypto.EncryptLoginPacket(makeLoginPacket(id,pw)));
            ArrayList<Integer> received = new ArrayList<Integer>();
            while(received.isEmpty())
            {
               received = c.getReceived();
            }
            parseLoginPacket(Crypto.DecryptLoginPacket(received));
            c.Close();
            

            
        }
        catch(Exception e)
        {
            sessionState = SessionEnum.NO_INTERNET_CONNECTION;
            e.printStackTrace();
        }
        
       
    }
    
    public void SelectServer(GameServer s)
    {
        c = new Connection();
        try
        {

        c.Connect(s.ip, s.port);
        c.Send(Crypto.EncryptGamePacket(packetId() + Integer.toString(this.session),this.session,true));
        Thread.sleep(200);
        c.Send(Crypto.EncryptGamePacket(packetId() + this.id,this.session,false) + Crypto.EncryptGamePacket(packetId() + this.pw,this.session,false));
        parseChars();
        Timer timer = new Timer();

        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
            	time+=60;
                try {
					send("pulse "+time+" 0");
				} catch (Exception e) {
					e.printStackTrace();
				}

            };
        };
        
        t.schedule(tt,60000,60000);
    	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
    
    private String packetId()
    {
      this.packetId++;
      return this.packetId+" ";
    }
    
    private String makeLoginPacket(String id,String pw)throws Exception
    {
       String login_packet = "";
       login_packet = "NoS0575 3001742 "+id+" "+Crypto.encrypt(pw).toUpperCase()+" 0080B9C9";   
       login_packet += (char)(11);
       login_packet += version+" 0 "+(Crypto.md5(HASH+id).toUpperCase());
       return login_packet;
    }
    private void parseLoginPacket(String packet)
    {
       try{
           
       //N￮TeST 10543 79.110.84.41:4014:0:1.5.Ae￮o￮ 79.110.84.41:4013:0:1.4.Ae￮o￮ 79.110.84.41:4012:0:1.3.Ae￮o￮ 79.110.84.41:4011:0:1.2.Ae￮o￮ 79.110.84.41:4010:5:1.1.Ae￮o￮ -1:-1:-1:10000.10000.1
       if(!packet.contains("TeST")){this.sessionState = SessionEnum.FAIL;System.out.println(packet);}
       String[] p = packet.split(" ");
       this.session = Integer.parseInt(p[2]);
       parseServer(Arrays.copyOfRange(p, 3, p.length));
      }
      catch(Exception e){
    	  e.printStackTrace();
      }
    }

    private void parseServer(String[] p)
    {
       ArrayList<GameServer> rray = new ArrayList<GameServer>();
       for (String item : p) {
          String[] ite = item.split(":");
          rray.add(new GameServer(ite[0],ite[1],ite[3].split("\\.")[0],ite[3].split("\\.")[1],ite[2]));
       }
       rray.remove(rray.size()-1);
       this.channels = rray.toArray(new GameServer[0]);
    }
    
    
    public void selectChar(Character ch)
    {

    	try{
    		

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
    	catch(Exception e){
    		
    	}
    }
    
    private void parseChars() throws Exception
    {
        String lastPacket = "";
        ArrayList<Character> tempChars = new ArrayList<Character>();
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
        this.characters = tempChars.toArray(new Character[0]);
    
    }
        
    private Character parseChar(String pa)
    {
       Character chara = new Character();
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
    	c.Send(Crypto.EncryptGamePacket(packetId()+s,this.session,false));
    }
    public void sendAfterWait(String s,int time) throws Exception
    {

    	new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                	try {
						c.Send(Crypto.EncryptGamePacket(packetId()+s,session,false));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }, 
            time 
    );
    
    }
    
    

}
/*

    private void parseLoginPacket(String packet)
    {
       //N￮TeST 10543 79.110.84.41:4014:0:1.5.Ae￮o￮ 79.110.84.41:4013:0:1.4.Ae￮o￮ 79.110.84.41:4012:0:1.3.Ae￮o￮ 79.110.84.41:4011:0:1.2.Ae￮o￮ 79.110.84.41:4010:5:1.1.Ae￮o￮ -1:-1:-1:10000.10000.1
       if(!packet.contains("TeST")){return;}
       String[] p = packet.split(" ");
       this.session = Integer.parseInt(p[1]);
       parseServer(Arrays.copyOfRange(p, 2, p.length));
       
    }


    
    private String makeLoginPacket(String id,String pw)throws Exception
    {
       String login_packet = "";
       login_packet = "NoS0575 3001742 "+id+" "+Crypto.encrypt(pw).toUpperCase()+" 0080B9C9";   
       login_packet += (char)(11);
       login_packet += version+" 0 "+(Crypto.md5(HASH+id).toUpperCase());
       return login_packet;
    }
*/