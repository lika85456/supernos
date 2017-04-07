package nostale.login;

//Game Server struct
public class GameServer{
  public String ip = "";
  public int port = 0;
  public String server;
  public String channel;
  public String usage;
  public GameServer(String i, String p,String server,String channel,String usage)
  {
    this.ip = i;
    this.port = Integer.parseInt(p);
    this.server = server;
    this.channel = channel;
    this.usage = usage;
  }
  
}