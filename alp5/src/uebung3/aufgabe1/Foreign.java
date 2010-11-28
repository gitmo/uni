package uebung3.aufgabe1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

public class Foreign {
	Dictionary dictionary = null;
	ArrayList<String> lines = null;
	final SortedSet<String> globalOcurrences = Collections.synchronizedSortedSet(new TreeSet<String>());

	
	private ArrayList<String> loadFile(String fileName) throws FileNotFoundException {
		String filePath = this.getClass().getResource(fileName).getPath();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		ArrayList<String> lines = new ArrayList<String>();
		
		String nextLine;
		try {
			while((nextLine = reader.readLine()) != null)
				lines.add(nextLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public void analyse(String fileName, int numberOfThreads) {
		try {
		 dictionary = new Dictionary();
		} catch(IOException e) {
			System.err.println("Could load dictionary!");
			return;
		}
		
		try {
			lines = this.loadFile(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			return;
		}
		
		
		int lineRange = lines.size() / numberOfThreads;
		int lineMod = lines.size()%numberOfThreads;
		
		Thread[] workers;
		if(lineMod == 0)
			workers = new Thread[lineRange];
		else
			workers = new Thread[lineRange+1];
			
		int i = 0;
		for(; i < lineRange; ++i)
			workers[i] = new Thread(new LineWorker(globalOcurrences, lines, i * lineRange, i*lineRange + lineRange));
		
		if(lineMod != 0)
			workers[i] = new Thread(new LineWorker(globalOcurrences, lines, i*lineRange, lines.size()));
		
		
		for(Thread thread : workers) {
			thread.start();
		}
		
		for(Thread thread : workers) {
			try {
				thread.join();
			} catch (InterruptedException e) { e.printStackTrace();	}
		}
		
		printOcurrences(globalOcurrences);
	}

	private void printOcurrences(SortedSet<String> ocurrences) {
		System.out.println("Ocurrences:");
		for(String word : ocurrences)
			System.out.println("\t" + word);
		
	}
	

	public static void main(String[] args) {
		Foreign foreign = new Foreign();
		
		foreign.analyse("test.txt", 2);
	}

}
