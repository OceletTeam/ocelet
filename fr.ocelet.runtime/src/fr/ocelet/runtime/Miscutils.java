package fr.ocelet.runtime;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import fr.ocelet.runtime.ocltypes.Color;
import fr.ocelet.runtime.ocltypes.List;

/**
 * A series of utility static functions that are directly available from any
 * part of the Ocelet langage.
 * 
 * @author Pascal Degenne - Initial contribution
 * 
 */
public class Miscutils {

	private static DecimalFormatSymbols dfs = new DecimalFormatSymbols(
			new Locale("en"));

	/**
	 * Number formatter
	 * 
	 * @param n
	 *            Number to be formatted
	 * @param pattern
	 *            The formatting pattern
	 * @return A formatted String representation of number n, depending on the
	 *         given pattern
	 */
	public static String format(Number n, String pattern) {

		try {
			NumberFormat formatter = new DecimalFormat(pattern, dfs);
			return formatter.format(n);
		} catch (IllegalArgumentException e) {
			System.err.println("Could not format the number " + n + " : "
					+ e.getMessage());
			return "" + n;
		}
	}

	/**
	 * /** Number formatter
	 * 
	 * @param n
	 *            Number to be formatted
	 * @param pattern
	 *            The formatting pattern
	 * @param lang
	 *            Lowercase two-letter ISO-639 code representing a langage.
	 * @return A formatted String representation of number n, depending on the
	 *         given pattern
	 */
	public static String format(Number n, String pattern, String lang) {
		dfs = new DecimalFormatSymbols(new Locale(lang));
		return format(n, pattern);
	}

	/**
	 * Creates a shade of colors between two colors given in argument
	 * 
	 * @param nbSteps
	 *            Number of colors (minimum is 2)
	 * @param c1
	 *            First Color
	 * @param c2
	 *            Last Color
	 * @return A List<Color> containing the color range
	 */
	public static List<Color> colorRange(int nbSteps, Color c1, Color c2) {
		return c1.colorRange(nbSteps, c2);
	}

	/**
	 * Creates a shade of colors between two colors given in argument
	 * 
	 * @param nbSteps
	 *            Number of colors (minimum is 2)
	 * @param sc1
	 *            First Color in String format
	 * @param sc2
	 *            Last Color in String format
	 * @return A List<Color> containing the color range
	 */
	public static List<Color> colorRange(int nbSteps, String sc1, String sc2) {
		Color c1 = Color.parse(sc1);
		Color c2 = Color.parse(sc2);
		return c1.colorRange(nbSteps, c2);
	}

	/**
	 * Creates a new dir
	 * 
	 * @param dirpath
	 * @return True is the new dir was created and false if anything went wrong.
	 */
	public static Boolean createDir(String dirpath) {
		File df = new File(dirpath);
		Boolean done = df.mkdirs();
		if (!done)
			System.err
					.println("Sorry the directory "
							+ dirpath
							+ " could not be created. Please check its name and your rights for the destination path.");
		return done;
	}

	
	/**
	 * Deletes a directory and all its content. Without warning. Use with
	 * caution.
	 * 
	 * @param dirpath Path of the directory to be removed
	 */
	public static void removeDir(String dirpath) {
		Path dir = Paths.get(dirpath);
		try {
			Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir,
						IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			System.err
					.println("Sorry the directory "
							+ dirpath
							+ " could not be deleted. Please check its name and your rights for that operation.");
		}
	}

	
	/**
	 * Deletes a file. Without warning. Use with caution.
	 * 
	 * @param filepath The file to be removed
	 */
	public static void removeFile(String filepath) {
		try {
			Files.deleteIfExists(Paths.get(filepath));
		} catch (IOException e) {
			System.err
					.println("Sorry the file "
							+ filepath
							+ " could not be deleted. Please check its name and your rights for that operation.");
		}
	}

}
