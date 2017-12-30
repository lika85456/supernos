package nostale.packet.receive;

import nostale.domain.UserType;
import nostale.packet.Packet;

//someone took item from ground
public class GetPacket extends Packet{
		/*        
 		get 1 351883 1681934 0
 		get UserType UserId TransportId 0
        */
	public UserType userType;
	public int userId;
	public int transportId;
	
	public GetPacket(String str) {
		super(str);
		byte userTypeByte = (byte)getIntParameter(1);
		if(userTypeByte==1) userType = UserType.Player;
		if(userTypeByte==2) userType = UserType.Npc;
		if(userTypeByte==3) userType = UserType.Monster;
		if(userTypeByte==9) userType = UserType.Object;
		userId = gip(2);
		transportId = gip(3);
	}

}
