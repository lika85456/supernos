package nostale.event.gameEvent;

import nostale.data.SkillData;
import nostale.event.GameEvent;

public class SkillHitGameEvent extends GameEvent{
	
	public SkillData skill;
	public int damage;
	
	public SkillHitGameEvent(SkillData skill,int damage)
	{
		this.skill = skill;
		this.damage = damage;
	}
}
