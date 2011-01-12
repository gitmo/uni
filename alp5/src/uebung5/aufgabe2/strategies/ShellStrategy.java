package uebung5.aufgabe2.strategies;

import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import uebung5.aufgabe2.JPanelStrategy;

public class ShellStrategy implements JPanelStrategy {

	public JPanel getJPanelForContent(InputStream inputStream) {
		
		String fileAsString = StreamUtil.getStreamAsString(inputStream);
		String processOutput = "";
		
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("sh");
			
			OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
			writer.write(fileAsString);
			writer.close();
			
			processOutput = StreamUtil.getStreamAsString(process.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JPanel panel = new JPanel(new GridLayout());
		
		//file:///home/cholin/Desktop/testscript.sh
		JTextArea scriptTextArea = new JTextArea(fileAsString);
		JPanel scriptPanel = new JPanel();
		scriptPanel.setLayout(new BoxLayout(scriptPanel, BoxLayout.Y_AXIS));
		scriptPanel.add(new JLabel("Input"));
		scriptPanel.add(scriptTextArea);
		
		JTextArea outputTextArea = new JTextArea(processOutput);
		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
		outputPanel.add(new JLabel("Output"));
		outputPanel.add(outputTextArea);
		
		panel.add(scriptPanel);
		panel.add(outputPanel);
		
		return panel;
		
	}	
	
	public boolean warnBeforeDispatch() {
		return true;
	}

}
