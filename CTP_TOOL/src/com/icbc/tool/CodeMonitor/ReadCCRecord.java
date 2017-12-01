package com.icbc.tool.CodeMonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.icbc.tool.db.CTPBean;

/**
 * 读取CC程序变更数据库服务器的记录
    deliver活动和rebase活动的记录
    activity字段是以deliver或rebase开头
 * @author kfzx-yanyj
 *
 */
public class ReadCCRecord {

	private Connection con = null;
	private String dbDriverStr = "oracle.jdbc.driver.OracleDriver";
	// private String dbUrl =
	// "jdbc:oracle:thin:@${dbHostIp}:${dbPort}:${dbInstName}";
	private String dbUrl = "jdbc:oracle:thin:@122.16.125.198:1521:SCMDB"; //统计CC的数据库链接串
	private String dbUser = "DATA";
	private String dbPass = "version";
	private String dbInstName = "SCMDB";
	//private String qrySql = "SELECT ${itemIdFd},${itemNameFd},${itemUrlFd} FROM ${itemTableName}";
	private String qrySql = "select t.branch,t.element_name,t.fullpath,t.create_date,t.creator,t.developer,t.add_valid,t.mod_valid,t.move_valid,t.del_valid from test.data_version t where t.branch = \'N2CBMS1611_B\' and t.add_valid+t.mod_valid+t.move_valid+t.del_valid>0 and  t.activity not like \'%rebase%\'";
	
	/*public void readConfigFile() {

		String curDirPath = System.getProperty("user.dir") + File.separator
				+ "src";
		// String curDirPath = System.getProperty("user.dir");
		try {
			String configPath = new File(curDirPath, "config/CCDBconfig.ini")
					.getAbsolutePath();
			FileInputStream fis = new FileInputStream(configPath);
			System.out.println(configPath);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String confLine = "";
			while ((confLine = br.readLine()) != null) {
				if (confLine.indexOf("dbHostIp") != -1) {
					String dbHostIp = confLine.substring(
							confLine.indexOf("=") + 1).trim();
					dbUrl = dbUrl.replace("${dbHostIp}", dbHostIp);
					System.out.println("dbHostIp=" + dbHostIp);
				}
				if (confLine.indexOf("dbPort") != -1) {
					String dbPort = confLine.substring(
							confLine.indexOf("=") + 1).trim();
					dbUrl = dbUrl.replace("${dbPort}", dbPort);
					System.out.println("dbPort=" + dbPort);
				}
				if (confLine.indexOf("dbInstName") != -1) {
					dbInstName = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					dbUrl = dbUrl.replace("${dbInstName}", dbInstName);
					System.out.println("dbInstName=" + dbInstName);
				}
				if (confLine.indexOf("dbUser") != -1) {
					dbUser = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					System.out.println("dbUser=" + dbUser);
				}
				if (confLine.indexOf("dbPass") != -1) {
					dbPass = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					System.out.println("dbPass=" + dbPass);
				}
				if (confLine.indexOf("itemTableName") != -1) {
					itemTableName = confLine.substring(
							confLine.indexOf("=") + 1).trim();
					qrySql = qrySql.replace("${itemTableName}", itemTableName);
					System.out.println("itemTableName=" + itemTableName);
				}
				if (confLine.indexOf("itemIdFd") != -1) {
					itemIdFd = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					qrySql = qrySql.replace("${itemIdFd}", itemIdFd);
					System.out.println("itemIdFd=" + itemIdFd);
				}
				if (confLine.indexOf("itemNameFd") != -1) {
					itemNameFd = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					qrySql = qrySql.replace("${itemNameFd}", itemNameFd);
					System.out.println("itemNameFd=" + itemNameFd);
				}
				if (confLine.indexOf("itemUrlFd") != -1) {
					itemUrlFd = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					qrySql = qrySql.replace("${itemUrlFd}", itemUrlFd);
					System.out.println("itemUrlFd=" + itemUrlFd);
				}
				if(confLine.indexOf("itemMenuName") != -1){
					itemMenuName = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					qrySql = qrySql.replace("${itemMenuName}", itemMenuName);
					System.out.println("itemMenuName=" + itemMenuName);
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e1) {
			System.out.println("读取config.ini失败" + e1.getMessage());
		}
	}*/
	
	public Connection getConnection() {
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
	
	public HashSet<CCRecordBean> readTableColumns() throws Exception {
		//readConfigFile();
		getConnection();
		HashSet<CCRecordBean> result = new HashSet<CCRecordBean>();
		CCRecordBean tmp = null;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(qrySql);
			while (rs.next()) {
				tmp = new CCRecordBean();
				tmp.setBranch(rs.getString("branch"));
				tmp.setFullPath(rs.getString("fullpath"));
				tmp.setCreate_date(rs.getString("create_date"));
				tmp.setCreator(rs.getString("creator"));
				tmp.setElement_name(rs.getString("element_name"));
				tmp.setDeveloper(rs.getString("developer"));
				tmp.setAdd_valid(rs.getInt("add_valid"));
				tmp.setMod_valid(rs.getInt("mod_valid"));
				tmp.setMov_valid(rs.getInt("move_valid"));
				tmp.setDel_valid(rs.getInt("del_valid"));
				System.out.println(tmp.getBranch()+" "+tmp.getElement_name()+" "+tmp.getFullPath()+" "+tmp.getCreate_date());
				result.add(tmp);
			}
			System.out.println("CC修改程序个数="+result.size());
			return result;
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return null;
	}
	public static void main(String[] args){
		try {
			HashSet<CCRecordBean> result = new ReadCCRecord().readTableColumns();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
