package nostale.login;
//Server struct
public class Server{
  public String ip = "";
  public int port = 0;

  public Server(String i, int p)
  {
    this.ip = i;
    this.port = p;
  }
  public Server(CServer t)
  {
      Server s = new Server("",1);
              switch(t)
              {
                case DE:
                s = ServerList.DE;
                break;
                case EN:
                s = ServerList.EN;
                break;
                case FR:
                s = ServerList.FR;
                break;
                case IT:
                s = ServerList.IT;
                break;
                case PL:
                s = ServerList.PL;
                break;
                case ES:
                s = ServerList.ES;
                break;
                case CZ:
                s = ServerList.CZ;
                break;
                case RU:
                s = ServerList.RU;
                break;
                case TR:
                s = ServerList.TR;
                break;
              }  
              this.ip = s.ip;
              this.port = s.port;
  }
  
}

