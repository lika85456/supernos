import java.util.Random;

import Database.Database;
import Database.Table;
import nostale.data.GameData;
import nostale.gameobject.Player;
import nostale.handler.TalkHandler;
import nostale.util.Log;

public class Jackpot {
	public Table bets;
	public Table rounds;
	public Table players;
	public final int minimumToBet = 1;
	public final int maximumToBet = 50000000;
	public final int roundLength = 60000;
	public int total;
	public Random random;
	public Boolean state = false;
	/*
	 * BETS PLAYER_ID|GOLD
	 */

	/*
	 * ROUNDS ROUND_ID|WINNER_PLAYER_ID
	 */

	/*
	 * PLAYERS PLAYER_ID|GOLD
	 */

	/** TIMINGS **/
	private long lastTimeNewRound = 0;
	private long lastTimeAd = 0;
	
	private static final int ROUND_TIME = 60000;
	private static final int AD_TIME = 20000;
	
	public Player player;
	public Jackpot(Player player) {
		random = new Random();
		bets = new Table();
		rounds = new Table();
		players = new Table();
		player = player;
	}

	public void save() {
		Database.save("bets", bets.dataToString());
		Database.save("rounds", rounds.dataToString());
		Database.save("players", players.dataToString());
		Log.log("jackpot", "saving");
		Log.save();

	}

	public void load() // loads jackpot data if possible
	{
		bets = Table.dataFromString(Database.load("bets"));
		rounds = Table.dataFromString(Database.load("rounds"));
		players = Table.dataFromString(Database.load("players"));
	}

	public void bet(int playerID, int gold) {
		bets.add(new String[] { String.valueOf(playerID), String.valueOf(gold) });
		total += gold;
		Log.log("jackpot", "Bet from:" + playerID + " gold:" + gold);
	}

	public int getPlayersMoney(long playerID) {
		for (String[] d : players.data) {
			if (d[0].equals(String.valueOf(playerID)))
				return Integer.parseInt(d[1]);
		}
		return 0;
	}

	public void setPlayersMoney(long playerID, int gold) {
		Boolean set = false;
		try {
			for (String[] d : players.data) {
				if (d[0].equals(String.valueOf(playerID))) {
					String[] toWrite = new String[] { d[0], String.valueOf(gold) };
					players.data.remove(d);
					players.data.add(toWrite);
					set = true;
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		if (set == false) {
			players.data.add(new String[] { String.valueOf(playerID), String.valueOf(gold) });
		}
	}

	public int getWinner() {
		save();
		int winningNumber = (int) (random.nextDouble() * total);
		int sum = 0;
		int sum1 = 0;
		for (String[] bet : bets.data) {
			sum1 = Integer.parseInt(bet[1]);
			if (winningNumber <= sum1 && winningNumber >= sum) {
				int winnerId = Integer.valueOf(bet[0]);
				// int go = Integer.valueOf(bet[1]);
				Main.println("JACKPOT Winner is: " + winnerId + " with:" + bet[1] + " gold");
				long winnersMoney = getPlayersMoney(winnerId);
				// if(winnersMoney!=0)
				// Log.log("jackpot", "WTF!! this guy:"+winnerId+" has:
				// "+winnersMoney+" even he won right now");
				float moneyToSet = winnersMoney + (total / 100f * 92.5f);
				setPlayersMoney(winnerId, (int) moneyToSet);
				newRound();
				total = 0;
				return winnerId;
			}
		}
		total = 0;
		
		return -1;
	}

	public void newRound() {
		rounds.add(new String[] { bets.dataToString() });
		bets = new Table();
		Log.log("jackpot", "New round number: " + rounds.data.size());
	}
	
	public void parse(TalkHandler talkHandler)
	{
		long time = System.currentTimeMillis();
		if(lastTimeNewRound+ROUND_TIME<time && state==true)
		{
			lastTimeNewRound = time;
			int id = getWinner();
			try {
				talkHandler.say("Kolo je u konce a výherce je: "+GameData.maps.get(player.mapId).Players.get(id));
			}
			catch(Exception e)
			{
				talkHandler.say("Kolo je u konce a výherce je: ERROR_UNKNOWN");
			}
		}
	}
}
