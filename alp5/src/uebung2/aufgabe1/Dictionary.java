package uebung2.aufgabe1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Set;

class Dictionary {
	String dictionaryEnFile = "english.txt";
	String dictionaryDeFile = "german.txt";
	Set<String> dictionaryEn = new TreeSet<String>();
	Set<String> dictionaryDe = new TreeSet<String>();

	public Dictionary() throws IOException {
		readInMemory(dictionaryEnFile, dictionaryEn);
		readInMemory(dictionaryDeFile, dictionaryDe);
	}

	private void readInMemory(String file, Set<String> dict) throws IOException {
		BufferedReader dictionaryReader = null;
		try {
			String path = this.getClass().getResource(file).getPath();
			dictionaryReader = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			System.err.printf("ERROR: Dictionary file '%s' not found.\n", file);
		}

		String line = null;
		while ((line = dictionaryReader.readLine()) != null)
			dict.add(line.toLowerCase());
	}

	/**
	 * Word is in the English dictionary but has not the same spelling as a
	 * German word.
	 */
	public boolean contains(String s) throws IOException {
		String lower = s.toLowerCase();
		return dictionaryEn.contains(lower) && !dictionaryDe.contains(lower);
	}
}