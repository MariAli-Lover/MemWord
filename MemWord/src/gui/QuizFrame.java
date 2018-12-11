package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import dbg.Debugger;
import dbmanager.DB;

public class QuizFrame extends JFrame {
	
	Container mainPanel;
	JPanel quizPanel;
	JButton prevBtn;
	Container c;
	String currentDict;
	Random rand;
	
	GridBagLayout gbl;
	GridBagConstraints gbc;

	public QuizFrame(String dict) {
		
		currentDict = dict;
		
		rand = new Random();
		
		setSize(500, 500);
		setTitle("Quiz");
		setLocationRelativeTo(null);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		quizPanel = setQuiz();
		
		prevBtn = new JButton("Prev");
		prevBtn.setBackground(Color.WHITE);
		prevBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MenuFrame mf = openMenuFrame(currentDict);
				
			}
			
		});		

		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		mainPanel = getContentPane();
		mainPanel.setLayout(gbl);
		mainPanel.setBackground(new Color(0x08, 0x00, 0x34));
		
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainPanel.add(prevBtn, gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPanel.add(quizPanel, gbc);

		((JPanel) mainPanel).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}
	
	public JPanel setQuiz() {
		int pivot = rand.nextInt(2);
		JPanel jp;
		switch(pivot) {
		case 0:
			jp = new Quiz1Panel(currentDict);
			break;
		case 1:
			jp = new Quiz2Panel(currentDict);
			break;
		default:
			jp = null;
		}
		jp.addComponentListener(new ComponentAdapter() {

			public void componentHidden(ComponentEvent e) {
				quizPanel = setQuiz();
				gbc.anchor = GridBagConstraints.CENTER;
				gbc.fill = GridBagConstraints.BOTH;
				gbc.weightx = 1.0;
				gbc.weighty = 1.0;

				gbc.gridx = 0;
				gbc.gridy = 1;
				mainPanel.add(quizPanel, gbc);
				Debugger.log("testing");
			}
			
		});
		Debugger.log(jp.getClass());
		return jp;
	}
	
	public MenuFrame openMenuFrame(String s) {
		setVisible(false);
		dispose();
		return new MenuFrame(s);
	}
}
