package nostale.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import Database.Database;

public class Log {
	private static String log = "";
	private static int logCall = 0;
	private static final int callsToSave = 50;
	public static Boolean debug = false;
	public static String getLog() {
		return log;
	}

	public static void log(String type, String content) {
		logCall++;
		String tLog = Log.getCurrentTimeStamp() + " " + type.toUpperCase() + "->" + content + "\n";
		log += tLog;
		if(debug)
			System.out.println(tLog);
		if (logCall > callsToSave) {
			logCall = 0;
			save();
		}
	}

	public static void save() {
		Database.save("log", Database.load("log") + Log.log);
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
}
