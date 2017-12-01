package com.icbc.HiveSQLParse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.BaseSemanticAnalyzer;
import org.apache.hadoop.hive.ql.parse.HiveParser;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;
import org.apache.hadoop.hive.ql.parse.SemanticException;

import com.icbc.DBUtils.DBConnectManager;
import com.icbc.DBUtils.Etl_job_dependencyBean;


/**
 * 目的：获取AST中的表，列，以及对其所做的操作，如SELECT,INSERT 
 * 重点：获取SELECT操作中的表和列的相关操作。其他操作这判断到表级别。
 * 实现思路：对AST深度优先遍历，遇到操作的token则判断当前的操作，
 * 遇到TOK_TAB或TOK_TABREF则判断出当前操作的表，遇到子句则压栈当前处理，处理子句。 子句处理完，栈弹出。
 * 
 * 1) 
       对于bdsp脚本，按目前的访问控制逻辑，同库、跨库访问一个新的表，都需要向上海提出权限申请，由上海同事赋权之后才能访问，授权配置上海会带版本出去

 */
public class HQLParser {

	private static final String UNKNOWN = "UNKNOWN";
	
	private String dbUrl = "jdbc:oracle:thin:@122.21.173.139:1521:bdsp";
	private String dbUser = "bdsp";
	private String dbPass = "bdsp";
	
	private Map<String,HashSet<String>> hasPermissionTables = new HashMap<String, HashSet<String>>();
	private Set<String> hqlVisitTables = new HashSet<String>();
	private Map<String, String> alias = new HashMap<String, String>();
	private Map<String, String> cols = new TreeMap<String, String>();
	private Map<String, String> colAlais = new TreeMap<String, String>();
	private Set<String> tables = new HashSet<String>();
	private Stack<String> tableNameStack = new Stack<String>();
	private Stack<Oper> operStack = new Stack<Oper>();
	private String nowQueryTable = "";// 定义及处理不清晰，修改为query或from节点对应的table集合或许好点。目前正在查询处理的表可能不止一个。
	private Oper oper;
	private boolean joinClause = false;

	private enum Oper {
		SELECT, INSERT, DROP, TRUNCATE, LOAD, CREATETABLE, ALTER
	}

	public Set<String> parseIteral(ASTNode ast) {
		Set<String> set = new HashSet<String>();// 当前查询所对应到的表集合
		prepareToParseCurrentNodeAndChilds(ast);
		set.addAll(parseChildNodes(ast));
		set.addAll(parseCurrentNode(ast, set));
		endParseCurrentNode(ast);
		return set;
	}

	private void endParseCurrentNode(ASTNode ast) {
		if (ast.getToken() != null) {
			switch (ast.getToken().getType()) {// join 从句结束，跳出join
			case HiveParser.TOK_RIGHTOUTERJOIN:
			case HiveParser.TOK_LEFTOUTERJOIN:
			case HiveParser.TOK_JOIN:
				joinClause = false;
				break;
			case HiveParser.TOK_QUERY:
				break;
			case HiveParser.TOK_INSERT:
			case HiveParser.TOK_SELECT:
				nowQueryTable = tableNameStack.pop();
				oper = operStack.pop();
				break;
			}
		}
	}

	private Set<String> parseCurrentNode(ASTNode ast, Set<String> set) {
		if (ast.getToken() != null) {
			switch (ast.getToken().getType()) {
			case HiveParser.TOK_TABLE_PARTITION:
				// case HiveParser.TOK_TABNAME:
				if (ast.getChildCount() != 2) {
					String table = BaseSemanticAnalyzer
							.getUnescapedName((ASTNode) ast.getChild(0));
					if (oper == Oper.SELECT) {
						nowQueryTable = table;
					}
					tables.add(table + "\t" + oper);
				}
				break;

			case HiveParser.TOK_TAB:
				String tableTab = BaseSemanticAnalyzer
						.getUnescapedName((ASTNode) ast.getChild(0));
				if (oper == Oper.SELECT) {
					nowQueryTable = tableTab;
				}
				tables.add(tableTab + "\t" + oper);
				break;
			case HiveParser.TOK_TABREF:// inputTable
				ASTNode tabTree = (ASTNode) ast.getChild(0);
				String tableName = (tabTree.getChildCount() == 1) ? BaseSemanticAnalyzer
						.getUnescapedName((ASTNode) tabTree.getChild(0))
						: BaseSemanticAnalyzer
								.getUnescapedName((ASTNode) tabTree.getChild(0))
								+ "." + tabTree.getChild(1);
				if (oper == Oper.SELECT) {
					if (joinClause && !"".equals(nowQueryTable)) {
						nowQueryTable += "&" + tableName;//
					} else {
						nowQueryTable = tableName;
					}
					set.add(tableName);
				}
				tables.add(tableName + "\t" + oper);
				if (ast.getChild(1) != null) {
					String alia = ast.getChild(1).getText().toLowerCase();
					alias.put(alia, tableName);
				}
				break;
			case HiveParser.TOK_TABLE_OR_COL:
				if (ast.getParent().getType() != HiveParser.DOT) {
					String col = ast.getChild(0).getText().toLowerCase();
					if (alias.get(col) == null
							&& colAlais.get(nowQueryTable + "." + col) == null) {
						if (nowQueryTable.indexOf("&") > 0) {
							cols.put(UNKNOWN + "." + col, "");
						} else {
							cols.put(nowQueryTable + "." + col, "");
						}
					}
				}
				break;
			case HiveParser.TOK_ALLCOLREF:
				cols.put(nowQueryTable + ".*", "");
				break;
			case HiveParser.TOK_SUBQUERY:
				if (ast.getChildCount() == 2) {
					String tableAlias = unescapeIdentifier(ast.getChild(1)
							.getText());
					String aliaReal = "";
					for (String table : set) {
						aliaReal += table + "&";
					}
					if (aliaReal.length() != 0) {
						aliaReal = aliaReal.substring(0, aliaReal.length() - 1);
					}
			
					alias.put(tableAlias, aliaReal);

				}
				break;

			case HiveParser.TOK_SELEXPR:
				if (ast.getChild(0).getType() == HiveParser.TOK_TABLE_OR_COL) {
					String column = ast.getChild(0).getChild(0).getText()
							.toLowerCase();
					if (nowQueryTable.indexOf("&") > 0) {
						cols.put(UNKNOWN + "." + column, "");
					} else if (colAlais.get(nowQueryTable + "." + column) == null) {
						cols.put(nowQueryTable + "." + column, "");
					}
				} else if (ast.getChild(1) != null) {
					String columnAlia = ast.getChild(1).getText().toLowerCase();
					colAlais.put(nowQueryTable + "." + columnAlia, "");
				}
				break;
			case HiveParser.DOT:
				if (ast.getType() == HiveParser.DOT) {
					if (ast.getChildCount() == 2) {
						if (ast.getChild(0).getType() == HiveParser.TOK_TABLE_OR_COL
								&& ast.getChild(0).getChildCount() == 1
								&& ast.getChild(1).getType() == HiveParser.Identifier) {
							String alia = BaseSemanticAnalyzer
									.unescapeIdentifier(ast.getChild(0)
											.getChild(0).getText()
											.toLowerCase());
							String column = BaseSemanticAnalyzer
									.unescapeIdentifier(ast.getChild(1)
											.getText().toLowerCase());
							String realTable = null;
							if (!tables.contains(alia + "\t" + oper)
									&& alias.get(alia) == null) {// [b SELECT, a
																	// SELECT]
								alias.put(alia, nowQueryTable);
							}
							if (tables.contains(alia + "\t" + oper)) {
								realTable = alia;
							} else if (alias.get(alia) != null) {
								realTable = alias.get(alia);
							}
							if (realTable == null || realTable.length() == 0
									|| realTable.indexOf("&") > 0) {
								realTable = UNKNOWN;
							}
							cols.put(realTable + "." + column, "");

						}
					}
				}
				break;
			case HiveParser.TOK_ALTERTABLE_ADDPARTS:
			case HiveParser.TOK_ALTERTABLE_RENAME:
			case HiveParser.TOK_ALTERTABLE_ADDCOLS:
				ASTNode alterTableName = (ASTNode) ast.getChild(0);
				tables.add(alterTableName.getText() + "\t" + oper);
				break;
			}
		}
		return set;
	}

	private Set<String> parseChildNodes(ASTNode ast) {
		Set<String> set = new HashSet<String>();
		int numCh = ast.getChildCount();
		if (numCh > 0) {
			for (int num = 0; num < numCh; num++) {
				ASTNode child = (ASTNode) ast.getChild(num);
				set.addAll(parseIteral(child));
			}
		}
		return set;
	}

	private void prepareToParseCurrentNodeAndChilds(ASTNode ast) {
		if (ast.getToken() != null) {
			switch (ast.getToken().getType()) {// join 从句开始
			case HiveParser.TOK_RIGHTOUTERJOIN:
			case HiveParser.TOK_LEFTOUTERJOIN:
			case HiveParser.TOK_JOIN:
				joinClause = true;
				break;
			case HiveParser.TOK_QUERY:
				tableNameStack.push(nowQueryTable);
				operStack.push(oper);
				nowQueryTable = "";
				oper = Oper.SELECT;
				break;
			case HiveParser.TOK_INSERT:
				tableNameStack.push(nowQueryTable);
				operStack.push(oper);
				oper = Oper.INSERT;
				break;
			case HiveParser.TOK_SELECT:
				tableNameStack.push(nowQueryTable);
				operStack.push(oper);
				// nowQueryTable = nowQueryTable
				// nowQueryTable = "";//语法树join
				// 注释语法树sql9， 语法树join对应的设置为""的注释逻辑不符
				oper = Oper.SELECT;
				break;
			case HiveParser.TOK_DROPTABLE:
				oper = Oper.DROP;
				break;
			case HiveParser.TOK_TRUNCATETABLE:
				oper = Oper.TRUNCATE;
				break;
			case HiveParser.TOK_LOAD:
				oper = Oper.LOAD;
				break;
			case HiveParser.TOK_CREATETABLE:
				oper = Oper.CREATETABLE;
				break;
			}
			if (ast.getToken() != null
					&& ast.getToken().getType() >= HiveParser.TOK_ALTERDATABASE_PROPERTIES
					&& ast.getToken().getType() <= HiveParser.TOK_ALTERVIEW_RENAME) {
				oper = Oper.ALTER;
			}
		}
	}

	public static String unescapeIdentifier(String val) {
		if (val == null) {
			return null;
		}
		if (val.charAt(0) == '`' && val.charAt(val.length() - 1) == '`') {
			val = val.substring(1, val.length() - 1);
		}
		return val;
	}

	private void output(Map<String, String> map) {
		java.util.Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.println(key + "\t" + map.get(key));
		}
	}

	public void parse(ASTNode ast) {
		parseIteral(ast);
		System.out.println("***************表名**************");
		for (String table : tables) {
			System.out.println(table);
			String[] arr = table.split("\\s+");
			if(arr.length>0)
			  hqlVisitTables.add(arr[1]);
			else
			  hqlVisitTables.add(table);
		}
		System.out.println("***************列名**************");
		output(cols);
		System.out.println("***************别名***************");
		output(alias);
	}

	public static ArrayList<String> readFile(String fileName) {
		File file = new File(fileName);
		ArrayList<String>result = new ArrayList<String>();
		String pattern1 = "${version_num}", pattern2 = "${process_date}",pattern3= "queuename",pattern4="use";
		StringBuffer strSB = new StringBuffer();
		BufferedReader reader = null;
		boolean ignoreFlag = false;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), "GBK"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				if (tempString.indexOf("**")!=-1) {
					ignoreFlag = !ignoreFlag;
					continue;
				}
				if (ignoreFlag)
					continue;
				if(tempString.indexOf(pattern3)!=-1 || tempString.indexOf(pattern4)!=-1){
					strSB.delete(0, strSB.length());
					continue;
				}
				if(tempString.indexOf("--")!=-1){
					continue;
				}
				tempString = tempString.trim();
				if (tempString.indexOf(pattern1) != -1) {
					tempString = tempString.replace(pattern1, "");
				}
				if (tempString.indexOf(pattern2) != -1) {
					tempString = tempString.replace(pattern2, "2017-01-31");
				}
				tempString.replaceAll("(\r\n|\r|\n|\n\r)", "");
				if (tempString.length() < 1)
					continue;
				if (tempString.charAt(tempString.length() - 1) != ';') {
					tempString = tempString + " ";
				}
				strSB.append(tempString);
				if(tempString.indexOf(';')!=-1){
					result.add(strSB.toString().replace(';', ' '));
					strSB.delete(0, strSB.length());
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return result;
	}
    
	public ArrayList<Etl_job_dependencyBean> getEtl_job_dependency(String dbUrl,String dbUser,String dbPass,String appName){ 
		ArrayList<Etl_job_dependencyBean> result = new ArrayList<Etl_job_dependencyBean>();
		String qrySql = "select t.etl_system,t.etl_job,t.dependency_system,t.dependency_job,t.enable from etl_job_dependency t where t.etl_system like \'%"+appName+"%\'";
		Connection con = new DBConnectManager().getConnection(dbUrl, dbUser, dbPass);
		if(!hasPermissionTables.containsKey(appName)){
			hasPermissionTables.put(appName, new HashSet<String>());
		}
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(qrySql);
			while (rs.next()) {
				Etl_job_dependencyBean etl = new Etl_job_dependencyBean();
				etl.setETL_SYSTEM(rs.getString("ETL_SYSTEM"));
				etl.setETL_JOB(rs.getString("ETL_JOB"));
				etl.setDEPENDENCY_SYSTEM(rs.getString("DEPENDENCY_SYSTEM"));
				etl.setDEPENDENCY_JOB(rs.getString("DEPENDENCY_JOB"));
				etl.setENABLE(rs.getString("ENABLE"));
				result.add(etl);
				if(etl.getDEPENDENCY_JOB().indexOf("S_000")!=-1){
					hasPermissionTables.get(appName).add(etl.getDEPENDENCY_JOB());
				}
				System.out.println(etl.getDEPENDENCY_JOB() + " "+etl.getDEPENDENCY_SYSTEM()+" "+etl.getETL_JOB()+" "+etl.getETL_SYSTEM());
			}
			System.out.println(result.size());
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    return result;	
		
	}
	
	
	public ArrayList<Etl_job_dependencyBean> getEtl_job_dependency_Table(String dbUrl,String dbUser,String dbPass,String etl_job_Name){ 
		ArrayList<Etl_job_dependencyBean> result = new ArrayList<Etl_job_dependencyBean>();
		String qrySql = "select t.etl_system,t.etl_job,t.dependency_system,t.dependency_job,t.enable from etl_job_dependency t where t.etl_job = \'"+etl_job_Name+"\'";
		Connection con = new DBConnectManager().getConnection(dbUrl, dbUser, dbPass);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(qrySql);
			while (rs.next()) {
				Etl_job_dependencyBean etl = new Etl_job_dependencyBean();
				etl.setETL_SYSTEM(rs.getString("ETL_SYSTEM"));
				etl.setETL_JOB(rs.getString("ETL_JOB"));
				etl.setDEPENDENCY_SYSTEM(rs.getString("DEPENDENCY_SYSTEM"));
				etl.setDEPENDENCY_JOB(rs.getString("DEPENDENCY_JOB"));
				etl.setENABLE(rs.getString("ENABLE"));
				result.add(etl);		
				System.out.println(etl.getDEPENDENCY_JOB() + " "+etl.getDEPENDENCY_SYSTEM()+" "+etl.getETL_JOB()+" "+etl.getETL_SYSTEM());
			}
			System.out.println(result.size());
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    return result;	
		
	}
	
	public void AssertPermissionTables(String appName){
		boolean flag = false;
		System.out.println("非已有权限的表");
		int cnt = 0 ;
		for(String hqlVisit: hqlVisitTables){
			if(hasPermissionTables.get(appName).contains(hqlVisit)){
				flag = true;				
			}else{
				System.out.println(hqlVisit);
				cnt++;
			}
		}
		System.out.println("共有"+cnt+"个表需要申请权限");
	}
	public void AssertDependencyTables(String etl_job_Name){
		ArrayList<Etl_job_dependencyBean> etl_job_Tables =  getEtl_job_dependency_Table(dbUrl,dbUser,dbPass,etl_job_Name);
		boolean flag;
		System.out.println("没有登记在依赖信息的表");
		int cnt = 0 ;
		for(String hqlVisit: hqlVisitTables){
			flag = false;
			for(Etl_job_dependencyBean etl : etl_job_Tables){
				if(hqlVisit.indexOf(etl.getDEPENDENCY_JOB())!=-1){
					flag = true;
					break;
				}
			}
			if(!flag){
				cnt++;
				System.out.println(hqlVisit);
			}
		}
		System.out.println("共有"+cnt+"个表需要登记信息");
	}
	
	public void proc(){
		etl_jobs=getEtl_job_dependency( dbUrl, dbUser, dbPass, "CBMS");
		AssertPermissionTables("CBMS");
		AssertDependencyTables("CBMS_MICFIN_CONOPENACC_T_030");
	}
	
	public static void main(String[] args) throws IOException, ParseException,
			SemanticException {
		ParseDriver pd = new ParseDriver();
		ArrayList<String> parsesqls = readFile("D:\\scala_workspace\\HiveSQL\\src\\com\\icbc\\HiveSQLParse\\CBMS_MICFIN_CONOPENACC_T_030.hql");
		HQLParser hp = new HQLParser();
		for (String parsesql : parsesqls) {
			System.out.println(parsesql);
			ASTNode ast = pd.parse(parsesql);
			//System.out.println(ast.toStringTree());
			hp.parse(ast);
		}
		new HQLParser().proc();
	}
	private ArrayList<Etl_job_dependencyBean> etl_jobs;
}

