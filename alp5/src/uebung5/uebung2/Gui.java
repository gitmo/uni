package uebung5.uebung2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;

public class Gui {

	private JFrame SimpleBrowser = null;
	private JPanel Main = null;
	private JTabbedPane panelTabs = null;
	private JPanel panelAddress = null;
	private JPanel tab = null;
	private JLabel labelUrl = null;
	private JTextField textUrl = null;
	private JPanel tabMain = null;
	private JPanel newTab = null;
	private JButton buttonUrl = null;

	/**
	 * This method initializes SimpleBrowser
	 * 
	 * @return javax.swing.JFrame
	 */
	protected JFrame getSimpleBrowser() {
		if (SimpleBrowser == null) {
			SimpleBrowser = new JFrame();
			SimpleBrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			SimpleBrowser.setSize(800, 600);
			SimpleBrowser.setContentPane(getMain());
			SimpleBrowser.setTitle("SimpleBrowser");
		}
		return SimpleBrowser;
	}

	/**
	 * This method initializes Main
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMain() {
		if (Main == null) {
			Main = new JPanel();
			Main.setLayout(new BorderLayout());
			Main.add(getPanelTabs(), BorderLayout.CENTER);
		}
		return Main;
	}

	/**
	 * This method initializes panelTabs	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getPanelTabs() {
		if (panelTabs == null) {
			panelTabs = new JTabbedPane();
			panelTabs.addTab("current Tab", null, getTab(), null);
			panelTabs.addTab("new Tab", null, getNewTab(), null);
		}
		return panelTabs;
	}

	/**
	 * This method initializes panelAddress	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelAddress() {
		if (panelAddress == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setColumns(1);
			labelUrl = new JLabel();
			labelUrl.setText("Url");
			labelUrl.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			labelUrl.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			labelUrl.setSize(new Dimension(5, 15));
			labelUrl.setHorizontalAlignment(SwingConstants.CENTER);
			labelUrl.setHorizontalTextPosition(SwingConstants.CENTER);
			labelUrl.setVerticalAlignment(SwingConstants.CENTER);
			panelAddress = new JPanel();
			panelAddress.setLayout(gridLayout);
			panelAddress.add(labelUrl, null);
			panelAddress.add(getTextUrl(), null);
			panelAddress.add(getButtonUrl(), null);
		}
		return panelAddress;
	}

	/**
	 * This method initializes tab	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTab() {
		if (tab == null) {
			tab = new JPanel();
			tab.setLayout(new BoxLayout(getTab(), BoxLayout.Y_AXIS));
			tab.add(getPanelAddress(), null);
			tab.add(getTabMain(), null);
		}
		return tab;
	}

	/**
	 * This method initializes textUrl	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTextUrl() {
		if (textUrl == null) {
			textUrl = new JTextField();
			textUrl.setText("http://www....");
		}
		return textUrl;
	}

	/**
	 * This method initializes tabMain	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTabMain() {
		if (tabMain == null) {
			tabMain = new JPanel();
			tabMain.setLayout(new GridBagLayout());
		}
		
		return tabMain;
	}

	/**
	 * This method initializes newTab	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNewTab() {
		if (newTab == null) {
			newTab = new JPanel();
			newTab.setLayout(new GridBagLayout());
			newTab.setName("new Tab");
		}
		return newTab;
	}

	/**
	 * This method initializes buttonUrl	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonUrl() {
		if (buttonUrl == null) {
			buttonUrl = new JButton();
			buttonUrl.setText("Go");
			
			buttonUrl.addActionListener(new ActionListener() {
				SimpleBrowser simpleBrowser = new SimpleBrowser();
				
				@Override
				public void actionPerformed(ActionEvent e) {
					tabMain.removeAll();

					JLabel label = new JLabel("Loading...");
					tabMain.add(label);
					tabMain.repaint();
					
					buttonUrl.setEnabled(false);
					
					label = simpleBrowser.dispatchUrl(getTextUrl().getText());

					tabMain.removeAll();
					tabMain.add(label);
					tabMain.repaint();
					buttonUrl.setEnabled(true);
				}
			});
		}
		return buttonUrl;
	}

}
