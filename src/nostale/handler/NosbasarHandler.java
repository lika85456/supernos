package nostale.handler;

import java.util.ArrayList;

import nostale.Packet;
import nostale.data.NosbasarItem;
import nostale.data.Player;
import nostale.resources.Resources;

public class NosbasarHandler extends Handler{
	public NosbasarItem[] loadedItems;
	public int lastPage = 0;
	public NosbasarHandler(Player player)
	{
		super(player);
	}
	
	/***
	 * Loads page from nosbasar
	 * @param page
	 * @return
	 */
	public NosbasarItem[] loadPage(int page)
	{
		c_blist(page,(short)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,"0");
		while(loadedItems == null){player.ReceiveAndParse();}
		NosbasarItem[] t = this.loadedItems.clone();
		this.loadedItems = null;
		return t;
	}
	
	public NosbasarItem[] loadItemsByVNUM(int VNUM)
	{
		c_blist(0,(short)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)1,String.valueOf(VNUM));
		while(loadedItems == null){player.ReceiveAndParse();}
		NosbasarItem[] t = this.loadedItems.clone();
		this.loadedItems = null;
		return t;
	}
	
	private void c_blist(int index,short filterType,byte subTypeFilter,byte levelFilter,byte rareFilter,byte upgradeFilter,byte numberOfVNUMS,String itemVnumFilter)
	{
		player.send("c_blist "+index+" "+filterType+" "+subTypeFilter+" "+levelFilter+" "+rareFilter+" "+upgradeFilter+" 0 0 "+numberOfVNUMS+" "+itemVnumFilter);
	}

	public void onReceive(String packet)
	{
        Packet pac = new Packet(packet);
        switch(pac.type)
        {
        case "rc_blist":
        	int index = pac.getInt(1);
        	int size = pac.splited.length;
        	ArrayList<NosbasarItem> items = new ArrayList<NosbasarItem>();
        	for(int i = 2;i<size;i++)
        	{
        		
        		if(pac.get(i).equals("\n")) continue;
        		String[] split = pac.get(i).split("\\|");
        		int VNUM = Short.parseShort(split[3]);
        		NosbasarItem temp = new NosbasarItem(Resources.getItemByVNUM(VNUM));
        		temp.BasarItemId = Long.parseLong(split[0]);
        		temp.SellerId = Long.parseLong(split[1]);
        		temp.Owner = split[2];
        		temp.VNUM = Short.parseShort(split[3]);
        		temp.Amount = Byte.parseByte(split[4]);
        		temp.IsPackage = split[5]=="1" ? true : false; 
        		temp.BasarPrice = Long.parseLong(split[6]);
        		temp.DateStart = Long.parseLong(split[7]);
        		temp.BasicUpgrade = Short.parseShort(split[11]);
        		temp.Rare = Byte.parseByte(split[10]);
        		try
        		{
        			temp.Info = split[12];
        		}
        		catch(Exception e)
        		{
        			
        		}
        		
        		items.add(temp);
        	}
        	this.loadedItems = items.toArray(new NosbasarItem[0]);
           //{bzlink.BazaarItem.BazaarItemId}|
        /*  {bzlink.BazaarItem.SellerId}|
        	{bzlink.Owner}|
        	{bzlink.Item.Item.VNum}|
        	{bzlink.Item.Amount}|
        	{(bzlink.BazaarItem.IsPackage ? 1 : 0)}
        	{bzlink.BazaarItem.Price}
        	{time}|
        	2
        	0
        	{bzlink.Item.Rare}
        	{bzlink.Item.Upgrade}
        	{info} +" "
        	*/
        break;
        case "rc_buy":
          
        break;
        }
	}
}
