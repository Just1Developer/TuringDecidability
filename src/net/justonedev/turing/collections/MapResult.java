package net.justonedev.turing.collections;

/**
 * Class to comprise a search result.
 * Since values can be null, it will be a useful feature to
 * determine if a Result has been found without the structure to link.
 * @param <K> The key type.
 * @param <V> The value type.
 *
 * @author justonedeveloper
 */
public class MapResult<K, V> {

    private final K key;
    private final V value;
    private final boolean found;

    /**
     * Creates a new negative map result,
     * key and value will be assigned and isFound will return true.
     */
    public MapResult(K key, V value) {
        this.key = key;
        this.value = value;
        this.found = true;
    }

    /**
     * Creates a new negative map result,
     * key and value will be null and isFound will return false.
     */
    public MapResult() {
        this.key = null;
        this.value = null;
        this.found = false;
    }

    /**
     * Gets the key (first pair element of the map).
     * @return The key.
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets the value (second pair element of the map).
     * @return The value.
     */
    public V getValue() {
        return value;
    }

    /**
     * If the result was found (true) or not (false).
     * If false, key and value will be null. Might not always be the other way around.
     * @return If the result was found.
     */
    public boolean isFound() {
        return found;
    }
}
