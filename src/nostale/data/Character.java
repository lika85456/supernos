package nostale.data;
public class Character {
    
    public ClassType Class;
    public int Faction;
    public long Gold;
    public byte HeroLevel;
    public long HeroXp;
    public int Hp;
    public Boolean HpBlocked;
    public byte JobLevel;
    public long JobLevelXp;
    public byte Level;
    public long LevelXp;
    public long LevelMaxXp;
    public long JobLevelMaxXp;
    public short MapId;

    public int Mp;
    public String Name;
    public byte Slot;
    public int SpAdditionPoint;
    public int SpPoint;
    public short Direction;
    public byte Authority;
    public int MaxHp;
    public int MaxMp;
    public Boolean IsSitting;
    public Boolean IsInvisible;
    
    public int Speed;
    public Skill[] skills;
    public Boolean Moving = false;
    public Boolean ShouldRest;
    
    public Inventory inventory;

    
    
    
    
    public Character()
    {
    }
    
    
    public Skill getSkillByCastID(int castID)
    {
    	for(int i=0;i<skills.length;i++)
    	{
    		if(skills[i].CastId==castID)
    			return skills[i];
    	}
    	return null;
    }
    
    public Skill getSkillByVNUM(int vnum)
    {
        for(Skill s:skills)
        {
            if(s.VNUM == vnum)
                return s;
        }
        return null;
    }

}
