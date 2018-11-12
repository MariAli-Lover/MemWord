package gui;

import java.awt.BorderLayout;

import javax.swing.*;

public class MenuFrame extends JFrame {
	
	String currentDict;
	JButton learnButton;
	JButton quizButton;
	
	public MenuFrame(String dict) {
		
		currentDict = dict;
		
		learnButton = new JButton("Learn");
		quizButton = new JButton("Quiz");
		
		setSize(300, 300);
		add(learnButton, BorderLayout.NORTH);
		add(quizButton, BorderLayout.SOUTH);
		setVisible(true);
		
	}

}
