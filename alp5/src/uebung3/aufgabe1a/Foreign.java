package uebung3.aufgabe1a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author cholin, gitmo
 * 
 */
public class Foreign {

	/**
	 * The default number of threads will be the number of CPU cores.
	 */
	private static int numThreads = Runtime.getRuntime().availableProcessors();

	/**
	 * Memory representation of the text file to be filtered.
	 */
	protected ArrayList<String> lines;

	/**
	 * Global set of all foreign words found by the worker threads.
	 */
	final protected SortedSet<String> globalOcurrences = Collections
			.synchronizedSortedSet(new TreeSet<String>());

	
	/**
	 * Create a Foreign word filter working on the specified file.
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public Foreign(String fileName) throws FileNotFoundException {
		lines = loadFile(fileName);
	}

	/**
	 * Loads a text file line by line into an ArrayList.
	 * 
	 * @param fileName
	 * @return An ArrayList which elements are the lines of text.
	 * 
	 * @throws FileNotFoundException
	 */
	protected ArrayList<String> loadFile(String fileName)
			throws FileNotFoundException {
		// Resolve resource given by file name to an absolute path if possible.
		String path = resourceLoc(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(path));
		ArrayList<String> lines = new ArrayList<String>();

		String nextLine;
		try {
			while ((nextLine = reader.readLine()) != null)
				lines.add(nextLine);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	/**
	 * Calls java.lang.Class.getResource() to get the absolute path to the given
	 * resource file name. If its not found it simply returns the file name
	 * again (which might already be an absolute path).
	 * 
	 * @param file
	 *            Name of the resource file.
	 * @return A String of the absolute path of file or just file again if not
	 *         found.
	 */
	private String resourceLoc(String file) {
		URL url = this.getClass().getResource(file);
		return url == null ? file : url.getPath();
	}

	/**
	 * Divide the text file up into equal chunks and filter them concurrently
	 * while maintaining a global list of words found so far. That list is
	 * synchronized by each worker thread.
	 */
	public void analyse() {

		LineWorker[] workers = new LineWorker[numThreads];

		// Divide in equally large chunks of text by the number of threads.
		// Add 1 so there's no rest.
		int range = lines.size() / numThreads + 1;

		// Divide and conquer
		for (int i = 0; i < numThreads; ++i) {
			// LineWorker adjusts end index if we're at the end of file.
			workers[i] = new LineWorker(globalOcurrences, lines, i * range,
					(i + 1) * range);
			workers[i].start();
		}

		// Wait for all threads to finish
		for (Thread thread : workers) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		printOcurrences(globalOcurrences);
	}

	/**
	 * Shows the global list of all the foreign words found so far.
	 */
	protected synchronized void printOcurrences(Set<String> ocurrences) {
		System.out.println("Ocurrences:");
		for (String word : ocurrences)
			System.out.println("\t" + word);
	}

	/**
	 * Test program.
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Usage: java Foreign file [threads]");
			System.exit(2);
		} else if (args.length > 1) {
			numThreads = Integer.parseInt(args[1]);
		}

		Foreign foreign;
		try {
			foreign = new Foreign(args[0]);
			foreign.analyse();
		} catch (FileNotFoundException e) {
			System.err
					.printf("Foreign: %s: No such file or directory", args[0]);
			System.exit(1);
		}
	}
}
