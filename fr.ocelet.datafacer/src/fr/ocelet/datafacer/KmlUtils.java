/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/

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
