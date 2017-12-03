package nostale.resources;
import nostale.data.Item;
import nostale.data.MapMobInstance;
import nostale.data.Mob;
import nostale.data.Skill;



public class Resources
{

    //public Pos pos; //Position
    public static Skill[] skills;
    public static Item[] items;
    public static Mob[] mobs;
    public static Boolean loaded = false;
    

    public static MapMobInstance fill(MapMobInstance m)
    {
    	return (MapMobInstance)m;
    }
    
    public static Mob getMobByVNUM(int id)
    {
       for(int i = 0;i<Resources.mobs.length;i++)
       {
         if(Resources.mobs[i].VNUM==id) 
         return Resources.mobs[i];
       }
       return null;
    }

    public static Item getItemByVNUM(int id)
    {
        for(int i = 0;i<Resources.items.length;i++)
        {
          if(Resources.items[i].VNUM==id) 
          return Resources.items[i];
        }
        return null;    	
    }
    public static Item getItemByNameID(int id)
    {
        for(int i = 0;i<Resources.items.length;i++)
        {
          if(Resources.items[i].NameID==id) 
          return Resources.items[i];
        }
        return null;      	
    }
    public static Skill getSkillByVNUM(int id)
    {
        for(int i = 0;i<Resources.skills.length;i++)
        {
          if(Resources.skills[i].VNUM==id) 
          return Resources.skills[i];
        }
        return null; 
    }
    public static void load()
    {
    	if(loaded==false)
    	{
    		try {
				skills = LoadSkills.loadSkills();
				items = LoadItems.loadItems();
				mobs = LoadMobs.loadMobs(items);
				loaded = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
}
