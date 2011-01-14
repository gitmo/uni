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

	final String persistenceStatisticFileName = "statistic.persistence";

	/**
	 * Läd eine serialisierte Map-Datenstruktur und gibt diese zurück
	 * 
	 * @return Map<String, Integer
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> loadStatistic() {
		Map<String, Integer> statistic = null;

		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(
					persistenceStatisticFileName));
			statistic = (Map<String, Integer>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			statistic = new HashMap<String, Integer>();
			this.saveStatistic(statistic);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return statistic;
	}

	/**
	 * Serialisiert eine Map-Datenstruktur und speichert diese als Datei ab
	 * 
	 * @param statistic
	 */
	protected void saveStatistic(Map<String, Integer> statistic) {

		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(
					persistenceStatisticFileName));
			oos.writeObject(statistic);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
}
