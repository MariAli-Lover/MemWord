package dbmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dbg.Debugger;

public class DB {

	String path = null; // later set to default
	Connection conn = null;
	Statement state = null;
	
	public DB(String path) {
		try {
			Debugger.log("init DB...");
			this.path = path;
			conn = DriverManager.getConnection(path);
			state = conn.createStatement();
			state.setQueryTimeout(30);
			
			if(!table_exists("dict")) 
				query("CREATE TABLE dict (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING)");
			
			Debugger.log("init DB ended!");
			
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public DB() {
		this("jdbc:sqlite:./test.db"); // set path to default
		this.path = "jdbc:sqlite:./test.db";
	}
	
	public boolean table_exists(String t_name) {
		try {
			Debugger.log("table_exists with " + t_name);
			
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, t_name, null);
			return rs.next();
			// rs.last();
			// return (rs.getRow() > 0);
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	
	public void query(String query) {
		insert(query);
	}
	
	public void insert(String query) {
		try {
			state.executeUpdate(query);
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public ResultSet fetch(String query) {
		try {
			ResultSet rs = state.executeQuery(query);
			return rs;
		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public void import_csv(String csv_path, String t_name) {
		BufferedReader br = null;
		String line = "";
		String query;

		
		try {
			// if(table_exists(t_name)) return;
			
			
			if(!table_exists(t_name)) {
				insert(String.format("INSERT INTO dict (id, name) VALUES (NULL, '%s')", t_name));
				insert(String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT, orig_word STRING, mean_word STRING, freq INTEGER)", t_name));
			}
			
			br = new BufferedReader(new FileReader(csv_path));
			
			while((line = br.readLine()) != null) {
				String[] row = line.split(",");
				String orig_word = row[0];
				String mean_word = row[1];
				
				Debugger.log("word: " + orig_word + " " + mean_word);
				
				// words are initialized in DB
				query = String.format("INSERT INTO %s (id, orig_word, mean_word, freq) VALUES (NULL, '%s', '%s', 0)", t_name, orig_word, mean_word);
				
				insert(query);
				
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	
	public void close() {
		try {
			if(conn != null)
				conn.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}
			
	

}
