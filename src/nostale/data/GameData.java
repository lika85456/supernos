package nostale.data;
import java.util.ArrayList;
import java.util.Arrays;
public class GameData {
    public MapInstance[] map;
    public GameData(){
    	this.map = new MapInstance[1];
    }
    
    //Adds map into the array
    public int addMap(MapInstance m)
    {
    	ArrayList<nostale.data.MapInstance> tempAL = new ArrayList<nostale.data.MapInstance>(Arrays.asList(this.map));
    	tempAL.add(m);
    	this.map = tempAL.toArray(new MapInstance[0]);
    	int id = tempAL.indexOf(m);
    	return id;
    }
}
