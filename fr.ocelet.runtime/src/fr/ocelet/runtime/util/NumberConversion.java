package fr.ocelet.runtime.util;

/**
 * Number type conversion simple utility class
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
public class NumberConversion {
	
	public static Integer convert2Int(Double value) {
		return value.intValue();
	}

	public static Float convert2Float(Double value) {
		return value.floatValue();
	}

	public static Long convert2Long(Double value) {
		return value.longValue();
	}

}
