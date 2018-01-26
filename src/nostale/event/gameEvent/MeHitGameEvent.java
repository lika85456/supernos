package nostale.event.gameEvent;

import nostale.data.MonsterMapInstance;
import nostale.event.GameEvent;

public class MeHitGameEvent extends GameEvent{
	public MonsterMapInstance mob;
	public int damage;
	
	
	public MeHitGameEvent(MonsterMapInstance mob,int damage)
	{
		this.mob = mob;
		this.damage = damage;
	}
}
