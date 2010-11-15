package uebung2.aufgabe1;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.net.URL;

public class Filter {

	public static void main(String[] args) throws IOException {
		PrintWriter out = null;

		if (args.length < 1) {
			System.err.println("Usage: java Filter input [output]");
			System.exit(2); // POSIX
		} else if (args.length < 2)
			// Optional arg: use stdout if no output file given.
			out = new PrintWriter(System.out);
		else
			// Wrap a BufferedWriter around the FileWriter as it may be costly.
			out = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));

		StreamTokenizer tokenizer = null;
		String res = resourceLoc(args[0]);
		try {
			tokenizer = new StreamTokenizer(new FileReader(res));
			tokenizer.lowerCaseMode(true);
		} catch (FileNotFoundException e) {
			System.err.printf("Input file %s was not found.\n", res);
			System.exit(-1);
		}

		Dictionary dictionary = new Dictionary();
		while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			if (tokenizer.ttype == StreamTokenizer.TT_WORD
					&& dictionary.contains(tokenizer.sval))
				out.println(tokenizer.sval);
		}

		out.close();
	}
	
	private static String resourceLoc(String file) {
		URL resource = Denglish.class.getResource(file);
		return resource == null ? file : resource.getPath();
	}
}
