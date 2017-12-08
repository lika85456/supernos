package nostale.handler;

import java.util.ArrayList;

import nostale.Packet;
import nostale.data.Player;

public class TradeRequestHandler extends Handler{
	public Boolean acceptRequests = false;
	public Trade trade;
	public ArrayList<Integer> ban;
	public Boolean pendingRequest = true;

	
	public TradeRequestHandler(Player p)
	{
		super(p);
		ban = new ArrayList<Integer>();
	}
	
	public void newRequest(int pid)
	{
		player.send("req_exc 1 "+pid);
		trade=new Trade(player);
		trade.playerID = pid;
	}
	

	public void onReceive(String packet)
	{
		Packet pac = new Packet(packet);
        switch(pac.type)
        {
        	case "dlg": //request came so accept it
        		//#req_exc^2^351883
        		if(!packet.contains("req_exc"))
        		{
        			player.send(pac.get(2));
        			break;
        		}
        		if(trade==null || trade.MyStatus==10)
        		{
        			
        			String splited[] = pac.get(1).split("\\^");
        			int playerID = Integer.parseInt(splited[2]);
        			
        		    if(ban.contains(playerID)) return;	
        			
            		trade = null;
            		trade = new Trade(player);   
            		trade.playerID = playerID;
        		}
        		else
        			pendingRequest = true;
        			


        	break;
        	
        	case "exc_list": //player executed their list or accepted traderequest
        		//exc_list 1 374541 -1
        		
        		if(pac.getInt(1)==1 && pac.getInt(3)==-1)
        		{
        			trade.OponentStatus = 1;
        		}
        		else
        		{
        			trade.playerExecutedList(pac.getInt(3));
        		}
        		
        		/*if(pac.getInt(3)==-1)
        			trade.playerExecutedList(pac.getInt(3));
        			
        		if(pac.getInt(3)>-1)
        		{
        			trade.playerAcceptedTrade();
        		}
        		
        		*/
        		break;
        	
        	case "exc_close": //request closed by player
        		try
        		{
            		if(pac.getInt(1)==1)
            			trade.playerAcceptedTrade();
            		else
            			trade.playerClosedTrade();
        		}
        		catch(Exception e)
        		{
        			e.printStackTrace();
        		}
        		
        	break;
        	
        }
	}
}
