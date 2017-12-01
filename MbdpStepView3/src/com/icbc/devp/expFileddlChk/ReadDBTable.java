package com.icbc.devp.expFileddlChk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.icbc.devp.tool.util.WriteExcel;

/***
 * 2014-10-11
 * 
 * @author kfzx-yanyj
 * @version 1.0
 * 
 * 2016-12-5
 * @version 2.0
 */

public class ReadDBTable {

	private String dbDriverStr = "oracle.jdbc.driver.OracleDriver";
	// private String dbUrl =
	// "jdbc:oracle:thin:@${dbHostIp}:${dbPort}:${dbInstName}";
	private String dbUrl = "jdbc:oracle:thin:@122.16.45.196:1521:MSCDB";
	private String dbUser = "msc";
	private String dbPass = "msc"; //bapp79226
	private String dbInstName = "MSCDB";
	private Connection con = null;
	private Pattern pattern_globe = null;
	private Matcher matcher_globe = null;
	// 记录数据库表与对应存储过程的关系
	private HashMap<String, HashSet<DBTableBean>> dbTableRefProc = new HashMap<String, HashSet<DBTableBean>>();
	//private HashMap<String, HashSet<String>> dbPckgProcRefTable = new HashMap<String, HashSet<String>>();
	private HashMap<String, HashSet<String>> pckgProcRefTable = new HashMap<String, HashSet<String>>();
	private HashSet<USER_SEQUENCES_Bean> user_Seqs = new HashSet<USER_SEQUENCES_Bean>(); //序号与存储过程的对照关系
	private HashMap<HashMap<String, String>, Boolean> mbdpMatrix = new HashMap<HashMap<String, String>, Boolean>();

	public HashMap<String, HashSet<DBTableBean>> getDbTableRefProc() {
		return dbTableRefProc;
	}

	public HashSet<MBDPMatrix> mbdpMatrixs = new HashSet<MBDPMatrix>();
   
	public ReadDBTable(String dbUrl,String dbUser,String dbPass,String dbInstName){
		this.dbUrl = "jdbc:oracle:thin:@"+dbUrl+":1521:"+dbInstName;
		this.dbUser = dbUser;
	    this.dbPass = dbPass;
	    this.dbInstName = dbInstName;
	}
	
	
	public void process() throws Exception {
		

		HashMap<String, HashSet<String>> result = getProcedureRefTable();
		//HashMap<String, HashSet<String>> result2 = getPckgRefTable();
		// ArrayList<MBDPBean> mbdpList = getProcFromMBDP_B_STEPDEF();
		// mbdpMatrixs = procSteps(mbdpList);
		// getProcFromMBDP_B_STEPREL(mbdpMatrixs);

	}

	/**
	 * 处理同一个 DATA_SOURCE 和 CYCLE下，抽取steps
	 * @param args
	 * @throws Exception
	 */
	public HashSet<MBDPMatrix> procSteps(ArrayList<MBDPBean> mbdpList) {
		HashSet<MBDPMatrix> result = new HashSet<MBDPMatrix>();
		for (MBDPBean mbdp : mbdpList) {
			boolean flag = false;
			for (MBDPMatrix ele : result) {
				if (mbdp.getDATA_SOURCE().equals(ele.getDATA_SOURCE())
						&& mbdp.getCYCLE().equals(ele.getDATA_SOURCE())) {
					ele.getSteps().add(mbdp.getSTEP_NO());
					flag = true;
					break;
				}
			}
			if (!flag) {
				MBDPMatrix newMBDP = new MBDPMatrix();
				newMBDP.setMbdp_ID(mbdp.getDATA_SOURCE() + "_"
						+ mbdp.getCYCLE());
				newMBDP.setDATA_SOURCE(mbdp.getDATA_SOURCE());
				newMBDP.setCYCLE(mbdp.getCYCLE());
				newMBDP.getSteps().add(mbdp.getSTEP_NO());
				result.add(newMBDP);
			}
		}
		for (MBDPMatrix ele : result) {
			ele.initMatrix();
		}
		return result;
	}
   /*
	public static void main(String[] args) throws Exception {
		new ReadDBTable().process();
	}
	*/

	/**
	 * 获取文件名称对应的接口表名
	 * 
	 * @param fname
	 * @return
	 * @throws IOException
	 */
	private String impTbname(String fname) throws Exception {
		String tbname = null;
		BufferedReader reader = new BufferedReader(new FileReader(
				"src/导入文件及表关系(不含EBM).csv"));
		while ((tbname = reader.readLine()) != null) {
			if (tbname.startsWith(fname + ",")) {
				tbname = tbname.substring(tbname.indexOf(",") + 1,
						tbname.indexOf(",", tbname.indexOf(",") + 1));
				break;
			}
		}
		reader.close();
		if (tbname == null) {
			System.out.println("导入文件及表关系(不含EBM).csv中查找不到文件" + fname + "对应的表名");
		}
		return tbname;
	}

	/**
	 * 把Oracle的序号查回来，user_sequences
	 * 
	 * @return
	 * @throws Exception
	 */
	public void getSeqNames() throws Exception {
		HashSet<USER_SEQUENCES_Bean> result = new HashSet<USER_SEQUENCES_Bean>();
		USER_SEQUENCES_Bean nSeq;
		getConnection();
		Statement stmt = con.createStatement();
		String Sqls = "select t.sequence_name,t.min_value,t.max_value,t.cycle_flag,t.order_flag,t.cache_size,t.last_number from user_sequences t";
		ResultSet rs = stmt.executeQuery(Sqls);
		while (rs.next()) {
			nSeq = new USER_SEQUENCES_Bean();
			String sequence_name = rs.getString("sequence_name");
			String min_value = rs.getString("min_value");
			String max_value = rs.getString("max_value");
			String cycle_flag = rs.getString("cycle_flag");
			String order_flag = rs.getString("order_flag");
			String cache_size = rs.getString("cache_size");
			String last_number = rs.getString("last_number");
			nSeq.setSequence_name(sequence_name);
			nSeq.setMin_value(min_value);
			nSeq.setMax_value(max_value);
			nSeq.setCycle_flag(cycle_flag);
			nSeq.setOrder_flag(order_flag);
			nSeq.setCache_size(cache_size);
			nSeq.setLast_Number(last_number);
			nSeq.setProcs(new HashSet<String>());
			user_Seqs.add(nSeq);
		}
		//return result;
	}

	/**
	 * 通过SQL语句，找到MBDP_B_STEPDEF表中定义的PROC过程或IMP导入数据 DATA_SOURCE 数据源 CYCLE 日切或月切
	 * PROC 存储过程 STEPNO 步骤号 PARAMETER 参数 tableNames 步骤对应的表清单:操作类型
	 * 
	 * @throws SQLException
	 */
	public ArrayList<MBDPBean> getProcFromMBDP_B_STEPDEF() throws Exception {
		ArrayList<MBDPBean> result = new ArrayList<MBDPBean>();
		getConnection();
		Statement stmt = con.createStatement();
		String Sqls = "SELECT T.DATA_SOURCE,T.CYCLE,T.PROG,T.PROG_TYPE,T.STEPNO,T.PARAMETER FROM MBDP_B_STEPDEF T WHERE T.PROG_TYPE in('PROC','IMP','EXP')";
		ResultSet rs = stmt.executeQuery(Sqls);
		while (rs.next()) {
			MBDPBean step = new MBDPBean();
			String data_source = rs.getString("DATA_SOURCE");
			String cycle = rs.getString("CYCLE");
			String prog = rs.getString("PROG");
			String prog_type = rs.getString("PROG_TYPE");
			String stepno = rs.getString("STEPNO");
			String parameter = rs.getString("PARAMETER");
			step.setDATA_SOURCE(data_source);
			step.setCYCLE(cycle);
			step.setPROG(prog);
			step.setPROG_TYPE(prog_type);
			step.setSTEP_NO(stepno);
			if ("IMP".equals(prog_type)) {
				String fname = parameter;
				fname = fname.substring(fname.lastIndexOf("/") + 1,
						fname.lastIndexOf(".xml"));
				String tbname = impTbname(fname);
				HashSet<String> tableNames = new HashSet<String>();
				tableNames.add(tbname + ":INSERT");
				step.setTableNames(tableNames);
			} else {
				step.setTableNames(pckgProcRefTable.get(prog));
			}
			System.out.print(step.getDATA_SOURCE() + " " + step.getCYCLE()
					+ " " + step.getPROG() + " " + step.getSTEP_NO() + " ");
			System.out.println(step.getTableNames() == null ? " " : step
					.getTableNames().toString());
			result.add(step);
		}
		con.close();
		return result;
	}

	/**
	 * 通过SQL语句，找到MBDP_B_STEPREL表中定义步骤的前后关系
	 * 
	 * @throws Exception
	 * 
	 */
	public void getProcFromMBDP_B_STEPREL(HashSet<MBDPMatrix> mbdpMatrixs)
			throws Exception {
		getConnection();
		Statement stmt = con.createStatement();
		String Sqls = "SELECT T.* FROM MBDP_B_STEPREL T";
		ResultSet rs = stmt.executeQuery(Sqls);
		// HashSet<String> steps = new HashSet<String>();
		while (rs.next()) {
			String data_source = rs.getString("DATA_SOURCE");
			String cycle = rs.getString("CYCLE");
			String stepno_p = rs.getString("STEPNO_P");
			String stepno = rs.getString("STEPNO");
			for (MBDPMatrix ele : mbdpMatrixs) {
				if (data_source.equals(ele.getDATA_SOURCE())
						&& cycle.equals(ele.getCYCLE())) {
					ele.addMatrix(stepno_p, stepno);
					break;
				}
			}
		}
		for (MBDPMatrix ele : mbdpMatrixs) {
			ele.genMatrix();
		}
	}

	/**
	 * 通过SQL语句 查询Oracle数据库表中 PCKG与数据库表的对照关系,通过硬链接的方式
	 * 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, HashSet<String>> getPckgRefTable() throws Exception {
		HashMap<String, HashSet<String>> pckgRefTable = new HashMap<String, HashSet<String>>();
		// readConfigFile();
		getConnection();
		Statement stmt = con.createStatement();
		String Sqls = "select * from user_dependencies t where t.TYPE ='PACKAGE BODY' and t.REFERENCED_TYPE = 'TABLE'";
		ResultSet rs = stmt.executeQuery(Sqls);
		while (rs.next()) {
			String key = rs.getString("NAME");
			String value = rs.getString("REFERENCED_NAME");
			if (pckgRefTable.containsKey(key)) {
				pckgRefTable.get(key).add(value);
			} else {
				HashSet<String> tmpValue = new HashSet<String>();
				tmpValue.add(value);
				pckgRefTable.put(key, tmpValue);
			}
			// 添加数据库表与对应存储过程关系
			if (!dbTableRefProc.containsKey(value)) {
				DBTableBean tmp = new DBTableBean();
				HashSet<DBTableBean> tmpHS = new HashSet<DBTableBean>();
				dbTableRefProc.put(value, tmpHS);
			}
		}
		con.close();
		WriteExcel wr = new WriteExcel();
	    wr.WriteDBProcRefTable(pckgRefTable, dbInstName + "_Proc");
		return pckgRefTable;
	}

	/**
	 * 1)通过获取的pckg body，得到 Procedure对应的Table列表
	 * 2)通过获取的pckg body, 得到Seqs与Procedure的对应关系
	 * 
	 * @return
	 * @throws Exception
	 */

	public HashMap<String, HashSet<String>> getProcedureRefTable()
			throws Exception {
		HashMap<String, HashSet<String>> procedureRefTable = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> pckgRefTable = getPckgRefTable();
		getSeqNames();
		String Sqls = "select * from user_source t where t.type ='PACKAGE BODY' and t.name =";
		// readConfigFile();
		getConnection();
		for (Object obj : pckgRefTable.keySet()) {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(Sqls + "'" + obj.toString() + "'");
			ArrayList<String> pckgBody = new ArrayList<String>();
			while (rs.next()) {
				pckgBody.add(rs.getString("TEXT"));
			}
			stmt.close();
			procedureRefTable.putAll(getProcedureName(obj.toString(), pckgBody,
					pckgRefTable.get(obj)));

		}
		
		/*
		HashMap<String, HashSet<DBTableBean>> dbTableRefProc = getDbTableRefProc();
		
		for (Object obj : procedureRefTable.keySet()) {
			System.out.print(obj.toString() + " : ");
			for (String db : procedureRefTable.get(obj)) {
				if (db != null) {
					System.out.print(db+" ");
				}
			}
			System.out.println();
		}
		
		for(USER_SEQUENCES_Bean user_seq : user_Seqs){
			System.out.print(user_seq.getSequence_name() + ":");
			int num = 0 ;
			for(String proc:user_seq.getProcs()){
				System.out.println((++num) +" " + proc );
			}
		}
		*/	
		
		WriteExcel wr = new WriteExcel();
		wr.WriteDBTableRefProc(dbTableRefProc, user_Seqs,dbInstName);
		//wr.WritePckgProcRefTable(pckgProcRefTable,dbInstName);
		con.close();

		return procedureRefTable;
	}

	/**
	 * 获取pckg body 里面 Procedure对应的Table列表
	 * 
	 * @param pckg存储过程代码
	 * @param tables数据库表列表
	 * @return
	 */

	public HashMap<String, HashSet<String>> getProcedureName(String pckgName,
			ArrayList<String> pckg, HashSet<String> tables)throws Exception {
		HashMap<String, HashSet<String>> procedureRefTable = new HashMap<String, HashSet<String>>();
		String regex_procedure = "^.*PROCEDURE.*$";
		String regex_function = "^.*FUNCTION.*$";
		String Const_INSERT = "INSERT INTO"; // 字典值2
		String Const_UPDATE = "UPDATE"; // 字典值3
		String Const_DELETE = "DELETE"; // 字典值4
		String Const_MERGE = "MERGE INTO"; // 字典值5
		int i = 0, len = pckg.size();						
		while (i < len) {
			if (matchesPattern(regex_procedure, pckg.get(i).toUpperCase())) {
				String line = pckg.get(i).toUpperCase();
				int idxBeg = line.indexOf("PROCEDURE") + 10;
				int idxEnd = line.indexOf("(");
				if (idxEnd != -1 && idxBeg <= idxEnd) {
					String procName = line.substring(idxBeg, idxEnd).trim();
					++i;
					HashSet<String> values = new HashSet<String>();
					procedureRefTable.put(pckgName+"."+procName, values);
					String operType = null;
					while (i < len
							&& !matchesPattern(regex_procedure, pckg.get(i)
									.toUpperCase())
							&& !matchesPattern(regex_function, pckg.get(i)
									.toUpperCase())) {
						line = pckg.get(i).toUpperCase();
						/*
						 * 找数据库表序号与存储过程的对照关系
						 */
						for(USER_SEQUENCES_Bean user_seq : user_Seqs ){
							if(line.indexOf(user_seq.getSequence_name())!=-1){
								user_seq.getProcs().add(pckgName+"."+procName);
							}
						}
						
						for (String str : tables) {
							if (line.indexOf(str) != -1) {
								// 记录对表的操作类型： 1 查询；2插入；3更新；4删除
								if (line.indexOf(Const_INSERT) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("INSERT");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName+ "." + procName)) {
										pckgProcRefTable.get(pckgName + "." + procName).add(str + ":INSERT");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":INSERT");
										pckgProcRefTable.put(pckgName + "."	+ procName, tableType);
									}
								} else if (line.indexOf(Const_UPDATE) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("UPDATE");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(str + ":UPDATE");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":UPDATE");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								} else if (line.indexOf(Const_DELETE) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("DELETE");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":DELETE");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":DELETE");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								} else if (line.indexOf(Const_MERGE) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("MERGE");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":MERGE");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":MERGE");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								} else {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("SELECT");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":SELECT");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":SELECT");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								}
								// 给存储过程添加操作的表名以及操作类型
								if(procedureRefTable.containsKey(pckgName+"."+procName)){
								  procedureRefTable.get(pckgName+"."+procName).add(str);
								}/*else{
									procedureRefTable.put(pckgName+"."+procName,str);
								}*/
							}
						}
						++i;
					}
					--i;
				}
			} else if (matchesPattern(regex_function, pckg.get(i).toUpperCase())) {
				String line = pckg.get(i).toUpperCase();
				int idxBeg = line.indexOf("FUNCTION") + 9;
				int idxEnd = line.indexOf("(");
				if (idxEnd != -1 && idxBeg <= idxEnd) {
					String procName = line.substring(idxBeg, idxEnd).trim();
					++i;
					HashSet<String> values = new HashSet<String>();
					procedureRefTable.put(pckgName+"."+procName, values);
					String operType = null;
					while (i < len
							&& !matchesPattern(regex_procedure, pckg.get(i)
									.toUpperCase())
							&& !matchesPattern(regex_function, pckg.get(i)
									.toUpperCase())) {
						line = pckg.get(i).toUpperCase();
						/*
						 * 找数据库表序号与存储过程的对照关系
						 */
						for(USER_SEQUENCES_Bean user_seq : user_Seqs ){
							if(line.indexOf(user_seq.getSequence_name())!=-1){
								user_seq.getProcs().add(pckgName+"."+procName);
							}
						}
						for (String str : tables) {
							if (line.indexOf(str) != -1) {
								// 记录对表的操作类型： 1 查询；2插入；3更新；4删除
								if (line.indexOf(Const_INSERT) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("INSERT");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":INSERT");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":INSERT");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								} else if (line.indexOf(Const_UPDATE) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("UPDATE");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":UPDATE");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":UPDATE");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								} else if (line.indexOf(Const_DELETE) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("DELETE");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":DELETE");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":DELETE");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								} else if (line.indexOf(Const_MERGE) != -1) {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("MERGE");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":MERGE");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":MERGE");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								} else {
									DBTableBean e = new DBTableBean();
									e.setPckgName(pckgName);
									e.setOperType("SELECT");
									operType = e.getOperType();
									e.setProcedureName(procName);
									dbTableRefProc.get(str).add(e);
									if (pckgProcRefTable.containsKey(pckgName
											+ "." + procName)) {
										pckgProcRefTable.get(
												pckgName + "." + procName).add(
												str + ":SELECT");
									} else {
										HashSet<String> tableType = new HashSet<String>();
										tableType.add(str + ":SELECT");
										pckgProcRefTable.put(pckgName + "."
												+ procName, tableType);
									}
								}
								// 给存储过程添加操作的表名以及操作类型
								procedureRefTable.get(pckgName+"."+procName).add(
										str);
							}
						}
						++i;
					}
					--i;
				}
			}
			++i;
		}
		/*for(Object str : procedureRefTable.keySet()){
			System.out.print(str.toString() +  " && ");
			for(String ele : procedureRefTable.get(str)){
				System.out.print(ele + "#");
			}
			System.out.println();
		}*/
		
		return procedureRefTable;
	}

	public synchronized boolean matchesPattern(String regex, String str) {
		pattern_globe = Pattern.compile(regex);
		matcher_globe = pattern_globe.matcher(str);
		return matcher_globe.find();
	}

	public Connection getConnection() {
		try {
			Class.forName(dbDriverStr);
			System.out.println("dbURL = "+dbUrl);
			con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			return con;
		} catch (Exception ex) {
			System.out.println("Exception Reason:" + ex.getMessage());
		}
		return con;
	}

	



}