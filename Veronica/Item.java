package group_project_p2;
import java.lang.Iterable;
public class Item {
	private int id;
	private int maxStock;
	private int realizedStock;
	private String name;
  private Iterable<String> path;

	public Item(int id, int maxStock, int realizedStock, String name, Iterable<String> path) {
		this.id = id;
		this.maxStock = maxStock;
		this.realizedStock = realizedStock;
		this.name = name;
    this.path = path;
	}

  public Iterable<String> getPath()
  {
    return path;
  }
	
	public int getID() {
		return id;
	}
	
	public int getMaxStock() {
		return maxStock;
	}
	
	public int getRealizedStock() {
		return realizedStock;
	}
	
	// Returns a percentage out of 100
	// Note Stock can exceed 100 in which case its over stocked
	public int getStockPercentage() {
		double stockPercentage = ((double)realizedStock/maxStock) * 100;
		return (int) stockPercentage;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRealizedStock(int num) {
		this.realizedStock = num;
	}
	
	public void setMaxStock(int num) {
		this.maxStock = num;
	}
	
	public boolean isStockToLow() {
		if (getStockPercentage() <= 50) {
			return true;
		}
		return false;
	}
	
	public boolean isStockToHigh() {
		if (getStockPercentage() >= 125) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String msg = "Name: " + name + " ID: " + id + "\n"
				+ "Stock: " + realizedStock + "/" + maxStock + " " + getStockPercentage() + "%";
		return msg;
	}
	
}
