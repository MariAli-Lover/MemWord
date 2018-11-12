package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

import javax.swing.*;

import dbmanager.DB;

public class DictSelectFrame extends JFrame {

	DB db;

	JPanel buttonPanel;
	final JLabel titleLabel;
	final DefaultListModel<String> dictListModel;
	final JList<String> dictList;
	JButton selectButton;
	JButton importButton;

	Vector<String> dictListData;

	public DictSelectFrame() {
		
		db = new DB();
		
		buttonPanel = new JPanel();

		titleLabel = new JLabel("Select Dictionary");
		titleLabel.setSize(500, 100);

		dictListModel = new DefaultListModel<>();

		dictListData = db.getdictListData();

		for(String s : dictListData)
			dictListModel.addElement(s);

		dictList = new JList<>(dictListModel);
		dictList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JList<String> list = (JList) e.getSource();
				if(e.getClickCount() == 2) {
					int index = list.locationToIndex(e.getPoint());
					if(index >= 0) {
						Object o = list.getModel().getElementAt(index);
						MenuFrame mf = openMenuFrame(o.toString());
					}
				}
			}
		});

		selectButton = new JButton("Select");
		selectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(dictList.getSelectedIndex() != -1) {
					String selectedDict;

					selectedDict = dictList.getSelectedValue();
					setVisible(false);
					dispose();
					MenuFrame mf = openMenuFrame(selectedDict);
				}
			}

		});
		
		importButton = new JButton("Import");
		importButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fc = new JFileChooser();
				File f;
				String filepath;
				String dictname;
				
				int i = fc.showOpenDialog(DictSelectFrame.this);
				if(i == JFileChooser.APPROVE_OPTION) {
					f = fc.getSelectedFile();
					filepath = f.getPath();
					System.out.println(filepath);
					dictname = JOptionPane.showInputDialog(DictSelectFrame.this, "Enter Dictionary Name");
					
					db.import_csv(filepath, dictname);
				}
				
			}
			
		});
		
		
		buttonPanel.add(selectButton);
		buttonPanel.add(importButton);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		setSize(300, 300);
		add(titleLabel, BorderLayout.NORTH);
		add(dictList, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);

	}
	
	public MenuFrame openMenuFrame(String s) {
		setVisible(false);
		dispose();
		return new MenuFrame(s);
		
	}

}
