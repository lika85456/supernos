package Database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Database {
	public static void save(String file, String content) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			out.write(content.getBytes());
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String load(String file) {
		try {
			String[] lines = Files.readAllLines(Paths.get(file)).toArray(new String[0]);
			String toReturn = "";
			for (int i = 0; i < lines.length; i++) {
				toReturn += lines[i] + "\n";
			}
			return toReturn;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";

		}
	}

}
