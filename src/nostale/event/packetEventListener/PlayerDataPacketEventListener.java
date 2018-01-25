package nostale.event.packetEventListener;

import java.util.HashMap;

import nostale.data.MapCharacterInstance;
import nostale.data.MapInstance;
import nostale.data.MapItemInstance;
import nostale.data.MonsterData;
import nostale.data.MonsterMapInstance;
import nostale.data.Portal;
import nostale.domain.UserType;
import nostale.event.PacketEvent;
import nostale.event.PacketEventListener;
import nostale.gameobject.Player;
import nostale.packet.Packet;
import nostale.packet.PacketType;
import nostale.packet.receive.AtPacket;
import nostale.packet.receive.CMapPacket;
import nostale.packet.receive.CondPacket;
import nostale.packet.receive.InPacket;
import nostale.packet.receive.LevPacket;
import nostale.packet.receive.MvPacket;
import nostale.resources.Resources;
import nostale.util.Pos;

public class PlayerDataPacketEventListener extends PacketEventListener{
	
	public PlayerDataPacketEventListener(Player p)
	{
		super(p);
	}
	
	@Override
	public void packetCall(Packet packet)
	{
		switch(packet.name)
		{
		case "c_map":
			CMapPacket cmapPacket = new CMapPacket(packet.packetString);
			MapInstance temp = new nostale.data.MapInstance();
			try {
				temp.load(cmapPacket.mapId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			player.mapId = cmapPacket.mapId;
			player.log("MapData", "Current map is: "+temp.Name);
			player.map = temp;
			break;
		case "lev":
			LevPacket levPacket = new LevPacket(packet.packetString);
			
			player.Level = levPacket.level;
			player.LevelXp = levPacket.levelXp;
			player.JobLevel = levPacket.jobLevel;
			player.JobLevelXp = levPacket.jobXp;
			player.LevelMaxXP = levPacket.xpLoad;
			player.JobMaxXP = levPacket.jobLoad;
			break;
		case "at":
			AtPacket atPacket = new AtPacket(packet.packetString);
			player.id = atPacket.characterId;
			player.pos = atPacket.pos;
			player.mapId = atPacket.mapId;
			break;

		case "cond":
			CondPacket condPacket = new CondPacket(packet.packetString);
			player.Speed = condPacket.speed;
			break;

		case "sc_p_stc":
			Packet toSend = new Packet("npinfo 0");
			toSend.packetType = PacketType.SEND;
			player.packetEvents.add(new PacketEvent(toSend));
			break;

	

		case "stat":
			// stat {Hp} {HPLoad()} {Mp} {MPLoad()} 0 {option}
			//TODO to statPacket
			int tempHP = player.HP;
			player.HP = packet.getIntParameter(1);
			if(tempHP!=player.HP)
				player.log("Player Info", "Player HP: "+player.HP+"/"+player.MaxHP);
			player.MaxHP = packet.getIntParameter(2);
			player.MP = packet.getIntParameter(3);
			player.MaxMP = packet.getIntParameter(4);
			
			break;



		case "mapout":
			player.sendPacket(("c_close"));
			player.sendPacket(("f_stash_end"));
			player.map = new MapInstance();
			player.map.Players = new HashMap<Integer, MapCharacterInstance>();
			player.map.Mobs = new HashMap<Integer, MonsterMapInstance>();
			player.map.Items = new HashMap<Integer, MapItemInstance>();
			player.map.Portals = new HashMap<Integer, Portal>();
			player.log("!!! Player Info !!!", "MAPOUT");
			//TODO ADD MAPOUT EVENT
			break;



		case "eff_ob":
			// eff_ob -1 -1 0 4269 - when i die
			//TODO ADD DEAD EVENT
			break;

		}
		}
	}
	

