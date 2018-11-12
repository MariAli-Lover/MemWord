package main;

import dbmanager.DB;
import gui.DictSelectFrame;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		System.out.println("Testing");
		
		DB db = new DB();
		db.import_csv("./test.csv", "test");
		*/
		
		DictSelectFrame dsf = new DictSelectFrame();
		dsf.setVisible(true);
	}
	
}
