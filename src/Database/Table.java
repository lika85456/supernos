package Database;

import java.util.ArrayList;

public class Table {
	public ArrayList<String[]> data;

	public Table() {
		data = new ArrayList<String[]>();
	}

	public Table(ArrayList<String[]> data) {
		this.data = data;
	}

	public int add(String[] toAdd) {
		data.add(toAdd);
		return data.size() - 1;
	}

	public String dataToString() {
		String toReturn = "";
		for (int x = 0; x < data.size(); x++) {
			for (int y = 0; y < data.get(0).length; y++) {
				toReturn += data.get(x)[y] + "|";
			}
			toReturn += "\n";
		}
		return toReturn;

	}

	public String[] getById(int id) {
		return data.get(id);
	}

	public static Table dataFromString(String str) {
		if (str == null || str.equals("")) {
			return new Table(new ArrayList<String[]>());
		}
		ArrayList<String[]> data = new ArrayList<String[]>();
		String[] splitedX = str.split("\\n");
		for (int x = 0; x < splitedX.length; x++) {
			String[] splitedY = splitedX[x].split("\\|");
			data.add(new String[splitedY.length]);
			for (int y = 0; y < splitedY.length - 1; y++) {
				data.get(x)[y] = splitedY[y];
			}
		}

		return new Table(data);
	}
}
