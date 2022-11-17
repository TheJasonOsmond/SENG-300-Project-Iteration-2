package com.diy.software.system;

import java.awt.Font;
import java.awt.Panel;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.diy.hardware.BarcodedProduct;

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

	/**
	 * Create the application.
	 */
	public DiyInterface(DIYSystem s) {
		sysRef = s;
		initialize();
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

	/**
	 * Initialize the contents of the frame.
	 */
	int count = 0;
	private JTextField TotalTxtField;
	private JTextArea ErrorMSG;
	private JButton ScanItem;
	private JButton PayNow;
	private JButton BaggingAreaButton;
	private JTextArea TotalWeight;
	private JComboBox<?> comboBox;
	private JTextPane ItemList;

	public void initialize() {
		/**
		 * Creates Error text area.
		 */
		ErrorMSG = new JTextArea();
		ErrorMSG.setWrapStyleWord(true);
		ErrorMSG.setBackground(new Color(255, 255, 255));
		ErrorMSG.setEditable(false);
		ErrorMSG.setLineWrap(true);
		ErrorMSG.setFont(new Font("Tahoma", Font.BOLD, 20));

		/**
		 * Action of "Scan Item" Button.
		 */
		ScanItem = new JButton("Scan Item");
		ScanItem.setFont(new Font("Tahoma", Font.PLAIN, 25));
		ScanItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.out.println("Scan Item button has been pressed");
				sysRef.systemStartScan();
			}
		});

		/**
		 * Creates Pay Now button.
		 */
		PayNow = new JButton("Pay Now");
		PayNow.setFont(new Font("Tahoma", Font.PLAIN, 25));
		PayNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.out.println("Pay Now button has been pressed");
				sysRef.payByCreditStart((String) comboBox.getSelectedItem());
				;
			}
		});
		/**
		 * Creates the Item list for items scanned.
		 */
		ItemList = new JTextPane();
		ItemList.setEditable(false);
		ItemList.setFont(new Font("Tahoma", Font.PLAIN, 17));

		/**
		 * Action of "ItemList" here.
		 */

		BaggingAreaButton = new JButton("Place Item In Bagging Area");
		BaggingAreaButton.setEnabled(false);
		BaggingAreaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sysRef.StartBagging();
			}

		});
		/**
		 * Creates the total amount text field.
		 */
		TotalTxtField = new JTextField();
		TotalTxtField.setEditable(false);
		TotalTxtField.setBackground(new Color(255, 255, 255));
		TotalTxtField.setFont(new Font("Tahoma", Font.BOLD, 14));
		TotalTxtField.setText("Total:");
		TotalTxtField.setColumns(10);

		/**
		 * Creates the Combo box for selecting cards.
		 */
		String card[] = { "VISA", "Master Card", "Other" };
		comboBox = new JComboBox<Object>(card);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));

		/**
		 * Creates the total weight text field.
		 */
		TotalWeight = new JTextArea();
		TotalWeight.setEditable(false);
		TotalWeight.setFont(new Font("Tahoma", Font.BOLD, 14));
		TotalWeight.setText("Total Weight: ");

		/**
		 * GUI Layout.
		 */
		GroupLayout gl_panel = new GroupLayout(this);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(PayNow, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(ScanItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(BaggingAreaButton, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
						.addComponent(comboBox, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
						.addComponent(PayNow, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup().addGap(12).addComponent(ItemList,
								GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(TotalTxtField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		this.setLayout(gl_panel);
	}

	public void setamountToBePayedLabel(double amountToBePayed) {
		TotalTxtField.setText("Total: $" + Double.toString(amountToBePayed));
	}

	public void addProductDetails(String name, double price, double weight) {
		String itemDesc = ItemList.getText() + "Name: " + name + " -> Weight: " + Double.toString(weight)
				+ " -> Price: " + Double.toString(price) + "\n";
		ItemList.setText(itemDesc);
	}

	public void updateWeightLabel(double weight) {
		TotalWeight.setText("Total Weight: " + Double.toString(weight) + " grams");
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
		PayNow.setEnabled(true);
	}

	public void disablePaying() {
		PayNow.setEnabled(false);
	}
}