package main;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import dbmanager.DB;
import gui.DictSelectFrame;

public class Main {

	public static void main(String[] args) {
		/*
		System.out.println("Testing");
		
		DB db = new DB();
		db.import_csv("./test.csv", "test");
		*/
		
		System.setProperty("file.encoding", "UTF-8");
		Field charset;
		try {
			charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null, null);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DictSelectFrame dsf = new DictSelectFrame();
		dsf.setVisible(true);
	}
	
}
