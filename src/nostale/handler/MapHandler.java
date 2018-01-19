package nostale.handler;

import java.util.HashMap;

import nostale.data.MapCharacterInstance;
import nostale.data.MapInstance;
import nostale.data.MapItemInstance;
import nostale.data.MonsterData;
import nostale.data.MonsterMapInstance;
import nostale.data.Portal;
import nostale.domain.UserType;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IHandler;
import nostale.handler.interfaces.IMapDataHandler;
import nostale.packet.Packet;
import nostale.packet.receive.AtPacket;
import nostale.packet.receive.CMapPacket;
import nostale.packet.receive.CondPacket;
import nostale.packet.receive.InPacket;
import nostale.packet.receive.LevPacket;
import nostale.packet.receive.MvPacket;
import nostale.resources.Resources;
import nostale.util.Pos;

public class MapHandler extends Handler implements IMapDataHandler,IHandler{
	public MapHandler(Player p) {
		super(p);
	}

	@Override
	public void parsePacket(Packet packet) {

		switch (packet.name) {
		case "c_map":
			try {
				CMapPacket cmapPacket = new CMapPacket(packet.packetString);
				player.map = new nostale.data.MapInstance();
				player.map.load(cmapPacket.mapId);
				player.mapId = cmapPacket.mapId;
				onMapChange(player.map);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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
			player.send(new Packet("npinfo 0"));
			break;

		case "mv":
			MvPacket mvPacket = new MvPacket(packet.packetString);
			if(mvPacket.userType==UserType.Player)
			{
				if(mvPacket.userId==player.id)
				{
					System.out.println("ERROL MapHandler:77 - not implemented exception");				
					return;
				}
				MapCharacterInstance tempP = player.map.Players.get(mvPacket.userId);
				tempP.pos = mvPacket.pos;
				tempP.Speed = mvPacket.speed;
				
			}
			else if(mvPacket.userType==UserType.Monster)
			{
				MonsterMapInstance monsterInstance = player.map.Mobs.get(mvPacket.userId);
				monsterInstance.Pos = mvPacket.pos;
				monsterInstance.Speed = mvPacket.speed;
			}
			//TODO add npc move
			break;

		case "in":
			InPacket inPacket = new InPacket(packet.packetString);			
			if(inPacket.userType==UserType.Player)
			{
				if(inPacket.userId==player.id)
				{
					
				}
			}
			
			if (packet.getIntParameter(1) == 1) {// Character move
													// TODO missing hp and
													// mp!!!!
				MapCharacterInstance tChar;
				try {
					tChar = player.map.Players.get(packet.getIntParameter(4));
				} catch (Exception e) {
					tChar = new MapCharacterInstance();
				}
				if (tChar == null) {
					tChar = new MapCharacterInstance();
				}
				tChar.Name = packet.getParameter(2);
				tChar.pos = new Pos(packet.getIntParameter(5), packet.getIntParameter(6));
				tChar.Direction = (byte) packet.getIntParameter(7);
				tChar.Authority = (byte) packet.getIntParameter(8);
				tChar.IsSitting = "1".equals(packet.getParameter(16));
				tChar.IsInvisible = "1".equals(packet.getParameter(28));
				tChar.Level = (byte) packet.getIntParameter(32);
				tChar.id = packet.getIntParameter(4);
				if (tChar.id != 0)
					player.map.Players.put((int) tChar.id, tChar);
				onPlayerIn(tChar);
			} else if (packet.getParameter(1).equals("3")) {// Mob move
				NpcMonsterInstance t = player.map.Mobs.get(packet.getIntParameter(3));
				if (t == null)
					t = new NpcMonsterInstance();
				t.Pos = new Pos(packet.getIntParameter(4), packet.getIntParameter(5));
				t.NpcMonsterVNum = (short) packet.getIntParameter(2);
				t.id = packet.getIntParameter(3);
				t.HP = t.MaxHP * (packet.getIntParameter(7) / 100);
				t.MP = t.MaxMP * (packet.getIntParameter(7) / 100);
				MonsterData mt = Resources.getMob((int) t.NpcMonsterVNum);
				if (mt != null)
					t.Name = mt.Name;
				player.map.Mobs.put(t.id, t);

			}
			break;

		case "stat":
			// stat {Hp} {HPLoad()} {Mp} {MPLoad()} 0 {option}
			player.HP = packet.getIntParameter(1);
			player.MaxHP = packet.getIntParameter(2);
			player.MP = packet.getIntParameter(3);
			player.MaxMP = packet.getIntParameter(4);
			break;

		case "drop":
			MapItemInstance i = new MapItemInstance();
			i.VNUM = (short) packet.getIntParameter(1);
			i.id = packet.getIntParameter(2);
			i.Pos = new Pos(packet.getIntParameter(3), packet.getIntParameter(4));
			i.Amount = packet.getIntParameter(5);
			i.OwnerID = packet.getIntParameter(7);

			player.map.Items.put(i.id, i);
			break;

		case "get":
			try {
				player.map.Items.remove(packet.getIntParameter(3));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case "out":
			try {
				if (packet.getIntParameter(1) == 1)// Player removed
				{
					player.map.Players.remove(packet.getIntParameter(2));

				} else if (packet.getIntParameter(1) == 9)// Item removed
				{

					player.map.Items.remove(packet.getIntParameter(2));
				}
			} catch (Exception e) {
			}
			break;

		case "st":
			int packetketType = packet.getIntParameter(1);
			if (packetketType == 1) {

				MapCharacterInstance ch = player.map.Players.get(packet.getIntParameter(2));
				if (ch == null)
					ch = new MapCharacterInstance();
				ch.Level = (byte) packet.getIntParameter(3);
				ch.HP = packet.getIntParameter(7);
				ch.HP = packet.getIntParameter(8);
				ch.MaxHP = ch.HP / packet.getIntParameter(5) * 100;
				ch.MaxMP = ch.MP / packet.getIntParameter(6) * 100;
				player.map.Players.put((int) ch.id, ch);
			} else if (packetketType == 3) {
				NpcMonsterInstance m = player.map.Mobs.get(packet.getIntParameter(2));
				if (m == null) {
					m = new NpcMonsterInstance();
				}
				m.id = packet.getIntParameter(2);
				m.Level = (short) packet.getIntParameter(3);
				m.HP = packet.getIntParameter(7);
				m.MP = packet.getIntParameter(8);
				m.MaxHP = m.HP / packet.getIntParameter(5) * 100;
				m.MaxMP = m.MP / packet.getIntParameter(6) * 100;
				player.map.Mobs.put(m.id, m);
			}

			break;

		case "gp":
			Portal pop = new Portal();
			pop.pos = new Pos(packet.getIntParameter(1), packet.getIntParameter(2));
			pop.MapID = packet.getIntParameter(3);
			pop.Type = packet.getIntParameter(4);
			pop.isEnabled = packet.getIntParameter(6) == 1;
			player.map.Portals.put(pop.pos.x * pop.pos.y, pop);
			break;

		case "mapout":
			player.send(new Packet("c_close"));
			player.send(new Packet("f_stash_end"));
			player.map = new MapInstance();
			player.map.Players = new HashMap<Integer, MapCharacterInstance>();
			player.map.Mobs = new HashMap<Integer, NpcMonsterInstance>();
			player.map.Items = new HashMap<Integer, MapItemInstance>();
			player.map.Portals = new HashMap<Integer, Portal>();
			mapout();
			break;

		case "rest":
			// rest 1 374541 0
			if (packet.getIntParameter(2) == player.id) {
				player.IsSitting = packet.getIntParameter(3) == 1;
			}
			break;

		case "dir":
			// dir 1 {CharacterId} {Direction}
			try {
				player.map.Players.get(packet.getIntParameter(2)).Direction = (byte) packet.getIntParameter(3);
			} catch (Exception e) {
			}
			break;

		case "rc":
			// rc 1 {CharacterId} {characterHealth} 0
			player.map.Players.get(packet.getIntParameter(2)).HP = packet.getIntParameter(3);
			break;

		case "eff_ob":
			// eff_ob -1 -1 0 4269 - when i die
			dead();
			break;

		}

	}

	@Override
	public void onMapChange(MapInstance map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerIn(MapCharacterInstance player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dead() {
		// TODO Auto-generated method stub
		
	}

}
