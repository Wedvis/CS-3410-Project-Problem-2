package group_project_p2;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.Collection;
import group_project_p2.Item;
import java.util.Set;
import java.util.List;

public interface Aisle extends Iterable<Aisle>
{
  public Iterable<Item> getItems(Iterable<String> path);

  public Item getItemById(int id);

  public Iterable<Item> getAllItems();

  public Iterable<String> getPath();

  public int getId();

  public String getAisleName();

  public Aisle getAisle(String next);

  public default String getName()
  {return getAisleName();}

  public Iterable<Item> getLowStock();

  public Iterable<Item> getInStock();

  public Iterable<Item> getOverstock();

  public boolean hasItem(Item it);

  public boolean hasItemId(int id);

  public boolean addItem(Item i, Iterable<String> path);



  public Aisle getTerminatingAisle(Iterable<String> path);

  public Collection<Aisle> getSubAisles();

  public boolean isEdge();
	// Get Full Stock Info on Aisle
	public default String getStockInfo() {
		String msg = "Stock Info for Aisle: " + getName() + "\n"
				+ "----------------------\n";
		String msgStockTooLow = "\nItems of Interest: STOCK TOO LOW\n"
				+ "----------------------\n";
		
		String msgStockTooHigh = "\nItems of Interest: STOCK TOO HIGH\n"
				+ "----------------------\n";
		int averageStockFullness = 0;
    int numItems = 0;
		for (Item i : getAllItems()) {
			msg += i.getName() + ": " + i.getRealizedStock() + "/" + i.getMaxStock() + " " + i.getStockPercentage() + "%\n";
			averageStockFullness += i.getStockPercentage();
			if (i.isStockToLow()) {
				msgStockTooLow += i.getName() + ": " + i.getRealizedStock() + "/" + i.getMaxStock() + " " + i.getStockPercentage() + "%\n";
			}
			if (i.isStockToHigh()) {
				msgStockTooHigh += i.getName() + ": " + i.getRealizedStock() + "/" + i.getMaxStock() + " " + i.getStockPercentage() + "%\n";
			}
      numItems++;
		}
		msg += "Average Stock Amount: " + (averageStockFullness/numItems) + "%\n"
				+ "----------------------\n";
		msg += msgStockTooLow + "\n";
		msg += msgStockTooHigh + "\n";
		return msg;
	}
	
	public default String toStringD() {
		String msg = "All Items\n"
				+ "----------------------\n";
		for (Item i : getAllItems()) {
			msg += "\n" + i.toString() + "\n";
		}
		return msg;
	}
}
