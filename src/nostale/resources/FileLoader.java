package nostale.resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileLoader {
	public static String[] loadFile(String path) {
		String line;
		ArrayList<String> lines = new ArrayList<String>();
		try (InputStream fis = new FileInputStream(path);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines.toArray(new String[0]);
	}
}
