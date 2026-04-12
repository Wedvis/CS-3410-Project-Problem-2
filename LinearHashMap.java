package group_project_p2;
import java.security.Key;
import java.util.*;

public class LinearHashMap<K, V> implements Map<K, V> {
    ArrayList<KeyVal<V, K>>[] array;
    int valCount = 0;
    boolean isResize = false;

    private void resize() {

        isResize = true;

        KeyVal<V, K>[] vals = new KeyVal[valCount];
        ArrayList<KeyVal<V, K>>[] newArray = new ArrayList[array.length * 2];
        for(int i = 0; i < array.length * 2; ++i) {
            newArray[i] = new ArrayList<>();
        }

        int j = 0;

        for(int i = 0; i < array.length; i++) {
            if(!array[i].isEmpty()) {
                for(KeyVal<V, K> keyval: array[i]) {
                    vals[j] = keyval;
                    j++;
                }
            }
        }

        array = newArray;

        for(KeyVal<V, K> keyval: vals) {
            put(keyval.getkey(), keyval.getVal());
        }

        isResize = false;
    }

    public LinearHashMap(int size) {
        array = new ArrayList[size];
        for(int i = 0; i < size; ++i) {
            array[i] = new ArrayList<>();
        }
    }
    @Override
    public int size() {
        return array.length;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        int index = (int)(hashFunction((K) key) % size());
        if(array[index].isEmpty()) {
            return null;
        }
        else {
            int i = 0;
            for(KeyVal<V, K> keyval: array[index]) {
                if(array[index].get(i).getkey().equals(key)) {
                    return array[index].get(i).getVal();
                }
                i++;
            }
        }

        return null;
    }

    private long hashFunction(K key) {
        if(key instanceof Number k)
            return HashFunctions.javaIntRandomize(k.longValue());
        int jbCode = key.hashCode();
        if(jbCode < 0) {
            return jbCode * -1;
        }
        return jbCode;
    }
    @Override
    public V put(K key, V value) {
        KeyVal<V, K> keyval = new KeyVal<V, K>(value, key);
        int index = (int)(hashFunction(key) % size());
        array[index].add(keyval);
        if(!isResize) {
            valCount++;
        }
        if(valCount > array.length / 2) {
            resize();
        }
        return value;
    }

    @Override
    public V remove(Object key) {
        int index = (int)(hashFunction((K) key) % array.length);
        if(array[index].isEmpty()) {
            return null;
        }
        else {
            for(int i = 0; i < array[index].size(); i++) {
                if(array[index].get(i).getkey().equals(key)) {
                    KeyVal<V, K> keyval = array[index].remove(i);
                    valCount--;
                    return keyval.getVal();
                }
            }
        }

        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();

        for(ArrayList<KeyVal<V, K>> alist: array) {
            if(!alist.isEmpty()) {
                for(KeyVal<V, K> keyval: alist) {
                    keySet.add(keyval.getkey());
                }
            }
        }

        return keySet;
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }
}
