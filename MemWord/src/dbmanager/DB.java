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

	public DB(String path) {

		Statement stmt;

		try {

			Debugger.log("init DB...");
			this.path = path;
			conn = DriverManager.getConnection(path);
			stmt = conn.createStatement();

			if(!tableExists("dict")) 
				stmt.executeUpdate("CREATE TABLE dict (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"name STRING)");

			Debugger.log("init DB ended!");
			stmt.close();
			conn.close();

		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public DB() {
		this("jdbc:sqlite:./test.db"); // set path to default
		this.path = "jdbc:sqlite:./test.db";
	}
	
	public void removeDict(String dictName) {
		String query = "DELETE FROM dict WHERE name = ?";
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(path);
			
			stmt = conn.prepareStatement(query);
			stmt.setString(1, dictName);
			stmt.executeUpdate();
			
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void purgeDict(String dictName) {
		String query = "DROP TABLE `$tableName`";
		Statement stmt = null;
		
		try {
			conn = DriverManager.getConnection(path);
			
			query = query.replace("$tableName", dictName);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int countWordQuiz(String dictName) {
		String query = "SELECT COUNT(*) AS total FROM `$tableName` WHERE freq <> 0";
		Statement stmt = null;
		ResultSet rs;
		int count;
		
		try {
			conn = DriverManager.getConnection(path);
			
			query = query.replace("$tableName", dictName);
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			count = rs.getInt("total");
			
			stmt.close();
			rs.close();
			
			return count;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return -1;
		}
		
	}

	public int countLearnedWord(String dictName) {
		String query = "SELECT COUNT(*) AS total FROM `$tableName` WHERE freq <> 0";
		Statement stmt = null;
		ResultSet rs;
		int count;
		
		try {
			conn = DriverManager.getConnection(path);
			
			query = query.replace("$tableName", dictName);
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			count = rs.getInt("total");
			
			stmt.close();
			rs.close();
			
			return count;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return -1;
		}
		
	}	
	public int countWord(String dictName) {
		String query = "SELECT COUNT(*) AS total FROM `$tableName`";
		Statement stmt = null;
		ResultSet rs;
		int count;
		
		try {
			conn = DriverManager.getConnection(path);
			
			query = query.replace("$tableName", dictName);
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			count = rs.getInt("total");
			
			stmt.close();
			rs.close();
			
			return count;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return -1;
		}
		
	}
	
	public void resetWordFreq(String dictName) {
		String query = "UPDATE `$tableName` set freq = 0";
		Statement stmt = null;
		
		try {
			conn = DriverManager.getConnection(path);
			
			query = query.replace("$tableName", dictName);

			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
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
			
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			Debugger.log(e.getMessage());
		}

	}
	
	public ArrayList<HashMap<String, String>> quizRandomWordSelect(String dictName, int count) {
		ArrayList<HashMap<String, String>> hm;
		String query = "SELECT * FROM `$tableName` WHERE freq <> 0 ORDER BY RANDOM() LIMIT ?";
		PreparedStatement stmt = null;
		ResultSet rs;
		
		try {
			hm = new ArrayList<HashMap<String, String>>();
			
			conn = DriverManager.getConnection(path);
			
			query = query.replace("$tableName", dictName);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, count);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				HashMap<String, String> thm = new HashMap<String, String>();
				thm.put("word", rs.getString("word"));
				thm.put("mean", rs.getString("mean"));
				thm.put("freq", String.valueOf(rs.getInt("freq")));
				hm.add(thm);
			}
			
			Debugger.log(hm.size());
			
			return hm;

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public HashMap<String, String> learnRandomWordSelect(String dictName) {
		
		HashMap<String, String> hm;
		String query = "SELECT * FROM `$tableName` WHERE freq = 0 ORDER BY RANDOM() LIMIT 1";
		PreparedStatement stmt = null;
		ResultSet rs;
		
		try {
			hm = new HashMap<String, String>();

			conn = DriverManager.getConnection(path);

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

				stmt.close();
				conn.close();
				return hm;
			}
			
			stmt.close();
			conn.close();
			
			return null;

		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
		
	}
	
	public boolean hasWordLeftQuiz(String dictName) {

		String query = "SELECT * FROM `$tableName` WHERE freq <> 0";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean exist;

		try {
			conn = DriverManager.getConnection(path);

			query = query.replace("$tableName", dictName);
			stmt = conn.prepareStatement(query);

			rs = stmt.executeQuery();
			exist = rs.next();

			stmt.close();
			conn.close();

			return exist;

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}

	}

	
	public boolean hasWordLeftLearn(String dictName) {
		
		String query = "SELECT * FROM `$tableName` WHERE freq = 0";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean exist;
		
		 try {
			conn = DriverManager.getConnection(path);

			 query = query.replace("$tableName", dictName);
			 stmt = conn.prepareStatement(query);

			 rs = stmt.executeQuery();
			 exist = rs.next();
			 
			 stmt.close();
			 conn.close();

			 return exist;

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
		PreparedStatement stmt;

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
		DatabaseMetaData md;
		ResultSet rs;
		Boolean exist;
		
		try {
			conn = DriverManager.getConnection(path);

			md = conn.getMetaData();

			rs = md.getTables(null, null, tableName, null);
			exist = rs.next();
			
			conn.close();
			
			return exist;
			// rs.last();
			// return (rs.getRow() > 0);

		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	public void query(String query) {
		Statement stmt;
		
		try {
			conn = DriverManager.getConnection(path);
			
			stmt = conn.createStatement();
			
			stmt.executeUpdate(query);
			
			stmt.close();
			conn.close();

		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public ResultSet fetch(String query) {
		
		Statement stmt;
		ResultSet rs;
		
		try {
			conn = DriverManager.getConnection(path);
			
			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);
			
			Debugger.log(query);
			
			return rs;

		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public ArrayList<String> getdictListData() {

		ArrayList<String> dictList;
		String query = "SELECT * FROM dict";
		Statement stmt;
		ResultSet rs;


		try {
			dictList = new ArrayList<String>();
			
			conn = DriverManager.getConnection(path);
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(query);
			while(rs.next())
				dictList.add(rs.getString("name"));

			stmt.close();
			conn.close();
			
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

}
