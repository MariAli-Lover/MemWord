package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import dbg.Debugger;
import dbmanager.DB;

public class Quiz1Panel extends JPanel {
	
	DB db;
	
	String currentDict;

	JLabel powerLabel;
	JLabel wordLabel;
	JPanel btnPanel;
	JButton[] selBtn;
	JButton prevBtn;
	
	GridBagLayout gbl;
	GridBagConstraints gbc;
	
	ArrayList<HashMap<String, String>> alhm;
	HashMap<String, String> anshm;

	int totalword;
	int totalbtn;
	int ans;
	Boolean correct;
	Random rand;
	
	public Quiz1Panel(String dict) {
		
		db = new DB();
		currentDict = dict;
		rand = new Random();

		totalword = db.countWordQuiz(currentDict);
		totalbtn = totalword > 4 ? 4 : totalword;
		
		Debugger.log("totalbtn: " + totalbtn);
		
		btnPanel = new JPanel(new GridLayout(1, totalbtn, 5, 5));
		btnPanel.setBackground(new Color(0x08, 0x00, 0x34));
		
		
		powerLabel = new JLabel();
		powerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		powerLabel.setVerticalAlignment(SwingConstants.CENTER);
		powerLabel.setForeground(Color.WHITE);

		wordLabel = new JLabel();
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setVerticalAlignment(SwingConstants.CENTER);
		wordLabel.setForeground(Color.WHITE);

		selBtn = new JButton[totalbtn];

		for(int i = 0; i < totalbtn; i++) {
			selBtn[i] = new JButton();
			selBtn[i].addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JButton tbtn = (JButton) e.getSource();
					String tbtntext = tbtn.getText();
					
					for(JButton ttbtn : selBtn)
						ttbtn.setEnabled(false);
							
					if(tbtntext.equals(anshm.get("mean"))) {
						correct = true;
						Debugger.log("correct");
						tbtn.setBackground(Color.GREEN);
			
						db.setWordFreq(currentDict, anshm.get("word"), 2);
						
						ActionListener al = new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								// ansGen();
								setVisible(false);
							}
						};
						Timer timer = new Timer(1000, al);
						timer.setRepeats(false);
						timer.start();
						// ansGen();
					}
					else {
						correct = false;
						Debugger.log("incorrect");
						tbtn.setBackground(Color.RED);
						selBtn[ans].setBackground(Color.GREEN);

						db.setWordFreq(currentDict, anshm.get("word"), 1);
						ActionListener al = new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								// ansGen();
								setVisible(false);
							}
						};
						Timer timer = new Timer(1000, al);
						timer.setRepeats(false);
						timer.start();
						// ansGen();
					}
				}
			});
			btnPanel.add(selBtn[i]);

		}
		
		prevBtn = new JButton("Prev");
		prevBtn.setBackground(Color.WHITE);

		gbl = new GridBagLayout();
		setLayout(gbl);
		
		gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(powerLabel, gbc);
		
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		gbc.gridx = 0;
		gbc.gridy = 1;
		add(wordLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		add(btnPanel, gbc);

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setVisible(true);
		setBackground(new Color(0x08, 0x00, 0x34));
		
		if(db.hasWordLeftQuiz(currentDict))
			ansGen();
		else {
			wordLabel.setText("Learn words first!");
			for(JButton btn : selBtn)
				btn.setEnabled(false);
		}
	}
	
	public void ansGen() {

		if(totalbtn <= 0) {
			Debugger.log("error or no words existing");
			return;
		}
		
		
		alhm = db.quizRandomWordSelect(currentDict, totalbtn);
		
		ans = rand.nextInt(alhm.size());
		anshm = alhm.get(ans);
		
		powerLabel.setText(Integer.parseInt(anshm.get("freq")) == 1 ? "weak word" : "strong word");
		wordLabel.setText(anshm.get("word"));
		
		
		for(int i = 0; i < totalbtn; i++) {
			selBtn[i].setText(alhm.get(i).get("mean"));
			selBtn[i].setBackground(Color.white);
			selBtn[i].setEnabled(true);
		}

		
	}
	
}
