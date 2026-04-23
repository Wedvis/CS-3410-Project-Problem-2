package group_project_p2;

import java.lang.Iterable;
import group_project_p2.Item;
import java.util.Set;
import java.util.List;

public interface Aisle
{
  public Set<Item> getItems(Iterable<String> path);

  public Item getItemById(int id);

  public List<Item> getAllItems();

  public int getId();

  public String getAisleName();

  public Set<Item> getLowStock();

  public Set<Item> getInStock();

  public Set<Item> getOverstock();

  public boolean hasItem(Item it);

  public boolean hasItemId(int id);

  public boolean addItem(Item i);

  public String getStockInfo();

  public String toString();
}
