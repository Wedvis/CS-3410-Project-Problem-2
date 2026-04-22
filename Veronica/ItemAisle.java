package group_project_p2;

import java.util.ArrayList;

//Aisle consists of an Aisle name, id, and array of Items 
public class ItemAisle {
	private int id;
	private String name;
	protected ArrayList<Item> Aisle; 

	public ItemAisle(int id, String name) {
		this.id = id;
		this.name = name;
		Aisle = new ArrayList<Item>();
	}
	
	// Getters
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Item getItemByID(int id) {
		for (Item i : Aisle) {
			if (i.getID() == id) {
				return i;
			}
		}
		return null;
	}
	
	public Item getItemByName(String name) {
		for (Item i : Aisle) {
			if (i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	public ArrayList<Item> getAllItems() {
		return Aisle;
	}
	
	public ArrayList<Item> getTooLowStockItems() {
		ArrayList<Item> lowStock = new ArrayList<Item>();
		for (Item i : Aisle) {
			if (i.isStockToLow()) {
				lowStock.add(i);
			}
		}
		return lowStock;
	}
	
	public ArrayList<Item> getTooHighStockItems() {
		ArrayList<Item> highStock = new ArrayList<Item>();
		for (Item i : Aisle) {
			if (i.isStockToHigh()) {
				highStock.add(i);
			}
		}
		return highStock;
	}
	
	
	// Booleans to check if Item exists in Aisle
	public boolean hasItem(Item i) {
		if (Aisle.contains(i)) {
			return true;
		}
		return false;
	}
	
	public boolean hasItemByID(int id) {
		Item i = getItemByID(id);
		if (i != null) {
			return true;
		}
		return false;
	}
	
	public boolean hasItemByName(String name) {
		Item i = getItemByName(name);
		if (i != null) {
			return true;
		}
		return false;
	}
	
	// Add or Remove an Item from Aisle
	public boolean addItem(Item i) {
		if (!Aisle.contains(i)) {
			Aisle.add(i);
			return true;
		}
		return false;
	}
	
	public boolean removeItem(Item i) {
		if (Aisle.contains(i)) {
			Aisle.remove(i);
			return true;
		}
		return false;
	}
	
	
	// Get Full Stock Info on Aisle
	public String getStockInfo() {
		String msg = "Stock Info for Aisle: " + name + "\n"
				+ "----------------------\n";
		String msgStockTooLow = "\nItems of Interest: STOCK TOO LOW\n"
				+ "----------------------\n";
		
		String msgStockTooHigh = "\nItems of Interest: STOCK TOO HIGH\n"
				+ "----------------------\n";
		int averageStockFullness = 0;
		for (Item i : Aisle) {
			msg += i.getName() + ": " + i.getRealizedStock() + "/" + i.getMaxStock() + " " + i.getStockPercentage() + "%\n";
			averageStockFullness += i.getStockPercentage();
			if (i.isStockToLow()) {
				msgStockTooLow += i.getName() + ": " + i.getRealizedStock() + "/" + i.getMaxStock() + " " + i.getStockPercentage() + "%\n";
			}
			if (i.isStockToHigh()) {
				msgStockTooHigh += i.getName() + ": " + i.getRealizedStock() + "/" + i.getMaxStock() + " " + i.getStockPercentage() + "%\n";
			}
		}
		msg += "Average Stock Amount: " + (averageStockFullness/Aisle.size()) + "%\n"
				+ "----------------------\n";
		msg += msgStockTooLow + "\n";
		msg += msgStockTooHigh + "\n";
		return msg;
	}
	
	@Override
	public String toString() {
		String msg = "All Items\n"
				+ "----------------------\n";
		for (Item i : Aisle) {
			msg += "\n" + i.toString() + "\n";
		}
		return msg;
	}
	
}
