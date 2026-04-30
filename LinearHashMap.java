package group_project_p2;
import java.security.Key;
import java.util.*;

public class LinearHashMap<K, V> implements Map<K, V> {
    //Note, the only functions used in the linear hash map class are size,  keySet, values, get, remove, put.
    KeyVal<V, K>[] array;
    public int valCount = 0;
    boolean isResize = false;

    private void resize() {

        isResize = true;

        KeyVal<V, K>[] vals = new KeyVal[valCount];
        KeyVal<V, K>[] newArray = new KeyVal[array.length * 2];
        //Note, to use regular doubling algorithm for resize, just replace the stuff in brackets with
        //array.length * 2, otherwise, use resizePrime[size()]

        int j = 0;

        for(int i = 0; i < array.length; i++) {
            if(array[i] != null && array[i].getVal() != null) {
                vals[j] = array[i];
                ++j;
            }
        }

        array = newArray;

        for(KeyVal<V, K> keyval: vals) {
            if(keyval == null) {
                break;
            }
            put(keyval.getkey(), keyval.getVal());
        }

        isResize = false;
    }

    public LinearHashMap(int size) {
        array = new KeyVal[size];
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
        long hash = hashFunction((K)key);
        int index = (int)(hash % size());
        if(array[index] == null) {
            return null;
        }
        else {
            //Note here there is no separation between the first attempt at get and the probing sequence, it all happens in this one for-loop.
            for(int i = 0; i < array.length; i++) {
                index = (int)((hash + i) % size());
                if(array[index] == null) {
                    break;
                }
                if(array[index].getkey().equals(key)) {
                    return array[index].getVal();
                }
            }
        }

        return null;
    }

    private long hashFunction(K key) {
        long jbCode = 0;

        if(key instanceof Number k)
            return Math.abs(HashFunctions.javaIntRandomize(k.longValue()));
        jbCode = key.hashCode();

        if(jbCode < 0) {
            return jbCode * -1;
        }
        return jbCode;
    }
    @Override
    public V put(K key, V value) {
        KeyVal<V, K> keyval = new KeyVal<V, K>(value, key);
        long hash = hashFunction(key);
        int index = (int)(hash % size());
        if(array[index] == null || array[index].getVal() == null) {
            array[index] = keyval;
            if(!isResize) {
                ++valCount;
            }

            if(valCount >= array.length/2) {
                resize();
            }
            return value;
        }
        else {
            //probing sequence starts here.
            for(int i = 1; i < array.length; i++) {
                index = (int)((hash + i) % size());
                if(array[index] == null || array[index].getVal() == null) {
                    if(!isResize) {
                        ++valCount;
                    }
                    array[index] = keyval;
                    if(valCount >= array.length/2) {
                        resize();
                    }
                    return value;
                }
            }
        }

        System.out.println("place not found");
        return null;
    }

    @Override
    public V remove(Object key) {
        long hash = hashFunction((K)key);
        int index = (int)(hash % array.length);
        if(array[index] == null) {
            return null;
        }
        else {

            if(array[index].getkey().equals(key)) {
                V val = array[index].getVal();
                array[index] = new KeyVal<>(null, (K)key);
                return val;
            }

            else {
                //probing sequence starts here.
                for(int i = 1; i < array.length; i++) {
                    index = (int)((hash + i) % size());
                    if(array[index] == null) {
                        break;
                    }
                    else if(array[index].getkey().equals(key)) {
                        V val = array[index].getVal();
                        array[index] = new KeyVal<>(null, (K)key);
                        return val;
                    }
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

        for(KeyVal<V, K> keyval: array) {
            if(keyval != null && keyval.getVal() != null) {
              keySet.add(keyval.getkey());
            }
        }

        return keySet;
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();

        for(KeyVal<V, K> keyval: array) {
            if(keyval != null && keyval.getVal() != null) {
                values.add(keyval.getVal());
            }
        }

        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }



    //Found this algorithm for finding prime numbers from
    // https://www.geeksforgeeks.org/java/java-prime-number-program/

    private int resizePrime(int size) {
        size *= 2;

        while(!isPrime(size)) {
            ++size;
        }

        return size;
    }

    private boolean isPrime(int n) {
        // Corner case
        if (n <= 1)
            return false;
        // For n=2 or n=3 it will check
        if (n == 2 || n == 3)
            return true;
        // For multiple of 2 or 3 This will check
        if (n % 2 == 0 || n % 3 == 0)
            return false;
        // It will check all the others condition
        for (int i = 5; i <= Math.sqrt(n); i = i + 6)
            if (n % i == 0 || n % (i + 2) == 0)
                return false;

        return true;
    }
}
