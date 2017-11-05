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
		Thread.sleep(3000); 
		n.ReceiveAndParse();
				
		Boolean loop = true;
        System.out.println(n.GameData.map.Name);
		//n.send("say ss");
		n.ReceiveAndParse();
		for (Map.Entry<Integer, nostale.data.MapCharacterInstance> entry : n.GameData.Characters.entrySet()) {
		    Integer key = entry.getKey();
		    MapCharacterInstance value = entry.getValue();
		    for (int i = 0; i < value.Name.length(); i++) {
		    	System.out.println((int)value.Name.charAt(i));
		    }
		   // n.send("/"+value.Name+" ss");
		    System.out.println("/"+value.Name+" ss");
		    

		}
	    while(loop)
		{

            n.ReceiveAndParse();
            Thread.sleep(10);
		}
		SSS
	}
}
