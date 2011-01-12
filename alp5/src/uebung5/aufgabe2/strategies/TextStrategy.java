package uebung5.aufgabe2.strategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import uebung5.aufgabe2.JPanelStrategy;

public class TextStrategy implements JPanelStrategy {

	@Override
	public JPanel getJPanelForContent(InputStream inputStream) {
		JPanel panel = new JPanel();
		
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		String line;
		try {
			while((line = reader.readLine()) != null)
				sb.append(line + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JTextArea textArea = new JTextArea(sb.toString(), 50, 50);
		
		panel.add(new JScrollPane(textArea));
		
		return panel;
	}

}
