package nostale.handler;

import nostale.data.Item;
import nostale.domain.RequestExchangeType;
import nostale.gameobject.Player;
import nostale.handler.interfaces.ITradeHandler;
import nostale.packet.Packet;

public class TradeHandler extends Handler implements ITradeHandler{

	private Boolean isTrading = false;
	private long playerID;
	private RequestExchangeType MyStatus = 
	public TradeHandler(Player p)
	{
		super(p);
	}
	
	@Override
	public void createRequest(long id) {
		player.send(new Packet("req_exc 1 "+id));
		this.playerID = id;		
	}

	@Override
	public RequestExchangeType getMyStatus() {
		return null;
	}

	@Override
	public RequestExchangeType getOponentStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onExecuteList(Item[] items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequest(Packet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declineRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declineTrade() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeList(Item[] items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptTrade() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTradeAccept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTradeDecline() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean isTrading() {
		// TODO Auto-generated method stub
		return null;
	}

}
