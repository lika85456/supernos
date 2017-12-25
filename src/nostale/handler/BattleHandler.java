package nostale.handler;

import java.util.ArrayList;

import nostale.Statistics;
import nostale.data.GameData;
import nostale.data.MapItemInstance;
import nostale.data.NpcMonsterInstance;
import nostale.data.Skill;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IBattleHandler;
import nostale.packet.Packet;
import nostale.resources.Resources;

public class BattleHandler extends Handler implements IBattleHandler {
	private Skill lastSkillRequest = null;
	private NpcMonsterInstance target = null;

	public BattleHandler(Player p) {
		super(p);
	}

	public void parsePacket(Packet p) {
		switch (p.name) {
		case "ski":
			String[] splited = p.packetString.split(" ");
			for (int i = 3; i < splited.length; i++) {
				if (splited[i].equals("ski"))
					continue;
				
				player.skills.put(Integer.parseInt(splited[i]),Resources.getSkill(Integer.parseInt(splited[i])));

			}
			break;
		case "sr":
			lastSkillRequest = null; // Skill succesfully reseted
			break;
		case "cancel": // casting spell canceled (lastSkillRequest)
			lastSkillRequest = null;
			break;
		case "su":
			Packet pac = p;
			String[] splitedd = p.packetString.split(" ");
			// Somebody attacked, get info from that
			// su 1 {hitRequest.Session.Character.CharacterId} 3 {MapMonsterId}
			// {hitRequest.Skill.SkillVNum} {hitRequest.Skill.Cooldown}
			// {hitRequest.Skill.AttackAnimation} {hitRequest.SkillEffect}
			// {hitRequest.Session.Character.PositionX}
			// {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)}
			// {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)} {damage}
			// {hitmode} {hitRequest.Skill.SkillType - 1}
			if (splitedd[1].equals("3")) // Monster attacking someone
			{
				if (pac.getIntParameter(2) != player.id) {
					return;
				} // If it isnt me who cares?
				onMeGettingHit(GameData.maps.get(player.mapId).Mobs.get(pac.getIntParameter(4)));
				player.HP -= pac.getIntParameter(13);
				player.send(new Packet("ncif 3 " + pac.getIntParameter(4)));
			} else if (splitedd[1].equals("1")) // Someone attacking something
			{
				if (pac.getIntParameter(2) == player.id) {
					player.skills.get(pac.getIntParameter(5)).IsOnCooldown = true;
				}
				// su 1 {hitRequest.Session.Character.CharacterId} 3
				// {MapMonsterId} {hitRequest.Skill.SkillVNum}
				// {hitRequest.Skill.Cooldown} {hitRequest.SkillCombo.Animation}
				// {hitRequest.SkillCombo.Effect}
				// {hitRequest.Session.Character.PositionX}
				// {hitRequest.Session.Character.PositionY} {(IsAlive ? 1 : 0)}
				// {(int)((float)CurrentHp / (float)Monster.MaxHP * 100)}
				// {damage} {hitmode} {hitRequest.Skill.SkillType - 1}
				// MapMobInstance m = n.GameData.get(pac.getInt(4));
				// TODO dodìlat!!!
				if (pac.getIntParameter(11) == 0) // Monster died
				{
					GameData.maps.get(player.mapId).Mobs.remove(pac.getIntParameter(4));
					if (pac.getIntParameter(2) == player.id)// I killed it
					{
						if (target.id == pac.getIntParameter(4)) {
							Statistics.mobsKilled++;
							onTargetDie();
							target = null;
						}
					}
				}

			}
			break;
		}

	}

	@Override
	public void useSkill(Skill skill) {
		if (this.lastSkillRequest != null) {
			this.lastSkillRequest = skill;
			player.send(
					new Packet("u_s " + skill.CastId + " 3 " + target.id + " " + player.pos.x + " " + player.pos.y));
		}

	}

	@Override
	public void setTarget(NpcMonsterInstance target) {
		this.target = target;

	}

	@Override
	public NpcMonsterInstance getTarget() {
		return target;
	}

	@Override
	public void onMeGettingHit(NpcMonsterInstance mob) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTargetDie() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pickUpItem(MapItemInstance item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemDrop(MapItemInstance item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSkillHit() {
		// TODO Auto-generated method stub

	}

}
