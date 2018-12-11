package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dbg.Debugger;
import dbmanager.DB;

public class ChartFrame extends JFrame {

	DB db;
	
	String currentDict;
	
	Container mainPanel;
	PiePanel pp;
	JButton prevBtn;
	JLabel totalLabel, learnedLabel, leftLabel;
	JPanel labelPanel;
	
	GridBagLayout gbl;
	GridBagConstraints gbc;
	
	int totalword;
	int learnedword;
	
	ChartFrame(String dict) {
		db = new DB();
		
		currentDict = dict;
		
		totalword = db.countWord(currentDict);
		learnedword = db.countLearnedWord(currentDict);
		
		pp = new PiePanel(learnedword, totalword - learnedword);
		
		totalLabel = new JLabel("Total: " + totalword);
		totalLabel.setForeground(Color.WHITE);
		learnedLabel = new JLabel("Learned: " + learnedword);
		learnedLabel.setForeground(Color.WHITE);
		leftLabel = new JLabel("Left: " + (totalword - learnedword));
		leftLabel.setForeground(Color.WHITE);
		
		labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(3, 1));
		labelPanel.setBackground(new Color(0x08, 0x00, 0x34));
		
		labelPanel.add(totalLabel);
		labelPanel.add(learnedLabel);
		labelPanel.add(leftLabel);
		
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
		mainPanel = getContentPane();
		mainPanel.setBackground(new Color(0x08, 0x00, 0x34));
		mainPanel.setLayout(gbl);
		
		gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainPanel.add(prevBtn, gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.25;
		gbc.weighty = 1.0;

		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPanel.add(labelPanel, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0.75;

		gbc.gridx = 1;
		gbc.gridy = 1;
		mainPanel.add(pp, gbc);

		((JPanel) mainPanel).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		setVisible(true);
		setTitle("Statistics");
		setLocationRelativeTo(null);
		setSize(400, 300);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public MenuFrame openMenuFrame(String s) {
		setVisible(false);
		dispose();
		return new MenuFrame(s);
	}
}

class PiePanel extends JPanel {

	int x, y;
	int xangle, yangle;
	int total;
	
	PiePanel(int x, int y) {
		this.x = x;
		this.y = y;
		total = x + y;
		xangle = 360 * x / total;
		yangle = 360 - xangle;
		setBackground(new Color(0x08, 0x00, 0x34));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Debugger.log("x, y: " + getX() + ", " + getY());
		g.setColor(Color.BLUE);
		g.fillArc(0, 0, getWidth(), getHeight(), 
				0, xangle);
		g.setColor(Color.GRAY);
		g.fillArc(0, 0, getWidth(), getHeight(), 
				xangle, yangle);
	}
}
