package nostale.gameobject;

import java.util.Timer;
import java.util.TimerTask;

import nostale.data.AccountData;
import nostale.handler.LoginHandler;
public class Player {
	public AccountData accData;
	private Timer t;
	private TimerTask tt;
	
	
	public void Login()
	{
		LoginHandler loginHandler = new LoginHandler(this);
	}
}
