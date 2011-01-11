package uebung5.uebung1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BaseWorker {

	final String persistenceHistogramFileName = "histogram.persistence";
	
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> loadHistogram(){
		 Map<String, Integer> histogram = null;
		
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(persistenceHistogramFileName));
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

	protected void saveHistogram(Map<String, Integer> histogram) {

		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(persistenceHistogramFileName));
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
}
