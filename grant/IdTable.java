package group_project_p2;

import java.util.TreeSet;
import group_project_p2.LinearHashMap;
public class IdTable<T>
{
    int size;
    private LinearHashMap<Integer,T> table;
    private TreeSet<Integer> freedIds;
    int nextId;
    public IdTable()
    {
        this.table = new LinearHashMap<>(2);
        this.freedIds = new TreeSet<>();
        nextId = 0;
        size=0;
    }

    public T get(int id)
    {
         return table.get(id);
    }
    public int add(T object, int id)
    {
      if(id<0)
        return add(object);
      set(id,object);
      return id;
    }
    public int add(T object)
    {
        int id = nextId;
        if(!freedIds.isEmpty())
        {
            id=freedIds.removeFirst();
        }
        else
            nextId++;
        table.put(id, object);
        size++;
        return id;
    }

    public T remove(int id)
    {
        T obj = get(id);
        if(obj==null)
            return null;
        table.put(id,null);
        freedIds.add(id);
        while((freedIds.getLast()+1)==nextId)
        {
            freedIds.removeLast();
            nextId--;
        }
        size--;
        return obj;
    }

    public void set(int id, T obj)
    {
        if(get(id)==null)
            size++;
        table.put(id,obj);
        freedIds.remove((Integer)id);
        if(id<nextId)
            return;
        for(int i = nextId; i<id;i++)
        {
            freedIds.add(i);
        }
        nextId=id+1;
    }

    public int size()
    {
        return size;
    }

}
