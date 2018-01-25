import nostale.data.AccountData;
import nostale.data.InventoryItemInstance;
import nostale.domain.ItemType;
import nostale.gameobject.Player;
import nostale.handler.InventoryHandler;
import nostale.handler.MapHandler;
import nostale.handler.SpecialistHandler;
import nostale.resources.Resources;


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
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	inventoryHandler.printInventory();
		                InventoryItemInstance[] food = inventoryHandler.getItemsByType(ItemType.Food);
		                inventoryHandler.useItem(food[0]);
		                System.out.println("Ate "+Resources.getItem((int)food[0].ItemVNum).Name);
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
