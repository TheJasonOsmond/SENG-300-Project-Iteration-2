package com.diy.software.system;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Leftover code from when we thought we needed a attendant station for the first iteration
 * @author Daniel
 *
 */
public class AttendantStation implements ActionListener {
	
	private static JPanel panel = new JPanel();
	private static JFrame frame = new JFrame();
	private static JTextField textField = new JTextField();
	private static JLabel label1 = new JLabel();
	private static JButton button1 =  new JButton();
	
	public static void main (String[] args) {
		
		frame.setTitle("*****Attendant Station*****");
		frame.add(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		frame.setSize(1000,800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel,BorderLayout.CENTER);
		panel.setLayout(new GridLayout(1,3));
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}