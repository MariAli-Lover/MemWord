package dbmanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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

			if(!tableExists("dict")) 
				query("CREATE TABLE dict (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"name STRING)");

			Debugger.log("init DB ended!");
			conn.close();

		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public DB() {
		this("jdbc:sqlite:./test.db"); // set path to default
		this.path = "jdbc:sqlite:./test.db";
	}

	public void setWordFreq(String dictName, String wordName, int freq) {
		String query = "UPDATE `$tableName` SET freq = ? WHERE word = ?";
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(path);
			query = query.replace("$tableName", dictName);
			Debugger.log(query);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, freq);
			stmt.setString(2, wordName);
			stmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Debugger.log(e.getMessage());
		}

	}

	public HashMap<String, String> learnRandomWordSelect(String dictName) {
		
		HashMap<String, String> hm;
		String query = "SELECT * FROM `$tableName` WHERE freq = 0 ORDER BY RANDOM() LIMIT 1";
		PreparedStatement stmt = null;
		ResultSet rs;
		
		try {
			conn = DriverManager.getConnection(path);
			hm = new HashMap<String, String>();
			query = query.replace("$tableName", dictName);
			Debugger.log(query);
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()) {
				Debugger.log("learnRandomWordSelect");
				hm.put("word", rs.getString("word"));
				hm.put("mean", rs.getString("mean"));
				hm.put("freq", String.valueOf(rs.getInt("freq")));
				
				Debugger.log("test2");

				conn.close();
				return hm;
			}
			conn.close();
			
			return null;
		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
		
	}
	
	public boolean hasWordLeft(String dictName) {
		
		String query = "SELECT * FROM `$tableName` WHERE freq = 0";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		 try {
			conn = DriverManager.getConnection(path);

			 query = query.replace("$tableName", dictName);
			 stmt = conn.prepareStatement(query);
			 rs = stmt.executeQuery();
			 conn.close();
			 return rs.next();
		 } catch (SQLException e) {
			 System.err.println(e.getMessage());
			 return false;
		 }
		
	}

	public void insertDict(String dictName) { // Insert dictionary name into dict

		String query = "INSERT INTO dict (id, name) VALUES (NULL, ?)";
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(path);
			stmt = conn.prepareStatement(query);
			stmt.setString(1, dictName);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	public void createDict(String dictName) { // Create new dictionary

		String query = "CREATE TABLE `$tableName` (id INTEGER PRIMARY KEY AUTOINCREMENT, word STRING, mean STRING, freq INTEGER)";
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(path);
			query = query.replace("$tableName", dictName);
			stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	public void insertWord(String dictName, String origWord, String meanWord) { // Insert word into dictionary

		String query = "INSERT INTO `$tableName` (id, word, mean, freq) VALUES (NULL, ?, ?, 0)";
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(path);
			query = query.replace("$tableName", dictName);
			stmt = conn.prepareStatement(query);
			stmt.setString(1, origWord);
			stmt.setString(2, meanWord);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	public boolean tableExists(String tableName) {
		try {
			Debugger.log("table exists with " + tableName);

			conn = DriverManager.getConnection(path);
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, tableName, null);
			return rs.next();
			// rs.last();
			// return (rs.getRow() > 0);
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	public void query(String query) {
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

	public Vector<String> getdictListData() {
		try {
			String query = "SELECT * FROM dict";
			ResultSet rs = fetch(query);

			Vector<String> dictList = new Vector<String>();

			while(rs.next())
				dictList.add(rs.getString("name"));

			return dictList;

		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public void importCSV(String CSVpath, String tableName) {
		BufferedReader br = null;
		String line = "";

		if(!tableExists(tableName)) {
			insertDict(tableName);
			createDict(tableName);
		}

		try {
			br = new BufferedReader(new FileReader(CSVpath));

			while((line = br.readLine()) != null) {
				String[] row = line.split(",");
				String orig_word = row[0];
				String mean_word = row[1];

				Debugger.log("word: " + orig_word + " " + mean_word);

				// words are initialized in DB
				insertWord(tableName, orig_word, mean_word);

			}
		} catch (IOException e) {
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
