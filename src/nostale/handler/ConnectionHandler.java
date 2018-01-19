package nostale.handler;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import nostale.Config;
import nostale.data.Character;
import nostale.data.GameServer;
import nostale.data.Server;
import nostale.domain.LoginFailType;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IConnectionHandler;
import nostale.net.Connection;
import nostale.net.Crypto;
import nostale.util.Log;

public class ConnectionHandler extends Handler implements IConnectionHandler{
	public Connection c;
	private String HASH = Config.HASH;
	private String version = Config.version;
	private Server s;
	private GameServer[] channels;
	private HashMap<Integer,Character> characters;
	public Timer t;
	public TimerTask tt;
	private int time = 0;
	public int session;
	public ConnectionHandler(Player p) {
		super(p);
		
	}
	
	
	public void login()
	{
		c = new Connection();
		Server server = new Server(player.accData.Nation);
		try {
			c.Connect(server.ip, server.port);
			c.send(Crypto.EncryptLoginPacket(generateLoginPacket()));
		} catch (Exception e) {
			Log.log("LoginError", "Cannot connect. No internet connection probably.");
			e.printStackTrace();
		}
	}
	
	private String generateLoginPacket()
	{
		String login_packet = "";
		login_packet = "NoS0575 3001742 " + player.accData.nickname + " " + Crypto.encrypt(player.accData.password).toUpperCase()
				+ " 0080B9C9";
		login_packet += (char) (11);
		try {
			login_packet += version + " 0 " + (Crypto.md5(HASH + player.accData.nickname).toUpperCase());
		} catch (Exception e) {
			Log.log("LoginError", "Something in making login packet screwed up.");
			e.printStackTrace();
		}
		return login_packet;
	}

	public void onLoginError(int loginError)
	{
		String errorMessage = "";
		if(loginError==LoginFailType.OldClient)
			errorMessage = "old client";
		if(loginError==LoginFailType.UnhandledError)
			errorMessage = "unhandled error";
		if(loginError==LoginFailType.Maintenance)
			errorMessage = "maintenance";
		if(loginError==LoginFailType.AlreadyConnected)
			errorMessage = "already connected";
		if(loginError==LoginFailType.AccountOrPasswordWrong)
			errorMessage = "login data wrong";
		if(loginError==LoginFailType.CantConnect)
			errorMessage = "cannot connect";
		if(loginError==LoginFailType.Banned)
			errorMessage = "banned";
		if(loginError==LoginFailType.WrongCountry)
			errorMessage = "wrong country";
		if(loginError==LoginFailType.WrongCaps)
			errorMessage = "wrong caps";
		Log.log("LoginError", "Error "+errorMessage+" when logging in on account:"+this.player.accData.nickname);
	}
}
