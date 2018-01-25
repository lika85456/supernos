import nostale.data.AccountData;
import nostale.data.CServer;
import nostale.resources.Resources;

public class Main {
	public static Boolean run = false;
	public static Bot bot;


	public static void main(String args[]) {	
		AccountData botData = new AccountData();
		// nostaleJackpot@post.cz Computer1
		//botData.nickname = "Zadek5212";
		botData.nickname = "Zadek512";
		botData.password = "Computer1";
		botData.Channel = 2;
		botData.Server = 1;
		botData.Character = 1;
		botData.Nation = CServer.CZ;
		Resources.load();
		bot = new Bot(botData);
		bot.run();
		while(true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
