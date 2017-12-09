package nostale.resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import nostale.data.Item;

public class LoadItems {

	public LoadItems() {

	}

	public static Item[] loadItems() throws Exception {
		// resources/monster.dat
		// resources/names/cz/item.txt

		ArrayList<Item> items = new ArrayList<Item>();
		String line;
		try (InputStream fis = new FileInputStream("resources/Item.dat");
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {

			Item item = new Item();

		}



		return items.toArray(new Item[0]);

	}

}
