package uebung5.aufgabe1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Stellt mehrfach gebrauchte Funktionalität zur Verfügung
 * 
 */
public abstract class BaseWorker {

	final String persistenceHistogramFileName = "histogram.persistence";

	/**
	 * Läd eine serialisierte Map-Datenstruktur und gibt diese zurück
	 * 
	 * @return Map<String, Integer
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> loadHistogram() {
		Map<String, Integer> histogram = null;

		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(
					persistenceHistogramFileName));
			histogram = (Map<String, Integer>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			histogram = new HashMap<String, Integer>();
			this.saveHistogram(histogram);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return histogram;
	}

	/**
	 * Serialisiert eine Map-Datenstruktur und speichert diese als Datei ab
	 * 
	 * @param histogram
	 */
	protected void saveHistogram(Map<String, Integer> histogram) {

		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(
					persistenceHistogramFileName));
			oos.writeObject(histogram);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Lädt den Inhalt einer Datei in einen String
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getFileContent(String fileName) throws FileNotFoundException {
		// Reads file content
		BufferedReader reader = new BufferedReader(new FileReader(
				WebServer.class.getResource(fileName).getPath()));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				stringBuilder.append(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
}
