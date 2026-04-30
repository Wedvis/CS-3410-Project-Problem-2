package group_project_p2;
import java.util.*;

public class LinearHashMapTest<K, V> implements Map<K, V> {
    //Check linearHashMap for more info on how to use linear hashmap class
    KeyVal<V, K>[] array;
    public int valCount = 0;
    boolean isResize = false;
    public int collisions = 0;
    public int probes_find = 0;


    private void resize() {

        isResize = true;

        KeyVal<V, K>[] vals = new KeyVal[valCount];
        KeyVal<V, K>[] newArray = new KeyVal[size() * 2];
        //Note, to use regular doubling algorithm for resize, just replace the stuff in brackets with
        //array.length * 2, otherwise use resizePrime(size())
        
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

    public LinearHashMapTest(int size) {
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
            for(int i = 0; i < array.length; i++) {
                probes_find++;
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
        //Note, for testing purposes we want to use strings only.
        //the first if statement is meant for that, to test different hash functions simply
        //Change the line with the comment beside it as per the comment's directions
        //Do not use any of the dbl hash functions, and for FNV functions use fnv1.
        long jbCode = 0;
        if(key instanceof String str) {
            jbCode = HashFunctions.djb2(str); //Change function to test different String hashfunctions
            if(jbCode < 0) {
                return jbCode * -1;
            }
            return jbCode;
        }

        if(key instanceof Number k)
            return HashFunctions.javaIntRandomize(k.longValue());
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
        if(array[index] == null) {
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
            if(!isResize) {
                ++collisions;
            }
            for(int i = 1; i < array.length; i++) {
                index = (int)((hash + i) % size());
                if(array[index] == null) {
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
            if(keyval != null) {
              keySet.add(keyval.getkey());
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
