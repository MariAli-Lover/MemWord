package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import dbg.Debugger;
import dbmanager.DB;

public class Quiz1Frame extends JFrame {
	
	DB db;
	
	String currentDict;
	
	JButton[] selBtn;
	
	ArrayList<HashMap<String, String>> alhm;
	HashMap<String, String> anshm;

	Boolean correct;
	
	public Quiz1Frame(String dict) {
		
		db = new DB();
		currentDict = dict;
		
		setSize(300, 300);
		setVisible(true);
	}
	
	public void ansGen() {
		int totalword = db.countWord(currentDict);
		int totalbtn = totalword > 3 ? 3 : totalword;
		int ans;
		Random rand = new Random();

		if(totalbtn <= 0) {
			Debugger.log("error or no words existing");
			return;
		}
		
		alhm = db.quizRandomWordSelect(currentDict, totalbtn);
		
		ans = rand.nextInt(alhm.size());
		anshm = alhm.get(ans);
		
		selBtn = new JButton[totalbtn];
		
		for(int i = 0; i < totalbtn; i++) {
			selBtn[i] = new JButton();
			selBtn[i].addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JButton tbtn = (JButton) e.getSource();
					String tbtntext = tbtn.getText();
					
					if(tbtntext == anshm.get("mean")) {
						correct = true;
					}
					else {
						correct = false;
					}

				}
				
			});

		}

		
	}

}
