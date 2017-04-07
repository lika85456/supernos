package nostale.resources;
import nostale.struct.*;


public class Resources
{

    //public Pos pos; //Position
    public static Skill[] skills;
    public static Item[] items;
    public static Mob[] mobs;
    
    //TODO, make more findXById funcs
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
    public static Skill getSkillByVNUM(int id)
    {
        for(int i = 0;i<Resources.skills.length;i++)
        {
          if(Resources.skills[i].VNUM==id) 
          return Resources.skills[i];
        }
        return null; 
    }
    
}
