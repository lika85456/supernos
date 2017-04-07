package nostale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class Logger {
	public static String logged = "";
    public static void log(LogType t, String l){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		if(l.split(" ")[0].equals("mv")) return;
    	if(t==LogType.Receive){
    		logged+=("[RECEIVED | "+dateFormat.format(date)+"]"+l)+"\n";
    	}
    	else{
    		logged+=("[SEND | "+dateFormat.format(date)+"]"+l)+"\n";
    	}
    }

}
