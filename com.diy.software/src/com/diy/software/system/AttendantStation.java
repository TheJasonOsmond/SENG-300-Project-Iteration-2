package com.diy.software.system;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.OverloadException;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * 
 * Attendant station GUI from iteration 1 adapted to use with iteration 2
 * 
 * @author Benjamin Niles
 *
 */
public class AttendantStation{
	
	public AttendantStation(CustomerData[] c) {
		
		//can add multiple checkout stations
		//will need to implement a way to select them later
		system = new ArrayList<DIYSystem>();
		
		for(int i =0; i<c.length;i++) {
			DIYSystem diy = new DIYSystem(c[i]);
			system.add(diy);
		}
		
		currentDIY = system.get(0).getMainWindow();
		diyNum = 0;//change this depending on which diy station is selected
		
		initialize();
	}
	
	
	private JFrame frame = new JFrame();
	//private JTextPane ItemList = new JTextPane();
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel infoPanel;
	private JPanel itemPanel;
	private JPanel printerPanel;
	
	private JLabel stationIDLabel;
	private JLabel billLabel;
	private JLabel totalLabel;
	//private JLabel weightLabel;
	private JLabel alertLabel;
	private JLabel inkLabel;
	private JLabel paperLabel;
	
	private JButton addInkButton;
	private JButton addPaperButton;
	private JButton enableStationButton;
	
	private JTextArea billTextArea;
	
	private int diyNum;
	private DiyInterface currentDIY;
	
	private ArrayList<DIYSystem> system;
	
	public void initialize(){

		frameSetup();
		buildLeftPanel();
		buildRightPanel();

		frame.pack();
		addListeners();
		
		frame.setVisible(true);
	}
	
		
	private void frameSetup() {
		
		frame = new JFrame("ATTENDANT STATION");
		frame.setMinimumSize(new Dimension(600,300));
		frame.setMaximumSize(new Dimension(600,300));
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridLayout(0,2));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	private void buildRightPanel() {
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		billLabel = new JLabel("Bill", JLabel.CENTER);
		
		
		//maybe make this a JTable or JList in future iterations- easier to find and remove specific items than lines of text
		billTextArea = new JTextArea(1,1);
		billTextArea.setEditable(false);
		JScrollPane scrollBill = new JScrollPane(billTextArea);
		
		
		infoPanel = new JPanel(new GridLayout());
		totalLabel = new JLabel("total: "+0);
		//weightLabel = new JLabel("weight: "+0.0);
		
		infoPanel.add(totalLabel);
		//infoPanel.add(weightLabel);
		
		rightPanel.add(billLabel, BorderLayout.NORTH);
		rightPanel.add(scrollBill, BorderLayout.CENTER);
		rightPanel.add(infoPanel, BorderLayout.SOUTH);

		
		frame.add(rightPanel);
		
	}
	
	public void buildLeftPanel() {
		leftPanel = new JPanel(new GridLayout(2,1));
		itemPanel = new JPanel(new BorderLayout());
	
		stationIDLabel = new JLabel("Current diy station: "+(diyNum+1),JLabel.CENTER);
		
		itemPanel.add(stationIDLabel, BorderLayout.NORTH);
		
		printerPanel = new JPanel(new GridLayout(3,2));
		addInkButton = new JButton("add ink");
		addPaperButton = new JButton("add paper");
		enableStationButton = new JButton("unlock diy station");
	
		alertLabel = new JLabel("printer out of paper/ink");
		inkLabel = new JLabel("no ink");
		paperLabel = new JLabel("no paper");
		
		//printerPanel.add(alertLabel);
		printerPanel.add(addInkButton);
		printerPanel.add(inkLabel);
		printerPanel.add(addPaperButton);
		printerPanel.add(paperLabel);
		
		itemPanel.add(printerPanel, BorderLayout.CENTER);
		leftPanel.add(itemPanel);	
		frame.add(leftPanel);
	}
	public void addListeners() {
		
		currentDIY.getTextPane().addPropertyChangeListener(e->{
			billTextArea.setText(currentDIY.getProductDetails());
			totalLabel.setText(currentDIY.getTotalAmount());
			//weightlabel.setText(null);
		});
		
		addPaperButton.addActionListener(e->{
			paperLabel.setText("paper added");
			try {
				system.get(diyNum).getPrinter().addPaper(500);
			} catch (OverloadException e1) {
				paperLabel.setText("Paper Tray Full");
				addPaperButton.setEnabled(false);
				e1.printStackTrace();
			}
		});
		
		addInkButton.addActionListener(e->{
			inkLabel.setText("ink added");
			try {
				system.get(diyNum).getPrinter().addInk(500);
			} catch (OverloadException e1) {
				inkLabel.setText("Ink Full");
				addInkButton.setEnabled(false);
				e1.printStackTrace();
			}
		});
		
		enableStationButton.addActionListener(e->{
			system.get(diyNum).systemEnable();
		});
		
	}

}	
