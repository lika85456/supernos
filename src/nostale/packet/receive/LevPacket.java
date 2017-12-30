package nostale.packet.receive;

import nostale.packet.Packet;

//Level info packet
public class LevPacket extends Packet{
	// lev {Level} {LevelXp} {JobLevel} {JobXP} {XPLoad()} {JobLoad}
				// {Reput} {GetCP()} {HeroXp} {HeroLevel} {HeroXPLoad()}";
	public byte level;
	public int levelXp;
	public byte jobLevel;
	public int jobXp;
	public int xpLoad;
	public int jobLoad;
	public int reput;
	public int cp; //skill points
	public int heroXp;
	public byte heroLevel;
	public int heroXpLoad;
	public LevPacket(String str) {
		super(str);
		level = (byte)getIntParameter(1);
		levelXp = getIntParameter(2);
		jobLevel = (byte)getIntParameter(3);
		jobXp = getIntParameter(4);
		xpLoad = getIntParameter(5);
		jobLoad = getIntParameter(6);
		reput = getIntParameter(7);
		cp = getIntParameter(8);
		heroXp = getIntParameter(9);
		heroLevel = (byte)getIntParameter(10);
		heroXpLoad = getIntParameter(11);
	}

}
