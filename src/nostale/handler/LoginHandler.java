package nostale.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import nostale.data.AccountData;
import nostale.data.GameServer;
import nostale.data.Server;
import nostale.domain.ClassType;
import nostale.gameobject.Player;
import nostale.net.Connection;
import nostale.net.Crypto;
import nostale.packet.Packet;
import nostale.data.Character;

public class LoginHandler extends Handler {
	public AccountData accData;
	private String HASH = "034D977451B01B474FF737EAA89E84E80A85BC5DBE6D49576D327A30E71A1460";
	private String version = "0.9.3.3080";
	private Connection c;
	private Server s;
	private GameServer[] channels;
	public Timer t;
	public TimerTask tt;
	private int time = 0;
	public int session;

	public Boolean succefullyLoggedIn = false;

	public LoginHandler(Player p) {
		super(p);
		accData = p.accData;

		s = new Server(accData.Nation);
		c = new Connection();
		try {
			c.Connect(s.ip, s.port);

			// Make login packet
			String login_packet = "";
			login_packet = "NoS0575 3001742 " + accData.nickname + " " + Crypto.encrypt(accData.password).toUpperCase()
					+ " 0080B9C9";
			login_packet += (char) (11);
			login_packet += version + " 0 " + (Crypto.md5(HASH + accData.nickname).toUpperCase());

			c.send(Crypto.EncryptLoginPacket(login_packet));
			ArrayList<Integer> received = new ArrayList<Integer>();
			while (received.isEmpty()) {
				received = c.getReceivedBytes();
			}
			String decryptedRec = Crypto.DecryptLoginPacket(received);

			parseLoginPacket(decryptedRec);
			for(GameServer gs:channels)
			{
				if(gs.channel.equals(String.valueOf(accData.Channel)) && gs.server.equals(String.valueOf(accData.Server)) )
				{
					SelectServer(gs);
				}
			}

		} catch (Exception e) {
			System.out.println("No internet connection!");
			e.printStackTrace();
			return;
		}

	}

	private void parseServer(String[] p) {
		ArrayList<GameServer> rray = new ArrayList<GameServer>();
		for (String item : p) {
			String[] ite = item.split(":");
			rray.add(new GameServer(ite[0], ite[1], ite[3].split("\\.")[0], ite[3].split("\\.")[1], ite[2]));
		}
		rray.remove(rray.size() - 1);
		this.channels = rray.toArray(new GameServer[0]);
	}

	private void parseLoginPacket(String packet) {
		try {

			if (!packet.contains("TeST")) {
				System.out.println(packet);
			}
			String[] p = packet.split(" ");
			this.session = Integer.parseInt(p[2]);
			parseServer(Arrays.copyOfRange(p, 3, p.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private void SelectServer(GameServer s)
    {
        c = new Connection();
        try
        {

        c.Connect(s.ip, s.port);
        Packet.id++;
        c.send(Crypto.EncryptGamePacket(Packet.id + Integer.toString(this.session),this.session,true));
        Thread.sleep(200);
        Packet.id++;
        c.send(Crypto.EncryptGamePacket(Packet.id + accData.nickname,this.session,false) + Crypto.EncryptGamePacket(Packet.id+1 + accData.password,this.session,false));
        Packet.id++;
        parseChars();

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
	
    private void parseChars() throws Exception
    {
        String lastPacket = "";
        ArrayList<Character> tempChars = new ArrayList<Character>();
        while(!lastPacket.contains("clist_end"))
        {
          String[] received = c.getReceived();
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
    
    public void send(String s) throws Exception
    {
    	c.Send(Crypto.EncryptGamePacket(packetId()+s,this.session,false));
}
	
	   private String[] parsePacket(String packet)
	    {
	       return packet.split(" ");
	}
	    private Character parseChar(String pa)
	    {
	       Character chara = new Character();
	       String[] p = parsePacket(pa);
	       chara.Slot = (byte)Integer.parseInt(p[1]);
	       chara.Name = p[2];
	       chara.Class = ClassType.values()[(Integer.parseInt(p[8]))];
	       chara.Level = (byte)Integer.parseInt(p[9]);
	       chara.JobLevel = (byte)Integer.parseInt(p[12]);

	       return chara;
	}
}
