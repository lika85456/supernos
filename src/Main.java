import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import nostale.data.AccountData;
import nostale.data.CServer;
import nostale.data.GameData;
import nostale.gameobject.Player;
import nostale.handler.LoginHandler;
import nostale.handler.MapDataHandler;
import nostale.handler.TalkHandler;
import nostale.handler.TradeHandler;
import nostale.packet.Packet;
import nostale.resources.Resources;
import nostale.util.Log;

public class Main {

	public static Boolean consoleReaderRead = true;
	public static Boolean debug = true;
	public static Boolean run = false;

	public static Thread botThread;

	public static Player bankBot;
	public static Player jackpotBot;
	public static TradeHandler bankBotTrade;
	public static TradeHandler jackpotBotTrade = null;
	public static Jackpot jackpot;
	public static HashMap<Long, Long> banList = new HashMap<Long, Long>();

	public static void theBot() {


		bankBot = new Player();
		jackpotBot = new Player();
		jackpot = new Jackpot(jackpotBot);
		jackpot.load();
		AccountData bankAccData = new AccountData();
		bankAccData.nickname = "Zadek512";
		bankAccData.password = "Computer1";
		bankAccData.Channel = 5;
		bankAccData.Server = 1;
		bankAccData.Character = 1;
		bankAccData.Nation = CServer.CZ;

		AccountData jackpotAccData = new AccountData();
		// nostaleJackpot@post.cz Computer1
		jackpotAccData.nickname = "NostaleJackpot58";
		jackpotAccData.password = "951852QwErTy";
		jackpotAccData.Channel = 5;
		jackpotAccData.Server = 1;
		jackpotAccData.Character = 1;
		jackpotAccData.Nation = CServer.CZ;

		bankBot.accData = bankAccData;
		jackpotBot.accData = jackpotAccData;

		LoginHandler tLogin = new LoginHandler(bankBot);
		tLogin = null;
		tLogin = new LoginHandler(jackpotBot);
		tLogin = null;
		MapDataHandler JHandlerMapData = new MapDataHandler(jackpotBot);
		jackpotBot.addHandler(JHandlerMapData);
		TradeHandler JHandlerTrade = new TradeHandler(jackpotBot) {

			@Override
			public void onRequest(Packet p) {
				super.onRequest(p);
				// If he is trading more than 10 secs ban him!
				int tradeID = this.tradeId;
				TradeHandler tt = this;
				this.timer = new Timer();
				this.timerTask = new TimerTask() {
					@Override
					public void run() {
						if (tt.tradeId == tradeID) {
							banList.put(playerID, System.currentTimeMillis() + 10000);
							declineTrade();
							println("Banning "+playerID+" for 10 secs (waiter)");

						}
					};
				};
				timer.schedule(timerTask, 10000);

				if (banList.containsKey(playerID)) {
					Long time = banList.get(playerID);
					if (time < System.currentTimeMillis()) // unban
					{
						banList.remove(playerID);

					} else {
						// TODO send him a message that he is banned for X
						// seconds
						declineRequest();
						return;
					}
				}
				acceptRequest();
				// this.playerID
				println("Banning "+playerID+" for 10 secs");
				banList.put(playerID, System.currentTimeMillis() + 10000);
			}

			@Override
			public void onRequestAccept() {
				super.onRequestAccept(); // i am not sending any requests???
			}

			@Override
			public void onExecuteList(int gold) {
				super.onExecuteList(gold);
				// Do something
				int playersMoney = jackpot.getPlayersMoney(playerID);
				if (playersMoney == 0 && gold == 0) {
					declineTrade();
					return;
				}

				if (gold < jackpot.minimumToBet && playersMoney == 0) {
					player.send(new Packet("/" + playerID + " minim�ln� v��e s�zky je:" + jackpot.minimumToBet));
					declineTrade();
					return;
				}
				if (gold > jackpot.maximumToBet) {
					player.send(new Packet("/" + playerID + " maxim�ln� v��e s�zky je:" + jackpot.maximumToBet));
					declineTrade();
					return;
				}

				executeList(playersMoney);

				acceptTrade();
			}

			@Override
			public void onTradeAccept() {
				super.onTradeAccept();
				this.closeTimer();
				jackpot.bet((int) playerID, OponentGold);
				jackpot.setPlayersMoney(playerID, jackpot.getPlayersMoney(playerID) - MyGold);
				try {
					String name = GameData.maps.get(jackpotBot.mapId).Players.get(playerID).Name;
					jackpotBot.send(new Packet("say " + name + " vsadil " + OponentGold));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// He accepted
				// this.playerID
				// this.OponentGold
			}

			@Override
			public void onTradeDecline() {
				super.onTradeDecline();
				this.closeTimer();
			}

		};
		jackpotBot.addHandler(JHandlerTrade);
		TradeHandler JHandlerBank = new TradeHandler(bankBot) {

			@Override
			public void onRequestAccept() {
				super.onRequestAccept();
				executeList(0);
			}

			@Override
			public void onExecuteList(int gold) {
				super.onExecuteList(gold);
				acceptTrade();
			}

			@Override
			public void onTradeAccept() {
				super.onTradeAccept();
				// He accepted
				// this.playerID
				// this.OponentGold
				println("Bank succesfully got " + this.OponentGold + " gold.");
			}

		};
		bankBot.addHandler(JHandlerBank);

		TalkHandler JHandlerTalk = new TalkHandler(jackpotBot) {
			@Override
			public void onPM(long id,String name, String content) {
				if (content.contains("/balance")) {
					pm(name, "Na sv�m kont� m�: " + jackpot.getPlayersMoney(id) + " zlata");
				}

				if (content.contains("/info")) {

					pm(name, "Nostale Jackpot");
					pm(name, "Ka�d� kolo je jeden v�t�z. Nov� kolo je v�dy ka�dou minutu.");
					pm(name, "V�t�z vyhr�v� v�e a 7.5% je poplatek.");
					pm(name, "Vsadit m��e� tak, kdy� mi d� ve v�m�n� ur�it� po�et pen�z.");
					pm(name, "V�herce dostane pen�ze p�i dal�� v�m�n� (m��e b�t pr�zdn�)");
					pm(name, "Maxim�ln� v��e s�zky je 50kk, minimum je 1 zlato");
					pm(name, "Za ztr�tu pen�z neberu odpov�dnost. (Ale neokradu v�s z�m�rn�)");
					pm(name, "P��kazy: ");
					pm(name, "/balance - zobraz� stav konta");
				}

			}
		};
		jackpotBot.addHandler(JHandlerTalk);
		
		while (run) {
			jackpotBot.receiveAndParse();
			bankBot.receiveAndParse();

			if (jackpot.getPlayersMoney(bankBot.id) > 10000) {
				JHandlerBank.createRequest(jackpotBot.id);
			}
			jackpot.parse(JHandlerTalk);
		}
		try {
			jackpotBot.c.Close();
			bankBot.c.Close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {
		Resources.load();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String read = "";
		String[] splited = new String[1];
		while (consoleReaderRead) {
			try {
				read = reader.readLine();
				splited = null;
				splited = read.split(" ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (splited[0].equals("start")) {
				botThread = new Thread(new Runnable() {
					@Override
					public void run() {
						theBot();
					}
				});
				run = true;
				botThread.start();
				println("Starting");
			} else if (splited[0].equals("restart")) {
				jackpot.save();
				if (run == true) {
					run = false;
					try {
						botThread.join(1000);
						botThread.interrupt();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					run = true;
					botThread = new Thread(new Runnable() {
						@Override
						public void run() {
							theBot();
						}
					});
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					botThread.start();
					println("Restarted");
				} else {
					println("Hasn't started yet. So cannot restart");
				}
			} else if (splited[0].equals("stop")) {
				jackpot.save();
				run = false;
				try {
					botThread.join(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				println("Stopping");
			} else if (splited[0].equals("info")) {
				println("*********INFO*********");
				println("***Money: " + bankBot.Gold);
				println("***Players on the map: "+GameData.maps.get(jackpotBot.mapId).Players.size());
				
			} else if (splited[0].equals("save")) {
				jackpot.save();
			} else if (splited[0].equals("say")) {
				jackpotBot.send(new Packet(read));
			}
			else if(splited[0].equals("log_debug=true"))
			{
				Log.debug = true;
			}
			else if(splited[0].equals("log_debug=false"))
			{
				Log.debug = false;
			}
			else if(splited[0].equals("debug=true"))
			{
				debug = true;
			}
			else if(splited[0].equals("debug=false"))
			{
				debug = false;
			}
			else if(splited[0].equals("jackpot=true"))
			{
				jackpot.state = true;
			}
			else if(splited[0].equals("jackpot=false"))
			{
				jackpot.state = false;
			}

		}
	}
	
	public static void println(String message)
	{
		if(debug)
			System.out.println(message);
	}
}

/*
 * public static Thread consoleReader; public static Boolean consoleReaderRead =
 * true; public static Nostale nostale; public static LoginData brgeoghad;
 * public static LoginData NostaleJackpotData; public static Player jackpotBot;
 * public static Player banka; public static Jackpot jackpot; public static
 * Boolean stateStart = false; public static Boolean succesfullyEnd = false;
 * 
 * public static HashMap<Integer, Long> blacklist; public static
 * HashMap<Integer, Integer> badTrades;
 * 
 * public static void consoleReaderMethodToRun() { BufferedReader reader = new
 * BufferedReader(new InputStreamReader(System.in)); String read = ""; while
 * (consoleReaderRead) { try { read = reader.readLine(); } catch (IOException e)
 * { // TODO Auto-generated catch block e.printStackTrace(); }
 * 
 * if (read.contains("start")) { stateStart = true;
 * println("Starting"); } else if (read.contains("restart")) { if
 * (stateStart == true) { stateStart = false; while (succesfullyEnd == false) {
 * try { Thread.sleep(10); } catch (InterruptedException e) { // TODO
 * Auto-generated catch block e.printStackTrace(); } } main(null);
 * succesfullyEnd = false; stateStart = true; println("Restarted"); }
 * else { println("Havent started yet. So cannot restart"); } } else
 * if (read.contains("stop")) { stateStart = false;
 * println("Stopping"); } else if (read.contains("info")) {
 * println("*********INFO*********"); println("***Money: "
 * + banka.Gold); } else if (read.contains("save")) { jackpot.save(); } else if
 * (read.contains("say")) { jackpotBot.send(read); }
 * 
 * } }
 * 
 * public static void main(String[] args) { blacklist = new HashMap<Integer,
 * Long>(); badTrades = new HashMap<Integer, Integer>(); consoleReader = new
 * Thread(new Runnable() {
 * 
 * @Override public void run() { consoleReaderMethodToRun(); } });
 * consoleReader.start(); // 80,119
 * 
 * // Table accounts = new Table(new ArrayList<String[]>());
 * 
 * nostale = new Nostale(); brgeoghad = new LoginData(); brgeoghad.nickname =
 * "Zadek512"; brgeoghad.password = "Computer1"; brgeoghad.channel = 4;
 * brgeoghad.server = 1; brgeoghad.country = CServer.CZ;
 * 
 * NostaleJackpotData = new LoginData(); // nostaleJackpot@post.cz Computer1
 * NostaleJackpotData.nickname = "NostaleJackpot58"; NostaleJackpotData.password
 * = "951852QwErTy"; NostaleJackpotData.channel = 4; NostaleJackpotData.server =
 * 1; NostaleJackpotData.country = CServer.CZ;
 * 
 * while (stateStart == false) { try { Thread.sleep(100); } catch
 * (InterruptedException e) { e.printStackTrace(); } } jackpotBot =
 * nostale.players.get(nostale.addPlayer(NostaleJackpotData)); banka =
 * nostale.players.get(nostale.addPlayer(brgeoghad)); nostale.parse(); try {
 * Thread.sleep(1000); } catch (InterruptedException e1) { e1.printStackTrace();
 * } nostale.parse(); /* jackpotBot.walkHandler.Walk(new Pos(80, 119)); while
 * (jackpotBot.pos.x!=80 && jackpotBot.pos.y!=119) { try { Thread.sleep(100); }
 * catch (InterruptedException e) { e.printStackTrace(); } }
 */
/*
 * jackpotBot.rest();
 * 
 * jackpotBot.tradeHandler.acceptRequests = true; Trade jackpotBotTrade =
 * jackpotBot.tradeHandler.trade;
 * 
 * jackpot = new Jackpot(); jackpot.newRound();
 * 
 * long lastTimeNewRound = 0; long timeNow = System.currentTimeMillis(); Boolean
 * toSay = false; Trade bankTrade = null; int roundWarnedForAdmin = -1;
 * 
 * while (stateStart) {
 * 
 * jackpotBotTrade = jackpotBot.tradeHandler.trade; if (toSay != true && timeNow
 * - lastTimeNewRound > 5000) {
 * jackpotBot.send("say Pro v�ce info o jackpotu mi za�eptej '/help'"); toSay =
 * true; } timeNow = System.currentTimeMillis(); if (timeNow - lastTimeNewRound
 * > jackpot.roundLength) // NEW ROUND { toSay = false; lastTimeNewRound =
 * timeNow; int total = jackpot.total; int winnerId = jackpot.getWinner();
 * 
 * if (winnerId == -1) {
 * jackpotBot.send("say Pr�zdn� kolo. Nikdo nevyhr�l :(."); continue; } float m
 * = (total / 100f * 92.5f); jackpot.setPlayersMoney((int) banka.id, total -
 * ((int) m)); MapCharacterInstance winnerInstance; try { winnerInstance =
 * jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players.get(winnerId); }
 * catch (Exception e) { winnerInstance = new MapCharacterInstance();
 * winnerInstance.Name = "nezname_jmeno"; }
 * 
 * jackpotBot.send("say V�t�zem " + (int) m + " gold� je: " +
 * winnerInstance.Name);
 * 
 * try { Thread.sleep(500); } catch (InterruptedException e) { // TODO
 * Auto-generated catch block e.printStackTrace(); }
 * 
 * jackpotBot.send("say Za��n� kolo #" + jackpot.rounds.data.size());
 * 
 * }
 * 
 * // Check if somebody spk to me if (jackpotBot.spk.size() > 0) { for (int i =
 * 0; i < jackpotBot.spk.size(); i++) { // spk 1 351883 5 Hel�a258 ��� String
 * packet = jackpotBot.spk.get(i); Packet p = new Packet(packet); if
 * (p.getInt(2) == jackpotBot.id) { jackpotBot.spk.remove(i); continue; }
 * 
 * jackpotBot.spk.remove(i); if (packet.contains("/balance")) { try {
 * jackpotBot.send("/" + p.get(4) + " M� na sv�m kont�: " +
 * jackpot.getPlayersMoney(p.getInt(2)) + " zlata"); } catch (Exception e) {
 * e.printStackTrace(); jackpotBot.send( "/" + p.get(4) +
 * " Bohu�el se mi nepoda�ilo zjistit kolik m� na sv�m ��t� pen�z."); } } else
 * if (packet.contains("/info")) {
 * 
 * jackpotBot.send("/" + p.get(4) + " " + "INFO #" +
 * jackpot.players.data.size()); nostale.parse(); try { Thread.sleep(50); }
 * catch (InterruptedException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } jackpotBot.send("/" + p.get(4) + " " + "Celkem: " +
 * jackpot.total); nostale.parse(); try { Thread.sleep(50); } catch
 * (InterruptedException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } else if (packet.contains("/help")) {
 * jackpotBot.send("/" + p.get(4) + " " + "*** Nostale Jackpot ***");
 * jackpotBot.send("/" + p.get(4) + " " +
 * "Za ztracen� pen�ze neberu odpov�dnost, pokud mi d� admin ban st�ujte si jemu."
 * ); jackpotBot.send("/" + p.get(4) + " " + "Maxim�ln� v��e s�zky je 50kk.");
 * jackpotBot.send("/" + p.get(4) + " " +
 * "V ka�d�m kole si beru 7.5% z celkov� vsazen� ��stky."); jackpotBot.send("/"
 * + p.get(4) + " " + "Vyhran� pen�ze dostane� p�i dal�� v�m�n�.");
 * jackpotBot.send("/" + p.get(4) + " " +
 * "V�m�nu m��e� ud�lat 1 za 10 sekund, aby se dostalo na v�echny tak nezdr�uj!"
 * ); jackpotBot.send("/" + p.get(4) + " " + "Dostupn� p��kazy:");
 * jackpotBot.send("/" + p.get(4) + " " + "/help"); jackpotBot.send("/" +
 * p.get(4) + " " + "/balance - zobraz� tvoje konto"); jackpotBot.send("/" +
 * p.get(4) + " " + "/info - zobraz� informace o pr�v� prob�haj�c�m kole"); //
 * jackpotBot.send("/"+p.get(4)+" "+"/info kolo - // zobraz� informace o kole
 * (p�. /info 1255)"); } } }
 * 
 * if (jackpotBotTrade != null && jackpotBotTrade.MyStatus == 0 &&
 * jackpotBotTrade.OponentStatus == 0) { // TODO check if this guy isnt in
 * blacklist Boolean isBlacklisted = false; try { Long l =
 * blacklist.get(jackpotBotTrade.playerID); if (l > 0) isBlacklisted = true; if
 * (l < System.currentTimeMillis() - 10000) {
 * blacklist.remove(jackpotBotTrade.playerID); isBlacklisted = false; } } catch
 * (Exception e) {
 * 
 * }
 * 
 * try { if (badTrades.get(jackpotBotTrade.playerID) > 3) { isBlacklisted =
 * true; blacklist.put(jackpotBotTrade.playerID, System.currentTimeMillis() +
 * 60000); jackpotBot.send("/" + jackpotBotTrade.playerID +
 * " Byl jsi na 60 sekund zablokov�n."); } } catch (Exception e) {
 * 
 * }
 * 
 * if (isBlacklisted == false) jackpotBotTrade.acceptRequest(); else
 * jackpotBotTrade.declineRequest(); blacklist.put(jackpotBotTrade.playerID,
 * System.currentTimeMillis()); } if (jackpotBotTrade != null) { if
 * (jackpotBotTrade.MyStatus == 10 || jackpotBotTrade.MyStatus > 3 ||
 * jackpotBotTrade.OponentStatus > 3) { jackpotBot.tradeHandler.trade = null; }
 * else {
 * 
 * // New trade even accepted MyStatus = 1; OponentStatus = 1; if
 * (jackpotBotTrade.OponentStatus == 2 && jackpotBotTrade.MyStatus == 1) //
 * Oponent // set // their // list { int moneyHeSet =
 * jackpotBotTrade.goldFromPlayer; int playersMoneyInDB =
 * jackpot.getPlayersMoney(jackpotBotTrade.playerID);
 * 
 * if (moneyHeSet < 1 && playersMoneyInDB < 1) // why is he // dafuq // trading
 * // me? { jackpotBotTrade.declineTrade(); jackpotBotTrade.MyStatus = 10; try {
 * badTrades.put(jackpotBotTrade.playerID,
 * badTrades.get(jackpotBotTrade.playerID) + 1); } catch (Exception e) {
 * badTrades.put(jackpotBotTrade.playerID, 1); } }
 * 
 * if (playersMoneyInDB > 0) { jackpotBotTrade.give(playersMoneyInDB);
 * 
 * } else jackpotBotTrade.give(0);
 * 
 * jackpotBotTrade.acceptTrade(); }
 * 
 * if (jackpotBotTrade.OponentStatus == 3) {
 * badTrades.remove(jackpotBotTrade.playerID); if (jackpotBotTrade.MyStatus !=
 * 3) jackpotBotTrade.acceptTrade(); jackpotBotTrade.MyStatus = 10; if
 * (jackpotBotTrade.playerID != banka.id)
 * jackpot.setPlayersMoney(jackpotBotTrade.playerID,
 * jackpot.getPlayersMoney(jackpotBotTrade.playerID) -
 * jackpotBotTrade.moneyGiven); int moneyHeSet = jackpotBotTrade.goldFromPlayer;
 * if (moneyHeSet > 0) { jackpot.bet(jackpotBotTrade.playerID, moneyHeSet);
 * jackpotBot .send("say " +
 * jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players
 * .get(jackpotBotTrade.playerID).Name + " pr�v� vsadil " + moneyHeSet); }
 * 
 * } }
 * 
 * }
 * 
 * int moneyInBank = jackpot.getPlayersMoney((int) banka.id); bankTrade =
 * banka.tradeHandler.trade; if ((bankTrade == null || bankTrade.MyStatus == 10)
 * && moneyInBank > 0) { banka.tradeHandler.newRequest((int) jackpotBot.id);
 * bankTrade = banka.tradeHandler.trade; bankTrade.MyStatus = 1; } else if
 * (bankTrade != null && bankTrade.MyStatus != 10) { if (bankTrade.OponentStatus
 * == 1 && bankTrade.MyStatus == 1) { bankTrade.give(0); } else if
 * (bankTrade.OponentStatus == 2) { bankTrade.acceptTrade();
 * banka.tradeHandler.trade.MyStatus = 10; } }
 * 
 * for (Map.Entry<Integer, MapCharacterInstance> entry :
 * banka.gameData.map[banka.gameDataMapID].Players .entrySet()) { // Integer key
 * = entry.getKey(); MapCharacterInstance value = entry.getValue(); if
 * (value.Authority != 0 && roundWarnedForAdmin < jackpot.rounds.data.size()) {
 * roundWarnedForAdmin = jackpot.rounds.data.size(); //
 * ADMINADMINADMINADMINADMINADMINADIANDIANDIANDIADNAIDN
 * 
 * Iterator it =
 * jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players.entrySet().iterator
 * (); while (it.hasNext()) { Map.Entry pair = (Map.Entry) it.next();
 * println("Admin on map"); jackpotBot.send( "say " +
 * ((MapCharacterInstance) pair.getValue()).Name + " Pozor, na map� je admin!");
 * 
 * }
 * 
 * }
 * 
 * }
 * 
 * nostale.parse(); try { Thread.sleep(10); } catch (InterruptedException e) {
 * // TODO Auto-generated catch block e.printStackTrace(); }
 * 
 * }
 * 
 * for (Player p : nostale.players) { try { p.login.c.Close();
 * p.login.tt.cancel(); p.login.t.cancel(); } catch (Exception e) { // TODO
 * Auto-generated catch block e.printStackTrace(); } }
 * 
 * // Admin control. map control, basar selling
 * 
 * }
 * 
 * }
 * 
 */

/*
 * if (jackpotBotTrade != null) { if(jackpotBotTrade.MyStatus ==10)
 * jackpotBot.tradeHandler.trade = null; if (jackpotBotTrade.MyStatus == 0 &&
 * jackpotBotTrade.OponentStatus == 1) // request // came // from // somebody {
 * // accept? jackpotBotTrade.acceptRequest(); } if(jackpotBotTrade.MyStatus==1
 * && jackpotBotTrade.OponentStatus==1 && jackpotBotTrade.playerID == banka.id)
 * { jackpotBotTrade.give(totalToPutIntoTheBank); totalToPutIntoTheBank = 0; }
 * if(jackpotBotTrade.OponentStatus == 2 && jackpotBotTrade.playerID==banka.id)
 * { jackpotBotTrade.acceptTrade(); bankTrade = null; putting = false;
 * Log.log("MONEY TRANSFER", "Money sucessfully given"); }
 * 
 * if (jackpotBotTrade.MyStatus == 1 && jackpotBotTrade.OponentStatus == 2) //
 * request // is // accepted // and // somebody // executed // their // list {
 * if (jackpotBotTrade.goldFromPlayer >= jackpot.minimumToBet &&
 * jackpotBotTrade.goldFromPlayer <= jackpot.maximumToBet) { int money = 0; try
 * { money = jackpot.getPlayersMoney(jackpotBotTrade.playerID); }
 * catch(Exception e) { money = 0; } jackpotBotTrade.give(money);
 * jackpotBotTrade.acceptTrade();
 * 
 * } else { int tPLayersMoney =
 * jackpot.getPlayersMoney(jackpotBotTrade.playerID); if (tPLayersMoney > 0)
 * jackpotBotTrade.give(tPLayersMoney); else jackpotBotTrade.declineTrade();
 * jackpotBotTrade.acceptTrade(); jackpotBotTrade.MyStatus = 10;
 * 
 * }
 * 
 * } // if both 3 then trade is good if 3 and 4/5 then canceled if
 * (jackpotBotTrade.MyStatus == 3 && jackpotBotTrade.OponentStatus == 3) { //
 * Good, trade was done // println("Good"); jackpotBotTrade.MyStatus
 * = 10; // brgeoad.tradeHandler.trade = null;
 * jackpot.bet(jackpotBotTrade.playerID, jackpotBotTrade.goldFromPlayer);
 * 
 * jackpotBot.send("say " +
 * jackpotBot.gameData.map[jackpotBot.gameDataMapID].Players
 * .get(jackpotBotTrade.playerID).Name + " pr�v� vsadil " +
 * jackpotBotTrade.goldFromPlayer);
 * 
 * } if ((jackpotBotTrade.MyStatus == 4 || jackpotBotTrade.MyStatus == 5 ||
 * jackpotBotTrade.OponentStatus == 4 || jackpotBotTrade.OponentStatus == 5) &&
 * jackpotBotTrade.MyStatus != 10) { // Bad, trade was declined //
 * println("Bad"); jackpotBotTrade.MyStatus = 10; //
 * brgeoad.tradeHandler.trade = null; } } else//Put my money into the "bank" {
 * if(totalToPutIntoTheBank>0 && putting == false) { //totalToPutIntoTheBank =
 * 0; putting = true; bankTrade = new Trade(banka);
 * banka.send("req_exc 1 "+jackpotBot.id);
 * 
 * }
 * 
 * }
 * 
 * if(bankTrade!=null) { if(bankTrade.OponentStatus==1) bankTrade.give(0);
 * if(bankTrade.OponentStatus==2) jackpotBotTrade.acceptTrade(); }
 */
