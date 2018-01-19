package nostale.data;

import java.util.HashMap;

public class MapInstance extends nostale.resources.Map{
    public HashMap<Integer, Portal> Portals = new HashMap<Integer, Portal>();
    public HashMap<Integer, MonsterMapInstance> Mobs = new HashMap<Integer, MonsterMapInstance>();
    public HashMap<Integer, MapItemInstance> Items = new HashMap<Integer, MapItemInstance>();
    public HashMap<Integer, MapCharacterInstance> Players = new HashMap<Integer, MapCharacterInstance>();
}