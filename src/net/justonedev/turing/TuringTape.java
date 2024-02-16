package net.justonedev.turing;

import net.justonedev.turing.collections.LimitlessBinaryMap;

import java.math.BigInteger;

/**
 * The tape for a turing machine. Stores any numerical value, so for characters use their
 * respective unicode index or ASCII code.
 * Provides functionality to go left and right, the head can start at any index,
 * but the machine needs to traverse to that point first.
 *
 * @author justonedeveloper
 */
public class TuringTape {

    // Todo redo @code section

    private static final boolean WRAP_AROUND = true;
    private static final String FILLER_TILE = "[ ] ";
    private static final String FILLER_TILE_SELECTED = "[> <] ";
    private static final String EMPTY_TAPE = "< >";

    private final BigInteger tapeSize;
    /**
     * Head location is used and necessary to creates tiles just as needed.
     */
    private BigInteger headPosition;
    private TuringTapeTile currentTapeTile;
    private TuringTapeTile firstTapeTile;
    private TuringTapeTile lastTapeTile;

    /**
     * Creates a new Turing Tape object. Needs to specify tapeSize and headPosition,
     * inserting values is optional.
     * Value insertion starts at zero by default, for filler values add null entries at the start.
     *
     * @throws IllegalArgumentException If any argument is invalid. Invalid argument cases are:
     * - Tape size is smaller than 1.
     * - Head position is negative.
     * - Head position is >= tape size. Equal is invalid since position functions as index.
     * - The tape is not long enough to store all values.
     * Please insure validity of arguments beforehand or catch thrown exceptions.
     *
     * @param tapeSize The size of the tape. Unchangeable.
     * @param headPosition The starting position (index) of the head of the turing machine.
     * @param values The contents of the tape.
     */
    public TuringTape(final long tapeSize, final long headPosition, final BigInteger... values) {
        this(BigInteger.valueOf(tapeSize), BigInteger.valueOf(headPosition), values);
    }

    /**
     * Creates a new Turing Tape object. Needs to specify tapeSize and headPosition,
     * inserting values is optional.
     * Value insertion starts at zero by default, for filler values add null entries at the start.
     *
     * @throws IllegalArgumentException If any argument is invalid. Invalid argument cases are:
     * - Tape size is smaller than 1.
     * - Head position is negative.
     * - Head position is >= tape size. Equal is invalid since position functions as index.
     * - The tape is not long enough to store all values.
     * Please insure validity of arguments beforehand or catch thrown exceptions.
     *
     * @param tapeSize The size of the tape. Unchangeable.
     * @param headPosition The starting position (index) of the head of the turing machine.
     * @param values The contents of the tape.
     */
    public TuringTape(final BigInteger tapeSize, final BigInteger headPosition, final BigInteger... values) {
        if (tapeSize.compareTo(BigInteger.ONE) < 0) {
            throw new IllegalArgumentException("Tape size may not be smaller than 1.");
        }
        if (headPosition.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Head position may not be negative.");
        }
        if (headPosition.compareTo(tapeSize) >= 0) {
            throw new IllegalArgumentException("Head position index must be smaller than the tape size.");
        }
        if (tapeSize.compareTo(BigInteger.valueOf(values.length)) < 0) {
            throw new IllegalArgumentException("Tape is not large enough to store the input.");
        }

        this.tapeSize = tapeSize;
        this.headPosition = headPosition;

        createTape(values);
    }

    /**
     * Creates the tape and assigns values;
     * @param values The tape values
     */
    private void createTape(BigInteger... values) {
        TuringTapeTile current = null;

        this.firstTapeTile = null;
        this.lastTapeTile = null;

        // Start with -1 to trigger if () even if values are empty
        BigInteger tilePos = BigInteger.ZERO;
        for (BigInteger value : values) {
            // Create a new tile and connect it
            TuringTapeTile newTile = new TuringTapeTile(tilePos, current, null, value);
            if (current != null) current.nextTile = newTile;
            current = newTile;
            if (tilePos.compareTo(this.headPosition) == 0) {
                this.currentTapeTile = newTile;
            }
            if (tilePos.compareTo(BigInteger.ZERO) == 0) {
                this.firstTapeTile = newTile;
            }
            if (tilePos.compareTo(tapeSize.subtract(BigInteger.ONE)) == 0) {
                this.lastTapeTile = newTile;
            }

            tilePos = tilePos.add(BigInteger.ONE);
        }

        // Insert a first tile
        if (this.firstTapeTile == null) {
            // None were created
            if (headPosition.compareTo(BigInteger.ZERO) == 0) {
                this.firstTapeTile = currentTapeTile;
            } else {
                this.firstTapeTile = new TuringTapeTile(BigInteger.ZERO, null, currentTapeTile, null);
            }
        }

        // Insert a last tile
        if (this.lastTapeTile == null) {
            // Tape size is larger than inputs
            this.lastTapeTile = new TuringTapeTile(tapeSize.subtract(BigInteger.ONE),
                    current == null ? this.firstTapeTile : current,
                    this.firstTapeTile,
                    null);
        }

        // If not yet accommodated and currentTapeTile is null
        if (headPosition.compareTo(tilePos) > 0) {
            if (current == null) {
                this.currentTapeTile = new TuringTapeTile(headPosition, firstTapeTile, lastTapeTile, null);
            } else {
                this.currentTapeTile = new TuringTapeTile(headPosition, current, lastTapeTile, null);
                current.nextTile = this.currentTapeTile;
            }
        }

        if (current != null && current.nextTile == null) {
            current.nextTile = lastTapeTile;
        }
        this.firstTapeTile.previousTile = this.lastTapeTile;
    }

    /**
     * Gets the current tile's value.
     * @return Current tile value. May be null.
     */
    public BigInteger getCurrentValue() {
        if (currentTapeTile == null) {
            return null;
        }
        return currentTapeTile.getValue();
    }

    /**
     * Sets the current tile's value. May be null.
     */
    public void setCurrentValue(BigInteger value) {
        if (currentTapeTile != null) {
            currentTapeTile.setValue(value);
        }
    }

    /**
     * Gets the current tile's value.
     * @return Current tile value. May be null.
     * Same as getCurrentValue();
     */
    public BigInteger read() {
        if (currentTapeTile == null) {
            return null;
        }
        return currentTapeTile.getValue();
    }

    /**
     * Sets the current tile's value. May be null.
     * Same as setCurrentValue();
     */
    public void write(BigInteger value) {
        if (currentTapeTile != null) {
            currentTapeTile.setValue(value);
        }
    }

    /**
     * Gets the current head position.
     * @return Head position
     */
    public BigInteger getHeadPosition() {
        return headPosition;
    }

    /**
     * Performs a move action on the tape.
     * If move action is NONE, does nothing, otherwise moves left or right.
     * @param moveAction The move action
     */
    public void move(MoveAction moveAction) {
        if (moveAction == MoveAction.NONE) return;
        if (moveAction == MoveAction.RIGHT) moveRight();
        else if (moveAction == MoveAction.LEFT) moveLeft();
    }

    /**
     * Moves left by one tile.
     * If the current tile does not exist in memory, a new tile will be created.
     * If property {@code WRAP_AROUND} is set to {@code true} and is on first tile,
     * will go to the right-most (last) tile.
     * If the head wrapped around to the other side, this method returns true, otherwise false.
     * If {@code WRAP_AROUND} is set to {@code false}, will always return false.
     *
     * @throws NullPointerException If the next tile is null. Although that shouldn't happen,
     * there is nothing you can do about that
     * @return If the move wrapped around to the other side.
     */
    public boolean moveLeft() {
        // All the way left:
        if (headPosition.compareTo(BigInteger.ZERO) == 0) {
            if (!WRAP_AROUND) {
                System.err.println("Cannot move left: Head Position is zero and wrap around is off.");
                return false;
            }
            this.headPosition = tapeSize.subtract(BigInteger.ONE);
            this.currentTapeTile = this.lastTapeTile;
            return true;
        }

        TuringTapeTile tile = currentTapeTile.getPreviousTile();
        // This shouldn't happen
        if (tile == null) throw new NullPointerException("Cannot move left: tile was null.");

        if (tile.getTilePosition().compareTo(headPosition.subtract(BigInteger.ONE)) < 0) {
            // Is NOT neighbouring tile. Create in-between tile
            this.currentTapeTile = new TuringTapeTile(
                    headPosition.subtract(BigInteger.ONE),
                    tile,
                    currentTapeTile,
                    null
            );
            this.headPosition = this.headPosition.subtract(BigInteger.ONE);
            return false;
        }

        this.currentTapeTile = tile;
        this.headPosition = this.headPosition.subtract(BigInteger.ONE);
        return false;
    }

    /**
     * Moves left by one tile.
     * If the current tile does not exist in memory, a new tile will be created.
     * If property {@code WRAP_AROUND} is set to {@code true} and is on first tile,
     * will go to the right-most (last) tile.
     * If the head wrapped around to the other side, this method returns true, otherwise false.
     * If {@code WRAP_AROUND} is set to {@code false}, will always return false.
     *
     * @throws NullPointerException If the next tile is null. Although that shouldn't happen,
     * there is nothing you can do about that
     * @return If the move wrapped around to the other side.
     */
    public boolean moveRight() {
        // All the way left:
        if (headPosition.compareTo(tapeSize.subtract(BigInteger.ONE)) == 0) {
            if (!WRAP_AROUND) {
                System.err.println("Cannot move right: Head Position is zero and wrap around is off.");
                return false;
            }
            this.headPosition = BigInteger.ZERO;
            this.currentTapeTile = this.firstTapeTile;
            return true;
        }

        TuringTapeTile tile = currentTapeTile.getNextTile();
        // This shouldn't happen
        if (tile == null) throw new NullPointerException("Cannot move right: tile was null.");

        if (tile.getTilePosition().compareTo(headPosition.add(BigInteger.ONE)) > 0) {
            // Is NOT neighbouring tile. Create in-between tile
            TuringTapeTile newCurrentTapeTile = new TuringTapeTile(
                    headPosition.add(BigInteger.ONE),
                    currentTapeTile,
                    tile,
                    null
            );
            currentTapeTile.nextTile = newCurrentTapeTile;
            currentTapeTile = newCurrentTapeTile;
            this.headPosition = this.headPosition.add(BigInteger.ONE);
            return false;
        }

        this.currentTapeTile = tile;
        this.headPosition = this.headPosition.add(BigInteger.ONE);
        return false;
    }

    /**
     * Returns the current tape as a String.
     * No character translations are applied.
     * @return Tape as String.
     */
    @Override
    public String toString() {
        return toString(null);
    }

    /**
     * Returns the current tape as a String while translating the integer values
     * that have a translation to their translation.
     *
     * @param translationMap The translation map. Can be partial.
     * @return The tape as String.
     */
    public String toString(LimitlessBinaryMap<BigInteger, String> translationMap) {
        if (this.firstTapeTile == null) return EMPTY_TAPE;  // Empty Tape
        
        StringBuilder builder = new StringBuilder();
        TuringTapeTile firstState = this.firstTapeTile;
        builder.append("{ Head: %d, Tape: <".formatted(headPosition));
        builder.append(firstState.toString(headPosition, translationMap));
        while (true) {
            TuringTapeTile next = firstState.getNextTile();
            BigInteger diff = next.getTilePosition().subtract(firstState.getTilePosition());
            if (diff.compareTo(BigInteger.ZERO) <= 0) {
                break;
            }
            builder.append(" ");
            if (diff.compareTo(BigInteger.ONE) == 0) {
                // Normal, just print the next one
                builder.append(next.toString(headPosition, translationMap));
            } else if (diff.compareTo(BigInteger.TWO) == 0) {
                // one in between
                builder.append(getFillerTile(firstState.tilePosition.add(BigInteger.ONE)));
                builder.append(next.toString(headPosition, translationMap));
            } else if (diff.compareTo(BigInteger.valueOf(3)) == 0) {
                // two in between
                builder.append(getFillerTile(firstState.tilePosition.add(BigInteger.ONE)));
                builder.append(getFillerTile(firstState.tilePosition.add(BigInteger.TWO)));
                builder.append(next.toString(headPosition, translationMap));
            } else {
                // more than two in between
                builder.append(getFillerTile(firstState.tilePosition.add(BigInteger.ONE)));
                // Why -3? Because:
                // We have a diff, but both borders are exclusive. Since when calculating the diff,
                // by default one border is inclusive, we need to subtract that on top of the 2 we
                // are drawing to depict the correct number.
                // In other words, when skipping from 7 to 14 (with diff 7), the tile 14 is also being drawn next.
                // When subtracting 2, we would be depicting the 14th with our second printed state, which is not
                // what's happening and would not make it clear what the contents of the ... are.
                builder.append("... %d ... ".formatted(diff.subtract(BigInteger.valueOf(3))));
                builder.append(getFillerTile(next.tilePosition.subtract(BigInteger.ONE)));
                builder.append(next.toString(headPosition, translationMap));
            }
            // Go to next
            firstState = next;
        }
        builder.append("> }");
        return builder.toString();
    }
    
    /**
     * Gets the Tape as a less readable, but shorter full string,
     * that is unique to this state of the tape. Different tapes
     * are guaranteed different unique strings.
     * Strings do contain the head position.
     *
     * @return The short unique string for this tape state.
     */
    public String getUniqueFullString() {
        if (this.firstTapeTile == null) return "<%d;>";  // Empty Tape
        
        StringBuilder builder = new StringBuilder();
        TuringTapeTile firstState = this.firstTapeTile;
        builder.append("%d".formatted(headPosition));
        builder.append(";%s".formatted(toUniqueStringSingleTile(firstState.value)));
        while (true) {
            TuringTapeTile next = firstState.getNextTile();
            if (next == null) {
                throw new NullPointerException("An unknown error occurred while generating a unique string: A state was null.");
            }
            BigInteger diff = next.getTilePosition().subtract(firstState.getTilePosition());
            if (diff.compareTo(BigInteger.ZERO) <= 0) {
                break;
            }
            builder.append(";%s".formatted(toUniqueStringSingleTile(next.value)));
            for (BigInteger i = BigInteger.ZERO; i.compareTo(diff.subtract(BigInteger.ONE)) < 0; i = i.add(BigInteger.ONE)) {
                builder.append(';');
            }
            // Go to next
            firstState = next;
        }
        return builder.toString();
    }
    
    /**
     * Make a single BigInt value to a String.
     * Returns empty String for null values.
     * @return Number as String or empty String.
     */
    private String toUniqueStringSingleTile(BigInteger number) {
        if (number == null) return "";
        return number.toString();
    }

    /**
     * Gets the appropriate filler tile, either marking as contains head or not.
     * Returns the empty tile with a head marker if the head position is the tile position.
     *
     * @param currentPosition The current position. Used for reference.
     * @return The Filler tile, either with head marker or normal.
     */
    private String getFillerTile(BigInteger currentPosition) {
        if (this.headPosition.compareTo(currentPosition) == 0)
            return FILLER_TILE_SELECTED;
        return FILLER_TILE;
    }

    /**
     * Object for a single Tile object.
     *
     * @author justonedeveloper
     */
    public static class TuringTapeTile {
        private BigInteger value;
        private TuringTapeTile previousTile;
        private TuringTapeTile nextTile;
        private final BigInteger tilePosition;

        /**
         * Creates a new Turing Tape Tile that can store neighbors and value.
         * All non-primitive parameters may be null.
         *
         * @param tilePosition The position of the tile. May not be null.
         * @param previous The previous (left) tile.
         * @param next The next (right) tile.
         * @param value The value
         */
        private TuringTapeTile(
                BigInteger tilePosition,
                TuringTapeTile previous,
                TuringTapeTile next,
                BigInteger value
        ) {
            this.tilePosition = tilePosition;
            this.previousTile = previous;
            this.nextTile = next;
            this.value = value;
        }

        /**
         * Gets the tile position.
         * @return The tile position.
         */
        private BigInteger getTilePosition() {
            return tilePosition;
        }

        /**
         * Gets the previous tile (left).
         * May be null.
         * @return The previous tile.
         */
        private TuringTapeTile getPreviousTile() {
            return previousTile;
        }

        /**
         * Gets the next tile (right).
         * May be null.
         * @return The next tile.
         */
        private TuringTapeTile getNextTile() {
            return nextTile;
        }

        /**
         * Gets the value of the current tile as BigInteger.
         * May be null if tile is empty.
         * @return Tile value.
         */
        private BigInteger getValue() {
            return value;
        }

        /**
         * Sets the value to the given BigInteger value.
         * Null is allowed.
         * @param integer the numerical value. May be null.
         */
        private void setValue(BigInteger integer) {
            value = integer;
        }

        /**
         * Gets the tile as String with its respective [] format.
         * @return The tile as String
         */
        @Override
        public String toString() {
            return "[%s]".formatted(getBigIntAsString(value));
        }

        /**
         * Gets the tile as String. Marks the tile with arrows if
         * the head position equals the tile position.
         *
         * @param headPosition The head position.
         * @return The tile as String with the BigInt as value.
         */
        public String toString(BigInteger headPosition) {
            if (headPosition.compareTo(this.tilePosition) == 0)
                return "[>%s<]".formatted(getBigIntAsString(value));
            return this.toString();
        }

        /**
         * Gets the tile as String with its respective [] format. Maps, if possible,
         * the BigInt value to its String representation as given by the map.
         *
         * @param translationMap The character translation map.
         * @return The tile as String
         */
        public String toString(LimitlessBinaryMap<BigInteger, String> translationMap) {
            return "[%s]".formatted(getBigIntAsString(value));
        }

        /**
         * Gets the tile as String. Marks the tile with arrows if
         * the head position equals the tile position. Maps, if possible,
         * the BigInt value to its String representation as given by the map.
         *
         * @param headPosition The head position.
         * @param translationMap The character translation map.
         * @return The tile as String with the BigInt as value.
         */
        public String toString(BigInteger headPosition, LimitlessBinaryMap<BigInteger, String> translationMap) {
            if (headPosition.compareTo(this.tilePosition) == 0)
                return "[>%s<]".formatted(getTranslation(value, translationMap));
            return this.toString(translationMap);
        }

        /**
         * Gets the translation for a BigInteger using the given map as lookup table.
         * Returns the BigInteger if the map does not contain the key or the map itself
         * is null.
         *
         * @param key The BigInteger key
         * @param map The translation map.
         * @return The String value or the BigInt as String.
         */
        private String getTranslation(BigInteger key, LimitlessBinaryMap<BigInteger, String> map) {
            if (map == null) return key.toString();
            String value = map.getValue(key);
            if (value != null) return value;
            return getBigIntAsString(key);
        }
        
        /**
         * Gets a BigInt as a String.
         * Different to the TuringMachine's method, converts
         * null to an empty String.
         * @param value The BigInteger
         * @return BigInteger as String
         */
        private static String getBigIntAsString(BigInteger value) {
            if (value == null) return " ";
            return value.toString();
        }
    }
}
