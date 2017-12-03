import nostale.CServer;
import nostale.Nostale;
import nostale.Packet;
import nostale.data.LoginData;
import nostale.data.Player;
import nostale.handler.Trade;
import nostale.util.Pos;

public class Main {

	public static void main(String[] args) {
		// 80,119

		// Table accounts = new Table(new ArrayList<String[]>());

		Nostale nostale = new Nostale();
		LoginData brgeoghad = new LoginData();
		brgeoghad.nickname = "Zadek512";
		brgeoghad.password = "Computer1";
		brgeoghad.channel = 3;
		brgeoghad.server = 1;
		brgeoghad.country = CServer.CZ;

		LoginData NostaleJackpotData = new LoginData();
		// nostaleJackpot@post.cz Computer1
		NostaleJackpotData.nickname = "NostaleJackpot58";
		NostaleJackpotData.password = "951852QwErTy";
		NostaleJackpotData.channel = 1;
		NostaleJackpotData.server = 1;
		NostaleJackpotData.country = CServer.CZ;

		Player jackpotBot = nostale.players.get(nostale.addPlayer(brgeoghad));
		nostale.parse();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		nostale.parse();
		jackpotBot.walkHandler.Walk(new Pos(80, 119));
		while (jackpotBot.IsMoving == true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		jackpotBot.rest();

		jackpotBot.tradeHandler.acceptRequests = true;
		Trade jackpotBotTrade = jackpotBot.tradeHandler.trade;

		Jackpot jackpot = new Jackpot();
		jackpot.newRound();

		long lastTimeNewRound = 0;
		long timeNow = System.currentTimeMillis();
		while (true) {
			
			timeNow = System.currentTimeMillis();
			if(timeNow-lastTimeNewRound>10000) //NEW ROUND
			{
				lastTimeNewRound = timeNow;
				int total = jackpot.total;
				int winnerId = jackpot.getWinner();
				if(winnerId==-1)
				{
					jackpotBot.send("say Prázdné kolo. Nikdo nevyhrál :(.");
					continue;
				}
				float m = ((float)total/100f*92.5f);
				jackpotBot.send("say Vítìzem "+(int)m+" goldù je: "+jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players.get(winnerId).Name);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jackpotBot.send("say Zaèíná kolo #"+jackpot.rounds.data.size());
			}
			
			//Check if somebody spk to me
			if(jackpotBot.spk.size()>0)
			{
				for(int i = 0;i<jackpotBot.spk.size();i++)
				{
					//spk 1 351883 5 Helèa258 ššš
					String packet = jackpotBot.spk.get(i);
					Packet p = new Packet(packet);
					if(p.getInt(2)==jackpotBot.id)
					{
						jackpotBot.spk.remove(i);
						continue;
					}
						
					jackpotBot.spk.remove(i);
					if(packet.contains("/balance"))
					{
						jackpotBot.send("/"+p.get(4)+" Máš na svém kontì: "+jackpot.getPlayersMoney(p.getInt(2))+" zlata");
					}
					else if(packet.contains("/info"))
					{
						try
						{
							//int roundNumber = p.getInt(6);
							//jackpotBot.send("/"+p.get(4)+" "+"INFO #"+roundNumber);
							//jackpotBot.send("/"+p.get(4)+" "+"Total: "+);
						}
						catch(Exception e)
						{
							jackpotBot.send("/"+p.get(4)+" "+"Pøi zobrazování informací o kole se nìco nevovedlo :/");
						}
					}
					else if(packet.contains("/help"))
					{
						jackpotBot.send("/"+p.get(4)+" "+"Dostupné pøíkazy:");
						jackpotBot.send("/"+p.get(4)+" "+"/balance - zobrazí tvoje konto");
						jackpotBot.send("/"+p.get(4)+" "+"/help");
						//jackpotBot.send("/"+p.get(4)+" "+"/info kolo - zobrazí informace o kole (pø. /info 1255)");
					}
				}
			}
			
			jackpotBotTrade = jackpotBot.tradeHandler.trade;
			if (jackpotBotTrade != null) {
				if (jackpotBotTrade.MyStatus == 0 && jackpotBotTrade.OponentStatus == 1) // request
																							// came
																							// from
																							// somebody
				{
					// accept?
					jackpotBotTrade.acceptRequest();
				}

				if (jackpotBotTrade.MyStatus == 1 && jackpotBotTrade.OponentStatus == 2) // request
																							// is
																							// accepted
																							// and
																							// somebody
																							// executed
																							// their
																							// list
				{
					if (jackpotBotTrade.goldFromPlayer >= jackpot.minimumToBet
							&& jackpotBotTrade.goldFromPlayer <= jackpot.maximumToBet) {
						jackpotBotTrade.give(0);
						jackpotBotTrade.acceptTrade();

					} else {
						int tPLayersMoney = jackpot.getPlayersMoney(jackpotBotTrade.playerID);
						if (tPLayersMoney > 0)
							jackpotBotTrade.give(tPLayersMoney);
						else
							jackpotBotTrade.declineTrade();
						jackpotBotTrade.MyStatus = 10;
						jackpotBotTrade.acceptTrade();
					}

				}
				// if both 3 then trade is good if 3 and 4/5 then canceled
				if (jackpotBotTrade.MyStatus == 3 && jackpotBotTrade.OponentStatus == 3) {
					// Good, trade was done
					// System.out.println("Good");
					jackpotBotTrade.MyStatus = 10;
					// brgeoad.tradeHandler.trade = null;
					jackpot.bet(jackpotBotTrade.playerID, jackpotBotTrade.goldFromPlayer);
					try
					{
						
					
					jackpotBot.send("say "
							+ jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players.get(jackpotBotTrade.playerID).Name
							+ " právì vsadil " + jackpotBotTrade.goldFromPlayer);
					}
					catch(Exception e)
					{
						
					}
				}
				if ((jackpotBotTrade.MyStatus == 4 || jackpotBotTrade.MyStatus == 5
						|| jackpotBotTrade.OponentStatus == 4 || jackpotBotTrade.OponentStatus == 5)
						&& jackpotBotTrade.MyStatus != 10) {
					// Bad, trade was declined
					// System.out.println("Bad");
					jackpotBotTrade.MyStatus = 10;
					// brgeoad.tradeHandler.trade = null;
				}
			}

			nostale.parse();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Admin control. map control, basar selling

	}

}
