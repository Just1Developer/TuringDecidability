package net.justonedev.turing;

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

    private final int tapeSize;
    /**
     * Head location is used and necessary to creates tiles just as needed.
     */
    private int headPosition;
    private TuringTapeTile currentTapeTile;
    private TuringTapeTile firstTapeTile;
    private TuringTapeTile lastTapeTile;

    // Todo startAt parameter
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
    public TuringTape(final int tapeSize, final int headPosition, final BigInteger... values) {
        if (tapeSize < 1) {
            throw new IllegalArgumentException("Tape size may not be smaller than 1.");
        }
        if (headPosition < 0) {
            throw new IllegalArgumentException("Head position may not be negative.");
        }
        if (headPosition >= tapeSize) {
            throw new IllegalArgumentException("Head position index must be smaller than the tape size.");
        }
        if (values.length > tapeSize) {
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
        int tilePos = -1;
        for (BigInteger value : values) {
            tilePos++;
            // Create a new tile and connect it
            TuringTapeTile newTile = new TuringTapeTile(tilePos, current, null, value);
            if (current != null) current.nextTile = newTile;
            current = newTile;
            if (tilePos == this.headPosition) {
                this.currentTapeTile = newTile;
            }
            if (tilePos == 0) {
                this.firstTapeTile = newTile;
            }
            if (tilePos == tapeSize - 1) {
                this.lastTapeTile = newTile;
            }
        }

        // Insert a first tile
        if (this.firstTapeTile == null) {
            // None were created
            if (headPosition == 0) {
                this.firstTapeTile = currentTapeTile;
            } else {
                this.firstTapeTile = new TuringTapeTile(0, null, currentTapeTile, null);
            }
        }

        // Insert a last tile
        if (this.lastTapeTile == null) {
            // Tape size is larger than inputs
            this.lastTapeTile = new TuringTapeTile(tapeSize - 1,
                    current == null ? this.firstTapeTile : current,
                    this.firstTapeTile,
                    null);
        }

        // If not yet accommodated and currentTapeTile is null
        if (headPosition > tilePos) {
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
    public int getHeadPosition() {
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
        if (headPosition == 0) {
            if (!WRAP_AROUND) {
                System.err.println("Cannot move left: Head Position is zero and wrap around is off.");
                return false;
            }
            this.headPosition = tapeSize - 1;
            this.currentTapeTile = this.lastTapeTile;
            return true;
        }

        TuringTapeTile tile = currentTapeTile.getPreviousTile();
        // This shouldn't happen
        if (tile == null) throw new NullPointerException("Cannot move left: tile was null.");

        if (tile.getTilePosition() < headPosition - 1) {
            // Is NOT neighbouring tile. Create in-between tile
            this.currentTapeTile = new TuringTapeTile(
                    headPosition - 1,
                    tile,
                    currentTapeTile,
                    null
            );
            this.headPosition--;
            return false;
        }

        this.currentTapeTile = tile;
        this.headPosition--;
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
        if (headPosition == tapeSize - 1) {
            if (!WRAP_AROUND) {
                System.err.println("Cannot move right: Head Position is zero and wrap around is off.");
                return false;
            }
            this.headPosition = 0;
            this.currentTapeTile = this.firstTapeTile;
            return true;
        }

        TuringTapeTile tile = currentTapeTile.getNextTile();
        // This shouldn't happen
        if (tile == null) throw new NullPointerException("Cannot move right: tile was null.");

        if (tile.getTilePosition() > headPosition + 1) {
            // Is NOT neighbouring tile. Create in-between tile
            TuringTapeTile newCurrentTapeTile = new TuringTapeTile(
                    headPosition + 1,
                    currentTapeTile,
                    tile,
                    null
            );
            currentTapeTile.nextTile = newCurrentTapeTile;
            currentTapeTile = newCurrentTapeTile;
            this.headPosition++;
            return false;
        }

        this.currentTapeTile = tile;
        this.headPosition++;
        return false;
    }
    
    @Override
    public String toString() {
        if (this.firstTapeTile == null) return EMPTY_TAPE;  // Empty Tape
        
        StringBuilder builder = new StringBuilder();
        TuringTapeTile firstState = this.firstTapeTile;
        builder.append("{ Head: %d, Tape: <".formatted(headPosition));
        builder.append(firstState.toString(headPosition));
        while (true) {
            TuringTapeTile next = firstState.getNextTile();
            int diff = next.getTilePosition() - firstState.getTilePosition();
            if (diff <= 0) {
                break;
            }
            builder.append(" ");
            switch (diff) {
                case 1:
                    // Normal, just print the next one
                    builder.append(next.toString(headPosition));
                    break;
                case 2:
                    // one in between
                    builder.append(getFillerTile(firstState.tilePosition + 1));
                    builder.append(next.toString(headPosition));
                    break;
                case 3:
                    // two in between
                    builder.append(getFillerTile(firstState.tilePosition + 1));
                    builder.append(getFillerTile(firstState.tilePosition + 2));
                    builder.append(next.toString(headPosition));
                    break;
                default:
                    // more than two in between
                    builder.append(getFillerTile(firstState.tilePosition + 1));
                    // Why -3? Because:
                    // We have a diff, but both borders are exclusive. Since when calculating the diff,
                    // by default one border is inclusive, we need to subtract that on top of the 2 we
                    // are drawing to depict the correct number.
                    // In other words, when skipping from 7 to 14 (with diff 7), the tile 14 is also being drawn next.
                    // When subtracting 2, we would be depicting the 14th with our second printed state, which is not
                    // what's happening and would not make it clear what the contents of the ... are.
                    builder.append("... %d ... ".formatted(diff - 3));
                    builder.append(getFillerTile(next.tilePosition - 1));
                    builder.append(next.toString(headPosition));
                    break;
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
            int diff = next.getTilePosition() - firstState.getTilePosition();
            if (diff <= 0) {
                break;
            }
            builder.append(";%s".formatted(toUniqueStringSingleTile(next.value)));
            builder.append(";".repeat(diff - 1));
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
    
    private String getFillerTile(int headPosition) {
        if (this.headPosition == headPosition)
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
        private final int tilePosition;

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
                int tilePosition,
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
        private int getTilePosition() {
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
        
        @Override
        public String toString() {
            return "[%s]".formatted(getBigIntAsString(value));
        }
        
        public String toString(int headPosition) {
            if (headPosition == this.tilePosition)
                return "[>%s<]".formatted(getBigIntAsString(value));
            return this.toString();
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
