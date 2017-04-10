package nostale.data;
import java.util.*;
public class GameData {
    public String id = "";
    public String pw = "";
    public nostale.data.Character[] characters;
    public int session = 0;
    public MapCharacterInstance Character;
    
    public nostale.resources.Map map;
    
    public HashMap<Integer, MapCharacterInstance> Characters = new HashMap<Integer, MapCharacterInstance>();
    public HashMap<Integer, Portal> Portals = new HashMap<Integer, Portal>();
    public HashMap<Integer, MapMobInstance> Mobs = new HashMap<Integer, MapMobInstance>();
    public HashMap<Integer, MapItemInstance> Items = new HashMap<Integer, MapItemInstance>();
    public HashMap<Integer, MapCharacterInstance> Players = new HashMap<Integer, MapCharacterInstance>();
    public GameData(){}
}
