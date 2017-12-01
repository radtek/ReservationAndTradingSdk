package com.icbc.devp.expFileddlChk;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.db.DBUtils;

/**
 * 批量导出文件与数据库表的关系
 *  1，select t.prog,t.parameter from mbdp_b_stepdef t where t.prog_type = 'EXP' 找 parameter表示的文件，与存过中调用的表关系
 *  2，PBMS_EXPFILE_CTL 与 MSC_BATCHEXP_PARAM 
 *  3，JAVA类导出文件
 * 
 * @author kfzx-yanyj
 * @since 2016/12/15
 * 
 */
public class ExpFilesWithDBTable {

	private HashMap<String, HashSet<String>> FileToDBTable = new HashMap<String,HashSet<String>>();// 导出文件对应的数据库表
	private HashMap<String, HashSet<String>> DBTabletoFile = new HashMap<String,HashSet<String>>();// 数据库表对应的文件
	private String DBUrl, tnsName, user, pwd, port,expTable,colName1,colName2;
	boolean isClob;
	private ConnectionManager connInstance = new ConnectionManager();
	private DBUtils dbUtil = new DBUtils();
	private boolean isConnOK = false;
	
	private final String sqlExp = "select t.prog,t.parameter from mbdp_b_stepdef t where t.prog_type = 'EXP' ";

	/**
	 * 构造函数链接数据库
	 * 
	 * @param DBUrl
	 * @param tnsName
	 * @param user
	 * @param pwd
	 * @param port
	 */
	public ExpFilesWithDBTable(String DBUrl, String tnsName, String user,
			String pwd, String port,String expTable, String colName1,
			String colName2, boolean isClob) {
		this.DBUrl = DBUrl;
		this.tnsName = tnsName;
		this.user = user;
		this.pwd = pwd;
		this.port = port;
		this.expTable = expTable;
		this.colName1 = colName1;
		this.colName2 = colName2;
		this.isClob = isClob;
		if (connInstance.initDBConnection(DBUrl, tnsName, user, pwd, port)) {
			isConnOK = true;
			EXPTypeBatchStep(sqlExp);
			EXPFileWithTable(expTable,colName1,colName2,isClob);
			disConnectDB();
		}
	}

	/**
	 * 断开数据库链接
	 */
	public void disConnectDB() {
		try {
			connInstance.closeDBConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 导出文件-存储过程-数据库表
	 *  1，select t.prog,t.parameter from mbdp_b_stepdef t where t.prog_type = 'EXP' 找 parameter表示的文件，与存过中调用的表关系
	 * @param sql
	 */
	public void EXPTypeBatchStep(String sql) {
		String fileName,pckgProcName;
		try {
			if (isConnOK) {
				ReadDBTable rdt = new ReadDBTable(DBUrl,  user, pwd,tnsName);
				HashMap<String,HashSet<String>> pckgProcDBTable=rdt.getProcedureRefTable();
				// 查回导出文件和存储过程的关系
				List result = dbUtil.executeQry(connInstance.getConnection(),sql);
                for(int i=0;i<result.size();++i){
                	LinkedHashMap ele = (LinkedHashMap) result.get(i);
                	pckgProcName = (String) ele.get("PROG");
                	fileName = handleFileName((String) ele.get("PARAMETER"));
                	if(FileToDBTable.containsKey(fileName)){
                		FileToDBTable.get(fileName).addAll(pckgProcDBTable.get(pckgProcName));
                	}else{
                		FileToDBTable.put(fileName, new HashSet<String>());
                		FileToDBTable.get(fileName).addAll(pckgProcDBTable.get(pckgProcName));
                	}
                	for(String tableName : pckgProcDBTable.get(pckgProcName)){
                		insertTableToFile(tableName,fileName);
                	}
                }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
  /**
   * 处理 IN_FILEPATH=DATA_EXP|FILENAME=POLICY-F-CBMS-NODE1-DCCSH-MSCDB-MSC-YYYYMMDD.TXT|IN_PAR1=84.24.17.59|IN_WORKDATE=$WORKDATE
   * @param str
   * @return
   */
	
	public String handleFileName(String str){
		int idx2,idx = str.indexOf("FILENAME=");
		if(idx==-1)return str;
		idx2 = idx+9;
		while(idx2<str.length()){
			if(str.charAt(idx2)=='|')break;
			idx2++;
		}		
		return str.substring(idx+9, idx2);
	}
	/**
	 * 建立数据库表跟文件名的关系
	 * @param tables
	 * @param fileName
	 */
	public void insertTableToFiles(HashSet<String>tables,String fileName){
		for(String tt : tables){
			if(DBTabletoFile.containsKey(tt))
			{
				DBTabletoFile.get(tt).add(fileName);
			}else{
				if(tt.isEmpty())continue;
				DBTabletoFile.put(tt, new HashSet<String>());
				DBTabletoFile.get(tt).add(fileName);
			}
		}
		
	}
	/**
	 * 建立数据库表跟文件名的关系
	 * @param tableName
	 * @param fileName
	 */
	public void insertTableToFile(String tableName,String fileName){
		if(DBTabletoFile.containsKey(tableName))
		{
			DBTabletoFile.get(tableName).add(fileName);
		}else{
			DBTabletoFile.put(tableName, new HashSet<String>());
			DBTabletoFile.get(tableName).add(fileName);
		}
	}
	/**
	 * 
	 * MBDP框架中，有通过PBMS_EXPFILE_CTL 与 MSC_BATCHEXP_PARAM 配置文件名和数据库表的关系
	 * 从配置文件处读取相关信息?
	 * @param tableName
	 * @param colName1
	 * @param colName2
	 * @param isClob
	 */
	public void EXPFileWithTable(String tableName, String colName1,
			String colName2, boolean isClob) {
		if (isConnOK) {
			String sqlText = "select " + colName1 + "," + colName2 + " from "
					+ tableName;
			String fileName, SQLStr;
			Clob clob;
			try {
				Statement stmt = null;
				ResultSet rs = null;
				stmt = connInstance.getConnection().createStatement();
				rs = stmt.executeQuery(sqlText);
				//PBMS的导出文件模块
				if(tableName.indexOf("PBMS")!=-1){
					while (rs.next()) {
						fileName = rs.getString(colName1);
						SQLStr = rs.getString(colName2);
						if(SQLStr==null)
						{
							FileToDBTable.put(fileName, new HashSet<String>());
							continue;
						}
						
						if (FileToDBTable.containsKey(fileName)) {
							FileToDBTable.get(fileName).addAll(
									getTableNameForPBMS(SQLStr));
							insertTableToFiles(getTableNameForPBMS(SQLStr),fileName);
						} else {
							FileToDBTable.put(fileName, new HashSet<String>());
							FileToDBTable.get(fileName).addAll(
									getTableNameForPBMS(SQLStr));
							insertTableToFiles(getTableNameForPBMS(SQLStr),fileName);
						}
					}
				}else{
				  //判断是否Clob字段	
				  if (isClob) {
					while (rs.next()) {
						fileName = rs.getString(colName1);
						clob = rs.getClob(colName2);
						SQLStr = clob.toString();
						clob.free();
						if (FileToDBTable.containsKey(fileName)) {
							FileToDBTable.get(fileName).addAll(
									getTableName(SQLStr));
							insertTableToFiles(getTableName(SQLStr),fileName);
						} else {
							FileToDBTable.put(fileName, new HashSet<String>());
							FileToDBTable.get(fileName).addAll(
									getTableName(SQLStr));
							insertTableToFiles(getTableName(SQLStr),fileName);
						}
					}
				} else {
					while (rs.next()) {
						fileName = rs.getString(colName1);
						SQLStr = rs.getString(colName2);
						if (FileToDBTable.containsKey(fileName)) {
							FileToDBTable.get(fileName).addAll(
									getTableName(SQLStr));
							insertTableToFiles(getTableName(SQLStr),fileName);
						} else {
							FileToDBTable.put(fileName, new HashSet<String>());
							FileToDBTable.get(fileName).addAll(
									getTableName(SQLStr));
							insertTableToFiles(getTableName(SQLStr),fileName);
						}
					}
				  }
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 把SQL语句的数据库表名抽取出来
	 * 
	 * @param sqlText
	 * @return
	 */
	public HashSet<String> getTableName(String sqlText) {
		HashSet<String> result = new HashSet<String>();
		int idxBeg = sqlText.toLowerCase().indexOf("from");
		int idxEnd = sqlText.toLowerCase().indexOf("where");
		if (idxBeg == -1 ) {
			return result;
		} else {
			String[] arr = sqlText.substring(idxBeg + 5, (idxEnd==-1 ? sqlText.length() : idxEnd - 1)).split(",");
			for (String ele : arr) {
				result.add(ele);
				//System.out.println(ele);
			}
		}
		return result;
	}
	
	/**
	 * 把数据库表名从拼串抽取出来 for PBMS
	 * 
	 * @param sqlText
	 * @return
	 */
	public HashSet<String>getTableNameForPBMS (String tableNames){
		HashSet<String> result = new HashSet<String>();
		try{
		   if(tableNames.isEmpty() || tableNames==null)return result;		
		   String[] arr = tableNames.split("\\|");
		   for (String ele : arr) {
			result.add(repSepChar(ele));
		   }
		}catch(Exception e){
			System.out.println("TableNames="+tableNames+"END");
			e.printStackTrace();
		}
		return result;
	}
    
	/**
	 * 
	 * 处理此种情况，PBMS_BASE_USER@BATCHDBLINK
	 * @param tableName
	 * @return
	 */
	public String repSepChar(String tableName){
		int idx = tableName.indexOf('@');
		if(idx==-1)return tableName;
		else{
			return tableName.substring(0,idx);
		}
	}
	/*
	public static void main(String[] args) {
		// new
		// ExpFilesWithDBTable("122.16.45.196","MSCDB","msc","msc","1521").EXPTypeBatchStep("select t.prog,t.parameter from mbdp_b_stepdef t where t.prog_type = \'EXP\'");
		//new ExpFilesWithDBTable("122.16.45.196", "MSCDB", "msc", "msc", "1521").EXPFileWithTable("MSC_BATCHEXP_PARAM", "FILE_NAME", "SQLSTR",false);
		//new ExpFilesWithDBTable("122.16.45.71", "npbmsdb", "batch", "batch3", "1521").EXPFileWithTable("PBMS_EXPFILE_CTL", "FILE_NAME", "REL_TABLES",true);

	}
	*/

	public HashMap<String, HashSet<String>> getFileToDBTable() {
		return FileToDBTable;
	}

	public void setFileToDBTable(HashMap<String, HashSet<String>> fileToDBTable) {
		FileToDBTable = fileToDBTable;
	}

	public HashMap<String, HashSet<String>> getDBTabletoFile() {
		return DBTabletoFile;
	}

	public void setDBTabletoFile(HashMap<String, HashSet<String>> dBTabletoFile) {
		DBTabletoFile = dBTabletoFile;
	}

}
