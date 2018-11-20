package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MenuFrame extends JFrame {
	
	String currentDict;
	JLabel dictLabel;
	JButton learnButton;
	JButton quizButton;
	
	JPanel buttonPanel;
	
	public MenuFrame(String dict) {
		
		currentDict = dict;
		
		dictLabel = new JLabel(currentDict, SwingConstants.CENTER);
		
		learnButton = new JButton("Learn");
		learnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LearnFrame lf = openLearnFrame(currentDict);
			}
			
		});
		
		quizButton = new JButton("Quiz");
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.add(learnButton);
		buttonPanel.add(quizButton);
		
		setSize(300, 300);
		setLayout(new GridLayout(2, 1));
		add(dictLabel);
		add(buttonPanel);
		setVisible(true);
		
	}
	
	public LearnFrame openLearnFrame(String s) {
		setVisible(false);
		dispose();
		return new LearnFrame(s);
	}

}
