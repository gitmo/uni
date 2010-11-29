package uebung3.aufgabe1a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

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
	protected Dictionary dictionary;

	/**
	 * 
	 */
	protected ArrayList<String> lines;

	/**
	 * 
	 */
	final protected SortedSet<String> globalOcurrences;

	/**
	 * 
	 */
	public Foreign() {
		globalOcurrences = Collections
				.synchronizedSortedSet(new TreeSet<String>());

		try {
			dictionary = new Dictionary();
		} catch (IOException e) {
			System.err.println("Could not load dictionary!");
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
		String filePath = this.getClass().getResource(fileName).getPath();
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
	 * @param fileName
	 */
	public void analyse(String fileName) {
		try {
			lines = this.loadFile(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			return;
		}

		int lineRange = lines.size() / numThreads;
		int lineMod = lines.size() % numThreads;

		LineWorker[] workers = new LineWorker[numThreads];

		int i = 0;
		for (; i < numThreads - 1; ++i)
			workers[i] = new LineWorker(globalOcurrences, lines, i
					* lineRange, i * lineRange + lineRange);

		workers[i] = new LineWorker(globalOcurrences, lines, i
				* lineRange, i * lineRange + lineRange + lineMod);

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
	 */
	protected void printOcurrences(Set<String> ocurrences) {
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

		Foreign foreign = new Foreign();
		foreign.analyse(args[0]);
	}
}
