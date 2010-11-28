package uebung3.aufgabe1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

public class LineWorker implements Runnable {
	ArrayList<String> lines = null;
	int start = 0, end = 0;
	SortedSet<String> localOcurrence = new TreeSet<String>();
	SortedSet<String> globalOcurrence = null;
	Dictionary dictionary;
	
	public LineWorker(SortedSet<String> globalOcurrence, ArrayList<String> lines, int start, int end) {
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
	
	public void run() {
		String[] words = null;
		
		for(int currentLine = start; currentLine < end; ++currentLine) {
			words = lines.get(currentLine).split("\\s");
			
			for(String currentWord : words) {
				try {
					if(!dictionary.contains(currentWord))
						continue;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(!localOcurrence.contains(currentWord)) {
					globalOcurrence.addAll(localOcurrence);
					
					if(!globalOcurrence.contains(currentWord))
						globalOcurrence.add(currentWord);
					
					localOcurrence.addAll(globalOcurrence);
				}	
			}
		}
		
		globalOcurrence.addAll(localOcurrence);
	}
}
