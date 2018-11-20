package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	HashMap<String, String> hm;
	
	public LearnFrame(String dict) {
		db = new DB();
		
		currentDict = dict;
		
		wordLabel = new JLabel();
		meanLabel = new JLabel();
		
		btn1 = new JButton("No");
		btn2 = new JButton("Probably");
		btn3 = new JButton("Yes");
		
		setSize(300, 300);
		setVisible(true);
		
		add(wordLabel, BorderLayout.NORTH);
		add(meanLabel, BorderLayout.CENTER);
		add(btn1, BorderLayout.SOUTH);
		
		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				refreshWord();
			}
			
		});
		
		refreshWord();
	}
	
	public void refreshWord() {
		hm = db.learnRandomWordSelect(currentDict);
		wordLabel.setText(hm.get("word"));
		meanLabel.setText(hm.get("mean"));
		Debugger.log(hm.get("word") + " " + hm.get("mean"));

		
	}

}
