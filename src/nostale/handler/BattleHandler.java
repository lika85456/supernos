package nostale.handler;

import java.util.HashMap;

import nostale.data.MonsterMapInstance;
import nostale.data.SkillData;
import nostale.event.packetEventListener.BattlePacketEventListener;
import nostale.gameobject.Player;
import nostale.packet.Packet;

public class BattleHandler extends Handler{
	public  SkillData lastSkillRequest = null;
	public MonsterMapInstance target = null;
	public HashMap<Short,SkillData> skills;
	public BattleHandler(Player p) {
		super(p);
		player.addPacketEventListener(new BattlePacketEventListener(p,this));
	}



	public void useSkill(SkillData skill) {
		if (this.lastSkillRequest != null) {
			System.out.println("Using a skill");
			this.lastSkillRequest = skill;
			player.sendPacket(
					new Packet("u_s " + skill.CastId + " 3 " + target.id + " " + player.pos.x + " " + player.pos.y));
		}

	}



}
