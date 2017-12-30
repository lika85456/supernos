package nostale.packet.receive;

import nostale.domain.UserType;
import nostale.packet.Packet;
import nostale.util.Pos;

public class MvPacket extends Packet{
	public UserType userType;
	public int userId;
	public int speed;
	public Pos pos;
	public MvPacket(String str) {
		super(str);
		/*
        [PacketIndex(0)]
        public byte MoveType { get; set; }
        [PacketIndex(1)]
        public long CharacterId { get; set; }
        [PacketIndex(2)]
        public short MapX { get; set; }
        [PacketIndex(3)]
        public short MapY { get; set; }
        [PacketIndex(4)]
        public byte Speed { get; set; }
		 */
		
		byte userTypeByte = (byte)getIntParameter(1);
		if(userTypeByte==1) userType = UserType.Player;
		if(userTypeByte==2) userType = UserType.Npc;
		if(userTypeByte==3) userType = UserType.Monster;
		if(userTypeByte==9) userType = UserType.Object;
		userId = getIntParameter(2);
		pos = new Pos(getIntParameter(3),getIntParameter(4));
		speed=  getIntParameter(5);
	}

}
