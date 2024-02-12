package net.justonedev.turing.supervisor;

/**
 * A collection class of theoretically infinite size.
 * Not as fast as any set, but in theory unlimited in size.
 * <p></p>
 * Doubly linked list style.
 *
 * @author justonedeveloper
 */
public class PrimitiveHistory<T> {

    /**
     * Default Behaviour: List, duplicates allowed.
     */
    private static final boolean DEFAULT_ALLOW_DUPES = true;

    private HistoryContainer firstContainer;
    private HistoryContainer lastContainer;
    private boolean allowDuplicates;

    /**
     * Creates a new primitive history storage collection.
     * Can be set to allow/disallow duplicate entries, default is allow.
     *
     * @param entries The entries to start. Optional.
     */
    @SafeVarargs
    public PrimitiveHistory(T... entries) {
        this(DEFAULT_ALLOW_DUPES, entries);
    }

    /**
     * Creates a new primitive history storage collection.
     * Can be set to allow/disallow duplicate entries.
     *
     * @param allowDuplicates If duplicate entries are allowed.
     * @param entries The entries to start. Optional.
     */
    @SafeVarargs
    public PrimitiveHistory(boolean allowDuplicates, T... entries) {
        this.allowDuplicates = allowDuplicates;
        if (entries != null) {
            for (T entry : entries) {
                add(entry);
            }
        }
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
            HistoryContainer container = new HistoryContainer(0,null, entry);
            // First element, always not in
            firstContainer = container;
            lastContainer = container;
            return true;
        }
        if (!allowDuplicates && contains(entry)) return false;
        HistoryContainer container = new HistoryContainer(lastContainer, entry);
        lastContainer.setNextContainer(container);
        lastContainer = container;
        return true;
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
        HistoryContainer frontSearcher = firstContainer, backSearcher = lastContainer;
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
    private class HistoryContainer {

        private T storageValue;
        private HistoryContainer previousContainer;
        private HistoryContainer nextContainer;
        private final int containerID;

        /**
         * Creates a new container with the specified linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param previous The previous linked container. Must not be null without id specification.
         */
        private HistoryContainer(HistoryContainer previous) {
            this(previous, null);
        }

        /**
         * Creates a new container with the specified linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param id The ID of the container.
         * @param previous The previous linked container.
         */
        private HistoryContainer(int id, HistoryContainer previous) {
            this(id, previous, null);
        }

        /**
         * Creates a new container with the specified value and linked containers.
         * The next container cannot be set here as insertions are not allowed.
         *
         * @param previous The previous linked container. Must not be null without id specification.
         * @param value Content value.
         */
        private HistoryContainer(HistoryContainer previous, T value) {
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
        private HistoryContainer(int id, HistoryContainer previous, T value) {
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
         * @return Existence of storage value.
         */
        private boolean hasValue() {
            return storageValue != null;
        }

        /**
         * Gets the next container in the list. May be null.
         * @return Next container or null.
         */
        private HistoryContainer getNextContainer() {
            return nextContainer;
        }

        /**
         * Sets the next container to a specified container.
         * @param nextContainer The next container.
         */
        private void setNextContainer(HistoryContainer nextContainer) {
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
        private HistoryContainer getPreviousContainer() {
            return previousContainer;
        }

        /**
         * Sets the previous container to a specified container.
         * @param previousContainer The next container.
         */
        private void setPreviousContainer(HistoryContainer previousContainer) {
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
            return obj != null && (obj.getClass() == HistoryContainer.class &&
                    ((HistoryContainer) obj).getContainerID() == containerID);
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
