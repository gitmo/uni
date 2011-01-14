package uebung5.aufgabe2.strategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtil {

	/**
	 * Liest den Inhalt eines Streams in einen String
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String getStreamAsString(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
