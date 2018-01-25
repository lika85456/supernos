package nostale;

import java.util.Arrays;
import java.util.HashMap;

import nostale.data.AccountData;
import nostale.resources.FileLoader;

public class Config {
	//md5(nostalex.dat)+md5(nostale.dat)
	public static String HASH = "639A66B66F59C9865459AD7BD7FE748E6E25A9C17BF35FD8F7752FF9C19D0823";
	public static String version = "0.9.3.3081";
	
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
