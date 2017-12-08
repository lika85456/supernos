package nostale.handler;

import nostale.data.Player;
import nostale.util.Log;

public class Trade {

	/*
	 *  Requested = 1,
        List = 2,
        Confirmed = 3,
        Cancelled = 4,
		Declined = 5
	 */

	public int goldFromPlayer;
	public int playerID;
	public Player player;
	
	public byte MyStatus = 0;
	public byte OponentStatus = 0;
	public Trade(Player player)
	{
		this.player = player;
		log("New trade created");
	}
	
	public void acceptRequest()
	{
		player.send("#req_exc^2^"+playerID);
		MyStatus = 1;
		OponentStatus = 1;
		log("Request accepted with player:"+playerID);
	}
	
	public void playerAcceptedTrade()
	{
		OponentStatus = 3;
		log("Player accepted trade");
	}
	
	public void playerClosedTrade()
	{
		OponentStatus = 4;
		log("Player closed trade");
	}
	
	public void playerExecutedList(int gold)
	{
		goldFromPlayer = gold;
		OponentStatus = 2;
		log("Player executed list with:"+gold+" gold");
	}
	
	public void declineRequest()
	{
		player.send("#req_exc^5^"+playerID);
		MyStatus = 5;
		log("Request declined for playerID:"+playerID);
	}
	public void acceptTrade()
	{
		if(MyStatus==2 && (OponentStatus==2 || OponentStatus==3))
		{
			player.send("req_exc 3");
			MyStatus=3;
			log("Trade accepted for playerID:"+playerID);
		}
	}
	
	public void closeRequest()
	{
		player.send("exc_close 0");
		MyStatus = 4;
		log("Trade closed");
	}
	
	public void declineTrade()
	{
		MyStatus = 4;
		player.send("req_exc 4");
		log("Trade declined");
	}
	
	public void give(int gold)
	{
		executeList(gold);
		log("Giving to player: "+gold+" gold");
	}
	
	private void executeList(int gold)
	{
		//exc_list 1257
		if(MyStatus==1)
		{
			player.send("exc_list "+gold);
			MyStatus = 2;
			
		}
		
	}
	
	private void log(String str)
	{
		Log.log("trade", str);
	}
	
}
