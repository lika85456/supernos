package nostale.domain;

public class LoginFailType {
	public static final byte OldClient = 1;
	public static final byte UnhandledError = 2;
	public static final byte Maintenance = 3;
	public static final byte AlreadyConnected = 4;
	public static final byte AccountOrPasswordWrong = 5;
	public static final byte CantConnect = 6;
	public static final byte Banned = 7;
	public static final byte WrongCountry = 8;
	public static final byte WrongCaps = 9;
	public int type = 0;
	public LoginFailType(int i)
	{
		type=i;
	}
}
