import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import nostale.CServer;
import nostale.Nostale;
import nostale.Packet;
import nostale.data.LoginData;
import nostale.data.MapCharacterInstance;
import nostale.data.Player;
import nostale.handler.Trade;
import nostale.util.Log;
import nostale.util.Pos;

public class Main {
	
	public static Thread consoleReader;
	public static Boolean consoleReaderRead = true;
	public static Nostale nostale;
	public static LoginData brgeoghad;
	public static LoginData NostaleJackpotData;
	public static Player jackpotBot;
	public static Player banka;
	
	public static Boolean stateStart=false;
	public static Boolean succesfullyEnd = false;
	
	public static void consoleReaderMethodToRun()
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String read="";
		while(consoleReaderRead)
		{
			try {
				read = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(read.contains("start"))
			{
				stateStart=true;
				System.out.println("Starting");
			}
			else if(read.contains("restart"))
			{
				if(stateStart==true)
				{
					stateStart = false;
					while(succesfullyEnd==false)
					{
						try {
							consoleReader.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					main(null);
					succesfullyEnd = false;
					stateStart = true;
					System.out.println("Restarted");
				}
				else
				{
					System.out.println("Havent started yet. So cannot restart");
				}
			}
			else if(read.contains("stop"))
			{
				stateStart = false;
				System.out.println("Stopping");
			}
			else if(read.contains("info"))
			{
				System.out.println("*********INFO*********");
				System.out.println("***Money: "+banka.Gold);
			}	
			
		}
	}
	
	public static void main(String[] args) {
		consoleReader = new Thread(new Runnable() {
		public void run()
		{
			consoleReaderMethodToRun();
		}});  
		consoleReader.start();
		// 80,119

		// Table accounts = new Table(new ArrayList<String[]>());

		nostale = new Nostale();
		brgeoghad = new LoginData();
		brgeoghad.nickname = "Zadek512";
		brgeoghad.password = "Computer1";
		brgeoghad.channel = 5;
		brgeoghad.server = 1;
		brgeoghad.country = CServer.CZ;

		NostaleJackpotData = new LoginData();
		// nostaleJackpot@post.cz Computer1
		NostaleJackpotData.nickname = "NostaleJackpot58";
		NostaleJackpotData.password = "951852QwErTy";
		NostaleJackpotData.channel = 5;
		NostaleJackpotData.server = 1;
		NostaleJackpotData.country = CServer.CZ;

		while(stateStart==false)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		jackpotBot = nostale.players.get(nostale.addPlayer(NostaleJackpotData));
		banka = nostale.players.get(nostale.addPlayer(brgeoghad));
		nostale.parse();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		nostale.parse();
		//jackpotBot.walkHandler.Walk(new Pos(80, 119));
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
		Boolean toSay = false;
		Trade bankTrade = null;

		while (stateStart) {
			
			jackpotBotTrade = jackpotBot.tradeHandler.trade;
			if (toSay != true && timeNow - lastTimeNewRound > 5000) {
				jackpotBot.send("say Pro v�ce info o jackpotu mi za�eptej '/help'");
				toSay = true;
			}
			timeNow = System.currentTimeMillis();
			if (timeNow - lastTimeNewRound > jackpot.roundLength) // NEW ROUND
			{
				toSay = false;
				lastTimeNewRound = timeNow;
				int total = jackpot.total;
				int winnerId = jackpot.getWinner();
				if (winnerId == -1) {
					jackpotBot.send("say Pr�zdn� kolo. Nikdo nevyhr�l :(.");
					continue;
				}
				float m = (total / 100f * 92.5f);
				jackpot.setPlayersMoney((int)banka.id, total-((int)m));
				MapCharacterInstance winnerInstance;
				try
				{
					winnerInstance = jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players.get(winnerId);
				}
				catch(Exception e)
				{
					winnerInstance = new MapCharacterInstance();
					winnerInstance.Name = "nezname_jmeno";
				}
				
				jackpotBot.send("say V�t�zem " + (int) m + " gold� je: "
						+ winnerInstance.Name);

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				jackpotBot.send("say Za��n� kolo #" + jackpot.rounds.data.size());

			}

			// Check if somebody spk to me
			if (jackpotBot.spk.size() > 0) {
				for (int i = 0; i < jackpotBot.spk.size(); i++) {
					// spk 1 351883 5 Hel�a258 ���
					String packet = jackpotBot.spk.get(i);
					Packet p = new Packet(packet);
					if (p.getInt(2) == jackpotBot.id) {
						jackpotBot.spk.remove(i);
						continue;
					}

					jackpotBot.spk.remove(i);
					if (packet.contains("/balance")) {
						try
						{		
						jackpotBot.send("/" + p.get(4) + " M� na sv�m kont�: " + jackpot.getPlayersMoney(p.getInt(2))
								+ " zlata");
						}
						catch(Exception e)
						{
							e.printStackTrace();
							jackpotBot.send("/" + p.get(4)+" Bohu�el se mi nepoda�ilo zjistit kolik m� na sv�m ��t� pen�z.");
						}
					} else if (packet.contains("/info")) {

							jackpotBot.send("/" + p.get(4) + " " + "INFO #" + jackpot.players.data.size());
							nostale.parse();
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							jackpotBot.send("/" + p.get(4) + " " + "Celkem: " + jackpot.total);
							nostale.parse();
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} else if (packet.contains("/help")) {
						jackpotBot.send("/" + p.get(4) + " " + "*** Nostale Jackpot ***");
						jackpotBot.send("/" + p.get(4) + " "
								+ "Za ztracen� pen�ze neberu odpov�dnost, pokud mi d� admin ban st�ujte si jemu.");
						jackpotBot.send("/" + p.get(4) + " " + "Maxim�ln� v��e s�zky je 50kk.");
						jackpotBot.send("/" + p.get(4) + " " + "V ka�d�m kole si beru 7.5% z celkov� vsazen� ��stky.");
						jackpotBot.send("/" + p.get(4) + " " + "Vyhran� pen�ze dostane� p�i dal�� v�m�n�.");
						jackpotBot.send("/" + p.get(4) + " " + "Dostupn� p��kazy:");
						jackpotBot.send("/" + p.get(4) + " " + "/help");
						jackpotBot.send("/" + p.get(4) + " " + "/balance - zobraz� tvoje konto");
						jackpotBot.send("/" + p.get(4) + " " + "/info - zobraz� informace o pr�v� prob�haj�c�m kole");
						// jackpotBot.send("/"+p.get(4)+" "+"/info kolo -
						// zobraz� informace o kole (p�. /info 1255)");
					}
				}
			}

			if(jackpotBotTrade!=null && jackpotBotTrade.MyStatus==0 && jackpotBotTrade.OponentStatus==0)
			{
				//TODO check if this guy isnt in blacklist
				jackpotBotTrade.acceptRequest();
			}
			if(jackpotBotTrade!=null)
			{
				if(jackpotBotTrade.MyStatus==10 || jackpotBotTrade.MyStatus>3 && jackpotBotTrade.OponentStatus>3)
				{
					jackpotBot.tradeHandler.trade = null;
				}
				else
				{
					
					//New trade even accepted MyStatus = 1; OponentStatus = 1;
					if(jackpotBotTrade.OponentStatus==2 && jackpotBotTrade.MyStatus == 1) // Oponent set their list
					{
						int moneyHeSet = jackpotBotTrade.goldFromPlayer;
						int playersMoneyInDB = jackpot.getPlayersMoney(jackpotBotTrade.playerID);
						
						if(moneyHeSet<1 && playersMoneyInDB<1) // why is he dafuq trading me?
						{
							jackpotBotTrade.declineTrade();
							jackpotBotTrade.MyStatus = 10;
						}
						
						if(playersMoneyInDB>0)
							jackpotBotTrade.give(playersMoneyInDB);
						else
							jackpotBotTrade.give(0);
						jackpotBotTrade.acceptTrade();
						if(moneyHeSet>0)
						{
							jackpot.bet(jackpotBotTrade.playerID, moneyHeSet);
						}
					}
					
					if(jackpotBotTrade.OponentStatus==3)
					{
						if(jackpotBotTrade.MyStatus!=3)
						jackpotBotTrade.acceptTrade();
						jackpotBotTrade.MyStatus = 10;
					}
				}

			}


					

			
			int moneyInBank = jackpot.getPlayersMoney((int)banka.id);
			if(moneyInBank>0)
			{
				bankTrade = banka.tradeHandler.trade;
				if(bankTrade==null)
				{
						banka.tradeHandler.newRequest((int)jackpotBot.id);
						bankTrade = banka.tradeHandler.trade;
						bankTrade.MyStatus = 1;
				}
				else if(bankTrade!=null && bankTrade.MyStatus!=10)
				{
					if(bankTrade.OponentStatus==1 && bankTrade.MyStatus==1)
					{
						bankTrade.give(0);
					}
					else if(bankTrade.OponentStatus==2)
					{
						bankTrade.acceptTrade();
						jackpot.setPlayersMoney((int)banka.id, moneyInBank);
						banka.tradeHandler.trade.MyStatus = 10;
					}
				}


				
				
			}
			


			    for (Map.Entry<Integer, MapCharacterInstance> entry : banka.gameData.map[banka.gameDataMapID].Players.entrySet()) {
			    	//Integer key = entry.getKey();
			    	MapCharacterInstance value = entry.getValue();
			        if(value.Authority!=0)
			        {
			        	//ADMINADMINADMINADMINADMINADMINADIANDIANDIANDIADNAIDN
			        	System.out.println("ADMIN ON THE MAP!!");
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
		
		for(Player p:nostale.players)
		{
			try {
				p.login.c.Close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Admin control. map control, basar selling

	}

}

/*if (jackpotBotTrade != null) {
				if(jackpotBotTrade.MyStatus ==10) jackpotBot.tradeHandler.trade = null;
				if (jackpotBotTrade.MyStatus == 0 && jackpotBotTrade.OponentStatus == 1) // request
																							// came
																							// from
																							// somebody
				{
					// accept?
					jackpotBotTrade.acceptRequest();
				}
				if(jackpotBotTrade.MyStatus==1 && jackpotBotTrade.OponentStatus==1 && jackpotBotTrade.playerID == banka.id)
				{
					jackpotBotTrade.give(totalToPutIntoTheBank);
					totalToPutIntoTheBank = 0;
				}
				if(jackpotBotTrade.OponentStatus == 2 && jackpotBotTrade.playerID==banka.id)
				{
					jackpotBotTrade.acceptTrade();
					bankTrade = null;
					putting = false;
					Log.log("MONEY TRANSFER", "Money sucessfully given");
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
						int money = 0;
						try
						{
							money = jackpot.getPlayersMoney(jackpotBotTrade.playerID);
						}
						catch(Exception e)
						{
							money = 0;
						}
						jackpotBotTrade.give(money);
						jackpotBotTrade.acceptTrade();

					} else {
						int tPLayersMoney = jackpot.getPlayersMoney(jackpotBotTrade.playerID);
						if (tPLayersMoney > 0)
							jackpotBotTrade.give(tPLayersMoney);
						else
							jackpotBotTrade.declineTrade();
						jackpotBotTrade.acceptTrade();
						jackpotBotTrade.MyStatus = 10;
						
					}

				}
				// if both 3 then trade is good if 3 and 4/5 then canceled
				if (jackpotBotTrade.MyStatus == 3 && jackpotBotTrade.OponentStatus == 3) {
					// Good, trade was done
					// System.out.println("Good");
					jackpotBotTrade.MyStatus = 10;
					// brgeoad.tradeHandler.trade = null;
					jackpot.bet(jackpotBotTrade.playerID, jackpotBotTrade.goldFromPlayer);

						jackpotBot.send("say "
								+ jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players
										.get(jackpotBotTrade.playerID).Name
								+ " pr�v� vsadil " + jackpotBotTrade.goldFromPlayer);

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
			else//Put my money into the "bank"
			{
				if(totalToPutIntoTheBank>0 && putting == false)
				{
					//totalToPutIntoTheBank = 0;
					putting = true;
					bankTrade = new Trade(banka);
					banka.send("req_exc 1 "+jackpotBot.id);
					
				}
				
			}

			if(bankTrade!=null)
			{
				if(bankTrade.OponentStatus==1)
				bankTrade.give(0);
				if(bankTrade.OponentStatus==2)
				jackpotBotTrade.acceptTrade();
			}
			*/
