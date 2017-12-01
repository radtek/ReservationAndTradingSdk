package com.icbc.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectManager {
	private String dbDriverStr = "oracle.jdbc.driver.OracleDriver";
    private Connection con = null;
	public Connection getConnection(String dbUrl,String dbUser,String dbPass){
		try {
			Class.forName(dbDriverStr);
			// System.out.println("dbUrl = "+dbUrl);
			con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			return con;
		} catch (Exception ex) {
			System.out.println("2:" + ex.getMessage());
		}
		return con;
	}
}
