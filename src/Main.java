import nostale.resources.Resources;

import nostale.*;
import nostale.data.MapCharacterInstance;
import nostale.data.MapItemInstance;
import nostale.data.Skill;
import nostale.util.Pos;
import java.util.*;

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

	       /* 
            if(n.target==null && bot) //Target is dead or not set
            {
            	//TODO Set new target
            	n.target = n.GetNearestMob();
            	
            }
            
            if(bot && n.target!=null)
            {
            	n.BattleHandler.UseSkill(n.GameData.Character.skills[0],n.target);
            }
            
            MapItemInstance[] MyItemsOnTheGround = n.GetPickupableItems();
            for(MapItemInstance item : MyItemsOnTheGround)
            {
            	System.out.println("Trying to pickup: "+item.id+" on "+item.Pos);
            	n.PickUpItem(item.id);
            }
            */
			n.Nosbasar.Search(1014);
            n.ReceiveAndParse();
            Thread.sleep(5);
		}
		
	}
}
