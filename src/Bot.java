import nostale.data.AccountData;
import nostale.data.MapCharacterInstance;
import nostale.data.MapItemInstance;
import nostale.data.NpcMonsterInstance;
import nostale.domain.AuthorityType;
import nostale.gameobject.Player;
import nostale.handler.BattleHandler;
import nostale.handler.LoginHandler;
import nostale.handler.MapDataHandler;
import nostale.handler.WalkHandler;

public class Bot {
	public Boolean run = true;
	public Player bot;
	public AccountData botData;
	public Thread thread;

	private void bot() {
		boolean ShouldSit = false;
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
		WalkHandler walkHandler = new WalkHandler(bot);
		
		BattleHandler battleHandler = new BattleHandler(bot){
			public void onMeGettingHit(NpcMonsterInstance mob,int damage){
				super.onMeGettingHit(mob, damage);
				if(ShouldSit)
				{
					setTarget(mob);
				}
			}
			
		};
		
		while (run) {
			bot.receiveAndParse();
			//HP and sitting
			if(bot.HP<bot.MaxHP*0.8 && bot.HP>bot.MaxHP*0.6)//if less than 80% and higher than 60%
			{
				
			}
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
