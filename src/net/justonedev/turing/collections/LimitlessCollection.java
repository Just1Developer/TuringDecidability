package net.justonedev.turing.collections;

/**
 * A collection class of theoretically infinite size.
 * Not as fast as any set, but in theory unlimited in size.
 * <p></p>
 * Doubly linked list style.
 *
 * @param <T> The type of the collection.
 *
 * @author justonedeveloper
 */
public class LimitlessCollection<T> {

    /**
     * Default Behaviour: List, duplicates allowed.
     */
    private static final boolean DEFAULT_ALLOW_DUPES = true;

    private CollectionContainer firstContainer;
    private CollectionContainer lastContainer;
    private boolean allowDuplicates;

    /**
     * Creates a new primitive history storage collection.
     * Can be set to allow/disallow duplicate entries, default is allow.
     */
    public LimitlessCollection() {
        this(DEFAULT_ALLOW_DUPES);
    }

    /**
     * Creates a new primitive history storage collection.
     * Can be set to allow/disallow duplicate entries.
     *
     * @param allowDuplicates If duplicate entries are allowed.
     */
    public LimitlessCollection(boolean allowDuplicates) {
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
     * Adds a new entry to the collection.
     * Returns if the element was added successfully. If duplicates
     * are not allowed and the entry is already in the collection,
     * it's not added again.
     *
     * @param entry The element to add.
     * @return True if the element was added, false if not.
     */
    public boolean add(T entry) {
        if (firstContainer == null) {
            CollectionContainer container = new CollectionContainer(0,null, entry);
            // First element, always not in
            firstContainer = container;
            lastContainer = container;
            return true;
        }
        if (!allowDuplicates && contains(entry)) return false;
        CollectionContainer container = new CollectionContainer(lastContainer, entry);
        lastContainer.setNextContainer(container);
        lastContainer = container;
        return true;
    }

    /**
     * Adds multiple elements to the collection.
     * Returns true if at least one element was added successfully.
     *
     * @return True if at least one element was added.
     */
    @SafeVarargs
    public final boolean addAll(T... entries) {
        boolean result = false;
        if (entries != null) {
            for (T entry : entries) {
                result |= add(entry);
            }
        }
        return result;
    }

    /**
     * If the collection contains a given element.
     *
     * @param element The element.
     * @return True if the set contains the element, false if not.
     */
    public boolean contains(T element) {
        if (lastContainer == null || firstContainer == null)
            return false;
        // Doubly recursive search
        CollectionContainer frontSearcher = firstContainer, backSearcher = lastContainer;
        while (frontSearcher != null && backSearcher != null) {

            // Compare values
            if (frontSearcher.getValue().equals(element)) return true;
            if (backSearcher.getValue().equals(element)) return true;

            // get next
            backSearcher = backSearcher.getPreviousContainer();
            // Compare if they met
            if (backSearcher != null && backSearcher.equals(frontSearcher)) break;
            frontSearcher = frontSearcher.getNextContainer();
        }
        return false;
    }

    /**
     * A single storage element for the history.
     *
     * @author justonedeveloper
     */
    private class CollectionContainer {

        private T storageValue;
        private CollectionContainer previousContainer;
        private CollectionContainer nextContainer;
        private final int containerID;

        /**
         * Creates a new container with the specified linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param previous The previous linked container. Must not be null without id specification.
         */
        private CollectionContainer(CollectionContainer previous) {
            this(previous, null);
        }

        /**
         * Creates a new container with the specified linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param id The ID of the container.
         * @param previous The previous linked container.
         */
        private CollectionContainer(int id, CollectionContainer previous) {
            this(id, previous, null);
        }

        /**
         * Creates a new container with the specified value and linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param previous The previous linked container. Must not be null without id specification.
         * @param value Content value.
         */
        private CollectionContainer(CollectionContainer previous, T value) {
            if (previous == null)
                throw new NullPointerException("Previous must not be null when creating an ID-less history container element!");
            this.containerID = previous.getContainerID() + 1;
            setValue(value);
            setPreviousContainer(previous);
        }

        /**
         * Creates a new container with the specified value and linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param previous The previous linked container. Can be null for this argument configuration.
         * @param value Content value.
         */
        private CollectionContainer(int id, CollectionContainer previous, T value) {
            this.containerID = id;
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
         * Gets the value of this container. May be null.
         * @return The stored value.
         */
        private T getValue() {
            return storageValue;
        }

        /**
         * Sets the value of the container. Can be null.
         * @param value The new value.
         */
        private void setValue(T value) {
            storageValue = value;
        }

        /**
         * If the stored value is != null.
         * @return Existence of stored value.
         */
        private boolean hasValue() {
            return storageValue != null;
        }

        /**
         * Gets the next container in the list. May be null.
         * @return Next container or null.
         */
        private CollectionContainer getNextContainer() {
            return nextContainer;
        }

        /**
         * Sets the next container to a specified container.
         * @param nextContainer The next container.
         */
        private void setNextContainer(CollectionContainer nextContainer) {
            this.nextContainer = nextContainer;
        }

        /**
         * If there is a next container != null.
         * @return If the next container is not null.
         */
        private boolean hasNextContainer() {
            return nextContainer != null;
        }

        /**
         * Gets the previous container in the list. May be null.
         * @return Previous container or null.
         */
        private CollectionContainer getPreviousContainer() {
            return previousContainer;
        }

        /**
         * Sets the previous container to a specified container.
         * @param previousContainer The next container.
         */
        private void setPreviousContainer(CollectionContainer previousContainer) {
            this.previousContainer = previousContainer;
        }

        /**
         * If there is a previous container != null.
         * @return If the previous container is not null.
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
            return obj != null && obj.getClass().equals(CollectionContainer.class) &&
                    ((CollectionContainer) obj).getContainerID() == containerID;
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
