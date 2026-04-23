package group_project_p2;

import java.lang.Iterable;
import group_project_p2.Item;
import java.util.Set;
import java.util.List;

public interface Aisle
{
  public Iterable<Item> getItems(Iterable<String> path);

  public Item getItemById(int id);

  public Iterable<Item> getAllItems();

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

  public Aisle getTerminatingAisle(Iterable<String> path);

  public Set<Aisle> getSubAisles();
}
