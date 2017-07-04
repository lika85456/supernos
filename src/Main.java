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
		n.Login("Jedle85456","Computer1",CServer.CZ);
		for(GameServer s:n.Login.channels)
			if(s.channel.equals("1"))
				n.SelectChannel(s);
		n.SelectCharacter(n.GameData.characters[1]);
		Thread.sleep(250); 
		n.ReceiveAndParse();
		
		//Print out skills
        for(Skill s:n.GameData.Character.skills)
        {
        	System.out.println(s.toString());
        }
		
		
		Boolean loop = true;
		Boolean bot = true;
		Boolean printed = false;
        System.out.println(n.GameData.map.Name);
		
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
            n.ReceiveAndParse();
            if(printed==false)
            {
    		for(Map.Entry<Integer, MapCharacterInstance> entry : n.GameData.Characters.entrySet()) {
    		    Integer key = entry.getKey();
    		    MapCharacterInstance value = entry.getValue();
                System.out.println(value.Name+"=>"+value.Authority);
                printed = true;
    		}
    		
            }
            Thread.sleep(5);
		}
		
	}
}
