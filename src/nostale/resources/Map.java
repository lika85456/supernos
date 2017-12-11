package nostale.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Map {
	public File file;
	public RandomAccessFile r;

	public int id;
	public String NameID;
	public int width, height;
	public String Name = "";
	public Boolean[] posi;

	public Map() {
	}

	public void load(int id) throws Exception {
		this.id = id;
		String line;
		try (InputStream fis = new FileInputStream("resources/mapID.txt");
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {

			while ((line = br.readLine()) != null) {
				if (line.contains("zts"))
					if (Integer.parseInt(line.split(" ")[0]) == id) {
						this.NameID = line.split(" ")[4];
					}
			}
		} catch (Exception e2134) {
			e2134.printStackTrace();
		}

		file = new File("resources/maps/" + id);
		r = new RandomAccessFile(file, "r");
		width = r.read() | (r.read() << 8);
		r.seek(2);
		height = r.read() | (r.read() << 8);
		getName();
		ArrayList<Boolean> tL = new ArrayList<Boolean>();
		for (int pos = 0; pos < r.length() - 4; pos++) {

			r.seek(4 + pos);
			byte b = r.readByte();
			if (b == 0) // walkable
				tL.add(true);
			else
				tL.add(false);

		}
		this.posi = tL.toArray(new Boolean[0]);
		r.close();

	}

	// 1 = can, 0=cant
	public Boolean canWalkHere(int x, int y) {
		if (x < 1 || y < 1 || x > width || y > height)
			return false;
		// 0 = walkable
		try {
			int pos = x + ((y - 1) * width);

			return posi[pos] == true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void getName() throws Exception {
		if (this.NameID == null)
			return;
		String line;
		try (InputStream fis = new FileInputStream("resources/names/cz/map.txt");
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				if (line.contains(this.NameID)) {
					this.Name = line.split("\\s+")[1];
					break;
				}
			}
		}
	}

}
