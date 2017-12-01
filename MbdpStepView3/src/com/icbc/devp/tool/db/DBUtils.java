package com.icbc.devp.tool.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.sql.Clob;

import oracle.jdbc.OracleTypes;

import com.icbc.devp.tool.log.Log;

//import oracle.jdbc;

public class DBUtils {
 	
	//�Ͽ����ݿ�����
	public void disconnect(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}			
		} catch (SQLException e) {			
			throw new RuntimeException("�رս�������ͷ������쳣��" + e.toString());
		} catch (Exception e) {
			throw new RuntimeException("�رս�������ͷ������쳣��" + e.toString());
		}
	}	
	
	//insert
	//update
	//delete	
	public synchronized boolean executeUpdate(Connection con, String sqltext) {
		Statement stmt = null;
		int rt = 0;
		try {
			if (con != null) {
				stmt = con.createStatement();
				rt = stmt.executeUpdate(sqltext);
				if (rt == 0) {
					con.commit();
					return true;
				} else {
					con.rollback();
				}
			}
			return true;
		} catch (SQLException e) {
//			System.out.println("ִ��["+sqltext+"]ʱ����"+e.getMessage());
			Log.getInstance().error("ִ��["+sqltext+"]ʱ����"+e.getMessage());
			try {
				con.rollback();
			}catch(Exception ex) {}					
		} finally {
			disconnect(null, stmt, null);
		} 
		return false;	
	}
	public static void main(String[] args) {
		//new DBUtils().executeUpdate()
	}
	//qry
	public synchronized List executeQuery(Connection con, String sqltext) {	
		List datas = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (con != null) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(sqltext);
				int idx = 0;
				while(rs.next()) {
					datas.add(rs.getString(++idx));
				}				
			}	
			return datas;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());			
		} finally {
			disconnect(null, stmt, rs);
		}		
	}
	
	public synchronized List executeQry(Connection con, String sqltext) {	
		List datas = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (con != null) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(sqltext);
				int idx = 0;
				ResultSetMetaData rsm =  rs.getMetaData();
				while(rs.next()) {
					Map resutlMap = new LinkedHashMap();
					for(int i=0;i<rsm.getColumnCount();i++)
					{
						String key = rsm.getColumnName(i+1);
						String value = rs.getString(key);
						resutlMap.put(key, value);
					}
					  datas.add(resutlMap);
					  
				}				
			}	
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());			
		} finally {
			disconnect(null, stmt, rs);
		}		
	}
	
	/**TODO:���ݴ����ƥ��ģʽ��ȡ*/
	
	//���ô洢����
	public synchronized String[][] executeProc(Connection conn, String procname, ArrayList<String> input, ArrayList<String> output) {		
		StringBuffer params = new StringBuffer();
		int in = input.size();
		int out = output.size();
		int pos = 0;
		String errMsg = null;
		String errPrefix = "[ִ�д洢����]";
		if (in + out > 1) {
			for (int k = 0; k < in + out; k++) { // ƴ�մ洢����ʹ�õĲ���������������κͳ���
				if (k == (in + out - 1))
					params.append("?)");
				else if (k == 0)
					params.append("(?, ");
				else
					params.append("?, ");
			}
		} else { // ����ֻ��һ�����������
			params.append("(?)");
		}
		String[][] rsdatas = null;
		CallableStatement cs = null;
		ResultSet rs = null;		
		try {
			cs = conn.prepareCall("{ call " + procname + params.toString() + " }");
			int i = 1;
			for (; i <= in; i++) {
				cs.setString(i, (String) input.get(i - 1));
			}
			for (; i <= (in + out); i++) {
				String typename = (String) output.get(i - in - 1);
				if ("CURSOR".equals(typename)) {
					cs.registerOutParameter(i, OracleTypes.CURSOR);
					pos = i;
				} else if ("VARCHAR".equals(typename)) {
					cs.registerOutParameter(i, Types.VARCHAR);
					pos = 0;
				}
			}
			cs.execute();	
			//���洢�����Ƿ�ɹ�ִ��
			String retcode = "";
			String retmsg = "";
			if(pos==0) { //���α�
				rsdatas = new String[1][out];
				for (int j = in; j < in+out; j++) {
					rsdatas[0][j-in] = cs.getString(j+1);
				}
				retcode = rsdatas[0][0];
				try {retmsg = rsdatas[0][1];} catch(Exception e) {}
				if(!"0".equals(retcode)) {
					if("1".equals(retcode)){
						rsdatas = null;
						rsdatas = new String[][]{{"info", retmsg}};
					}else {
						rsdatas = null;
						errMsg = "ִ�д洢����["+procname+"]��������["+retcode+"], ["+retmsg+"]";
						rsdatas = new String[][]{{"error", errMsg}};
						System.out.println(errPrefix+errMsg);
					}
					return rsdatas;
				}
			}else {
				retcode = cs.getString(i - out);				
				try {retmsg = cs.getString(i-out+1);} catch(Exception e) {}
				if(!"0".equals(retcode)) {
					if("1".equals(retcode)){
						rsdatas = null;
						rsdatas = new String[][]{{"info", retmsg}};											
					}else {
						rsdatas = null;
						errMsg = "ִ�д洢����["+procname+"]��������["+retcode+"], ["+retmsg+"]";
						rsdatas = new String[][]{{"error", errMsg}};
						System.out.println(errPrefix+errMsg);											
					}
					return rsdatas;
				}				
				int count = cs.getInt(i - 2);
				int idx = 0;
				if(count==0) return null; //û������
				rsdatas = new String[count][];
				if (pos != 0) {
					rs = (ResultSet) cs.getObject(pos);
					ResultSetMetaData rsm = rs.getMetaData();
					int cols = rsm.getColumnCount();
					while (rs.next()) {
						rsdatas[idx] = new String[cols];
						for (int z = 1; z <= cols; z++) {
							rsdatas[idx][z-1] = rs.getString(z);
						}
						idx++;
					}
				}
			}
		} catch (Exception e) {
			rsdatas = null;
			errMsg = "ϵͳδ֪����["+e.getMessage()+"]";
			rsdatas = new String[][]{{"error", errMsg}};
//			System.out.println(errPrefix+errMsg);
			Log.getInstance().error(errPrefix+errMsg);
		} finally {
			try {
				if (rs != null) rs.close();
				if (cs != null) cs.close();
			} catch (Exception e) {
				rsdatas = null;
				errMsg = "�ͷ���Դʱ��������["+e.getMessage()+"]";
				rsdatas = new String[][]{{"error", errMsg}};
//				System.out.println(errPrefix+errMsg);
				Log.getInstance().error(errPrefix+errMsg);
			}
		}			
		return rsdatas;
	}
	//�������ֶ���CLOB
	//���ô洢����
	public synchronized String[][] executeClobProc(Connection conn, String procname, ArrayList<String> input, ArrayList<String> output) {		
		StringBuffer params = new StringBuffer();
		int in = input.size();
		int out = output.size();
		int pos = 0;
		String errMsg = null;
		String errPrefix = "[ִ�д洢����]";
		if (in + out > 1) {
			for (int k = 0; k < in + out; k++) { // ƴ�մ洢����ʹ�õĲ���������������κͳ���
				if (k == (in + out - 1))
					params.append("?)");
				else if (k == 0)
					params.append("(?, ");
				else
					params.append("?, ");
			}
		} else { // ����ֻ��һ�����������
			params.append("(?)");
		}
		String[][] rsdatas = null;
		CallableStatement cs = null;
		ResultSet rs = null;		
		try {
			cs = conn.prepareCall("{ call " + procname + params.toString() + " }");
			int i = 1;
			for (; i <= in; i++) {
				cs.setString(i, (String) input.get(i - 1));
			}
			for (; i <= (in + out); i++) {
				String typename = (String) output.get(i - in - 1);
				if ("CURSOR".equals(typename)) {
					cs.registerOutParameter(i, OracleTypes.CURSOR);
					pos = i;
				} else if ("VARCHAR".equals(typename)) {
					cs.registerOutParameter(i, Types.VARCHAR);
					pos = 0;
				}else if ("CLOB".equals(typename)){
					cs.registerOutParameter(i, Types.CLOB);
					pos = 0;
				}
			}
			cs.execute();	
			//���洢�����Ƿ�ɹ�ִ��
			String retcode = "";
			String retmsg = "";
			Clob clob;
			if(pos==0) { //���α�
				rsdatas = new String[1][out];
				//System.out.println(in+out);
				for (int j = in; j < in+out; j++) {
					//System.out.println(j+1);
					if((j+1) < (in+out)){
						rsdatas[0][j-in] = cs.getString(j+1);
					}else{
						clob = cs.getClob(j+1);
						rsdatas[0][j-in] = clob.getSubString(1, (int)clob.length());
					}
				}
				retcode = rsdatas[0][0];
				try {retmsg = rsdatas[0][1];} catch(Exception e) {}
				if(!"0".equals(retcode)) {
					if("1".equals(retcode)){
						rsdatas = null;
						rsdatas = new String[][]{{"info", retmsg}};
					}else {
						rsdatas = null;
						errMsg = "ִ�д洢����["+procname+"]��������["+retcode+"], ["+retmsg+"]";
						rsdatas = new String[][]{{"error", errMsg}};
//						System.out.println(errPrefix+errMsg);
						Log.getInstance().error(errPrefix+errMsg);
					}
					return rsdatas;
				}
			}else {
				retcode = cs.getString(i - out);				
				try {retmsg = cs.getString(i-out+1);} catch(Exception e) {}
				if(!"0".equals(retcode)) {
					if("1".equals(retcode)){
						rsdatas = null;
						rsdatas = new String[][]{{"info", retmsg}};											
					}else {
						rsdatas = null;
						errMsg = "ִ�д洢����["+procname+"]��������["+retcode+"], ["+retmsg+"]";
						rsdatas = new String[][]{{"error", errMsg}};
//						System.out.println(errPrefix+errMsg);		
						Log.getInstance().error(errPrefix+errMsg);
					}
					return rsdatas;
				}				
				int count = cs.getInt(i - 2);
				int idx = 0;
				if(count==0) return null; //û������
				rsdatas = new String[count][];
				if (pos != 0) {
					rs = (ResultSet) cs.getObject(pos);
					ResultSetMetaData rsm = rs.getMetaData();
					int cols = rsm.getColumnCount();
					while (rs.next()) {
						rsdatas[idx] = new String[cols];
						for (int z = 1; z <= cols; z++) {
							
							//��2����clob(��0��ʼ)
							if(z!=3){
								rsdatas[idx][z-1] = rs.getString(z);
							}else{
								 clob = rs.getClob(z);
								 rsdatas[idx][z-1] = clob.getSubString(1, (int)clob.length());
							}
						}
						idx++;
					}
				}
			}
		} catch (Exception e) {
			rsdatas = null;
			errMsg = "ϵͳδ֪����["+e.getMessage()+"]";
			rsdatas = new String[][]{{"error", errMsg}};
//			System.out.println(errPrefix+errMsg);
			Log.getInstance().error(errPrefix+errMsg);
		} finally {
			try {
				if (rs != null) rs.close();
				if (cs != null) cs.close();
			} catch (Exception e) {
				rsdatas = null;
				errMsg = "�ͷ���Դʱ��������["+e.getMessage()+"]";
				rsdatas = new String[][]{{"error", errMsg}};
//				System.out.println(errPrefix+errMsg);
				Log.getInstance().error(errPrefix+errMsg);
			}
		}			
		return rsdatas;
	}
}
