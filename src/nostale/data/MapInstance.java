package nostale.data;

import java.util.HashMap;

import nostale.resources.Map;

public class MapInstance extends nostale.resources.Map{
    public HashMap<Integer, Portal> Portals = new HashMap<Integer, Portal>();
    public HashMap<Integer, MapMobInstance> Mobs = new HashMap<Integer, MapMobInstance>();
    public HashMap<Integer, MapItemInstance> Items = new HashMap<Integer, MapItemInstance>();
    public HashMap<Integer, MapCharacterInstance> Players = new HashMap<Integer, MapCharacterInstance>();
}
