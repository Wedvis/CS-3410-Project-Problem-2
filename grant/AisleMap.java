package group_project_p2;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.RuntimeException;
import java.util.Collection;
import java.util.Iterator;
import java.lang.Iterable;
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
    this.subAisles = new LinearHashMap<String,Aisle>(2);
    this.id = aisleJumpTable.add(this,id);
  }

  public AisleMap(String name, IdTable<Item> items, IdTable<Aisle> aisleJumpTable)
  {
    this(name,items,aisleJumpTable,-1);
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
          return new ItemIterable(this);
        }

          public int getId()
          {
            return id;
          }

            public String getAisleName()
            {
              return name;
  
            }

              public Iterable<Item> getLowStock()
              {
                ArrayList<Item> lowStocked = new ArrayList();
                for(Item it : getAllItems())
                {
                    if(it.isStockToLow())
                      lowStocked.add(it);
                }
                return lowStocked;
              }


                public Iterable<Item> getInStock()
              {
                ArrayList<Item> inStocked = new ArrayList();
                for(Item it : getAllItems())
                {
                    if(!it.isStockToLow())
                      inStocked.add(it);
                }
                return inStocked;
              }

                  public Iterable<Item> getOverstock()
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
                        for(Item item : getAllItems())
                          if(item.equals(it))
                            return true;
                        return false;
                    }


                          
                      public boolean hasItemId(int id)
                      {
                          for(Item item : getAllItems())
                            if(item.getID()==id)
                              return true;
                          return false;
                      }

                        public boolean addItem(Item i, Iterable<String> path)
                        {
                            getTerminatingAisle(path);
                            Aisle e = new AisleTerminator(i.getName(),items,aisleJumpTable,-1);
                            return e.getId()>=0;
                        }


                              public Aisle getTerminatingAisle(Iterable<String> path)
                              {
                                  var aiterable = new AisleIterable(this,path);
                                  var aiterator = aiterable.iterator();
                                  Aisle current = null;
                                  while(aiterator.hasNext())
                                    current = aiterator.next();
                                  return current;
                              }
                              private Aisle buildPath(Iterable<String> path)
                              {
                                  AisleIterable aiterable = new AisleIterable(this,path);
                                  AisleIterable.AisleIterator aiterator = (AisleIterable.AisleIterator)(aiterable.iterator());
                                  Aisle current = null;
                                  while(aiterator.hasNext())
                                    current = aiterator.next();
                                  while(aiterator.path.hasNext())
                                    {
                                      if(!(current instanceof AisleMap))
                                        throw new RuntimeException("Whoops, dead end");
                                      var cMap = (AisleMap)current;
                                      String str = aiterator.path.next();
                                      AisleMap newAisle = new AisleMap(str,items,aisleJumpTable);
                                      cMap.subAisles.put(str,newAisle);
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
                                public boolean isEdge()
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

    protected class AisleIterator implements Iterator<Aisle>
    {
      private Aisle currentTarget;
      protected Iterator<String> path;
      private boolean nextAisle;

      public AisleIterator(Aisle target,Iterator<String> path)
      {
        this.currentTarget = target;
        this.path = path;
        nextAisle = true;
      }

      public Aisle next()
      {
        currentTarget = currentTarget.getTerminatingAisle(Collections.singleton(path.next()));
        return currentTarget;
      }

      public boolean hasNext()
      {
        return path.hasNext();
      }
    }
  }
    private class RecursiveAisleIterator implements Iterator<Aisle>
    {
      private Iterator<Aisle> subAisles;
      private Iterator<Aisle> currentIterator;

      public RecursiveAisleIterator(Iterator<Aisle> subAisles)
      {
        this.subAisles = subAisles;
        this.currentIterator = subAisles;
      }

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

        public Iterator<Item> iterator()
        {
          return new ItemIterator(target);
        }

        private class ItemIterator implements Iterator<Item>
        {
          protected ItemIterator(Aisle target)
          {
            aisles = target.iterator();
            queued = null;
          }
          private Iterator<Aisle> aisles;
          private Item queued;
          
          public Item next()
          {
            if(queued==null && !hasNext())
              throw new RuntimeException("Oof iterator ran out");

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
  public Iterator<Aisle> iterator()
  {
    return new RecursiveAisleIterator(subAisles.values().iterator());
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
      this.items = items;
      this.id = this.aisleJumpTable.add(this,id);
    }
    public AisleTerminator(String name, IdTable<Item> items, IdTable<Aisle> aisleJumpTable)
    {
      this(name,items,aisleJumpTable,-1);
    }
    public boolean addItem(Item it, Iterable<String> path)
    {
      throw new RuntimeException("Whoops Terminator Node");
    }
    public int getId()
    {
      return id;
    }


    public Aisle getAisle(String next)
    {
      throw new RuntimeException("Whoops This Is a Terminator Node");
    }

    public Iterator<Aisle> iterator()
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
    public Item getItemById(int id)
    {
      return items.get(id);
    }
    public Iterable<Item> getItems(Iterable<String> path)
    {
      if(path.iterator().hasNext())
        throw new RuntimeException("Whoops Terminator Node");
      return getAllItems();
    }

    public boolean hasItem(Item it)
    {
      return item.equals(it);
    }
    public boolean hasItemId(int id)
    {
      return item.getID()==id;
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
      if(path.iterator().hasNext())
        throw new RuntimeException("Whoops This is a Terminator Node");
      return this;
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
}

