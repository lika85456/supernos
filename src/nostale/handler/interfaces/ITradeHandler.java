package nostale.handler.interfaces;

import nostale.domain.RequestExchangeType;
import nostale.packet.Packet;

public interface ITradeHandler {
	public void createRequest(long id);
	public RequestExchangeType getMyStatus();
	public RequestExchangeType getOponentStatus();
	public void onExecuteList(int gold);
	public void onRequest(Packet p);
	public void onRequestAccept();
	public void acceptRequest();
	public void declineRequest();
	public void declineTrade();
	public void executeList(int gold);
	public void acceptTrade();
	public void onTradeAccept();
	public void onTradeDecline();
	public Boolean isTrading();
}
