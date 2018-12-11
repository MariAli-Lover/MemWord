package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MenuFrame extends JFrame {
	
	String currentDict;
	JLabel dictLabel;
	JButton learnBtn;
	JButton quizBtn;
	JButton chartBtn;
	JButton prevBtn;

	Container mainPanel;
	JPanel buttonPanel;
	
	GridBagLayout gbl;
	GridBagConstraints gbc;
	
	public MenuFrame(String dict) {
		
		currentDict = dict;
		
		dictLabel = new JLabel(currentDict, SwingConstants.CENTER);
		dictLabel.setForeground(Color.WHITE);
		
		
		learnBtn = new JButton("Learn");
		learnBtn.setBackground(Color.WHITE);
		learnBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LearnFrame lf = openLearnFrame(currentDict);
			}
			
		});
		
		quizBtn = new JButton("Quiz");
		quizBtn.setBackground(Color.WHITE);
		quizBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				QuizFrame qf = openQuiz1Frame(currentDict);
			}
			
		});
		
		chartBtn = new JButton("Stat.");
		chartBtn.setBackground(Color.WHITE);
		chartBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ChartFrame cf = openChartFrame(currentDict);
			}
			
		});
		
		prevBtn = new JButton("Prev");
		prevBtn.setBackground(Color.WHITE);
		prevBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				DictSelectFrame dsf = openDictSelectFrame();
				
			}
			
		});

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
		gbc.gridwidth = 3;
		mainPanel.add(dictLabel, gbc);
		
		gbc.weightx = 1.0 / 3;
		gbc.weighty = 0.5;

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		mainPanel.add(learnBtn, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		mainPanel.add(quizBtn, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		mainPanel.add(chartBtn, gbc);

		((JPanel) mainPanel).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setSize(300, 300);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public LearnFrame openLearnFrame(String s) {
		setVisible(false);
		dispose();
		return new LearnFrame(s);
	}

	public QuizFrame openQuiz1Frame(String s) {
		setVisible(false);
		dispose();
		return new QuizFrame(s);
	}

	public ChartFrame openChartFrame(String s) {
		setVisible(false);
		dispose();
		return new ChartFrame(s);
	}

	public DictSelectFrame openDictSelectFrame() {
		setVisible(false);
		dispose();
		return new DictSelectFrame();
	}
}
