package nostale.data;

public class Skill {
	public short AttackAnimation;
    public short BuffId;
    public short CastAnimation;
    public short CastEffect;
    public short CastId;
    public short CastTime;
    public short Class;
    public short Cooldown;
    public short CPCost;
    public short Damage;
    public short Duration;
    public short Effect;
    public short Element;
    public short ElementalDamage;
    public short HitType;
    public short ItemVNum;
    public short Level;
    public short LevelMinimum;
    public short MinimumAdventurerLevel;
    public short MinimumArcherLevel;
    public short MinimumMagicianLevel;
    public short MinimumSwordmanLevel;
    public short MpCost;
    public String Name;
    public int Price;
    public short Range;
    public short SecondarySkillVNum;
    public short SkillChance;
    public short SkillType;
    public short VNUM;
    public short TargetRange;
    public short TargetType;
    public short Type;
    public short UpgradeSkill;
    public short UpgradeType;
    public String NameID;
    public Boolean IsOnCooldown = false;
    public Skill(){}
    
    public String toString()
    {
    	String ret = "";
    	ret+=this.Name+"\n";
    	ret+="	VNUM:"+this.VNUM+"\n";
    	ret+="	MP_Cost:"+this.MpCost+"\n";
    	return ret;
    }
}
