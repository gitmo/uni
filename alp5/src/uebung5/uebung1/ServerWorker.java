package uebung5.uebung1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerWorker extends BaseWorker implements Runnable {

	final static String persistenceFileName = "counter.persistence";
	int port;
	
	public ServerWorker(int port) {
		this.port = port;
	}
	
	public String getFileContent(String fileName) throws FileNotFoundException {
		//Reads file content
		BufferedReader reader = new BufferedReader(new FileReader(WebServer.class.getResource(fileName).getPath()));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try {
			while((line = reader.readLine()) != null)
				stringBuilder.append(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
	public Map<String, String> getHeaderFields(InputStream stream) {
		BufferedReader requestStream = new BufferedReader(new InputStreamReader(stream));
		
		Map<String, String> map = new HashMap<String,String>();
		String line = null;
		
		/*TODO* error checking */
		try {
			map.put(null, requestStream.readLine());
		while(!(line = requestStream.readLine()).equals("")) {
				int colonIndex = line.indexOf(':');
				
				String fieldName = line.substring(0,colonIndex).trim();
				String fieldValue = line.substring(colonIndex+1).trim();
				
				map.put(fieldName, fieldValue);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public String loadCounter(String responseMsg) {
		final String placeholder = "<!-- COUNTER -->";
		
		
		Integer counter = 0;
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(persistenceFileName));
			counter = ois.readInt();
			ois.close();
			
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(persistenceFileName));
			oos.writeInt(counter + 1);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseMsg.replace(placeholder, counter.toString());
	}
	@Override
	public void run() {
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			while (true) {
				// Anfrage entgegennehmen
				Socket connection = socket.accept();
	
				String responseMessage = loadCounter(getFileContent("404.html"));
	
				Map<String, String> fieldMap = this.getHeaderFields(connection.getInputStream());
				
				OutputStreamWriter responseStream = new OutputStreamWriter(connection.getOutputStream());
				responseStream.append(responseMessage);
				responseStream.flush();
				connection.close();
				
				Map<String, Integer> histogram = this.loadHistogram();
				String userAgent = fieldMap.get("User-Agent");
				if (histogram .containsKey(userAgent))
					histogram.put(userAgent, histogram.get(userAgent) + 1);
				else
					histogram.put(userAgent, 1);
				this.saveHistogram(histogram);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
