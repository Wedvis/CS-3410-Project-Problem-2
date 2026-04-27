package group_project_p2;
import java.util.ArrayList;
import java.util.Collections;
public class AisleMap implements Aisle
{
  private IdTable<Item> items;
  private LinearHashMap<String,Aisle> subAisles;
  private IdTable<Aisle> aisleJumpTable;
  private String name;
  private int id;

  public AisleMap(String name, IdTable<Item> items, IdTable<Aisle> aisleJumpTable,int id)
  {
    this.name = name;
    this.items = items;
    this.aisleJumpTable = aisleJumpTable;
    this.subAisles = new LinearHashMap<>();
    this.id = id;
  }
  
    public Iterable<Item> getItems(Iterable<String> path)
    {
      Aisle endIsle = getTerminatingAisle(path);
      return endIsle.getAllItems();
    }

      public Item getItemById(int id)
      {
        return items.get(id);
      }

        public Iterable<Item> getAllItems()
        {
          return ItemIterable(this);
        }

          public int getId()
            return id;

            public String getAisleName()
              return name;

              public Iterable<Item> getLowStock();
              {
                ArrayList<Item> lowStocked = new ArrayList();
                for(Item it : getAllItems())
                {
                    if(it.isStockToLow())
                      lowStocked.add(it);
                }
                return lowStocked;
              }


                public Iterable<Item> getInStock();
              {
                ArrayList<Item> inStocked = new ArrayList();
                for(Item it : getAllItems())
                {
                    if(!it.isStockToLow())
                      inStocked.add(it);
                }
                return inStocked;
              }

                  public Iterable<Item> getOverstock();
              {
                ArrayList<Item> highStocked = new ArrayList();
                for(Item it : getAllItems())
                {
                    if(it.isStockToHigh())
                      highStocked.add(it);
                }
                return highStocked;
              }

                    public boolean hasItem(Item it)
                    {
                        for(item in getAllItems())
                          if(item.equals(it))
                            return true;
                        return false;
                    }


                          
                      public boolean hasItemId(int id)
                      {
                          for(item in getAllItems())
                            if(item.getId()==id)
                              return true;
                          return false;
                      }

                        public boolean addItem(Item i, Iterable<String> path)
                        {
                            getTerminatingAisle(path)
                            Aisle e = new AisleTerminator(i.getName(),items,null)
                            e.setId(subAisles.add(e));
                        }

                          public String getStockInfo();

                            public String toString();

                              public Aisle getTerminatingAisle(Iterable<String> path)
                              {
                                  var aiterable = AisleIterable(this,path);
                                  var aiterator = aiterable.iterator();
                                  Aisle current;
                                  while(aiterator.hasNext())
                                    current = aiterator.next();
                                  return current;
                              }
                              private Aisle buildPath(Iterable<String> path)
                              {
                                  var aiterable = AisleIterable(this,path);
                                  var aiterator = aiterable.iterator();
                                  Aisle current;
                                  while(aiterator.hasNext())
                                    current = aiterator.next();
                                  while(aiterator.path.hasNext())
                                    {
                                      String str = aiterator.path.next();
                                      Aisle newAisle = new AisleMap(str,items,aisleJumpTable);
                                      aisleJumpTable.add(newAisle);
                                      current.subAisles.put(str,newAisle);
                                      current = newAisle;
                                    }
                                  return current;
                              }

                                public Collection<Aisle> getSubAisles()
                                {
                                    return subAisles.values();
                                }

                                public Aisle getAisle(String next)
                                {
                                    return subAisles.get(next);
                                }
                                public Aisle isEdge()
                                {
                                  return false;
                                }


  
  private class AisleIterable implements Iterable<Aisle>
  {
    private Aisle target;
    protected Iterable<String> path;
    
    public AisleIterable(Aisle target, Iterable<String> path)
    {
      this.target = target;
      this.path = path;
    }

    public Iterator<Aisle> iterator()
    {
      return new AisleIterator(target,path.iterator());
    }

    private class AisleIterator implements Iterator<Aisle>
    {
      private Aisle currentTarget;
      protected Iterator<String> path;
      private boolean nextAisle;

      public AisleIterator(target,path)
      {
        this.currentTarget = target;
        this.path = path.iterator();
        nextAisle = true;
      }

      public Aisle next()
      {
        currentTarget = currentTarget.getAisle(path.next());
        return currentTarget;
      }

      public Aisle hasNext()
      {
        return path.hasNext();
      }
    }
  }
    private class RecursiveAisleIterator implements Iterator<Aisle>
    {
      private Iterator<Aisle> subAisles;
      private Iterator<Aisle> currentIterator;
      public Aisle next()
      {
        if(currentIterator.hasNext())
          return currentIterator.next();
        currentIterator = subAisles.next().iterator();
        return next();
      }
      public boolean hasNext()
      {
        return currentIterator.hasNext() && subAisles.hasNext();
      }
    }


      private class ItemIterable implements Iterable<Item>
      {
        private Aisle target;

        protected ItemIterable(Aisle target)
        {
          this.target = target;
        }

        private class ItemIterator implements Iterator<Item>
        {
          private Iterator<Aisle> aisles;
          private Item queued;
          
          public Item next()
          {
            if(queued==null && !hasNext())
              throw RuntimeErrorException("Oof iterator ran out");

            Item ret = queued;
            queued = null;
            return ret;
          }

          public boolean hasNext()
          {
                while(aisles.hasNext() && queued == null)
                {
                  Aisle current = aisles.next();
                  if(current.isEdge())
                    continue;
                  queued = current.getAllItems().iterator().next();
                }
                return queued!=null;
          }
        }
      

    }
  public Aisle iterator()
  {
    return RecursiveAisleIterator(subAisles.iterator());
  }

  private class AisleTerminator implements Aisle
  {
    private String name;
    private Item item;
    private IdTable<Aisle> aisleJumpTable;
    private IdTable<Item> items;
    private int id;

    public AisleTerminator(String name, IdTable<Item> items, IdTable<Aisle> aisleJumpTable,int id)
    {
      this.name = name;
      this.items = items;
      this.aisleJumpTable = aisleJumpTable;
      this.id = id;
      this.items = items;
    }


    public Aisle getAisle(String next)
    {
      throw RuntimeErrorException("Whoops This Is a Terminator Node");
    }

    public Aisle iterator()
    {
      return Collections.emptyIterator();
    }

    public Iterable<Item> getAllItems()
    {
        return Collections.singleton(item);
    }
    public String getAisleName()
    {
      return item.getName();
    }

    public boolean hasItem(Item it)
    {
      return item.equals(it);
    }
    public boolean hasItemId(int id)
    {
      return item.getId()==id;
    }
    public Iterable<Item> getLowStock()
    {
      if(item.isStockToLow())
        return Collections.singleton(item);
      return Collections.emptySet();
    }
    public Iterable<Item> getOverstock()
    {
      if(item.isStockToHigh())
        return Collections.singleton(item);
      return Collections.emptySet();
    }
    public Iterable<Item> getInStock()
    {
      if(!item.isStockToLow())
        return Collections.singleton(item);
      return Collections.emptySet();
    }
    public String toString()
    {
      return item.toString();
    }
    public Aisle getTerminatingAisle(Iterable<String> path)
    {
      throw new RuntimeErrorException("Whoops This is a Terminator Node");
    }
    public Collection<Aisle> getSubAisles()
    {
      return Collections.emptySet();
    }

    public boolean isEdge()
    {
      return true;
    }
}
