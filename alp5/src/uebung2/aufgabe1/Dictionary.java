package uebung2.aufgabe1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


class Dictionary {
	String dictionaryFile = "english.txt";
	
	public boolean contains(String var) throws IOException {
		String path = this.getClass().getResource(dictionaryFile).getPath();
		BufferedReader dictionaryReader = new BufferedReader(new FileReader(path));
		
		String line = null;
		
		while((line = dictionaryReader.readLine()) != null) {
			if(line.toLowerCase().equals(var))//equals(var))
				return true;
		}
		return false;
	}
}