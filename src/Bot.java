import nostale.data.AccountData;
import nostale.data.InventoryItemInstance;
import nostale.domain.ItemType;
import nostale.gameobject.Player;
import nostale.handler.BattleHandler;
import nostale.handler.InventoryHandler;
import nostale.handler.MapHandler;
import nostale.handler.SpecialistHandler;
import nostale.handler.WalkHandler;
import nostale.resources.Resources;
import nostale.util.Pos;


public class Bot {
	public Player bot;
	public Thread thread;
	private boolean run = false;
	private boolean threadRun = false;
	public Bot(AccountData botData) {
		bot = new Player(botData);
		MapHandler mapHandler = new MapHandler(bot);
		InventoryHandler inventoryHandler = new InventoryHandler(bot);
		SpecialistHandler specialistHandler = new SpecialistHandler(bot);
		BattleHandler battleHandler = new BattleHandler(bot);
		WalkHandler walkHandler = new WalkHandler(bot);
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	inventoryHandler.printInventory();
		            	int hp = bot.HP;
		            	while(bot.HP==hp)
		            	{
		            		battleHandler.target = Pos.GetNearestMob(bot);
		            		System.out.println("Target fount - "+Resources.getMob(battleHandler.target.VNum).Name+" range: "+Pos.getRange(bot.pos, battleHandler.target.Pos));
		            		if(Pos.getRange(bot.pos, battleHandler.target.Pos)>battleHandler.skills.get(battleHandler.skills.values().toArray()[0]).Range)
							{
		            			walkHandler.Walk(Pos.getShortestPosInRange(1, battleHandler.target.Pos, bot.pos));
		            			System.out.println("Walking...");
		            			while(bot.IsMoving==true)
		            			{
		            				try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		            			}
							}
		            		battleHandler.useSkill(battleHandler.skills.get(battleHandler.skills.values().toArray()[0]));
		            	}
		                InventoryItemInstance[] food = inventoryHandler.getItemsByType(ItemType.Snack);
		                inventoryHandler.useItem(food[1]);
		                System.out.println("Ate "+Resources.getItem((int)food[1].ItemVNum).Name);
		            }
		        }, 
		        5000 
		);
		//specialistHandler.putOn();
		thread = new Thread(){
			@Override
			public void run()
			{
				while(threadRun==true)
				{
					while(run==true)
					{
						bot.loop();
					}
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				
			}
		};
	}
	
	public void run()
	{
		this.run = true;
		this.threadRun = true;
		if(!thread.isAlive())
			thread.start();
	}
	public void stop()
	{
		this.run=false;
		this.threadRun=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void restart(){
		stop();
		run();
	}

}
