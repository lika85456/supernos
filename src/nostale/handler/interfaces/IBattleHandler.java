package nostale.handler.interfaces;

import nostale.data.MapItemInstance;
import nostale.data.NpcMonsterInstance;
import nostale.data.Skill;

public interface IBattleHandler {
	public void useSkill(Skill skill);
	public void setTarget(NpcMonsterInstance target);
	public NpcMonsterInstance getTarget();
	public void onMeGettingHit(NpcMonsterInstance mob);
	public void onTargetDie();
	public void pickUpItem(MapItemInstance item);
	public void onItemDrop(MapItemInstance item);
	public void onSkillHit();
}
