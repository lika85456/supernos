package nostale.packet.receive;

import nostale.data.Skill;
import nostale.packet.Packet;
import nostale.resources.Resources;

//Sent at start of game or if skills changed. - list of skills
public class SkiPacket extends Packet{
	public int base;
	public Skill[] generatedSkills;
	public SkiPacket(String str) {
		super(str);
		base = Integer.parseInt(parameters[1]);
		this.generatedSkills = new Skill[parameters.length-2];
		for(int i = 2;i<parameters.length;i++)
		{
			this.generatedSkills[i] = Resources.getSkill(Integer.parseInt(parameters[i]));
		}
	}
	
}
