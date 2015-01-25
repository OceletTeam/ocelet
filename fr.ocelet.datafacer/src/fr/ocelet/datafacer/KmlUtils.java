package fr.ocelet.datafacer;

/**
 * Utility instructions to be used with the Kml datafacer
 * 
 * @author Pascal Degenne, Initial contributor.
 */
public class KmlUtils {

	/**
	 * Produces a hexadecimal string representation of an opaque color
	 * compliant with the Kml syntax.
	 * The expression produced is aabbggrr, where aa=alpha (00 to ff); bb=blue (00 to ff); gg=green (00 to ff); rr=red (00 to ff).
	 * @param red A value between 0 and 255
	 * @param green A value between 0 and 255
	 * @param blue A value between 0 and 255
	 * @return The Kml formatted color String
	 */
	public static String kmlColorRGB(int red, int green, int blue) {
		return kmlColorRGBT(red,green,blue,255);
	}

	/**
	 * 
	 * Produces a hexadecimal string representation of a transparent color
	 * compliant with the Kml syntax :
	 * The expression produced is aabbggrr, where aa=alpha (00 to ff); bb=blue (00 to ff); gg=green (00 to ff); rr=red (00 to ff).
	 * @param red A value between 0 and 255
	 * @param green A value between 0 and 255
	 * @param blue A value between 0 and 255
	 * @param transp A value between 0 and 255 for transparency : 0 is insivible and 255 is fully opaque.
	 * @return The Kml formatted color String
	 */
	public static String kmlColorRGBA(int red, int green, int blue, int alpha){
		return toHex2Digit(alpha)+toHex2Digit(blue)+toHex2Digit(green)+toHex2Digit(red);
	}

	@Deprecated
	public static String kmlColorRGBT(int red, int green, int blue, int transp){
		return kmlColorRGBA(red,green,blue,transp);
	}
	
	
	private static String toHex2Digit(int a) {
		String prefix = "";
		if (a % 256 < 16)
			prefix = "0";
		return prefix + Integer.toHexString(a % 256);
	}
	
}
