package group_project_p2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TEST {
	// Protected store hashmap here

	public static void main(String[] args) {
//		try {
//			readFile();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		AisleMap store = new AisleMap("Store", new IdTable<Item>(), new IdTable<Aisle>());
		

		ArrayList<String> PATH = new ArrayList<>();
		PATH.add("canned");
		
		store.addItem(new Item(10, 10, 0, "FOOD", PATH), PATH);
		

	}
	
	protected static void readFile() throws FileNotFoundException {

		File file1 = new File("src/group_project2/copy/TESTdata.txt");
		AisleMap store = new AisleMap("Store", new IdTable<Item>(), new IdTable<Aisle>());
		
		Scanner input = new Scanner(file1);
		String[]  temp = input.nextLine().split(":");
		int Existance = Integer.parseInt(temp[1]); //Don't mind this, but do keep it so the file reading works
		
		
		while (input.hasNext()) {
			String line = input.nextLine();
			int id = -1;
			int maxStock = -1;
			int realStock = -1;
			String aisleName = "";
			if (line.equals("(start aisle)")) {
				id = Integer.parseInt(input.nextLine().split(":")[1]);
				aisleName = input.nextLine().split(":")[1];
				// Create an Aisle here
				//store.getAisle(aisleName);
				
			}
			if (line.equals("---")) {
				int itemID = Integer.parseInt(input.nextLine().split(":")[1]);
				maxStock = Integer.parseInt(input.nextLine().split(":")[1]);
				realStock = Integer.parseInt(input.nextLine().split(":")[1]);
				String itemName = input.nextLine().split(":")[1];
				String stringPATH = input.nextLine().split(":")[1];
				String[] temp1 = stringPATH.split("[\\]");
				ArrayList<String> PATH = new ArrayList<>();
				for (String s : temp1) {
					PATH.add(s);
				}
				store.addItem(new Item(itemID, maxStock, realStock, itemName, PATH), PATH);
				
				// Add the item to an aisle here
				
			}
			if (line.equals("(end aisle)")) {
				
				// Aisle is finished here, add to other Aisles here if thats how it works
				
				
			}
		}
		//buildDropDowns();
		input.close();
	}

}
