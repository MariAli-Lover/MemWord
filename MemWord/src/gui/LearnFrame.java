package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import dbg.Debugger;
import dbmanager.DB;

public class LearnFrame extends JFrame {
	DB db;
	
	String currentDict;
	JLabel wordLabel;
	JLabel meanLabel;
	JButton btn1, btn2, btn3;
	JPanel btnPanel;
	
	HashMap<String, String> hm;
	
	public LearnFrame(String dict) {
		db = new DB();
		
		currentDict = dict;
		
		wordLabel = new JLabel();
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setVerticalAlignment(SwingConstants.CENTER);
		meanLabel = new JLabel();
		meanLabel.setHorizontalAlignment(SwingConstants.CENTER);
		meanLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		btn1 = new JButton("No");
		btn2 = new JButton("Probably");
		btn3 = new JButton("Yes");
		
		btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(1, 3));
		
		btnPanel.add(btn1);
		btnPanel.add(btn2);
		btnPanel.add(btn3);
		
		setLayout(new GridLayout(3, 1));
		setSize(300, 300);
		setVisible(true);
		
		add(wordLabel);
		add(meanLabel);
		add(btnPanel);
		
		
		if(db.hasWordLeft(currentDict)) {
			refreshWord();
			Debugger.log("Test");
		}
		else
			prompt_reset();
		
		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(db.hasWordLeft(currentDict))
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
		db.setWordFreq(currentDict, hm.get("word"), 1);
		Debugger.log(hm.get("word") + " " + hm.get("mean"));
	}
	
	public void prompt_reset() {
		
	}

}
