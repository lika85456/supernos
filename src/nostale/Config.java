package nostale;

import java.util.Arrays;
import java.util.HashMap;

import nostale.data.AccountData;
import nostale.resources.FileLoader;

public class Config {
	
	public static String HASH = "12972B86463CB2323B1B3CE265C41BD2D1A8D3C8D4EA07DB5BA72625EDDFC94E";
	public static String version = "0.9.3.3082";
	
	public static void load()
	{
		String[] file = FileLoader.loadFile("config.ini");
		for(int i = 0;i<file.length;i++)
		{
			String line = file[i];
			String[] currentLine = line.split("\\s+");
			switch(currentLine[0])
			{
			//TODO add some config.ini params
			}
		}
	}
	
	public static HashMap<String,AccountData> loadAccounts()
	{
		HashMap<String,AccountData> toRet = new HashMap<String,AccountData>();
		String[] file = FileLoader.loadFile("accounts");
		for(int i = 0;i<file.length-1;i+=7)
		{
			AccountData n = AccountData.fromString(Arrays.copyOfRange(file, i, i+7));
			toRet.put(n.name,n);
		}
		return toRet;
	}
}
