package nostale.data;

import java.util.HashMap;

public class MapInstance extends nostale.resources.Map{
    public HashMap<Integer, Portal> Portals = new HashMap<Integer, Portal>();
    public HashMap<Integer, NpcMonsterInstance> Mobs = new HashMap<Integer, NpcMonsterInstance>();
    public HashMap<Integer, MapItemInstance> Items = new HashMap<Integer, MapItemInstance>();
    public HashMap<Integer, MapCharacterInstance> Players = new HashMap<Integer, MapCharacterInstance>();
}