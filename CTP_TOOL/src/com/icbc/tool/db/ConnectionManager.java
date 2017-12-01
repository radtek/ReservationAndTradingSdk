package com.icbc.tool.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**���ݿ����ӹ����ֻ࣬��һ������- -|||���̹�*/
public class ConnectionManager {

	public ConnectionManager(){
		isFree = false;
	}
	
	/**��ʼ�����ݿ�����
	 * @param String DBUrl
	 * ���ݿ�ip��ַ
	 * @param String tnsName
	 * TNS����
	 * @param String user
	 * ���ݿ��û���
	 * @param String pwd
	 * ���ݿ�����
	 * @param String port
	 * ���ݿ���ʶ˿�
	 * @return boolean
	 * ��ʼ���ɹ�����true,���򷵻�false*/
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

	/**��ʼ�����ݿ�����
	 * @param String dbFullUrl
	 * ƴ�Ӻõ�url
	 * @param String tnsName
	 * TNS����
	 * @param String user
	 * ���ݿ��û���
	 * @param String pwd
	 * ���ݿ�����
	 * @param String port
	 * ���ݿ���ʶ˿�
	 * @return boolean
	 * ��ʼ���ɹ�����true,���򷵻�false*/
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
	/**�ر����ݿ���Դ*/
	public void closeDBConnection()
	  throws Exception{
		if(con != null){
			con.close();
		}
	}
	
	/**��ȡ���ӣ�û�п�����Դ�򷵻�null*/
	public Connection getConnection()
	  throws Exception{
		if(con==null || con.isClosed()){
			con = DriverManager.getConnection(conStr,dbUser,dbPwd);
			return con;
		}
		return con;
	}
	
	/**�ͷ�����*/
	public void freeConnection()
	  throws Exception{
		if(con == null || con.isClosed()){
			isFree = false;
		}
		//�ͷ��ˣ���������
		isFree = true;
	}
	private static String conStr;
	private static String dbUser;
	private static String dbPwd;
	private static Connection con = null;
	private static boolean isFree;
}
