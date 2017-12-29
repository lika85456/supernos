package nostale.data;

public class AccountData {
	public String name;
	public String nickname;
	public String password;
	public int Server;
	public int Channel;
	public int Character;
	public CServer Nation;
	
	@Override
	public String toString()
	{
		String toRet = "";
		toRet+=name+"\n";
		toRet+=nickname+"\n";
		toRet+=password+"\n";
		toRet+=Server+"\n";
		toRet+=Channel+"\n";
		toRet+=Character+"\n";
		toRet+=Nation.name()+"\n";
		return toRet;
	}
	
	public static AccountData fromString(String[] data)
	{
		AccountData toRet = new AccountData();
		toRet.name = data[0];
		toRet.nickname = data[1];
		toRet.password = data[2];
		toRet.Server = Integer.parseInt(data[3]);
		toRet.Channel = Integer.parseInt(data[4]);
		toRet.Character = Integer.parseInt(data[5]);
		toRet.Nation = CServer.valueOf(data[6]);
		return toRet;
	}
}
