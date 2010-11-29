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
	 * 
	 */
	protected ArrayList<String> lines;

	/**
	 * 
	 */
	final protected SortedSet<String> globalOcurrences = Collections
			.synchronizedSortedSet(new TreeSet<String>());

	public Foreign(String fileName) {
		try {
			lines = this.loadFile(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			return;
		}
	}

	/**
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	protected ArrayList<String> loadFile(String fileName)
			throws FileNotFoundException {
		String filePath = resourceLoc(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
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
	 * @param fileName
	 */
	public void analyse() {

		int lineRange = lines.size() / numThreads;
		int lineMod = lines.size() % numThreads;

		LineWorker[] workers = new LineWorker[numThreads];

		int i = 0;
		for (; i < numThreads - 1; ++i)
			workers[i] = new LineWorker(globalOcurrences, lines, i * lineRange,
					i * lineRange + lineRange);

		workers[i] = new LineWorker(globalOcurrences, lines, i * lineRange, i
				* lineRange + lineRange + lineMod);

		for (Thread thread : workers)
			if (thread != null)
				thread.start();

		for (Thread thread : workers) {
			if (thread != null) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		printOcurrences(globalOcurrences);
	}

	/**
	 * @param ocurrences
	 * @return
	 */
	protected synchronized void printOcurrences(Set<String> ocurrences) {
		System.out.println("Ocurrences:");
		for (String word : ocurrences)
			System.out.println("\t" + word);

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws InterruptedException,
			FileNotFoundException {

		if (args.length < 1) {
			System.err.println("Usage: java Foreign file [threads]");
			System.exit(2);
		} else if (args.length > 1) {
			numThreads = Integer.parseInt(args[1]);
		}

		Foreign foreign = new Foreign(args[0]);
		foreign.analyse();
	}
}
