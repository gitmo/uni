package uebung2.aufgabe1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Merge {
	public static void main(String[] args) {
		if(args.length < 2) {
			System.err.println("Usage: java Merge pipe1 pipe2");
			System.exit(0);
		}
		
		final ConcurrentHashMap<String, Integer> histogram = new ConcurrentHashMap<String, Integer>();
		
		class Worker implements Runnable {
			BufferedReader reader = null;
			
			public Worker(String input) {
				try {
					reader = new BufferedReader(new FileReader(input));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

			public void run() {
				if(reader == null) {
					System.err.println("No input given");
					return;
				}
				
				String line = null;
				try {
					while((line = reader.readLine()) != null) {
						if(histogram.containsKey(line))
							histogram.put(line,histogram.get(line)+1);
						else
							histogram.put(line, 1);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
						
			}
		}
		
		Thread t1 = new Thread(new Worker(args[0]));
		Thread t2 = new Thread(new Worker(args[1]));
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		printHistogram(histogram);
	}

	private static void printHistogram(
			ConcurrentHashMap<String, Integer> histogram) {
		
		for(String word : histogram.keySet())
			System.out.println(word + ":\t" + histogram.get(word));
		
	}
}
