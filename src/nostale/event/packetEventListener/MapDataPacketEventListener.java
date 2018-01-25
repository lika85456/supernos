package nostale.event.packetEventListener;

import nostale.data.MapCharacterInstance;
import nostale.data.MapItemInstance;
import nostale.data.MonsterData;
import nostale.data.MonsterMapInstance;
import nostale.data.Portal;
import nostale.domain.UserType;
import nostale.event.PacketEventListener;
import nostale.gameobject.Player;
import nostale.packet.Packet;
import nostale.packet.receive.InPacket;
import nostale.packet.receive.MvPacket;
import nostale.resources.Resources;
import nostale.util.Pos;

public class MapDataPacketEventListener extends PacketEventListener{
	public MapDataPacketEventListener(Player p) {
		super(p);
	}
	
	@Override
	public void packetCall(Packet packet)
	{
		switch(packet.name)
		{
		case "mv":
			MvPacket mvPacket = new MvPacket(packet.packetString);
			if(mvPacket.userType==UserType.Player)
			{
				if(mvPacket.userId==player.id)
				{
					System.out.println("ERROL PlayerDataPacketEventListener:92 - not implemented exception");				
					return;
				}
				try{
					MapCharacterInstance tempP = player.map.Players.get(mvPacket.userId);
					tempP.pos = mvPacket.pos;
					tempP.Speed = mvPacket.speed;
				}
				catch(Exception e)
				{}
				
			}
			else if(mvPacket.userType==UserType.Monster)
			{
				try{
					MonsterMapInstance monsterInstance = player.map.Mobs.get(mvPacket.userId);
					monsterInstance.Pos = mvPacket.pos;
					monsterInstance.Speed = mvPacket.speed;
				}
				catch(Exception e){
					
				}

			}
			//TODO add npc move
			break;

		case "in":
			InPacket inPacket = new InPacket(packet.packetString);			
			if(inPacket.userType==UserType.Player)
			{
				if(inPacket.userId==player.id)
				{
					System.out.println("ERROL PlayerDataPacketEventListener:115 - not implemented exception");				
					return;
				}
				MapCharacterInstance tChar;
				try {
					tChar = player.map.Players.get(packet.getIntParameter(4));
				} catch (Exception e) {
					tChar = new MapCharacterInstance();
				}
				if (tChar == null) {
					tChar = new MapCharacterInstance();
				}
				tChar.Name = inPacket.name;
				tChar.pos = inPacket.pos;
				tChar.Direction = inPacket.direction;
				tChar.Authority = inPacket.authority;
				tChar.IsSitting = inPacket.isSitting;
				tChar.IsInvisible = inPacket.isInvisible;
				tChar.Level = inPacket.level;
				tChar.id = inPacket.userId;
				//if (tChar.id != 0)
				player.map.Players.put((int) tChar.id, tChar);
				
				player.log("MapData", "Player on map: "+tChar.Name+" "+tChar.Level+"lvl, "+tChar.Authority+" authority");
			} else if (inPacket.userType==UserType.Monster) {// Mob move
				MonsterMapInstance t = player.map.Mobs.get(packet.getIntParameter(3));
				if (t == null)
					t = new MonsterMapInstance();
				t.Pos = new Pos(packet.getIntParameter(4), packet.getIntParameter(5));
				t.VNum = (short) packet.getIntParameter(2);
				t.id = packet.getIntParameter(3);
				MonsterData mt = Resources.getMob(t.VNum);
				if (mt != null)
				{
					t.HP = mt.MaxHP * (packet.getIntParameter(7) / 100);
					t.MP = mt.MaxMP * (packet.getIntParameter(7) / 100);
				}
					
				
				
				player.map.Mobs.put(t.id, t);
			}
			

			break;
		case "drop":
			MapItemInstance i = new MapItemInstance();
			i.VNUM = (short) packet.getIntParameter(1);
			i.id = packet.getIntParameter(2);
			i.Pos = new Pos(packet.getIntParameter(3), packet.getIntParameter(4));
			i.Amount = packet.getIntParameter(5);
			i.OwnerID = packet.getIntParameter(7);

			player.map.Items.put(i.id, i);
			try{
				
			player.log("Item Drop", "Item dropped: "+i.Pos.toString()+" Name: "+Resources.getItem(i.VNUM).Name+" Owner: "+i.OwnerID);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
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
				MonsterMapInstance m = player.map.Mobs.get(packet.getIntParameter(2));
				if (m == null) {
					m = new MonsterMapInstance();
				}
				m.id = packet.getIntParameter(2);
				m.Level = (short) packet.getIntParameter(3);
				m.HP = packet.getIntParameter(7);//TODO CHECK
				m.MP = packet.getIntParameter(8);
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
		case "rest":
			// rest 1 374541 0
			if (packet.getIntParameter(2) == player.id) {
				player.IsSitting = packet.getIntParameter(3) == 1;//TODO make it for all
			}
			break;

		case "dir":
			// dir 1 {CharacterId} {Direction}
			try {//TODO MAke it for player
				player.map.Players.get(packet.getIntParameter(2)).Direction = (byte) packet.getIntParameter(3);
			} catch (Exception e) {
			}
			break;

		case "rc":
			// rc 1 {CharacterId} {characterHealth} 0
			try {//TODO MAke it for player
				player.map.Players.get(packet.getIntParameter(2)).HP = packet.getIntParameter(3);
			} catch (Exception e) {
			}
			
			break;
		}
	}

}
