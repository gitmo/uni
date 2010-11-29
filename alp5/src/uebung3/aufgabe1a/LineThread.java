package uebung3.aufgabe1a;

import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

public class LineThread extends Thread {
	ArrayList<String> lines = null;
	int start = 0, end = 0;
	SortedSet<String> localOcurrence = new TreeSet<String>();
	SortedSet<String> globalOcurrence = null;
	Dictionary dictionary;

	/**
	 * @param globalOcurrence
	 * @param lines
	 * @param start
	 * @param end
	 */
	public LineThread(SortedSet<String> globalOcurrence,
			ArrayList<String> lines, int start, int end) {
		this.lines = lines;
		this.start = start;
		this.end = end;
		this.globalOcurrence = globalOcurrence;
		try {
			this.dictionary = new Dictionary();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String[] words = null;

		for (int currentLine = start; currentLine < end; ++currentLine) {
			words = lines.get(currentLine).split("\\s");

			for (String currentWord : words) {
				try {
					if (!dictionary.contains(currentWord))
						continue;
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (!localOcurrence.contains(currentWord)) {
					globalOcurrence.addAll(localOcurrence);

					if (!globalOcurrence.contains(currentWord))
						globalOcurrence.add(currentWord);

					localOcurrence.addAll(globalOcurrence);
				}
			}
		}

		globalOcurrence.addAll(localOcurrence);
	}
}
