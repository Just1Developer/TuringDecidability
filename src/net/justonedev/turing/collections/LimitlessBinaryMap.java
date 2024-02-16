package net.justonedev.turing.collections;

/**
 * A limitless collection that pairs up two values.
 * Contrary to a map in Java, this collection maps 1-to-1.
 *
 * @param <K> The "key", but actually first value type.
 * @param <V> The "value", but actually second value type.
 *
 * @author justonedeveloper
 */
public class LimitlessBinaryMap<K, V> {

    /**
     * Default Behaviour: List, duplicates allowed.
     */
    private static final boolean DEFAULT_ALLOW_DUPES = true;

    private CollectionPair firstContainer;
    private CollectionPair lastContainer;
    private boolean allowDuplicates;

    /**
     * Creates a new primitive history storage collection.
     * Can be set to allow/disallow duplicate entries, default is allow.
     */
    public LimitlessBinaryMap() {
        this(DEFAULT_ALLOW_DUPES);
    }

    /**
     * Creates a new primitive history storage collection.
     * Can be set to allow/disallow duplicate entries.
     *
     * @param allowDuplicates If duplicate entries are allowed.
     */
    public LimitlessBinaryMap(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    /**
     * If the collection allows duplicate elements.
     * @return True if collection allows duplicates.
     */
    public boolean getAllowDuplicated() {
        return allowDuplicates;
    }

    /**
     * Sets if the collection should allow duplicate entries.
     * Entries already in the set will not be affected by this change.
     */
    public void setAllowDuplicates(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    /**
     * Adds a new entry pair to the collection.
     * Returns if the pair was added successfully. If duplicates
     * are not allowed and the entry is already in the collection,
     * it's not added again.
     * <p></p>
     * Note that only key or value of them need to already be contained to result
     * in a negative, though if it's key or value matters.
     *
     * @param key The key (first element) of the pair.
     * @param value The value (second element) of the pair.
     * @return True if the pair was added, false if not.
     */
    public boolean add(K key, V value) {
        if (firstContainer == null) {
            CollectionPair container = new CollectionPair(0,null, key, value);
            // First element, always not in
            firstContainer = container;
            lastContainer = container;
            return true;
        }
        if (!allowDuplicates && containsKey(key)) return false;
        if (!allowDuplicates && containsValue(value)) return false;
        CollectionPair container = new CollectionPair(lastContainer, key, value);
        lastContainer.setNextContainer(container);
        lastContainer = container;
        return true;
    }

    /**
     * Gets the entire pair by its key.
     * Will return a static struct that will always be not null.
     * The isFound() method indicates if the search yielded any result,
     * Key and Value may be null either way.
     *
     * @param key The key (first pair element).
     * @return The pair associated with the key.
     */
    public MapResult<K, V> getPairByKey(K key) {
        if (lastContainer == null || firstContainer == null)
            return new MapResult<>();
        // Doubly recursive search
        CollectionPair frontSearcher = firstContainer, backSearcher = lastContainer;
        while (frontSearcher != null && backSearcher != null) {

            // Compare values
            if (frontSearcher.getKey().equals(key)) return new MapResult<>(frontSearcher.getKey(), frontSearcher.getValue());
            if (backSearcher.getKey().equals(key)) return new MapResult<>(backSearcher.getKey(), backSearcher.getValue());

            // get next
            backSearcher = backSearcher.getPreviousContainer();
            // Compare if they met
            if (backSearcher != null && backSearcher.equals(frontSearcher)) break;
            frontSearcher = frontSearcher.getNextContainer();
        }
        return new MapResult<>();
    }

    /**
     * Gets the entire pair by its value.
     * Will return a static struct that will always be not null.
     * The isFound() method indicates if the search yielded any result,
     * Key and Value may be null either way.
     *
     * @param value The value (second pair element).
     * @return The pair associated with the value.
     */
    public MapResult<K, V> getPairByValue(V value) {
        if (lastContainer == null || firstContainer == null)
            return new MapResult<>();
        // Doubly recursive search
        CollectionPair frontSearcher = firstContainer, backSearcher = lastContainer;
        while (frontSearcher != null && backSearcher != null) {

            // Compare values
            if (frontSearcher.getValue().equals(value)) return new MapResult<>(frontSearcher.getKey(), frontSearcher.getValue());
            if (backSearcher.getValue().equals(value)) return new MapResult<>(backSearcher.getKey(), backSearcher.getValue());

            // get next
            backSearcher = backSearcher.getPreviousContainer();
            // Compare if they met
            if (backSearcher != null && backSearcher.equals(frontSearcher)) break;
            frontSearcher = frontSearcher.getNextContainer();
        }
        return new MapResult<>();
    }

    /**
     * Gets the key mapped to a given value.
     * If the map does not contain the value, returns null.
     *
     * @param value The value (second pair element).
     * @return The key of the value or null.
     */
    public K getKey(V value) {
        return getPairByValue(value).getKey();
    }

    /**
     * If the collection contains a given key.
     * Keys are always the first argument, so does not check if any pair
     * has the key as it's second value.
     *
     * @param key The key (first pair element).
     * @return True if the set contains the key, false if not.
     */
    public boolean containsKey(K key) {
        return getPairByKey(key).isFound();
    }

    /**
     * Gets the value mapped to a given key.
     * If the map does not contain the key, returns null.
     *
     * @param key The key (first pair element).
     * @return The value of the key or null.
     */
    public V getValue(K key) {
        return getPairByKey(key).getValue();
    }

    /**
     * If the collection contains a given value.
     * Values are always the second argument, so does not check if any pair
     * has the value as it's first value.
     *
     * @param value The value (second pair element).
     * @return True if the set contains the value, false if not.
     */
    public boolean containsValue(V value) {
        return getPairByValue(value).isFound();
    }
    
    /**
     * Gets a LimitlessCollection with all keys of the map.
     * The collection is generated on the fly every time and not cached.
     * @return Limitless collection of keys.
     */
    public LimitlessCollection<K> keys() {
        LimitlessCollection<K> keys = new LimitlessCollection<>();
        CollectionPair pair = firstContainer;
        while (pair != null) {
            keys.add(pair.getKey());
            pair = pair.getNextContainer();
        }
        return keys;
    }
    
    /**
     * Gets a LimitlessCollection with all values of the map.
     * The collection is generated on the fly every time and not cached.
     * @return Limitless collection of values.
     */
    public LimitlessCollection<V> values() {
        LimitlessCollection<V> values = new LimitlessCollection<>();
        CollectionPair pair = firstContainer;
        while (pair != null) {
            values.add(pair.getValue());
            pair = pair.getNextContainer();
        }
        return values;
    }
    
    /**
     * A single storage element for the history.
     *
     * @author justonedeveloper
     */
    private class CollectionPair {

        private K storageKey;
        private V storageValue;
        private CollectionPair previousContainer;
        private CollectionPair nextContainer;
        private final int containerID;

        /**
         * Creates a new container with the specified linked containers.
         * The next container cannot be set here as insertions are not allowed.
         * <p></p>
         * Key and Value will be null.
         *
         * @param previous The previous linked container. Must not be null without id specification.
         */
        private CollectionPair(CollectionPair previous) {
            this(previous, null, null);
        }

        /**
         * Creates a new container with the specified linked containers.
         * The next container cannot be set here as insertions are not allowed.
         * <p></p>
         * Key and Value will be null.
         *
         * @param id The ID of the container.
         * @param previous The previous linked container.
         */
        private CollectionPair(int id, CollectionPair previous) {
            this(id, previous, null, null);
        }

        /**
         * Creates a new container with the specified value and linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param previous The previous linked container. Must not be null without id specification.
         * @param key Content key, first pair element.
         * @param value Content value, second pair element.
         */
        private CollectionPair(CollectionPair previous, K key, V value) {
            if (previous == null)
                throw new NullPointerException("Previous must not be null when creating an ID-less history container element!");
            this.containerID = previous.getContainerID() + 1;
            setKey(key);
            setValue(value);
            setPreviousContainer(previous);
        }

        /**
         * Creates a new container with the specified value and linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param previous The previous linked container. Can be null for this argument configuration.
         * @param key Content key, first pair element.
         * @param value Content value, second pair element.
         */
        private CollectionPair(int id, CollectionPair previous, K key, V value) {
            this.containerID = id;
            setKey(key);
            setValue(value);
            setPreviousContainer(previous);
        }

        /**
         * Gets the unique container ID.
         * @return The container id.
         */
        private int getContainerID() {
            return containerID;
        }

        /**
         * Gets the key (first element) of this container. May be null.
         * @return The stored value.
         */
        private K getKey() {
            return storageKey;
        }

        /**
         * Gets the value (second element) of this container. May be null.
         * @return The stored value.
         */
        private V getValue() {
            return storageValue;
        }

        /**
         * Sets the key of the container. Can be null.
         * @param key The new key.
         */
        private void setKey(K key) {
            storageKey = key;
        }

        /**
         * Sets the value of the container. Can be null.
         * @param value The new value.
         */
        private void setValue(V value) {
            storageValue = value;
        }

        /**
         * If the stored key is != null.
         * @return Existence of stored key.
         */
        private boolean hasKey() {
            return storageValue != null;
        }

        /**
         * If the stored value is != null.
         * @return Existence of stored value.
         */
        private boolean hasValue() {
            return storageValue != null;
        }

        /**
         * Gets the next pair container in the list. May be null.
         * @return Next pair container or null.
         */
        private CollectionPair getNextContainer() {
            return nextContainer;
        }

        /**
         * Sets the next pair container to a specified pair container.
         * @param nextContainer The next pair container.
         */
        private void setNextContainer(CollectionPair nextContainer) {
            this.nextContainer = nextContainer;
        }

        /**
         * If there is a next pair container != null.
         * @return If the next pair container is not null.
         */
        private boolean hasNextContainer() {
            return nextContainer != null;
        }

        /**
         * Gets the previous pair container in the list. May be null.
         * @return Previous pair container or null.
         */
        private CollectionPair getPreviousContainer() {
            return previousContainer;
        }

        /**
         * Sets the previous pair container to a specified pair container.
         * @param previousContainer The next pair container.
         */
        private void setPreviousContainer(CollectionPair previousContainer) {
            this.previousContainer = previousContainer;
        }

        /**
         * If there is a previous pair container != null.
         * @return If the previous pair container is not null.
         */
        private boolean hasPreviousContainer() {
            return previousContainer != null;
        }

        /**
         * Compares equality by comparing the ID value.
         * Different Types must not be compared to one another, which is luckily not
         * possible.
         *
         * @param obj The object to compare.
         * @return True if container IDs are equal.
         */
        @Override
        public boolean equals(Object obj) {
            return obj != null && obj.getClass().equals(CollectionPair.class) &&
                    ((CollectionPair) obj).getContainerID() == containerID;
        }

        /**
         * Returns the container ID.
         * @return Container ID.
         */
        @Override
        public int hashCode() {
            return containerID;
        }
    }

}
