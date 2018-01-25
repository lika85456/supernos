package nostale.handler;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import nostale.Config;
import nostale.data.AccountData;
import nostale.data.GameServer;
import nostale.data.Server;
import nostale.domain.ClassType;
import nostale.domain.LoginFailType;
import nostale.gameobject.Player;

import nostale.net.Connection;
import nostale.net.Crypto;
import nostale.packet.Packet;
import nostale.data.Character;

public class LoginHandler extends Handler{
	//TODO CLEANUP
	public AccountData accData;
	//md5(nostalex.dat)+md5(nostale.dat)
	private String HASH = Config.HASH;
	private String version = Config.version;
	public Connection c;
	private Server s;
	private GameServer[] channels;
	private HashMap<Integer,Character> characters;
	public Timer t;
	public TimerTask tt;
	private int time = 0;
	public int session;
	public int packetId=254;
	public Boolean succefullyLoggedIn = false;

	public int getPacketId()
	{
		return packetId;
	}
	
	public LoginHandler(Player p) {
		super(p);
		this.characters = new HashMap<Integer,Character>();
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
				received = c.getReceived();
			}
			String decryptedRec = Crypto.DecryptLoginPacket(received);

			parseLoginPacket(decryptedRec);
			for (GameServer gs : channels) {
				if (gs.channel.equals(String.valueOf(accData.Channel))
						&& gs.server.equals(String.valueOf(accData.Server))) {
					selectServer(gs);
					selectChar(characters.get(accData.Character));
					succefullyLoggedIn = true;
					player.log("login", "Login status: CONNECTED");
	     			p.session = session;

				}
			}

		} catch (Exception e) {
			System.out.println("No internet connection!");
			e.printStackTrace();
			return;
		}

	}

	private void selectChar(Character ch) {

		try {

			send("select " + ch.Slot);
			Boolean s = true;
			while (s == true) {
				String[] received = Crypto.DecryptGamePacket(c.getReceived()).toArray(new String[0]);
				for (String rec : received) {

					if (rec.contains("OK")) {
						send("game_start");
						send("lbs 0");
						s = false;
					}
				}
				Thread.sleep(5);
			}

		} catch (Exception e) {

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
				Packet p  =new Packet(packet);
				onError(p.getIntParameter(1));
				return;
			}
			String[] p = packet.split(" ");
			this.session = Integer.parseInt(p[2]);
			parseServer(Arrays.copyOfRange(p, 3, p.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void selectServer(GameServer s) {
		try {
			c.Close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		c = new Connection();
		try {

			c.Connect(s.ip, s.port);
			packetId++;
			c.send(Crypto.EncryptGamePacket(packetId +" "+Integer.toString(this.session), this.session, true));
			Thread.sleep(200);
			packetId++;
			c.send(Crypto.EncryptGamePacket(packetId +" "+ accData.nickname, this.session, false)
					+ Crypto.EncryptGamePacket((packetId + 1 )+" "+ accData.password, this.session, false));
			packetId+=2;
			parseChars();

			t = new Timer();
			tt = new TimerTask() {
				@Override
				public void run() {
					time += 60;
					try {
						send("pulse " + time + " 0");
					} catch (Exception e) {
						e.printStackTrace();
					}

				};
			};

			t.schedule(tt, 60000, 60000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseChars() throws Exception {
		String lastPacket = "";
		while (!lastPacket.contains("clist_end")) {
			String[] received = Crypto.DecryptGamePacket(c.getReceived()).toArray(new String[0]);
			for (String rec : received) {
				lastPacket = rec;

				// System.out.println(":"+parsePacket(rec)[0]+":"+(parsePacket(rec)[0]=="clist"));
				// System.out.println(":"+parsePacket(rec)[0]+":");
				if (parsePacket(rec)[0].equals("clist")) {
					Character tmp = parseChar(rec);
					characters.put((int)tmp.Slot,parseChar(rec));
				}
			}
		}
		send("c_close");
		send("f_stash_end");

	}

	public void send(String s) throws Exception {
		c.send(Crypto.EncryptGamePacket(packetId +" "+ s, this.session, false));
		packetId++;
	}

	private String[] parsePacket(String packet) {
		return packet.split(" ");
	}

	private Character parseChar(String pa) {
		Character chara = new Character();
		String[] p = parsePacket(pa);
		chara.Slot = (byte) Integer.parseInt(p[1]);
		chara.Name = p[2];
		chara.Class = ClassType.values()[(Integer.parseInt(p[8]))];
		chara.Level = (byte) Integer.parseInt(p[9]);
		chara.JobLevel = (byte) Integer.parseInt(p[12]);

		return chara;
	}

	@Override
	public void onError(int error) {
		/*
		 * 	public static final byte OldClient = 1;
	public static final byte UnhandledError = 2;
	public static final byte Maintenance = 3;
	public static final byte AlreadyConnected = 4;
	public static final byte AccountOrPasswordWrong = 5;
	public static final byte CantConnect = 6;
	public static final byte Banned = 7;
	public static final byte WrongCountry = 8;
	public static final byte WrongCaps = 9;
		 */
			String errorS = ("Cannot login because ");
			switch(error)
			{
			case LoginFailType.OldClient:
				errorS+=("old client!");
				break;
			case LoginFailType.UnhandledError:
				errorS+=("unhandled error!");
				break;
			case LoginFailType.Maintenance:
				errorS+=("maintenance!");
				break;
			case LoginFailType.AlreadyConnected:
				errorS+=("already connected!");
				break;
			case LoginFailType.AccountOrPasswordWrong:
				errorS+=("account or password wrong!");
				break;
			case LoginFailType.CantConnect:
				errorS+=("cannot connect!");
				break;
			case LoginFailType.Banned:
				errorS+=("banned!");
				break;
			case LoginFailType.WrongCountry:
				errorS+=("wrong country!");
				break;
			case LoginFailType.WrongCaps:
				errorS+=("wrong caps!");
				break;
			}
			player.log("LoginERROR", errorS);
	}
}
