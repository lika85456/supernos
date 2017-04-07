package nostale.struct;
import java.util.*;
public class GameData {
    public String id = "";
    public String pw = "";
    public Character[] characters;
    public int session = 0;
    public Character Character;
    
    public nostale.resources.Map map;
    
    public HashMap<Integer, Character> Characters = new HashMap<Integer, Character>();
    public HashMap<Integer, Portal> Portals = new HashMap<Integer, Portal>();
    public HashMap<Integer, Mob> Mobs = new HashMap<Integer, Mob>();
    public HashMap<Integer, Item> Items = new HashMap<Integer, Item>();
    public HashMap<Integer, nostale.struct.Character> Players = new HashMap<Integer, nostale.struct.Character>();
    public GameData(){}
}
