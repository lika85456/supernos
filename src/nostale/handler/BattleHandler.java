package nostale.handler;

import nostale.Statistics;
import nostale.data.GameData;
import nostale.data.MapItemInstance;
import nostale.data.MonsterMapInstance;
import nostale.data.SkillData;
import nostale.domain.UserType;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IBattleHandler;
import nostale.packet.Packet;
import nostale.packet.receive.SkiPacket;
import nostale.packet.receive.SuPacket;
import nostale.resources.Resources;

public class BattleHandler extends Handler implements IBattleHandler {
	private SkillData lastSkillRequest = null;
	private MonsterMapInstance target = null;

	public BattleHandler(Player p) {
		super(p);
	}

	@Override
	public void parsePacket(Packet p) {
		switch (p.name) {
		case "ski":
			SkiPacket temp = new SkiPacket(p.packetString);
			for (SkillData s : temp.generatedSkills) {
				player.skills.put((int) s.SkillVNum, s);
			}
			break;
		case "sr":
			lastSkillRequest = null; // Skill succesfully reseted
			break;
		case "cancel": // casting spell canceled (lastSkillRequest)
			lastSkillRequest = null;
			break;
		case "su":
			SuPacket tempSuPacket = new SuPacket(p.packetString);
			player.send(new Packet("ncif " + tempSuPacket.attackerType.getValue() + " " + tempSuPacket.attackerId));
			player.send(new Packet("ncif " + tempSuPacket.attackedType.getValue() + " " + tempSuPacket.attackedId));
			if (tempSuPacket.isAlive == false && tempSuPacket.attackedType == UserType.Monster) {
				GameData.maps.get(player.mapId).Mobs.remove(tempSuPacket.attackedId);
			}
			if (tempSuPacket.isAlive == false && tempSuPacket.attackedId == target.id) {
				onTargetDie();
				target = null;
			}
			if (tempSuPacket.attackerId != player.id || tempSuPacket.attackedId != player.id
					|| tempSuPacket.attackedId != target.id) {
				return;
			}
			if (tempSuPacket.attackedType == UserType.Player && tempSuPacket.attackedId == player.id) {
				player.HP -= tempSuPacket.damage;
				if (tempSuPacket.attackerType == UserType.Monster)
					onMeGettingHit(GameData.maps.get(player.mapId).Mobs.get(tempSuPacket.attackerId),
							tempSuPacket.damage);
			}

			if (tempSuPacket.attackerId == player.id && tempSuPacket.attackedId == target.id) {
				// me attacking target
				player.skills.get(tempSuPacket.skillVNum).IsOnCooldown = true;
				onSkillHit(tempSuPacket.damage);
			}
			break;
		}
	}

	@Override
	public void useSkill(SkillData skill) {
		if (this.lastSkillRequest != null) {
			this.lastSkillRequest = skill;
			player.send(
					new Packet("u_s " + skill.CastId + " 3 " + target.id + " " + player.pos.x + " " + player.pos.y));
		}

	}

	@Override
	public void setTarget(MonsterMapInstance target) {
		this.target = target;

	}

	@Override
	public MonsterMapInstance getTarget() {
		return target;
	}

	@Override
	public void onMeGettingHit(MonsterMapInstance mob, int damage) {
		Statistics.dmgTaked += damage;

	}

	@Override
	public void onTargetDie() {
		Statistics.mobsKilled++;
	}

	@Override
	public void pickUpItem(MapItemInstance item) {
	}

	@Override
	public void onItemDrop(MapItemInstance item) {
	}

	@Override
	public void onSkillHit(int damage) {
		Statistics.dmgDealed += damage;

	}

}
