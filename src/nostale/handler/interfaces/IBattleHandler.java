package nostale.handler.interfaces;

import nostale.data.MapItemInstance;
import nostale.data.MonsterMapInstance;
import nostale.data.SkillData;

public interface IBattleHandler {
	public void useSkill(SkillData skill);
	public void setTarget(MonsterMapInstance target);
	public MonsterMapInstance getTarget();
	public void onMeGettingHit(MonsterMapInstance mob, int damage);
	public void onTargetDie();
	public void pickUpItem(MapItemInstance item);
	public void onItemDrop(MapItemInstance item);
	public void onSkillHit(int damage);
}
