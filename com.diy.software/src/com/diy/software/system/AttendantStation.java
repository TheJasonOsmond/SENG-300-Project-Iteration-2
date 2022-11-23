package com.diy.software.system;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.virgilio.ElectronicScale;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * Attendant station GUI from iteration 1 adapted to use with iteration 2
 * 
 * @author Benjamin Niles
 *
 */
public class AttendantStation{

	private ElectronicScale baggingArea;

	public AttendantStation(CustomerData[] c) {
		
		//can add multiple checkout stations
		//will need to implement a way to select them later
		system = new ArrayList<DIYSystem>();
		
		for(int i =0; i<c.length;i++) {
			DIYSystem diy = new DIYSystem(c[i], this);
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
	
	private JButton addInkButton;
	private JButton addPaperButton;
	private JButton enableStationButton;
	private JButton disableStationButton;
	private JButton addBagButton;
	private JButton approveWeightButton;
	
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
		frame.getContentPane().setLayout(new GridLayout(0,2));
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

		
		frame.getContentPane().add(rightPanel);
		
	}
	
	public void buildLeftPanel() {
		leftPanel = new JPanel(new GridLayout(2,1));
		itemPanel = new JPanel(new BorderLayout());
	
		stationIDLabel = new JLabel("Current diy station: "+(diyNum+1),JLabel.CENTER);
		
		itemPanel.add(stationIDLabel, BorderLayout.NORTH);
		
		printerPanel = new JPanel(new GridLayout(3,2));
		addInkButton = new JButton("add ink");
		addInkButton.setEnabled(false);
		addPaperButton = new JButton("add paper");
		addPaperButton.setEnabled(false);
		enableStationButton = new JButton("unlock diy station");
		enableStationButton.setEnabled(false);
		disableStationButton = new JButton("lock diy station");
		approveWeightButton = new JButton ("Approve weight change");
		approveWeightButton.setEnabled(false);
		addBagButton = new JButton("add bags");	
		addBagButton.setEnabled(false);
		alertLabel = new JLabel("");
		//printerPanel.add(alertLabel);
		printerPanel.add(approveWeightButton);
		printerPanel.add(addBagButton);
		printerPanel.add(disableStationButton);
		printerPanel.add(enableStationButton);
		printerPanel.add(addInkButton);
		printerPanel.add(addPaperButton);
		itemPanel.add(alertLabel, BorderLayout.CENTER);
		itemPanel.add(printerPanel, BorderLayout.SOUTH);
		printerPanel.add(addBagButton);
		leftPanel.add(itemPanel);	
		frame.getContentPane().add(leftPanel);
	}
	public void addListeners() {

		currentDIY.getTextPane().addPropertyChangeListener(e->{
			billTextArea.setText(currentDIY.getProductDetails());
			totalLabel.setText(currentDIY.getTotalAmount());
			//weightlabel.setText(null);
		});

		addPaperButton.addActionListener(e->{
			try {
				system.get(diyNum).getPrinter().addPaper(500);
			} catch (OverloadException e1) {
				addPaperButton.setText("Paper Tray Full");
				addPaperButton.setEnabled(false);
				//e1.printStackTrace();
			}
		});

		addInkButton.addActionListener(e->{
			try {
				system.get(diyNum).getPrinter().addInk(500000);
			} catch (OverloadException e1) {
				addInkButton.setText("Ink Full");
				addInkButton.setEnabled(false);
				//e1.printStackTrace();
			}
		});

		enableStationButton.addActionListener(e->{
			system.get(diyNum).systemEnable();
			enableStationButton.setEnabled(false);
			disableStationButton.setEnabled(true);
			addInkButton.setEnabled(false);
			addPaperButton.setEnabled(false);

		});
		approveWeightButton.addActionListener(e->{
			try {
				system.get(diyNum).updateExpectedWeight(system.get(diyNum).getCurrentWeight());
			} catch (OverloadException ex) {
				throw new RuntimeException(ex);
			}
			enableStationButton.setEnabled(true);
			disableStationButton.setEnabled(false);
			approveWeightButton.setEnabled(false);
		});

		addBagButton.addActionListener(e->{
			system.get(diyNum).getBagDispenserData().addBagsToDispenser(50);
			sendAlert("");
			enableStationButton.setEnabled(true);
			disableStationButton.setEnabled(false);
			addBagButton.setEnabled(false);
			system.get(diyNum).bagsRefilled();
		});

		disableStationButton.addActionListener(e->{
			system.get(diyNum).systemDisable();
			enableStationButton.setEnabled(true);
			disableStationButton.setEnabled(false);
		});

	}

	//updates the text in the alert label
	public void sendAlert(String alert) {
		alertLabel.setText(alert);
	}
	
	
	//maybe move these to ReceiptPrinterObserver?
	//would need a way to access/enable buttons
	public void lowInk() {
		addInkButton.setEnabled(true);
		if(!system.get(diyNum).isEnabled()) {
			enableStationButton.setEnabled(true);
			disableStationButton.setEnabled(false);
		}
	}
	
	public void lowPaper() {
		addPaperButton.setEnabled(true);
		if(!system.get(diyNum).isEnabled()) {
			enableStationButton.setEnabled(true);
			disableStationButton.setEnabled(false);
		}
	}
	
	public void noBags(int difference) {
		if (difference == 0) {
			sendAlert("Please refill bag dispenser.");
			addBagButton.setEnabled(true);
			if(!system.get(diyNum).isEnabled()) {
				enableStationButton.setEnabled(false);
				disableStationButton.setEnabled(false);
			}
		} else {
			sendAlert("Bag dispenser empty. Give customer " + Math.abs(difference) + " bag(s).");
			addBagButton.setEnabled(true);
			if(!system.get(diyNum).isEnabled()) {
				enableStationButton.setEnabled(false);
				disableStationButton.setEnabled(false);
			}
		}
	}

	public void notifyWeightChange() {
		sendAlert("Unauthorized weight change at station. Please approve");
		//approveWeightButton.setEnabled(true);
		//if (!system.get(diyNum).isEnabled()) {
		//	enableStationButton.setEnabled(false);
		//	disableStationButton.setEnabled(false);
		//}
	}

	
	public DIYSystem getCurrentDIY() {
		return system.get(diyNum);
	}

}	
