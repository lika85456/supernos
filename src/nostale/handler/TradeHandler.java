package nostale.handler;

import nostale.domain.RequestExchangeType;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IHandler;
import nostale.handler.interfaces.ITradeHandler;
import nostale.packet.Packet;

public class TradeHandler extends Handler implements ITradeHandler, IHandler {

	private Boolean isTrading = false;
	public long playerID;
	public String playerName;
	public int OponentGold = 0;
	private RequestExchangeType MyStatus = RequestExchangeType.Unknown;
	private RequestExchangeType OponentStatus = RequestExchangeType.Unknown;

	public TradeHandler(Player p) {
		super(p);
	}

	@Override
	public void createRequest(long id) {
		player.send(new Packet("req_exc 1 " + id));
		this.playerID = id;
	}
	
	@Override
	public void acceptRequest()
	{
		this.isTrading = true;
		this.MyStatus = RequestExchangeType.Requested;
		this.OponentStatus = RequestExchangeType.Requested;
		player.send(generateAnswer(true));
	}

	@Override
	public RequestExchangeType getMyStatus() {
		return MyStatus;
	}

	@Override
	public RequestExchangeType getOponentStatus() {
		return OponentStatus;
	}

	@Override
	public void onExecuteList(int gold) {
		this.OponentStatus = RequestExchangeType.List;
		this.OponentGold = gold;
	}

	@Override
	public void onRequest(Packet p) {
		String parse = p.getParameter(1);
		this.playerID = Long.parseLong(parse.split("\\^")[2]);
	}

	@Override
	public void onRequestAccept() {
		this.isTrading = true;
		this.MyStatus = RequestExchangeType.Requested;
		this.OponentStatus = RequestExchangeType.Requested;
	}
	
	private Packet generateAnswer(Boolean decision) {
		if (decision)
			return new Packet("#req_exc^2^" + playerID);
		else
			return new Packet("#req_exc^5^" + playerID);
	}

	// #req_exc^2^351883
	/***
	 * if(!packet.packetString.contains("req_exc")) { player.send(pac.get(2));
	 * break; } if(trade==null || trade.MyStatus==10) {
	 * 
	 * String splited[] = pac.get(1).split("\\^"); int playerID =
	 * Integer.parseInt(splited[2]);
	 * 
	 * if(ban.contains(playerID)) return;
	 * 
	 * trade = null; trade = new Trade(player); trade.playerID = playerID; }
	 * else pendingRequest = true;
	 */

	@Override
	public void declineRequest() {
		this.isTrading = false;
		this.MyStatus = RequestExchangeType.Unknown;
		this.OponentStatus = RequestExchangeType.Unknown;
		player.send(generateAnswer(false));
	}

	@Override
	public void declineTrade() {
		this.isTrading = false;
		this.MyStatus = RequestExchangeType.Unknown;
		this.OponentStatus = RequestExchangeType.Unknown;
		player.send(new Packet("req_exc 4"));
	}

	@Override
	public void executeList(int gold) {
		player.send(new Packet("exc_list " + gold));
		this.MyStatus = RequestExchangeType.Confirmed;
	}

	@Override
	public void acceptTrade() {
		this.isTrading = false;
		this.MyStatus = RequestExchangeType.Requested;
		this.OponentStatus = RequestExchangeType.Requested;
		player.send(new Packet("req_exc 3"));
	}

	@Override
	public void onTradeAccept() {
		this.OponentStatus = RequestExchangeType.Unknown;
		this.MyStatus = RequestExchangeType.Unknown;
		this.isTrading = false;
		
	}

	@Override
	public void onTradeDecline() {
		this.OponentStatus = RequestExchangeType.Unknown;
		this.MyStatus = RequestExchangeType.Unknown;
		this.isTrading = false;
	}

	@Override
	public Boolean isTrading() {
		return isTrading;
	}

	@Override
	public void parsePacket(Packet packet) {
		switch (packet.name) {
		case "dlg":
			onRequest(packet);
			break;
			
		case "exc_list": // player executed their list or accepted traderequest
			// exc_list 1 374541 -1

			if (packet.getIntParameter(1) == 1 && packet.getIntParameter(3) == -1) {
				onRequestAccept();
			} else {
				onExecuteList(packet.getIntParameter(3));
			}

			break;

		case "exc_close": // request closed by player
			try {
				if (packet.getIntParameter(1) == 1)
					onTradeAccept();
				else
					onTradeDecline();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		}
	}



}
