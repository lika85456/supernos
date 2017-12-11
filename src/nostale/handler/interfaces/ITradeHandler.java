package nostale.handler.interfaces;

import nostale.data.Item;
import nostale.domain.RequestExchangeType;
import nostale.packet.Packet;

public interface ITradeHandler {
	public void createRequest(long id);
	public RequestExchangeType getMyStatus();
	public RequestExchangeType getOponentStatus();
	public void onExecuteList(Item[] items);
	public void onRequest(Packet p);
	public void declineRequest();
	public void declineTrade();
	public void executeList(Item[] items);
	public void acceptTrade();
	public void onTradeAccept();
	public void onTradeDecline();
	public Boolean isTrading();
}
