package nostale.event.packetEventListener;

import java.util.HashMap;

import nostale.data.SkillData;
import nostale.domain.UserType;
import nostale.event.PacketEventListener;
import nostale.event.gameEvent.MeHitGameEvent;
import nostale.event.gameEvent.SkillHitGameEvent;
import nostale.event.gameEvent.IKilledAMobGameEvent;
import nostale.gameobject.Player;
import nostale.handler.BattleHandler;
import nostale.packet.Packet;
import nostale.packet.receive.SkiPacket;
import nostale.packet.receive.SuPacket;
import nostale.resources.Resources;

public class BattlePacketEventListener extends PacketEventListener{
	
	public BattleHandler battleHandler;
	public BattlePacketEventListener(Player p,BattleHandler battleHandler) {
		super(p);
		this.battleHandler = battleHandler;
	}
	
	public void packetCall(Packet p)
	{
		switch (p.name) {
		case "ski":
			battleHandler.skills = new HashMap<Short,SkillData>();
			SkiPacket temp = new SkiPacket(p.packetString);
			for (SkillData s : temp.generatedSkills) {
				battleHandler.skills.put(s.SkillVNum, s);
			}
			break;
		case "sr":
			battleHandler.lastSkillRequest = null; // Skill succesfully reseted
			break;
		case "cancel": // casting spell canceled (lastSkillRequest)
			battleHandler.lastSkillRequest = null;
			break;
		case "su":
			SuPacket tempSuPacket = new SuPacket(p.packetString);
			player.sendPacket(new Packet("ncif " + tempSuPacket.attackerType.getValue() + " " + tempSuPacket.attackerId));
			player.sendPacket(new Packet("ncif " + tempSuPacket.attackedType.getValue() + " " + tempSuPacket.attackedId));
			if (tempSuPacket.isAlive == false && tempSuPacket.attackedType == UserType.Monster) {
				player.map.Mobs.remove(tempSuPacket.attackedId);
			}
			if (tempSuPacket.isAlive == false && tempSuPacket.attackerId == player.id) {
				player.addGameEvent(new IKilledAMobGameEvent());
				if(battleHandler.target.id==tempSuPacket.attackedId)
					battleHandler.target = null;
			}
			if (tempSuPacket.attackerId != player.id || tempSuPacket.attackedId != player.id
					|| tempSuPacket.attackedId != battleHandler.target.id) {
				return;
			}
			if (tempSuPacket.attackedType == UserType.Player && tempSuPacket.attackedId == player.id) {
				player.HP -= tempSuPacket.damage;
				if (tempSuPacket.attackerType == UserType.Monster)
					player.addGameEvent(new MeHitGameEvent(player.map.Mobs.get(tempSuPacket.attackerId),
							tempSuPacket.damage));
			}

			if (tempSuPacket.attackerId == player.id) {
				// me attacking target
				battleHandler.skills.get(tempSuPacket.skillVNum).IsOnCooldown = true;
				player.addGameEvent(new SkillHitGameEvent(Resources.getSkill((int)tempSuPacket.skillVNum),tempSuPacket.damage));
			}
			break;
		}
	}

}
