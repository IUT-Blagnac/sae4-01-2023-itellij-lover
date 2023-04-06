package com.github.itellijlover.bd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SingleConnexion {

	private String db;
	private String url ;
	private String user;
	private String pwd;
	private static Connection connect = null;
	private Properties prop = new Properties();

	private void loadProp() {
		try {
			this.prop.load(SingleConnexion.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private SingleConnexion() {
		try {
			loadProp();
			Class.forName(prop.getProperty("DBDriver"));
			this.db=prop.getProperty("DBName");
			this.user=prop.getProperty("DBUser");
			this.pwd="toor";
			this.url= "jdbc:"+prop.getProperty("DBProtocol")+"://"+prop.getProperty("DBIP")+":"+prop.getProperty("DBPort")+"/";
			Connection connect = DriverManager.getConnection(url+db, user, pwd);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static Connection getUniqueInstance() {
		if (connect == null)
			new SingleConnexion();
		return connect;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SingleConnexion.getUniqueInstance();
	}

}
