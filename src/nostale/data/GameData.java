package nostale.data;

import java.util.HashMap;

public class GameData {
	public static HashMap<Integer,MapInstance> maps = new HashMap<Integer,MapInstance>();
	
	public static Integer addMap(MapInstance mp)
	{
		if(maps.containsKey(mp.id))
		{
			
		}
		else
		{
			maps.put(mp.id,mp);
		}
		return mp.id;
	}
}
