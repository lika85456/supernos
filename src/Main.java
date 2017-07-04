import nostale.resources.Resources;

import nostale.*;
import nostale.data.BasarItemInstance;


public class Main {
       
	public static void main(String[] args)throws Exception {

		Resources.load();
		Nostale n = new Nostale();
		n.Login("Zadek512","Computer1",CServer.CZ);
		for(GameServer s:n.Login.channels)
			if(s.channel.equals("2"))
				n.SelectChannel(s);
		n.SelectCharacter(n.GameData.characters[0]);
		Thread.sleep(250); 
		n.ReceiveAndParse();
		

		
		
		Boolean loop = true;

        System.out.println("Mapa:"+n.GameData.map.Name);
		
        
        
		while(loop)
		{
			n.ReceiveAndParse();
			long price_cella = n.Nosbasar.Search(1014)[0].Price;
			//n.ReceiveAndParse();
	        //long price_gilli = n.Nosbasar.Search(1013)[0].Price;
	        
	        BasarItemInstance[] myItems = n.Nosbasar.GetMyItems();
	        for(BasarItemInstance i : myItems)
	        {
	        	if(i.soldedAmount==i.Amount){
	        		n.Nosbasar.GetMoney(i);
	        	}
	        }
	        
           
            Thread.sleep(500);
		}
		
	}
}
