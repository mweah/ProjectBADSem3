package db;

import java.sql.*;


public class Connect {
	private final String username = "root";
	private final String password = "";
	private final String database = "hoohdie";
	private final String host = "localhost:3306";
	private final String connection = String.format("jdbc:mysql://%s/%s", host, database);
	
	private Connection con;
	private Statement state;
	public static Connect connect;
	private static PreparedStatement ps;
	public ResultSet rs;
	
	

	private Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(connection, username, password);
			state = con.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connect getInstance() {
		if(connect == null) {
			connect = new Connect();
		}
		return connect;
	}
	
	public ResultSet selectData(String query){
		try {
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public void executeUpdate(String query) {
		try {
			state.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void executeInsert(String query) {
		try {
			ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setResultSet(ResultSet rs) {
		
	}
	
	public ResultSet getResultSet() {
		return this.rs;
	}
}
