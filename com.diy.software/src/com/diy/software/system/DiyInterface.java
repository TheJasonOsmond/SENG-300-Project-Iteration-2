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
	//private JButton PayNow;
	private JButton PayNowCredit;
	private JButton PayNowDebit;
	private JButton PayNowCash;
	private JButton AddBag;
	private JButton BaggingAreaButton;
	private JButton ExitButton;
	
	private JButton enterMembership;
	private JTextArea weightMSG;
	
	private JTextArea TotalWeight;
	//private JComboBox<?> comboBox;
	private JComboBox<?> comboBoxCredit;
	private JComboBox<?> comboBoxDebit;
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
		 * Creates an AddBag button to allow customer to add their own bags.
		 */
		AddBag = new JButton("Add Bag(s)");
		AddBag.setFont(new Font("Tahoma", Font.PLAIN, 25));
		AddBag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Add Bag button has been pressed");
				sysRef.addBag();
			}
		});
		
		/**
		 * Creates Pay Now button.
		 * Iteration II Updated PayNow to PayNowCredit (variable name change
		 * by Simrat Benipal
		 */
		PayNowCredit = new JButton("Pay Now By Credit");
		PayNowCredit.setFont(new Font("Tahoma", Font.PLAIN, 25));
		PayNowCredit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pay Now button has been pressed");
				sysRef.payByCreditStart((String) comboBoxCredit.getSelectedItem());
			}
		});
		
		/** @author simrat_benipal
		 * Added in Iteration II for debit card payments
		 */
		PayNowDebit = new JButton("Pay Now By Debit");
		PayNowDebit.setFont(new Font("Tahoma", Font.PLAIN, 25));
		PayNowDebit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pay Now (Debit) button has been pressed");
				sysRef.payByDebitStart((String) comboBoxDebit.getSelectedItem());
			}
		});
		
		PayNowCash = new JButton("Pay With Cash");
		PayNowCash.setFont(new Font("Tahoma", Font.PLAIN, 25));
		PayNowCash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pay With Cash button has been pressed");
				sysRef.payByCashStart();
			}
		});
		/**
		 * @author Saja Abufarha
		 * Creates Enter Membership Number button.
		 */
		enterMembership = new JButton("Enter Membership Number");
		enterMembership.setFont(new Font("Tahoma", Font.PLAIN, 25));
		enterMembership.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.out.println("Enter Membership Number button has been pressed");
				sysRef.enterMembershipStart((String) comboBoxCredit.getSelectedItem());
				;
			}
		});
		
		/**
		 *  @author Saja Abufarha
		 *  Will be used to set the weight-discrepancy text 
		 * 
		 * **/
		weightMSG = new JTextArea();
		weightMSG.setEditable(false);
		weightMSG.setFont(new Font("Tahoma", Font.BOLD, 14));
		weightMSG.setText(" ");
		
		//ExitButton = new JButton("Exit");
		//ExitButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
		//ExitButton.addActionListener(new ActionListener() {
		//	public void actionPerformed(ActionEvent e) {
		//		exit();
		//	}
		//});
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
		
		
		/**Creates combo box for selecting cards
		 * @author simrat_benipal, Eusa
		 */

		//Create the Combo box for selecting cards.
		ArrayList<String> creditCardsSB = new ArrayList<>();
		ArrayList<String> debitCardsSB = new ArrayList<>();
		for(Card card : sysRef.getUserData().customer.wallet.cards) {
			if(card.kind.contains("VISA") || card.kind.contains("Master")) {
				String data = card.cardholder + ", " + card.kind;
				creditCardsSB.add(data);
			}
			if(card.kind.contains("Debit") || card.kind.contains("Interac")) {
				String data = card.cardholder + ", " + card.kind;
				debitCardsSB.add(data);
			}
		}
		comboBoxCredit = new JComboBox<>(creditCardsSB.toArray());
		comboBoxCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxDebit = new JComboBox<>(debitCardsSB.toArray());
		comboBoxDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));

		/**
		 * Creates the Combo box for selecting cards.
		
		//String card[] = { "VISA", "Master Card", "Other" };
		//ArrayList <String> cards = new ArrayList<String>();
		String cards[] = new String[sysRef.getUserData().customer.wallet.cards.size()] ;
		int i = 0;
		for(Card card1 : sysRef.getUserData().customer.wallet.cards)
		{
			//name of card
			if(card1.kind.contains("VISA") || card1.kind.contains("Master"))
			{
				cards[i] = card1.cardholder + " , " + card1.kind;
			}
			i++;
		}
		//Updated to show automatic list using Customer Data (Wallet)
		//@simrat @Eusa
		comboBoxCredit = new JComboBox<Object>(cards);
		comboBoxCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		/** @author simrat_benipal
		 * For debit card payments
		
		
		String debitCards[] = new String[sysRef.getUserData().customer.wallet.cards.size()] ;
		int j = 0;
		for(Card card1 : sysRef.getUserData().customer.wallet.cards)
		{
			//name of card
			if(card1.kind.contains("Debit") || card1.kind.contains("Interac"))
			{
				debitCards[j] = card1.cardholder + " , " + card1.kind;
			}
			j++;
		}
		//Updated to show automatic list using Customer Data (Wallet)
		//@simrat
		//String Debitcard[] = { "A Debit Card", "Interac", "Other" };
		comboBoxDebit = new JComboBox<Object>(debitCards);
		comboBoxDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
*/
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
						.addComponent(PayNowDebit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						
						.addComponent(enterMembership, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						
						.addComponent(PayNowCredit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(PayNowCash, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(ScanItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(AddBag, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						//.addComponent(ExitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(BaggingAreaButton, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
						.addComponent(comboBoxDebit, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(comboBoxCredit, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					
						.addComponent(ErrorMSG).addComponent(TotalWeight).addComponent(weightMSG))
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
						
						.addComponent(weightMSG, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						
						.addComponent(ErrorMSG, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(AddBag, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(BaggingAreaButton, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(ScanItem, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(PayNowCredit, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						
						.addComponent(enterMembership, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
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
				.addPreferredGap(ComponentPlacement.RELATED)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(PayNowCash, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				//.addComponent(ExitButton, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
				//.addPreferredGap(ComponentPlacement.RELATED)
				.addContainerGap()));
		this.setLayout(gl_panel);
	}

	public void setamountToBePayedLabel(double amountToBePayed) {
		TotalTxtField.setText("Total: $" + Double.toString(amountToBePayed));
	}
	
	public String getTotalAmount() {
		return TotalTxtField.getText();
	}

	public void addProductDetails(String name, double price, double weight) {
		String itemDesc = ItemList.getText() + "Name: " + name + " -> Weight: " + Double.toString(weight)
				+ " -> Price: $" + Double.toString(price) + "\n";
		ItemList.setText(itemDesc);
	}
	
	public void addPaymentToItems(double amountPaid) {
		String itemDesc = ItemList.getText() + 
				"***PAYMENT SUCCESSFUL -> Price: - $" + 
				Double.toString(amountPaid) + "\n";
		ItemList.setText(itemDesc);
	}
	
	public String getProductDetails() {
		return ItemList.getText();
	}

	public void updateWeightLabel(double weight) {
		TotalWeight.setText("Total Weight:77 " + Double.toString(weight) + " grams");
	}
	
	// Will use to set the weight-discrepancy text
		public void updateWeightDiscrepancyLabel(String msg) {
			// get msg from DIYSystem
			weightMSG.setText(msg);
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
	
	public JTextPane getTextPane() {
		return ItemList;
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
		PayNowCash.setEnabled(true);
	}

	public void disablePaying() {
		PayNowCredit.setEnabled(false);
		PayNowDebit.setEnabled(false);
		PayNowCash.setEnabled(false);
	}
	
	public void enableMembership() {
		enterMembership.setEnabled(true);
	}
	
	public void disableMembership() {
		enterMembership.setEnabled(false);
	}
	
	public void enableAddBagging() {
		AddBag.setEnabled(true);
	}
	
	public void disableAddBagging() {
		AddBag.setEnabled(false);
	}

	private void exit() {
		System.exit(0); 
	}

}