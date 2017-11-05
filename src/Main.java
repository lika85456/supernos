import nostale.resources.Resources;

import nostale.*;
import nostale.data.MapCharacterInstance;
import nostale.data.MapItemInstance;
import nostale.data.Skill;
import nostale.util.Pos;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
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

		n.ReceiveAndParse();
		
		for (Map.Entry<Integer, nostale.data.MapCharacterInstance> entry : n.GameData.Characters.entrySet()) {
		    Integer key = entry.getKey();
		    MapCharacterInstance charOnTheMap = entry.getValue();
		    if(charOnTheMap.Name.contains("a258"))
		    {
		    	n.send("/"+charOnTheMap.Name+" ss");
		    }
		    

		}
		n.send("say Helèa258");
	    while(loop)
		{

            n.ReceiveAndParse();
            Thread.sleep(10);
		}
	    
	}
}
