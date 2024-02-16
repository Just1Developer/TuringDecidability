package net.justonedev.turing;

import net.justonedev.turing.collections.LimitlessBinaryMap;

import java.math.BigInteger;

/**
 * Conversion class to get specific types of maps from inputs or other util stuff.
 *
 * @author justonedeveloper
 */
public final class Convert {
	
	/**
	 * Private Constructor.
	 */
	private Convert() { }
	
	/**
	 * Adds all characters from a String to the map and automatically assigns them a BigInteger.
	 * This method is not a general feature from LimitlessBinaryMap because of the types K and V needing to match.
	 * <p></p>
	 * The input is split into it's unicode/ASCII components using a split call with empty regex. If you wish to
	 * combine some characters, use the getTranslationMapAuto(String... input) variant.
	 *
	 * @param input The String input.
	 * @return The new translation map.
	 */
	public static LimitlessBinaryMap<BigInteger, String> getTranslationMapFromSingleString(String input) {
		return getTranslationMapAuto(input.split(""));
	}
	
	/**
	 * Adds all characters from a String to the map and automatically assigns them a BigInteger.
	 * This method is not a general feature from LimitlessBinaryMap because of the types K and V needing to match.
	 * @param input The String inputs.
	 * @return The new translation map.
	 */
	public static LimitlessBinaryMap<BigInteger, String> getTranslationMapAuto(String... input) {
		LimitlessBinaryMap<BigInteger, String> map = new LimitlessBinaryMap<>();
		autofillCharsToMap(map, input);
		return map;
	}
	
	/**
	 * Adds all characters from a String to the map and automatically assigns them a BigInteger.
	 * This method is not a general feature from LimitlessBinaryMap because of the types K and V needing to match.
	 * @param map The map to insert the chars into.
	 * @param input The String input.
	 */
	public static void autofillCharsToMap(LimitlessBinaryMap<BigInteger, String> map, String... input) {
		BigInteger counter = BigInteger.ZERO;
		for (String in : input) {
			if (map.containsValue(in)) continue;
			// Count to next free
			while (map.containsKey(counter)) counter = counter.add(BigInteger.ONE);
			
			map.add(counter, in);
			// So we don't needlessly loop over the map
			counter = counter.add(BigInteger.ONE);
		}
	}
	
	/**
	 * Adds all objects from a type V to the map and automatically assigns them a BigInteger.
	 * This method is not a general feature from LimitlessBinaryMap because Keys need to be of type BigInteger.
	 *
	 * @param input The inputs.
	 * @return The new translation map.
	 */
	@SafeVarargs
	public static <V> LimitlessBinaryMap<BigInteger, V> getAnyTranslationMapAuto(V... input) {
		LimitlessBinaryMap<BigInteger, V> map = new LimitlessBinaryMap<>();
		autofillAnyToMap(map, input);
		return map;
	}
	
	/**
	 * Adds all objects from a type V to the map and automatically assigns them a BigInteger.
	 * This method is not a general feature from LimitlessBinaryMap because Keys need to be of type BigInteger.
	 *
	 * @param map The map to insert the objects into.
	 * @param input The input.
	 */
	@SafeVarargs
	public static <V> void autofillAnyToMap(LimitlessBinaryMap<BigInteger, V> map, V... input) {
		BigInteger counter = BigInteger.ZERO;
		for (V in : input) {
			if (map.containsValue(in)) continue;
			// Count to next free
			while (map.containsKey(counter)) counter = counter.add(BigInteger.ONE);
			
			map.add(counter, in);
			// So we don't needlessly loop over the map
			counter = counter.add(BigInteger.ONE);
		}
	}
}
