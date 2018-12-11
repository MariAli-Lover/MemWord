package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

import dbmanager.DB;

public class DictSelectFrame extends JFrame {

	DB db;

	Container c;
	JTextArea searchTA;
	JPanel buttonPanel;
	JLabel titleLabel;
	JLabel searchLabel;
	DefaultListModel<String> dictListModel;
	JList<String> dictList;
	JScrollPane jsp;
	DefaultListCellRenderer dictListRenderer;
	JButton selectButton;
	JButton importButton;
	JButton deleteButton;
	GridBagLayout gbl;
	GridBagConstraints gbc;

	ArrayList<String> dictListData;

	public DictSelectFrame() {
		
		db = new DB();
		
		buttonPanel = new JPanel();

		titleLabel = new JLabel("Select Dictionary");
		titleLabel.setForeground(Color.WHITE);
		
		searchLabel = new JLabel("Search");
		searchLabel.setForeground(Color.WHITE);
		
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
		
		jsp = new JScrollPane(dictList);
		
		dictListRenderer = (DefaultListCellRenderer) dictList.getCellRenderer();
		dictListRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		searchTA = new JTextArea();
		searchTA.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String str = searchTA.getText();
				DefaultListModel filteredData = new DefaultListModel();
				dictListData = db.getdictListData();
				for(String s : dictListData) {
					if(s.contains(str))
						filteredData.addElement(s);
				}
				dictListModel = filteredData;
				dictList.setModel(dictListModel);
				
			}
		});


		selectButton = new JButton("Select");
		selectButton.setBackground(Color.WHITE);
		selectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(dictList.getSelectedIndex() != -1) {
					String selectedDict;

					selectedDict = dictList.getSelectedValue();
					MenuFrame mf = openMenuFrame(selectedDict);
				}
			}

		});
		
		importButton = new JButton("Import");
		importButton.setBackground(Color.WHITE);
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
					if(dictname == null) return;
					if(dictname.trim().length() == 0) return;
					
					db.importCSV(filepath, dictname.trim());
					dictListModel.addElement(dictname.trim());
				}
				
			}
			
		});
		
		deleteButton = new JButton("Delete");
		deleteButton.setBackground(Color.WHITE);
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(dictList.getSelectedIndex() != -1) {
					String selectedDict;
					int dictIndex;

					selectedDict = dictList.getSelectedValue();
					dictIndex = dictList.getSelectedIndex();
					db.purgeDict(selectedDict);
					db.removeDict(selectedDict);
					
					dictListModel.remove(dictIndex);
					dictList.setModel(dictListModel);
				}
			}

		});	
		
		buttonPanel.add(selectButton);
		buttonPanel.add(importButton);
		buttonPanel.add(deleteButton);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonPanel.setBackground(new Color(0x08, 0x00, 0x34));

		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		
		c = getContentPane();
		c.setLayout(gbl);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		c.add(titleLabel, gbc);

		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridwidth = 1;

		gbc.gridx = 0;
		gbc.gridy = 1;
		c.add(searchLabel, gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;

		gbc.gridx = 1;
		gbc.gridy = 1;
		c.add(searchTA, gbc);
		
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridwidth = 2;

		gbc.gridx = 0;
		gbc.gridy = 2;
		c.add(jsp, gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;

		gbc.gridx = 0;
		gbc.gridy = 3;
		c.add(buttonPanel, gbc);
		
		c.setBackground(new Color(0x08, 0x00, 0x34));
		/*
		c.setLayout(gbl);
		c.add(titleLabel, BorderLayout.NORTH);
		c.add(searchTA, BorderLayout.NORTH);
		c.add(dictList, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.SOUTH);
		*/
		((JPanel) c).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setSize(500, 500);
		setTitle("Select/Import Dictionary");
		setLocationRelativeTo(null);
		setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	public MenuFrame openMenuFrame(String s) {
		setVisible(false);
		dispose();
		return new MenuFrame(s);
		
	}

}
