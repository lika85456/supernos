import nostale.data.AccountData;
import nostale.data.CServer;
import nostale.data.MapCharacterInstance;
import nostale.domain.AuthorityType;
import nostale.gameobject.Player;
import nostale.handler.LoginHandler;
import nostale.handler.MapDataHandler;

public class Bot {
	public Boolean run = true;
	public Player bot;
	public AccountData botData;
	public Thread thread;

	private void bot() {
		bot = new Player();
		bot.accData = botData;
		LoginHandler botLoginHandler = new LoginHandler(bot);
		MapDataHandler mapDataHandler = new MapDataHandler(bot) {
			public void onPlayerIn(MapCharacterInstance player) {
				if (player.Authority != AuthorityType.User.getValue()) { //GM on map
					stop();
					try {
						Thread.sleep(120000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					run();
				}
			}
			
			public void mapout()
			{
				stop();
			}
			public void dead()
			{
				stop();
			}
		};
		while (run) {
			bot.receiveAndParse();
		}
		try {
			bot.c.Close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Bot(AccountData botData) {
		this.botData = botData;
	}

	public void stop() {
		try {
			run=false;
			bot.c.Close();
			thread.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				bot();
			}
		});
		thread.start();
	}

	public void restart() {
		stop();
		run();
	}
}
