package com.icbc.tool.CodeMonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

/**
 * 读取覆盖率工具记录的覆盖程序
 * @author kfzx-yanyj
 *
 */
public class ReadCoverageDB {
	private Connection con = null;
	private String dbDriverStr = "oracle.jdbc.driver.OracleDriver";
	// private String dbUrl =
	// "jdbc:oracle:thin:@${dbHostIp}:${dbPort}:${dbInstName}";
	private String dbUrl = "jdbc:oracle:thin:@122.20.109.170:1521:npbmsdb"; //测试覆盖率的数据库链接串
	private String dbUser = "PBMS";
	private String dbPass = "pbms";
	private String dbInstName = "npbmsdb";
	//private String qrySql = "SELECT ${itemIdFd},${itemNameFd},${itemUrlFd} FROM ${itemTableName}";
	private String qrySql = "select t.app_name,t.birthland,t.app_version,t.file_name,t.file_path,t.file_type,t.last_modified from prog_coverage_detail t where app_version = \'201611\' and t.has_test = \'否\' and t.app_name=\'F-CBMS\'";

	
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
	
	public HashSet<CoverageBean> readTableColumns() throws Exception {
		//readConfigFile();
		getConnection();
		HashSet<CoverageBean> result = new HashSet<CoverageBean>();
		CoverageBean tmp = null;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(qrySql);
			while (rs.next()) {
				tmp = new CoverageBean();
				tmp.setApp_name(rs.getString("app_name"));
				tmp.setApp_version(rs.getString("app_version"));
				tmp.setBirthland(rs.getString("birthland"));
				tmp.setFile_name(rs.getString("file_name"));
				tmp.setFile_path(rs.getString("file_path"));
				tmp.setLast_modified(rs.getString("last_modified"));
				tmp.setFile_type(rs.getString("file_type"));
				System.out.println(tmp.getApp_name()+" "+tmp.getFile_name()+ " " + tmp.getFile_path() + " "+tmp.getLast_modified());
				result.add(tmp);
			}
			System.out.println("未覆盖程序个数="+result.size());
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
			HashSet<CoverageBean> result = new ReadCoverageDB().readTableColumns();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
