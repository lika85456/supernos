package nostale.handler;

import java.util.HashMap;

import nostale.data.GameData;
import nostale.data.MapCharacterInstance;
import nostale.data.MapInstance;
import nostale.data.MapItemInstance;
import nostale.data.NpcMonster;
import nostale.data.NpcMonsterInstance;
import nostale.data.Portal;
import nostale.gameobject.Player;
import nostale.handler.interfaces.IHandler;
import nostale.handler.interfaces.IMapDataHandler;
import nostale.packet.Packet;
import nostale.resources.Resources;
import nostale.util.Pos;

public class MapDataHandler extends Handler implements IMapDataHandler,IHandler{
	public MapDataHandler(Player p) {
		super(p);
	}

	@Override
	public void parsePacket(Packet packet) {

		MapInstance tMap = GameData.maps.get(player.mapId);
		switch (packet.name) {
		case "c_map":
			try {

				MapInstance temp = new nostale.data.MapInstance();
				temp.load(packet.getIntParameter(2));
				player.mapId = GameData.addMap(temp);
				onMapChange(temp);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		case "lev":
			// lev {Level} {LevelXp} {JobLevel} {JobXP} {XPLoad()} {JobLoad}
			// {Reput} {GetCP()} {HeroXp} {HeroLevel} {HeroXPLoad()}";
			player.Level = (byte) (packet.getIntParameter(1));
			player.LevelXp = packet.getIntParameter(2);
			player.JobLevel = (byte) packet.getIntParameter(3);
			player.JobLevelXp = packet.getIntParameter(4);
			player.LevelMaxXP = Long.parseLong(packet.getParameter(5));
			player.JobMaxXP = Long.parseLong(packet.getParameter(6));
			break;
		case "at":
			player.id = packet.getIntParameter(1);
			player.pos = new Pos(packet.getIntParameter(3), packet.getIntParameter(4));
			break;

		case "cond":
			player.Speed = packet.getIntParameter(5);
			break;

		case "sc_p_stc":
			player.send(new Packet("npinfo 0"));
			break;

		case "mv":
			if (packet.getIntParameter(1) == 3) {
				int id = packet.getIntParameter(2);
				Pos pos = new Pos(packet.getIntParameter(3), packet.getIntParameter(4));
				NpcMonsterInstance mob = tMap.Mobs.get(id);
				if (mob != null && mob.Name == null) {
					player.send(new Packet("ncif 3 " + id));
				}
				if (mob == null) {
					mob = new NpcMonsterInstance();
				}
				mob.id = id;
				mob.Pos = pos;
				tMap.Mobs.put(mob.id, mob);

			} else if (packet.getIntParameter(1) == 1) {
				int id = packet.getIntParameter(2);
				MapCharacterInstance ch = tMap.Players.get(id);
				if (ch == null) {
					ch = new MapCharacterInstance();
				}
				ch.pos = new Pos(packet.getIntParameter(3), packet.getIntParameter(4));
				ch.id = id;
				tMap.Players.put(id, ch);
			}
			break;

		case "in":
			if (packet.getIntParameter(1) == 1) {// Character move
													// TODO missing hp and
													// mp!!!!
				MapCharacterInstance tChar;
				try {
					tChar = tMap.Players.get(packet.getIntParameter(4));
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
					tMap.Players.put((int) tChar.id, tChar);
				onPlayerIn(tChar);
			} else if (packet.getParameter(1).equals("3")) {// Mob move
				NpcMonsterInstance t = tMap.Mobs.get(packet.getIntParameter(3));
				if (t == null)
					t = new NpcMonsterInstance();
				t.Pos = new Pos(packet.getIntParameter(4), packet.getIntParameter(5));
				t.NpcMonsterVNum = (short) packet.getIntParameter(2);
				t.id = packet.getIntParameter(3);
				t.HP = t.MaxHP * (packet.getIntParameter(7) / 100);
				t.MP = t.MaxMP * (packet.getIntParameter(7) / 100);
				NpcMonster mt = Resources.getMob((int) t.NpcMonsterVNum);
				if (mt != null)
					t.Name = mt.Name;
				tMap.Mobs.put(t.id, t);

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

			tMap.Items.put(i.id, i);
			break;

		case "get":
			try {
				tMap.Items.remove(packet.getIntParameter(3));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case "out":
			try {
				if (packet.getIntParameter(1) == 1)// Player removed
				{
					tMap.Players.remove(packet.getIntParameter(2));

				} else if (packet.getIntParameter(1) == 9)// Item removed
				{

					tMap.Items.remove(packet.getIntParameter(2));
				}
			} catch (Exception e) {
			}
			break;

		case "st":
			int packetketType = packet.getIntParameter(1);
			if (packetketType == 1) {

				MapCharacterInstance ch = tMap.Players.get(packet.getIntParameter(2));
				if (ch == null)
					ch = new MapCharacterInstance();
				ch.Level = (byte) packet.getIntParameter(3);
				ch.HP = packet.getIntParameter(7);
				ch.HP = packet.getIntParameter(8);
				ch.MaxHP = ch.HP / packet.getIntParameter(5) * 100;
				ch.MaxMP = ch.MP / packet.getIntParameter(6) * 100;
				tMap.Players.put((int) ch.id, ch);
			} else if (packetketType == 3) {
				NpcMonsterInstance m = tMap.Mobs.get(packet.getIntParameter(2));
				if (m == null) {
					m = new NpcMonsterInstance();
				}
				m.id = packet.getIntParameter(2);
				m.Level = (short) packet.getIntParameter(3);
				m.HP = packet.getIntParameter(7);
				m.MP = packet.getIntParameter(8);
				m.MaxHP = m.HP / packet.getIntParameter(5) * 100;
				m.MaxMP = m.MP / packet.getIntParameter(6) * 100;
				tMap.Mobs.put(m.id, m);
			}

			break;

		case "gp":
			Portal pop = new Portal();
			pop.pos = new Pos(packet.getIntParameter(1), packet.getIntParameter(2));
			pop.MapID = packet.getIntParameter(3);
			pop.Type = packet.getIntParameter(4);
			pop.isEnabled = packet.getIntParameter(6) == 1;
			tMap.Portals.put(pop.pos.x * pop.pos.y, pop);
			break;

		case "mapout":
			player.send(new Packet("c_close"));
			player.send(new Packet("f_stash_end"));
			tMap = new MapInstance();
			tMap.Players = new HashMap<Integer, MapCharacterInstance>();
			tMap.Mobs = new HashMap<Integer, NpcMonsterInstance>();
			tMap.Items = new HashMap<Integer, MapItemInstance>();
			tMap.Portals = new HashMap<Integer, Portal>();
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
				tMap.Players.get(packet.getIntParameter(2)).Direction = (byte) packet.getIntParameter(3);
			} catch (Exception e) {
			}
			break;

		case "rc":
			// rc 1 {CharacterId} {characterHealth} 0
			tMap.Players.get(packet.getIntParameter(2)).HP = packet.getIntParameter(3);
			break;

		case "eff_ob":
			// eff_ob -1 -1 0 4269 - when i die
			dead();
			break;

		}
		if (tMap != null)
			GameData.maps.put(tMap.id, tMap);

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
