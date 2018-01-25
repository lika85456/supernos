package nostale.handler;


import nostale.event.gameEventListener.PacketReceiveListener;
import nostale.event.gameEventListener.PacketSendListener;
import nostale.gameobject.Player;

import nostale.net.Connection;

public class ConnectionHandler extends Handler{
	public Connection connection;
	public int session;
	public int packetId = 0;
	
	public PacketSendListener packetSendListener;
	public PacketReceiveListener packetReceiveListener;
	
	public ConnectionHandler(Player p,LoginHandler lHandler) {
		super(p);	
		this.packetId = lHandler.packetId;
		this.connection = lHandler.c;
		packetSendListener = new PacketSendListener(p,this);
		packetReceiveListener = new PacketReceiveListener(p,this);
	}
	
	
	/*public void onLoginError(int loginError)
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
	*/
}
