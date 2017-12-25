package nostale.handler.interfaces;

import nostale.data.GameServer;
import nostale.data.LoginCharacter;
import nostale.domain.LoginFailType;
import nostale.packet.Packet;

public interface ILoginHandler {
	public LoginFailType login();
	public void selectChannel(GameServer gameServer);
	public void parseChannels(Packet packet);
	public void onLogin(LoginFailType status, GameServer[] channels);
	public void onCharacters(LoginCharacter[] characters);
	public void selectCharacter(LoginCharacter character);
}
