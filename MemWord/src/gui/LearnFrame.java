package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import dbg.Debugger;
import dbmanager.DB;

public class LearnFrame extends JFrame {
	DB db;
	
	Container mainPanel;
	String currentDict;
	JLabel wordLabel;
	JLabel meanLabel;
	JButton btn1, btn2, btn3;
	JButton prevBtn;
	JPanel btnPanel;
	JSeparator sep;
	
	GridBagLayout gbl;
	GridBagConstraints gbc;
	
	HashMap<String, String> hm;
	
	public LearnFrame(String dict) {
		db = new DB();
		
		currentDict = dict;
		
		wordLabel = new JLabel("", SwingConstants.CENTER);
		wordLabel.setForeground(Color.WHITE);
		meanLabel = new JLabel("", SwingConstants.CENTER);
		meanLabel.setForeground(Color.WHITE);
		
		btn1 = new JButton("No");
		btn1.setBackground(Color.WHITE);
		btn2 = new JButton("Probably");
		btn2.setBackground(Color.WHITE);
		btn3 = new JButton("Yes");
		btn3.setBackground(Color.WHITE);
		
		btnPanel = new JPanel();
		btnPanel.setBackground(new Color(0x08, 0x00, 0x34));
		btnPanel.setLayout(new GridLayout(1, 3, 5, 5));
		
		btnPanel.add(btn1);
		btnPanel.add(btn2);
		btnPanel.add(btn3);
		
		prevBtn = new JButton("Prev");
		prevBtn.setBackground(Color.WHITE);
		prevBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MenuFrame mf = openMenuFrame(currentDict);
				
			}
			
		});
		
		sep = new JSeparator(JSeparator.HORIZONTAL);
		
		gbl = new GridBagLayout();
		mainPanel = getContentPane();
		mainPanel.setLayout(gbl);
		mainPanel.setBackground(new Color(0x08, 0x00, 0x34));
		
		gbc = new GridBagConstraints();
		
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
		mainPanel.add(wordLabel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		mainPanel.add(meanLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		mainPanel.add(btnPanel, gbc);

		((JPanel) mainPanel).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		setSize(500, 500);
		setTitle("Learn Word");
		setLocationRelativeTo(null);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		if(db.hasWordLeftLearn(currentDict)) {
			refreshWord();
			Debugger.log("Test");
		}
		else
			prompt_reset();
		
		btn1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String wordName = wordLabel.getText();
				
				db.setWordFreq(currentDict, wordName, 0);
				
				if(db.hasWordLeftLearn(currentDict))
					refreshWord();
				else
					prompt_reset();
			}
			
		});
		
		btn2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String wordName = wordLabel.getText();
				
				db.setWordFreq(currentDict, wordName, 1);
				
				if(db.hasWordLeftLearn(currentDict))
					refreshWord();
				else
					prompt_reset();
			}
			
		});
		
		btn3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String wordName = wordLabel.getText();
				
				db.setWordFreq(currentDict, wordName, 2);
				
				if(db.hasWordLeftLearn(currentDict))
					refreshWord();
				else
					prompt_reset();
			}
			
		});
	}
	
	public void refreshWord() {
		hm = db.learnRandomWordSelect(currentDict);
		wordLabel.setText(hm.get("word"));
		meanLabel.setText(hm.get("mean"));
		db.setWordFreq(currentDict, hm.get("word"), 0);
		Debugger.log(hm.get("word") + " " + hm.get("mean"));
	}
	
	public void prompt_reset() {
		Object[] options = {"Yes(Removes all progress)",
							"No(Go back)"};
		int sel = JOptionPane.showOptionDialog(new JFrame(),
				"No words left to learn! Reset?",
				"Reset Prompt",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);
		
		Debugger.log("prompt_reset: " + sel);
		
		if(sel == 0) {
			db.resetWordFreq(currentDict);
			refreshWord();
		}
		else if(sel == 1) {
			MenuFrame mf = openMenuFrame(currentDict);
		}
		
		
	}
	
	public MenuFrame openMenuFrame(String s) {
		setVisible(false);
		dispose();
		return new MenuFrame(s);
	}

}
