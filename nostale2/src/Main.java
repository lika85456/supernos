import nostale.*;
import nostale.login.*;
import nostale.struct.Mob;
import nostale.util.Pos;
import java.util.*;
public class Main {
       
	public static void main(String[] args)throws Exception {
		Nostale n = new Nostale();
		n.Login("Zadek512","Computer1",CServer.CZ);
		for(GameServer s:n.Login.channels)
			if(s.channel.equals("2"))
				n.SelectChannel(s);
		n.SelectCharacter(n.GameData.characters[0]);
		Thread.sleep(1000); 
		n.ReceiveAndParse();
		
		Boolean loop = true;
		Boolean bot = true;
		//n.Walk(30,30);
		int iter = 0;
		while(loop)
		{
			iter++;
	        n.ReceiveAndParse();
            if(n.target==null && bot) //Target is dead or not set
            {
            	//TODO Set new target
            	n.target = n.GetNearestMob();
            	System.out.println("Selected target: "+n.target.Name+"| Range:"+Pos.getRange(n.target.Pos, n.GameData.Character.Pos));
            }
            
            if(bot && n.target!=null)
            {
            	n.Attack(n.target, n.GameData.Character.skills[0]);
            }
            
            if(iter==10) {System.out.println(Logger.logged);}
            Thread.sleep(10);
		}
	}
}
