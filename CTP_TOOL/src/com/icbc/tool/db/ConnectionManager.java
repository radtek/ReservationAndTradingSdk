package com.icbc.tool.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**数据库连接管理类，只有一个链接- -|||忍忍哈*/
public class ConnectionManager {

	public ConnectionManager(){
		isFree = false;
	}
	
	/**初始化数据库连接
	 * @param String DBUrl
	 * 数据库ip地址
	 * @param String tnsName
	 * TNS名称
	 * @param String user
	 * 数据库用户名
	 * @param String pwd
	 * 数据库密码
	 * @param String port
	 * 数据库访问端口
	 * @return boolean
	 * 初始化成功返回true,否则返回false*/
	public static  boolean initDBConnection(String DBUrl,
			                        String tnsName,
			                        String user,
			                        String pwd,
			                        String port){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String tmp = "jdbc:oracle:thin:@"+DBUrl +":"+port+":"+tnsName;
			conStr = tmp;
			dbUser = user;
			dbPwd  = pwd;
			if(con != null){
				try{
					con.close();
					con = null;
				}catch(Exception xe){
					
				}
			}
			con = DriverManager.getConnection(tmp,user,pwd);
			isFree = true;
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**初始化数据库连接
	 * @param String dbFullUrl
	 * 拼接好的url
	 * @param String tnsName
	 * TNS名称
	 * @param String user
	 * 数据库用户名
	 * @param String pwd
	 * 数据库密码
	 * @param String port
	 * 数据库访问端口
	 * @return boolean
	 * 初始化成功返回true,否则返回false*/
	public boolean initDBConnection(String dbFullUrl,
			                        String user,
			                        String pwd){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String tmp = dbFullUrl;
			conStr = tmp;
			dbUser = user;
			dbPwd  = pwd;
			con = DriverManager.getConnection(tmp,user,pwd);
			isFree = true;
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**关闭数据库资源*/
	public void closeDBConnection()
	  throws Exception{
		if(con != null){
			con.close();
		}
	}
	
	/**获取连接，没有可用资源则返回null*/
	public Connection getConnection()
	  throws Exception{
		if(con==null || con.isClosed()){
			con = DriverManager.getConnection(conStr,dbUser,dbPwd);
			return con;
		}
		return con;
	}
	
	/**释放连接*/
	public void freeConnection()
	  throws Exception{
		if(con == null || con.isClosed()){
			isFree = false;
		}
		//释放了，可以用啦
		isFree = true;
	}
	private static String conStr;
	private static String dbUser;
	private static String dbPwd;
	private static Connection con = null;
	private static boolean isFree;
}
