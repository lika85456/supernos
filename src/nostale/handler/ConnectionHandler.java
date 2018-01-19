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
	public int session;
	public ConnectionHandler(Player p) {
		super(p);	
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
