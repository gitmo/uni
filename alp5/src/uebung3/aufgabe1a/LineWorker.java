package uebung3.aufgabe1a;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

/**
 * @author cholin, gitmo
 * 
 */
public class LineWorker extends Thread {
	private ArrayList<String> lines;
	private int start, end;
	private SortedSet<String> localOcurrence, globalOcurrence;
	private static Dictionary dictionary = new Dictionary();

	/**
	 * @param globalOcurrence
	 * @param lines
	 * @param start
	 * @param end
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

	/*
	 * (non-Javadoc)
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
