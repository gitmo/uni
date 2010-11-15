package uebung2.aufgabe1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Set;

class Dictionary {
	String dictionaryFile = "english.txt";
	Set<String> dictionary = new TreeSet<String>();

	public Dictionary() throws IOException {
		BufferedReader dictionaryReader = null;
		try {
			String path = this.getClass().getResource(dictionaryFile).getPath();
			dictionaryReader = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			System.err.printf("ERROR: Dictionary file '%s' not found.\n",
					dictionaryFile);
		}

		String line = null;
		while ((line = dictionaryReader.readLine()) != null)
			this.dictionary.add(line.toLowerCase());
	}

	public boolean contains(String s) throws IOException {
		return dictionary.contains(s);
	}
}