package uebung2.aufgabe1;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;

public class Filter {
	
	public static void main(String[] args) throws IOException {
		if(args.length < 2) {
			System.err.println("Usage: java Filter inputFile outputPipe");
			System.exit(0);
		}
		
		BufferedWriter outputPipe = null;
		try {
			outputPipe = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1])));
		} catch (FileNotFoundException e) {
			System.err.println("Pipe was not found.");
		}
		
		StreamTokenizer tokenizer = null;
		try {
			tokenizer = new StreamTokenizer(new FileReader(args[0]));
			tokenizer.lowerCaseMode(true);
		} catch (FileNotFoundException e) {
			System.err.println("Input file was not found.");
		}
		
		Dictionary dictionary = new Dictionary();
		while(tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			if(tokenizer.ttype != StreamTokenizer.TT_WORD)
				continue;
			
			if(dictionary.contains(tokenizer.sval))
				outputPipe.write(tokenizer.sval + "\n");
		}
		
		outputPipe.close();
	}
}
