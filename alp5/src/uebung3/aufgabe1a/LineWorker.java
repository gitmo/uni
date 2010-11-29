package uebung3.aufgabe1a;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

/**
 * LineWorker is a thread object to concurrently filter a text for occurrences
 * of foreign words. Each instance works on a disjunct range of lines.
 * 
 * @author cholin, gitmo
 * 
 */
public class LineWorker extends Thread {

	/**
	 * A list with all the lines of text and two indices specifying the first
	 * and last line to filter.
	 */
	private ArrayList<String> lines;
	private int start, end;

	/**
	 * A locally accumulated set of loan words a the reference to the global
	 * set.
	 */
	private SortedSet<String> localOcurrence, globalOcurrence;

	/**
	 * The dictionary of foreign words is static, so it is read just once.
	 */
	private static Dictionary dictionary = new Dictionary();

	/**
	 * Creates a LineWorker thread to filter disjunct range of lines of text for
	 * foreign words. A global set is synchronized with a local set to
	 * synchronize between threads.
	 * 
	 * @param globalOcurrence
	 *            A reference to the global set of words found.
	 * @param lines
	 *            A bunch of lines.
	 * @param start
	 *            First line in range.
	 * @param end
	 *            Last line in range.
	 */
	public LineWorker(SortedSet<String> globalOcurrence,
			ArrayList<String> lines, int start, int end) {
		this.lines = lines;
		this.start = start;
		// Truncate end index to the actual end if end was to big.
		this.end = Math.min(end, lines.size());
		this.localOcurrence = new TreeSet<String>();
		this.globalOcurrence = globalOcurrence;
	}

	/**
	 * Check for loan words and collect those in localOcurrence. Synchronize
	 * with a set of words global to all threads, whenever there's a new match.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String[] words;

		for (int currentLine = start; currentLine < end; ++currentLine) {
			// Split at word boundaries (Boundary matcher \b).
			words = lines.get(currentLine).split("\\b");

			for (String currentWord : words) {
				if (localOcurrence.contains(currentWord))
					continue;
				else if (dictionary.contains(currentWord)) {
					// Add word to local set and sync with global set.
					localOcurrence.add(currentWord);
					synchronized (globalOcurrence) {
						globalOcurrence.addAll(localOcurrence);
						localOcurrence.addAll(globalOcurrence);
					}
				}
			}
		}
	}
}
