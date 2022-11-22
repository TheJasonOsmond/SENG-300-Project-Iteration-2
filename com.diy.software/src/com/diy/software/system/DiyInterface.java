package com.diy.software.system;

import java.awt.Font;
import java.awt.Panel;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;

import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.diy.hardware.BarcodedProduct;
import com.jimmyselectronics.opeechee.Card;

/**
 * The DIY Interface running through the touchscreen class.
 * 
 * @authors Brandon, Daniel, Saja, Travis, Sai, Rose
 *
 */
public class DiyInterface extends Panel {

	private static final long serialVersionUID = 1L;
	/**
	 * ArrayList containing all the items needed for the UI
	 */
	ArrayList<String> items = new ArrayList<String>();
	ArrayList<BarcodedProduct> products = new ArrayList<BarcodedProduct>();
	private DIYSystem sysRef;
	private JTextField TotalTxtField;
	private JTextArea ErrorMSG;
	private JButton ScanItem;
	//private JButton PayNow;
	private JButton PayNowCredit;
	private JButton PayNowDebit;
	private JButton BaggingAreaButton;
	private JTextArea TotalWeight;
	//private JComboBox<?> comboBox;
	private JComboBox<?> comboBoxCredit;
	private JComboBox<?> comboBoxDebit;
	private JTextPane ItemList;

	/**
	 * Create the application.
	 */
	public DiyInterface(DIYSystem s) {
		sysRef = s;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @author Simrat Benipal
	 */
	public void initialize() {
		//Create Error text and response area.
		ErrorMSG = new JTextArea();
		ErrorMSG.setWrapStyleWord(true);
		ErrorMSG.setBackground(new Color(255, 255, 255));
		ErrorMSG.setEditable(false);
		ErrorMSG.setLineWrap(true);
		ErrorMSG.setFont(new Font("Tahoma", Font.BOLD, 20));

		//Create Scan Item Button.
		ScanItem = new JButton("Scan Item");
		ScanItem.setFont(new Font("Tahoma", Font.PLAIN, 25));
		ScanItem.addActionListener(e -> {
			System.out.println("Scan Item button has been pressed");
			sysRef.systemStartScan();
		});

		//Create Pay Now By Credit button.
		PayNowCredit = new JButton("Pay Now By Credit");
		PayNowCredit.setFont(new Font("Tahoma", Font.PLAIN, 25));
		PayNowCredit.addActionListener(e -> {
			System.out.println("Pay Now button has been pressed");
			sysRef.payByCreditStart((String) comboBoxCredit.getSelectedItem());
		});

		//Create Pay Now By Debit button.
		PayNowDebit = new JButton("Pay Now By Debit");
		PayNowDebit.setFont(new Font("Tahoma", Font.PLAIN, 25));
		PayNowDebit.addActionListener(e -> {
			System.out.println("Pay Now (Debit) button has been pressed");
			sysRef.payByDebitStart((String) comboBoxDebit.getSelectedItem());
			;
		});

		//Create the Item list for items scanned.
		ItemList = new JTextPane();
		ItemList.setEditable(false);
		ItemList.setFont(new Font("Tahoma", Font.PLAIN, 17));

		//Create the Place Item In Bagging Area button to place item in the bagging area.
		BaggingAreaButton = new JButton("Place Item In Bagging Area");
		BaggingAreaButton.setEnabled(false);
		BaggingAreaButton.addActionListener(e -> sysRef.StartBagging());

		//Create the total amount text field.
		TotalTxtField = new JTextField();
		TotalTxtField.setEditable(false);
		TotalTxtField.setBackground(new Color(255, 255, 255));
		TotalTxtField.setFont(new Font("Tahoma", Font.BOLD, 14));
		TotalTxtField.setText("Total:");
		TotalTxtField.setColumns(10);

		//Create the Combo box for selecting cards.
		ArrayList<String> creditCardsSB = new ArrayList<>();
		ArrayList<String> debitCardsSB = new ArrayList<>();
		for(Card card : sysRef.getUserData().customer.wallet.cards) {
			if(card.kind.contains("VISA") || card.kind.contains("Master")) {
				String data = card.cardholder + " , " + card.kind;
				creditCardsSB.add(data);
			}
			if(card.kind.contains("Debit") || card.kind.contains("Interac")) {
				String data = card.cardholder + " , " + card.kind;
				debitCardsSB.add(data);
			}
		}
		comboBoxCredit = new JComboBox<>(creditCardsSB.toArray());
		comboBoxCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxDebit = new JComboBox<>(debitCardsSB.toArray());
		comboBoxDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));

		//Create the Total Weight text field.
		TotalWeight = new JTextArea();
		TotalWeight.setEditable(false);
		TotalWeight.setFont(new Font("Tahoma", Font.BOLD, 14));
		TotalWeight.setText("Total Weight: ");

		/*
		  GUI Layout.
		 */
		GroupLayout gl_panel = new GroupLayout(this);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(PayNowDebit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(PayNowCredit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)		
						.addComponent(ScanItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(BaggingAreaButton, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
						.addComponent(comboBoxDebit, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(comboBoxCredit, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					
						.addComponent(ErrorMSG).addComponent(TotalWeight))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(TotalTxtField, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
						.addComponent(ItemList, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
						.addContainerGap()));
		
		
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(TotalWeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(ErrorMSG, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(BaggingAreaButton, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(ScanItem, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(PayNowCredit, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBoxCredit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(PayNowDebit, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup().addGap(12).addComponent(ItemList,
								GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBoxDebit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxCredit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(TotalTxtField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		this.setLayout(gl_panel);
	}

	public void setAmountToBePayedLabel(double amountToBePayed) {
		TotalTxtField.setText("Total: $" + Double.toString(amountToBePayed));
	}

	public void addProductDetails(String name, double price, double weight) {
		String itemDesc = ItemList.getText() + "Name: " + name + " -> Weight: " + Double.toString(weight)
				+ " -> Price: " + Double.toString(price) + "\n";
		ItemList.setText(itemDesc);
	}

	public void updateWeightLabel(double weight) {
		TotalWeight.setText("Total Weight:77 " + Double.toString(weight) + " grams");
	}

	public void setMsg(String msg) {
		ErrorMSG.setText(msg);
	}

	public void disableScanning() {
		ScanItem.setEnabled(false);
	}

	public void enableScanning() {
		ScanItem.setEnabled(true);
	}

	public void disableBagging() {
		BaggingAreaButton.setEnabled(false);
	}

	public void enableBagging() {
		BaggingAreaButton.setEnabled(true);
	}

	public void enablePaying() {
		PayNowCredit.setEnabled(true);
		PayNowDebit.setEnabled(true);
	}

	public void disablePaying() {
		PayNowCredit.setEnabled(false);
		PayNowDebit.setEnabled(false);
	}

	public void addUIItem(String item) {
		this.items.add(item);
	}

	public void addUIProduct(BarcodedProduct product) {
		this.products.add(product);
	}

	public ArrayList<BarcodedProduct> getCurrentProducts() {
		return (this.products);
	}
}