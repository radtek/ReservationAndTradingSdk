package com.icbc.devp.relstep;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import com.icbc.devp.gui.UiContext;
import com.icbc.devp.tool.log.Log;

/**
 * @author kfzx-liangxch
 *
 */
public class RelStep {

	private Connection conn;
	private static String fileName = "FileTableRel.csv";
	private static String updateFileName = "update.csv";
//	public String[] updateProg = new String[] {
//		"PCKG_MBDP_B_PUB.CLN_DATA",
//		"PCKG_MBDP_B_PUB.RB_UNIDX"
//	};
	private ArrayList<String> updateProc = new ArrayList<String>();
	private ArrayList<Integer> updateTbIndex = new ArrayList<Integer>();

/*	public static void main(String[] args) {
		Connection conn = null;
//		String dbip = "122.16.93.50";
//		String port = "1521";
//		String sname = "MSCDB";
//		String user = "msc";
//		String pwd = "msc";
//		String tmp = "jdbc:oracle:thin:@" + dbip + ":1521:MSCDB";
		
		String tbname = "MSC_CCRMACCNT_CTL";
//		tbname = "CBMS_CUST_CASHFLOW_IMP";

		RelStep test = new RelStep();
		try {
			DBConnection dbconn = new DBConnection();
//			conn = dbconn.getConnection("CBMS1505");

			ArrayList<String> steplist = null;
			
//			String str = "123  456   789";
//			System.out.println(str.replaceAll("\\s+", " "));
//			System.out.println(str);
//			String stepno = "CM2002_D_PROC_5020";
			
//			String temp = stepno.substring(0, stepno.indexOf("_", stepno.indexOf("_") + 1));
//			String temp = "IN_PAR1=$APP|IN_PAR2=$CYCLE|IN_PAR3=$DATASOURCE|IN_PAR4=$WORKDATE|IN_PAR5=S|IN_PAR6=CBMS_AGTRET_IMP|IN_PAR7=AGTTYPE IN (2)|IN_PAR8=";
//			String[] strarray = temp.split("[|]");
//			for (int i = 0; i < strarray.length; ++i) {
////				System.out.println(strarray[i]);
//				strarray[i] = strarray[i].substring(strarray[i].indexOf("=") + 1);
//				System.out.println(strarray[i]);
//			}
			
//			System.out.println(File.separator);
			
//			System.out.println("�ñ�" + tbname + "��Ϊ�洢���̲����Ĳ��裺");
//			stepno = test.getStepnoByTbnameAsArg(conn, tbname);
//			for( int i = 0; i < stepno.size(); ++i) {
//				System.out.println(stepno.get(i));
//			}
//			

//			
//			System.out.println("���ʹ�ĳ����Ĳ��裺");
//			steplist = test.getSteplist(conn, tbname);
//			for ( int i = 0; i < steplist.size(); ++i ) {
//				System.out.println(steplist.get(i));
//			}

			// ĳ�������ȡ�͸��¹��ı�
//			ArrayList<ArrayList<String>> tblist = test.getTablesByStepno(conn,"CM2002_D_PROC_5020");
//			System.out.println("read:");
//			for ( int i = 0; i < tblist.get(0).size(); ++i ) {
//				System.out.println(tblist.get(0).get(i));
//			}
//			System.out.println("update:");
//			for ( int i = 0; i < tblist.get(1).size(); ++i ) {
//				System.out.println(tblist.get(1).get(i));
//			}
			
			// ���¹�ĳ����Ĵ洢����
//			ArrayList<String> proclist = test.getProcUByTbname(conn, tbname);
//			for (int i = 0; i < proclist.size(); ++i) {
//				System.out.println(proclist.get(i));
//			}
			
			// ��������
//			steplist = test.procRelStep(conn, "AAM_D_PROC_5002");
//			for (int i = 0; i < steplist.size(); ++i) {
//				System.out.println(steplist.get(i));
//			}
//			steplist = test.impRelStep(conn, "AAM_D_IMP_5001");
//			for (int i = 0; i < steplist.size(); ++i) {
//				System.out.println(steplist.get(i));
//			}

			
//			steplist = test.getFnameByTbname("CBMS_AGTRET_IMP");
//			for ( int i = 0; i < steplist.size(); ++i ) {
//				System.out.println(steplist.get(i));
//			}
			String text = "        UPDATE MBDP_BRANCH_INF";
			tbname = "MBDP_BRANCH";
			if (!text.toUpperCase().matches(".*\\s+" + tbname.toUpperCase() + "\\s*")
					&&!text.toUpperCase().matches(".*\\s+" + tbname.toUpperCase() + "\\s+.*"))  {
				System.out.println("1");
			}
//			conn.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
//		DBUtils util = new DBUtils();
//		sql = "select T.PARAMETER from mbdp_b_stepdef t where t.Stepno = 'CCRM_D_IMP_0057'";
//		List result = util.executeQuery(con, sql);
//		System.out.print(result.get(0));

	}*/
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public RelStep() throws Exception {
		conn = UiContext.getInstance().getDbManager().getConnection();
		getUpdateProc();
	}
	
	
	/**��ȡʹ��ĳ������Ϊ�洢���̲����Ĳ����
	 * @param conn
	 * @param tbname
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> getStepnoByTbnameAsArg(String tbname) throws SQLException {
		
		ArrayList<String> stepno = new ArrayList<String>();
//		String sql = "SELECT T.STEPNO FROM MBDP_B_STEPDEF T WHERE T.PARAMETER LIKE '%" + tbname + "%'";
		String sql = "SELECT T.STEPNO FROM MBDP_B_STEPDEF T WHERE T.PARAMETER LIKE '%" + tbname + "|%' OR T.PARAMETER LIKE '%" + tbname + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while( result.next() ) {
			stepno.add(result.getString("STEPNO"));
		}
		
		closeSR(stmt, result);
		return stepno;
		
	}
	/**��ȡʹ��ĳ������Ϊ�洢���̲������Ҹ����˸ñ�Ĳ����
	 * @param conn
	 * @param tbname
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> getStepnoUByTbnameAsArg(String tbname) throws SQLException {
		
		ArrayList<String> stepno = new ArrayList<String>();
//		String sql = "SELECT T.STEPNO FROM MBDP_B_STEPDEF T WHERE T.PARAMETER LIKE '%" + tbname + "%'";
		String sql = "SELECT T.STEPNO,T.PROG FROM MBDP_B_STEPDEF T WHERE T.PARAMETER LIKE '%" + tbname + "|%' OR T.PARAMETER LIKE '%" + tbname + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		String prog = null;
		while( result.next() ) {
			prog = result.getString("PROG");
//			if (prog.equals("PCKG_MBDP_B_PUB.CLN_DATA") || prog.equals("PCKG_MBDP_B_PUB.RB_UNIDX")) {
//				stepno.add(result.getString("STEPNO"));
//			}
			for ( int i = 0; i < updateProc.size(); ++i) {
				if (prog.equals(updateProc.get(i))) {
					stepno.add(result.getString("STEPNO"));
					break;
				}
			}
		}
		
		closeSR(stmt, result);
		return stepno;
		
	}
	
	/**
	 * ���ݱ�����ѯ���ʹ��ñ�Ĳ��裬���ز�����б�
	 * @param conn
	 * @param tbname
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<String> getStepAByTbname(String tbname) throws Exception {
		
		ArrayList<String> stepno = new ArrayList<String>();
//		ArrayList<RelStepBean> rlist = new ArrayList<RelStepBean>();
		// ��ȡ�ñ�tbname��Ϊ�洢���̲����Ĳ����
		stepno = getStepnoByTbnameAsArg(tbname);
		
		// ��ȡ�ڴ洢�����з��ʹ���tbname��������Ϊ�洢���̲������Ĳ����
		ArrayList<String> proList = getProcAByTbname(tbname); 
		ArrayList<String> steplist = null;
		String temp = null;
		for ( int i = 0; i < proList.size(); ++i ) {
			steplist = getStepByProname(proList.get(i));
			for ( int j = 0; j < steplist.size(); ++j ) {
				temp = steplist.get(j);
				if ( stepno.contains(temp) ) continue;
				stepno.add(temp);
			}
		}
		
		// ��ȡ��������ӿڱ�Ĳ����
		steplist = getImpstepByTbname(tbname);
		for (int i = 0; (steplist != null) && i < steplist.size(); ++i) {
			temp = steplist.get(i);
			if (stepno.contains(temp)) {
				continue;
			}
			stepno.add(temp);
		}
		
		return stepno;
		
	}
	
	/**
	 * ���ݱ������Ҹ��¹��ñ�Ĳ��裬���ز�����б�
	 * @param conn
	 * @param tbname
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getStepUByTbname(String tbname) throws Exception {
		
		ArrayList<String> steplist = new ArrayList<String>();
		ArrayList<String> templist = new ArrayList<String>();
		
		// ��ȡ�ñ�tbname��Ϊ�洢���̲����Ĳ����
		steplist = getStepnoUByTbnameAsArg(tbname);
		
		// ��ȡ�ڴ洢�����и��¹��ñ�Ĳ����
		ArrayList<String> proclist = getProcUByTbname(tbname);
		
		for (int i = 0; (proclist != null) && i < proclist.size(); ++i) {
			templist = getStepByProname(proclist.get(i));
			for (int j = 0; j < templist.size(); ++j) {
				if (steplist.contains(templist.get(j))) {
					continue;
				}
				steplist.add(templist.get(j));
			}
		}
		
		// ��ȡ����ýӿڱ�Ĳ����
		templist = getImpstepByTbname(tbname);
		for (int i = 0; i < templist.size(); ++i) {
			if (steplist.contains(templist.get(i))) {
				continue;
			}
			steplist.add(templist.get(i));
		}
		
		return steplist;
		
	}
	
	/**
	 * ���ݱ����ֱ���Ҷ�ȡ�͸��¹��ñ�Ĳ���
	 * @param conn
	 * @param tbname
	 * @return ���ز�����б���һ���б��Ƕ�ȡ���ñ�Ĳ��裬�ڶ����б��Ǹ��¹��ñ�Ĳ���
	 * @throws Exception
	 */
	public ArrayList<ArrayList<String>> getStepRUByTbname(String tbname) throws Exception {
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> resultr = getStepAByTbname(tbname);
		ArrayList<String> resultu = getStepUByTbname(tbname);
		
		Iterator<String> iterator = resultr.iterator();
		String stepno = null;
		while (iterator.hasNext()) {
			stepno = iterator.next();
			if (resultu.contains(stepno)) {
				iterator.remove();
			}
		}

		result.add(resultr);
		result.add(resultu);
		
		return result;

	}
		
	/**
	 * ��ȡ���ʵ�ĳ�����ñ��Ǵ洢���̲������Ĵ洢���������б���ʽΪ������.�洢��������
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<String> getProcAByTbname(String tbname) throws Exception {
		
		ArrayList<String> proList = new ArrayList<String>();
		
		// ��ѯ��tbname��ĳ�����г��ֵ��к�
//		String sqlS = "SELECT T.NAME, T.LINE FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND T.REFERENCED_TYPE = 'TABLE' AND T.NAME = ? AND T.TEXT LIKE ?";
		String sqlS = "SELECT T.NAME, T.LINE, T.TEXT FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND T.NAME = ? AND T.TEXT LIKE ?";

		ArrayList<String> resultPac = getPacByTbname(tbname); // ��ѯ���ʱ�tbname�İ���
		
		PreparedStatement stmtS = conn.prepareStatement(sqlS);
		ResultSet resultS = null;
		String pacName = null;
		String proName = null;
		String tempText = null;
		// �����õ��ñ��ÿ����
		for( int i = 0; i < resultPac.size(); ++i ) {

			pacName = resultPac.get(i);
			stmtS.setString(1, pacName);
			stmtS.setString(2, "% " + tbname + "%");
			resultS = stmtS.executeQuery();
			// �������г��ָñ��ÿһ��
			while(resultS.next()) {
				
				tempText = resultS.getString("TEXT").trim().toUpperCase();
				
				// ����ע��
				if (tempText.matches(".*--.*" + tbname + ".*")) {
					continue;
				}
				// �����������
				if (tempText.matches(".*\\s+" + tbname.toUpperCase() + "\\..*")) {
					continue;
				}
				
				// �������MBDP_BRANCHƥ�䵽UPDATE MBDP_BRANCH_INF�����
				if (!tempText.matches(".*\\s+" + tbname.toUpperCase() + "\\s*")
						&&!tempText.toUpperCase().matches(".*\\s+" + tbname.toUpperCase() + "\\s+.*"))  {
					continue;
				}
				
				// ��ȡ���ж�Ӧ�Ĵ洢��������
				proName = getProname(pacName, resultS.getInt("LINE"));
				if ( proName != null ) {
					proName = pacName + "." + proName;
					if ( proList.contains(proName) ) continue;
					proList.add(proName);
				}
			}
			if (resultS != null) {
				resultS.close();
			}
		}
		
		closeSR(stmtS, resultS);

		return proList;
	}
	
	/**
	 * ��ȡ����ĳ�����ñ��Ǵ洢���̲������Ĵ洢���������б���ʽΪ������.�洢��������
	 * @param conn
	 * @param tbname
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getProcUByTbname(String tbname) throws Exception {
		
		if (tbname.startsWith("MBDP_B_")) {
			return null;
		}
		
		// TEST
//		if (tbname.endsWith("PBMS_MPVS_SCP_INF")) {
//			System.out.println();
//		}
		
		ArrayList<String> proList = new ArrayList<String>();
		// ��ѯ��tbname��ĳ�����г��ֵ��к�
		String sqlS = "SELECT T.LINE,T.TEXT FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND T.NAME = ? AND T.TEXT LIKE ?";
		String sqlp = "SELECT T.TEXT FROM USER_SOURCE T WHERE T.NAME = ? AND T.TYPE = 'PACKAGE BODY' AND T.LINE = ?";

		ArrayList<String> resultPac = getPacByTbname(tbname);
		PreparedStatement stmtS = conn.prepareStatement(sqlS);
		PreparedStatement stmtp = conn.prepareStatement(sqlp);
		ResultSet resultS = null, resultp = null;
		String pacName = null;
		String proName = null;
		String tempText = null, textp = null;
		// �����õ��ñ��ÿ����
		for( int i = 0; i < resultPac.size(); ++i ) {
			pacName = resultPac.get(i);
			stmtS.setString(1, pacName);
			stmtS.setString(2, "% " + tbname + "%");
			resultS = stmtS.executeQuery();

			// �������г��ָñ��ÿһ��
			while (resultS.next()) {
				
				tempText = resultS.getString("TEXT").trim().toUpperCase();
				
				// ����ע��
				if (tempText.matches(".*--.*" + tbname + ".*")) {
					continue;
				}
				// �����������
				if (tempText.matches(".*\\s+" + tbname.toUpperCase() + "\\..*")) {
					continue;
				}
				
				// �������MBDP_BRANCHƥ�䵽UPDATE MBDP_BRANCH_INF�����
				if (!tempText.matches(".*\\s+" + tbname.toUpperCase() + "\\s*")
						&&!tempText.toUpperCase().matches(".*\\s+" + tbname.toUpperCase() + "\\s+.*"))  {
					continue;
				}

				// ��ȡ��һ�У��͵�ǰ��ƴ�ӵ�һ�����Ƿ��и��¸ñ�
				stmtp.setString(1, pacName);
				stmtp.setInt(2, resultS.getInt("LINE") - 1);
				resultp = stmtp.executeQuery();
				if (resultp.next()) {
					textp = resultp.getString("TEXT").trim().toUpperCase();

				} else {
					throw new Exception("��ȡ" + pacName +"��" + (resultS.getInt("LINE") - 1) + "�г���");
				}
				if (resultp != null) {
					resultp.close();
				}
				
				tempText = textp + " " + tempText;
//				if (tempText.contains("DELETE") 
//						|| tempText.contains("INSERT")
//						|| tempText.contains("UPDATE")
//						|| tempText.contains("MERGE")) {
				if (isUpdate(tempText, tbname)) {
					// ��ȡ���ж�Ӧ�Ĵ洢��������
//					try{
					proName = getProname(pacName, resultS.getInt("LINE"));
//				} catch (Exception e) {
//					System.out.println("tbname" + tbname);
//				}
					if (proName != null) {
						proName = pacName + "." + proName;
						if (proList.contains(proName))
							continue;
						proList.add(proName);
					}
//					System.out.println(tempText);
				}
			}
			if (resultS != null) {
				resultS.close();
			}
		}
		
		closeSR(stmtp, resultp);
		closeSR(stmtS, resultS);
		

		return proList;
	}

	
	/**��ȡ����ָ���ж�Ӧ�Ĵ洢��������
	 * @param conn
	 * @param pacName
	 * @param line
	 * @return
	 * @throws SQLException
	 */
	public String getProname(String pacName, int line) throws SQLException {
		
		String proname = null;
//		String sqlPro = "SELECT T.TEXT FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND T.NAME = ? AND T.TEXT LIKE '%PROCEDURE %' AND T.line < ? ORDER BY T.LINE DESC";
		String sqlPro = "SELECT TEXT FROM (SELECT T.TEXT FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND T.NAME = ? AND T.TEXT LIKE '%PROCEDURE %' AND T.line < ? ORDER BY T.LINE DESC) WHERE ROWNUM = 1";

		PreparedStatement stmtPro = conn.prepareStatement(sqlPro);
		stmtPro.setString(1, pacName);
		stmtPro.setInt(2, line);
		ResultSet resultPro = null;
		resultPro = stmtPro.executeQuery();
		if (resultPro.next()) {
			proname = resultPro.getString("TEXT");

			proname = proname.trim();
			proname = proname.substring(10, proname.indexOf("("));
			proname.trim();
		}
		
		closeSR(stmtPro, resultPro);
		return proname;
		
	}

	/**��ȡ����ĳ���洢���̵Ĳ�����б�
	 * @param conn
	 * @param proName
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> getStepByProname(String proName) throws SQLException {
		
		ArrayList<String> stepno = new ArrayList<String>();
		String sql = "SELECT T.STEPNO FROM MBDP_B_STEPDEF T WHERE T.PROG = '" + proName + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while( result.next() ) {
			stepno.add(result.getString("STEPNO"));
		}
		
		closeSR(stmt, result);
		return stepno;
		
	}
	
	public ArrayList<ArrayList<RelStepBean>> relStepFilteredSep(String stepno) throws Exception {
		
		return stepSeperate(relStepFiltered(stepno), stepno);
	}
	
	/**
	 * ���ݲ���Ż�ȡ���ܴ��ڹ�����ϵ�Ĳ��裨���˵������ù�����ϵ�Ĳ��裩
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public ArrayList<RelStepBean> relStepFiltered(String stepno) throws Exception {
		
//		ArrayList<String> stepList = null;
//		String sql = "SELECT T.PROG_TYPE FROM MBDP_B_STEPDEF T WHERE T.STEPNO = '" + stepno + "'";
//		Statement stmt = conn.createStatement();
//		ResultSet result = stmt.executeQuery(sql);
//		if (!result.next()) {
//			throw new Exception("����" + stepno + "������");
//		}
//		String proType = result.getString("PROG_TYPE");
//		closeStmt(stmt, result);
		String stepType = getStepType(stepno);
		ArrayList<RelStepBean> steplist = null;
		if (stepType.equals("IMP")) {	//�����IMP���벽��
			steplist = impRelStep(stepno);
		} else if (stepType.equals("PROC") || stepType.equals("EXP") || stepType.equals("_ADVPROC")) {
			steplist = procRelStep(stepno);
		} else {
			throw new Exception("����" + stepno + "�����Ͳ���IMP��PROC��_ADVPROC����EXP��");
		}
		steplist = stepFilter(stepno, steplist);
		
		return steplist;
		
	}
	/**
	 * ���ݲ���Ż�ȡ���ܴ��ڹ�����ϵ�Ĳ��裨δ���ˣ�
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public ArrayList<RelStepBean> relStep(String stepno) throws Exception {
		
		String stepType = getStepType(stepno);
		ArrayList<RelStepBean> steplist = null;
		if (stepType.equals("IMP")) {	//�����IMP���벽��
			steplist = impRelStep(stepno);
		} else if (stepType.equals("PROC") || stepType.equals("EXP") || stepType.equals("_ADVPROC")) {
			steplist = procRelStep(stepno);
		} else {
			throw new Exception("����" + stepno + "�����Ͳ���IMP��PROC��_ADVPROC����EXP��");
		}
		
		return steplist;
		
	}
	
	/**
	 * ���ֳ�������Դ����������Դ�Ĳ��裬��һ���Ǳ�����Դ�Ĳ��裬�ڶ�������������Դ�Ĳ���
	 * @param steplist
	 * @param stepno
	 * @return
	 */
	public ArrayList<ArrayList<RelStepBean>> stepSeperate(ArrayList<RelStepBean> steplist, String stepno) {
		
		if (steplist == null) {
			return null;
		}
		
		ArrayList<ArrayList<RelStepBean>> result = new ArrayList<ArrayList<RelStepBean>>();
		ArrayList<RelStepBean> resulta = new ArrayList<RelStepBean>();
		ArrayList<RelStepBean> resultb = new ArrayList<RelStepBean>();
		
		String temp = stepno.substring(0, stepno.indexOf("_", stepno.indexOf("_") + 1));
		for (int i = 0; i < steplist.size(); ++i) {
			if (steplist.get(i).getStepno().startsWith(temp)) {
				resulta.add(steplist.get(i));
			} else {
				resultb.add(steplist.get(i));
			}
		}
		result.add(resulta);
		result.add(resultb);
		
		return result;
		
	}
	
	/**
	 * ��ȡ�����ǰ��
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> priorStep(String stepno) throws Exception {
		
		ArrayList<String> stepList = new ArrayList<String>();
		String sql = "SELECT DISTINCT R.STEPNO_P FROM MBDP_B_STEPREL R START WITH  R.STEPNO = '" + stepno + "' CONNECT BY NOCYCLE PRIOR R.STEPNO_P = R.STEPNO";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while( result.next() ) {
			stepList.add(result.getString("STEPNO_P"));
		}
		
		closeSR(stmt, result);
		return stepList;
	}
	
	/**
	 * ��ȡ����ĺ���
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> posteriorStep(String stepno) throws Exception {
		
		ArrayList<String> stepList = new ArrayList<String>();
		String sql = "SELECT DISTINCT R.STEPNO FROM MBDP_B_STEPREL R START WITH  R.STEPNO = '" + stepno + "' CONNECT BY NOCYCLE PRIOR R.STEPNO = R.STEPNO_P";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while( result.next() ) {
			stepList.add(result.getString("STEPNO"));
		}
		
		closeSR(stmt, result);
		return stepList;
	}
	
	/**
	 * ����һ������Ϳ��ܴ��ڹ�����ϵ�Ĳ����б����˵��Ѿ����ù�����ϵ�Ĳ���󷵻ؿ��ܴ��ڹ�����ϵ�Ĳ����б�
	 * @param conn
	 * @param stepno
	 * @param steplist
	 * @return
	 * @throws Exception
	 */
	public ArrayList<RelStepBean> stepFilter(String stepno, ArrayList<RelStepBean> steplist) throws Exception {
		
		if (steplist == null) {
			return null;
		}
		
		ArrayList<RelStepBean> resultf = new ArrayList<RelStepBean> ();
		
		ArrayList<String> resultp = priorStep( stepno );
		ArrayList<String> resulta = posteriorStep( stepno );
//		String temp = null;
		RelStepBean tempStep = null;
		for( int i = 0; i < steplist.size(); ++i ) {
			tempStep = steplist.get(i);
			if( resultp.contains( tempStep.getStepno() ) || resulta.contains( tempStep.getStepno() ) ) {
				continue;
			}
			resultf.add( steplist.get(i) );
		}
		
		return resultf;
		
	}
	
	/**���غ͵��벽������й�����ϵ�Ĳ�����б�
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public ArrayList<RelStepBean> impRelStep(String stepno) throws Exception {
		
		if (!stepno.contains("_IMP_")) {
			new Exception("����" + stepno + "���ǵ����ļ�����");
		}
		
		ArrayList<String> stepnoList = null;
		ArrayList<RelStepBean> rlist = new ArrayList<RelStepBean>();
//		String sql = "SELECT T.PARAMETER FROM MBDP_B_STEPDEF T WHERE T.STEPNO = '" + stepno + "'";
//		Statement stmt = conn.createStatement();
//		ResultSet result = stmt.executeQuery(sql);
//		String fname = result.next() ? result.getString("PARAMETER") : "";
//		closeStmt(stmt, result);
//		
//		fname = fname.substring(fname.lastIndexOf("/") + 1, fname.lastIndexOf(".xml"));
//		System.out.println(fname);
		String tbname = getImpTbnameByStepno(stepno);
		if (tbname == null) {
			return null;
		}
		stepnoList = getStepAByTbname(tbname);
		for (int i = 0; i < stepnoList.size(); ++i) {
			rlist.add(new RelStepBean(stepnoList.get(i), tbname));
		}
//		stepnoList.
		
//		closeStmt(stmt, result);
		return rlist;
	}
	
	/**
	 * ���غ͵��벽������й�����ϵ�Ĳ�����б��ѹ��������ù�����ϵ�Ĳ��裩
	 * @param conn
	 * @param stepno
	 * @param tbname
	 * @return
	 * @throws Exception
	 */
	public ArrayList<RelStepBean> impRelStep(String stepno, String tbname) throws Exception {
	
		ArrayList<String> stepnoList = null;
		ArrayList<RelStepBean> rlist = new ArrayList<RelStepBean>();
		//stepnoList = getSteplist(tbname);
		stepnoList = getStepAByTbname(tbname);
		for (int i = 0; i < stepnoList.size(); ++i) {
			rlist.add(new RelStepBean(stepnoList.get(i), tbname));
		}
		rlist = stepFilter(stepno, rlist);

		return rlist;

	}
	
	
	/**��ȡ�ļ����ƶ�Ӧ�Ľӿڱ���
	 * @param fname
	 * @return
	 * @throws IOException
	 */
	private String getImpTbname(String fname){

		String tbname = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			
			while ((tbname = reader.readLine()) != null) {
				if (tbname.contains("\\" + fname)) {
					tbname = tbname.substring(tbname.indexOf(",") + 1, tbname.indexOf(",", tbname.indexOf(",") + 1));
					break;
				}
			}
			reader.close();
			//		System.out.println(tbname);
			/*if (tbname == null) {
				reader = new BufferedReader(new FileReader(fileName));
				fname = fname.substring(fname.lastIndexOf("\\"), fname.lastIndexOf(".xml")) + ".xml";
				while ((tbname = reader.readLine()) != null) {
					if (tbname.contains(fname)) {
						tbname = tbname.substring(tbname.indexOf(",") + 1, tbname.indexOf(",", tbname.indexOf(",") + 1));
						System.out.println("�ڶ��β���");
						break;
					}
				}
				reader.close();
				if (tbname == null) {
//					throw new Exception("�����ļ������ϵ�в��Ҳ����ļ�" + fname + "��Ӧ�ı���");
					Log.getInstance().info("�����ļ������ϵ�в��Ҳ����ļ�" + fname + "��Ӧ�ı���");
				}
			}*/
			if (tbname == null) {
				Log.getInstance().info("�����ļ������ϵ�в��Ҳ����ļ�" + fname + "��Ӧ�ı���");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().errorAndLog(e.getMessage());
			return null;
		}

		return tbname;
	}
	
	/**
	 * ���ļ��ж�ȡ���±�Ĵ洢���̺ͱ��ڲ����е�λ��
	 */
	private void getUpdateProc(){

		String tempStr = null;
		String[] strsplit;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(updateFileName));
			reader.readLine();
			while ((tempStr = reader.readLine()) != null) {
				
				strsplit = tempStr.split(",");
				updateProc.add(strsplit[0]);
				updateTbIndex.add(Integer.valueOf(strsplit[1]));

			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getImpTbnameByStepno(String stepno) throws Exception {
		
		String sql = "SELECT T.PARAMETER FROM MBDP_B_STEPDEF T WHERE T.STEPNO = '" + stepno + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		String fname = result.next() ? result.getString("PARAMETER") : "";
		closeSR(stmt, result);
		
//		fname = fname.substring(fname.lastIndexOf("/") + 1, fname.lastIndexOf(".xml"));
		String[] fnameArray = fname.split("/");
		if (fnameArray != null && fnameArray.length > 3) {
			fname = fnameArray[fnameArray.length-4] + "\\" +fnameArray[fnameArray.length-3] + "\\" + fnameArray[fnameArray.length-2] + "\\" + fnameArray[fnameArray.length-1];
		} else {
			fname = fname.substring(fname.lastIndexOf("/") + 1, fname.lastIndexOf(".xml")) + ".xml";
		}
//		System.out.println(fname);
		return getImpTbname(fname);
		
	}
	
	private ArrayList<String> getFnameByTbname(String tbname) {

		ArrayList<String> filelist = new ArrayList<String>();
		BufferedReader reader;
		try {
//			reader = new BufferedReader(new FileReader("config" + File.separator + "�����ļ������ϵ.csv"));
			reader = new BufferedReader(new FileReader(fileName));
			String text = "";
			while ((text = reader.readLine()) != null) {
				if (text.contains("," + tbname + ",")) {
					filelist.add(text.substring(0, text.indexOf(",")));
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return filelist;
	}
	
	/**
	 * ���ݱ�����ȡ��������ӿڱ�Ĳ����б�
	 * @param conn
	 * @param tbname
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getImpstepByTbname(String tbname) throws Exception {
		
		ArrayList<String> steplist = new ArrayList<String>();
		
		String sql = "SELECT T.STEPNO FROM MBDP_B_STEPDEF T WHERE T.PROG_TYPE = 'IMP' AND T.PARAMETER LIKE ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = null;
		String stepno;
		ArrayList<String> filelist = new ArrayList<String>();
		filelist = getFnameByTbname(tbname);

		for (int i = 0; (filelist != null) && i < filelist.size(); ++i) {
			stmt.setString(1, "%" + filelist.get(i) + "%");
			result = stmt.executeQuery();
			while(result.next()) {
				stepno = result.getString("STEPNO");
				if (steplist.contains(stepno)) {
					continue;
				}
				steplist.add(stepno);
			}
			if (result != null) {
				result.close();
			}
		}
		
		closeSR(stmt, result);
		return steplist;
		
	}
	
	/**
	 * �ҳ���ָ��������ܴ��ڹ�����ϵ�Ĳ�����б�δ���������ù�����ϵ�Ĳ��裩
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public ArrayList<RelStepBean> procRelStep(String stepno) throws Exception {
		
		ArrayList<ArrayList<String>> tblist = getTablesByStepno(stepno);
		
		if (tblist == null) {
			return null;
		}
		
		ArrayList<String> steplist = new ArrayList<String>();
		ArrayList<RelStepBean> rlist = new ArrayList<RelStepBean>();
		
		ArrayList<String> templist = null;
		
		// �����ȡ���ı�
		String tbname = null;
		for (int i = 0; i < tblist.get(0).size(); ++i) {
			tbname = tblist.get(0).get(i);
			templist = getStepUByTbname(tbname);
//			System.out.println("��ȡ��"+ tblist.get(0).get(i));
			
			if (templist == null) {
				continue;
			}
			
			for (int j = 0; j < templist.size(); ++j) {
				if (steplist.contains(templist.get(j))) {
					continue;
				}
				steplist.add(templist.get(j));
				rlist.add(new RelStepBean(templist.get(j), tbname));
//				System.out.println(templist.get(j));
			}
		}
		// ������¹��ı����ڸ��¹��ı��ҳ����ʹ��ı�Ĳ���
		tbname = null;
		for (int i = 0; i < tblist.get(1).size(); ++i) {
			tbname = tblist.get(1).get(i);
			templist = getStepAByTbname(tbname);
//			System.out.println("���±�"+ tblist.get(1).get(i));
			for (int j = 0; j < templist.size(); ++j) {
				if (!steplist.contains(templist.get(j))) 
				{
					steplist.add(templist.get(j));
					rlist.add(new RelStepBean(templist.get(j), tbname));
				}
				
			}
		}
		
		return rlist;
	}
	
	/**
	 * ����ָ������ţ����Ҷ�Ӧ�洢���̷��ʹ��ı������б��һ���Ƕ�ȡ���ı��ڶ����Ǹ��¹��ı�
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ArrayList<String>> getTablesByStepno(String stepno) throws Exception {
		
		ArrayList<ArrayList<String>> tblist = new ArrayList<ArrayList<String>>();
		String stepType = getStepType(stepno);
		if (stepType.equals("PROC") || stepType.equals("EXP") || stepType.equals("_ADVPROC")) {			
			String[] ppname = getProcByStepno(stepno);
			
			String pronamet = ppname[0] + "." + ppname[1];
			//����ǵ���ǰ������PCKG_MBDP_B_PUB.CLN_DATA�����6�������Ǳ���
			/*if (pronamet.equalsIgnoreCase(updateProg[0])) {
				ArrayList<String> rlist = new ArrayList<String>();
				tblist.add(rlist);
				ArrayList<String> ulist = new ArrayList<String>();
				String param = this.getStepParam(stepno);
				String[] paramsplit = param.split("\\|");

				String tb = paramsplit[5].substring(paramsplit[5].indexOf("=") + 1);
				ulist.add(tb);
				tblist.add(ulist);
				return tblist;
			}
			//������ؽ���������PCKG_MBDP_B_PUB.RB_UNIDX�����5�������Ǳ���
			if (pronamet.equalsIgnoreCase(updateProg[1])) {
				ArrayList<String> rlist = new ArrayList<String>();
				tblist.add(rlist);
				ArrayList<String> ulist = new ArrayList<String>();
				String param = this.getStepParam(stepno);
				String[] paramsplit = param.split("\\|");
				String tb = paramsplit[4].substring(paramsplit[4].indexOf("=") + 1);
				ulist.add(tb);
				tblist.add(ulist);
				return tblist;
			}*/
			for (int i = 0; i < updateProc.size(); ++i) {
				if (pronamet.equalsIgnoreCase(updateProc.get(i))) {
					ArrayList<String> rlist = new ArrayList<String>();
					tblist.add(rlist);
					ArrayList<String> ulist = new ArrayList<String>();
					String param = this.getStepParam(stepno);
					String[] paramsplit = param.split("\\|");
					int tbIndex = updateTbIndex.get(i) - 1;
					String tb = paramsplit[tbIndex].substring(paramsplit[tbIndex].indexOf("=") + 1);
					ulist.add(tb);
					tblist.add(ulist);
					return tblist;
				}
			}
			ArrayList<String> tblistpac = getTableByPacname(ppname[0]);
			tblist = getTableByProcname(tblistpac, ppname[0], ppname[1]);
			
		} else if (stepType.equals("IMP")) {
			String tb = getImpTbnameByStepno(stepno);
			ArrayList<String> rlist = new ArrayList<String>();
			tblist.add(rlist);
			ArrayList<String> ulist = new ArrayList<String>();
			if (tb != null) {
				ulist.add(tb);
			}
			tblist.add(ulist);
		}

		return tblist;
		
	}
	
	/**
	 * ���ݲ���Ż�ȡ�洢�����ƺʹ洢��������
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public String[] getProcByStepno(String stepno) throws Exception {
		String sql = "SELECT T.PROG FROM MBDP_B_STEPDEF T WHERE T.STEPNO = '" + stepno + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		if (!result.next()) {
			throw new Exception("����" + stepno + "������");
		}
		String pacname = result.getString("PROG");
		if (result != null) {
			result.close();
		}
		String proname = pacname.substring(pacname.indexOf(".") + 1);
		pacname = pacname.substring(0, pacname.indexOf("."));
		
//		ArrayList<String> names = new ArrayList<String>();
//		names.add(pacname);
//		names.add(proname);
		String[] names = new String[2];
		names[0] = pacname;
		names[1] = proname;
		
		closeSR(stmt, result);
		return names;
	}
	
	/**
	 * ��ȡ�洢���̵���ʼ�ͽ����к�
	 * @param conn
	 * @param pacname
	 * @param proname
	 * @return
	 * @throws Exception
	 */
	public int[] getProcPosition(String pacname, String proname) throws Exception {

		int startline = 0, endline = 0;
		
		// ��ǰ�洢������ʼλ��
		String sql = "SELECT T.LINE,T.TEXT FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND  T.name = '" + pacname + "' AND T.TEXT LIKE '%PROCEDURE " + proname + "%'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		String temp = null;
		while (result.next()) {
			temp = result.getString("TEXT");
			temp = temp.substring(temp.indexOf("PROCEDURE") + 9, temp.indexOf("(")).trim();
			if (temp.equals(proname)) {
				startline = result.getInt("LINE");	// ��ǰ�洢���̵���ʼλ��
				break;
			}
		}
		if (result != null) {
			result.close();
		}
		
		// ��һ���洢���̵���ʼλ��
		sql = "SELECT T.LINE FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND  T.NAME = '" + pacname + "' AND T.LINE > " + startline + " AND T.TEXT LIKE '%PROCEDURE %' ORDER BY T.LINE";
		result = stmt.executeQuery(sql);
		if (result.next()) {
			endline = result.getInt("LINE");	// ��ǰ�洢���̵Ľ���λ��
			result.close();
			sql = "SELECT T.LINE FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND  T.NAME = '" + pacname + "' AND T.LINE < " + endline + " AND T.TEXT LIKE '%END%;%' ORDER BY T.LINE DESC";
			result = stmt.executeQuery(sql);
			if (result.next()) {
				endline = result.getInt("LINE");
				result.close();
			} else {
				endline = endline - 1;
			}
		} else {
			result.close();
			sql = "SELECT T.LINE FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND  T.NAME = '" + pacname + "' ORDER BY T.LINE DESC";
			result = stmt.executeQuery(sql);
			if (result.next()) {
				endline = result.getInt("LINE") - 1;
			} else {
//				System.out.println(sql);
				endline = startline;
			}
		}
		closeSR(stmt, result);
		
		int[] lineno = new int[2];
		lineno[0] = startline;
		lineno[1] = endline;

		return lineno;
	}
	
	/**
	 * ��ȡ�洢�����ʹ��ı�
	 * @param conn
	 * @param pacname
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getTableByPacname(String pacname) throws Exception {
		
		ArrayList<String> tablelist = new ArrayList<String>();
		
		String sql = "SELECT T.REFERENCED_NAME FROM USER_DEPENDENCIES T WHERE T.NAME = '" + pacname + "' AND T.REFERENCED_TYPE IN ('TABLE', 'SYNONYM')";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while( result.next() ) {
			tablelist.add(result.getString("REFERENCED_NAME"));
		}
		
		closeSR(stmt, result);
		return tablelist;
	}
	
	/**
	 * ����һ����������ȡ�����������з��ʹ��ñ�İ�
	 * @param conn
	 * @param tbname
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getPacByTbname(String tbname) throws Exception {
		
		ArrayList<String> paclist = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT D.NAME FROM USER_DEPENDENCIES D WHERE D.REFERENCED_TYPE IN ('TABLE', 'SYNONYM') AND D.TYPE = 'PACKAGE BODY' AND D.REFERENCED_NAME = '" + tbname + "' AND EXISTS (SELECT * FROM MBDP_B_STEPDEF S WHERE S.PROG_TYPE = 'PROC' AND S.PROG LIKE D.name||'%')";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while( result.next() ) {
			paclist.add(result.getString(1));
		}
		
		closeSR(stmt, result);
		return paclist;
	}
	
	/**
	 * ���ڴ���Ĵ洢���̺ͱ��������ô洢���̶�ȡ�͸��¹��ı������б��һ���Ƕ�ȡ���ı��ڶ����Ǹ��¹��ı�
	 * @param conn
	 * @param tablelist
	 * @param pacname
	 * @param proname
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ArrayList<String>> getTableByProcname(ArrayList<String> tablelist, String pacname, String proname) throws Exception {
		
		ArrayList<String> tbread = new ArrayList<String>();
		ArrayList<String> tbupdate = new ArrayList<String>();
		ArrayList<ArrayList<String>> tblist = new ArrayList<ArrayList<String>>();
		
		int[] lineno = getProcPosition(pacname, proname );	// ��ȡ�洢���̵���ʼ�ͽ���λ��
		if (lineno[0] == lineno[1]) {
			return null;
		}
//		System.out.println(lineno[0] + "\t" + lineno[1]);
		String sql = "SELECT T.LINE, T.TEXT FROM USER_SOURCE T WHERE T.TYPE = 'PACKAGE BODY' AND T.NAME = '" + pacname + "' AND (T.LINE BETWEEN " + lineno[0] + " AND " + lineno[1] + ") AND T.TEXT LIKE ?";
		String sqlp = "SELECT T.TEXT FROM USER_SOURCE T WHERE T.NAME = '" + pacname + "' AND T.TYPE = 'PACKAGE BODY' AND T.LINE = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		PreparedStatement stmtp = conn.prepareStatement(sqlp);
		ResultSet result = null, resultp = null;
		String text = null, textp = null;
		String tbname = null;
		int updateflag = 0;
		for( int i = 0; i < tablelist.size(); ++i) {
			tbname = tablelist.get(i);
			
//			if (tbname.equals("MBDP_BRANCH")) {
//				System.out.println();
//			}
			
			updateflag = 0;
			stmt.setString(1, "%" + tbname + "%");
			result = stmt.executeQuery();
			
			while (result.next()) {
				text = result.getString("TEXT").toUpperCase().trim();
				
				// ����ע��
				if (text.matches(".*--.*" + tbname + ".*")) {
					continue;
				}
				// �����������
				if (text.matches(".*\\s+" + tbname.toUpperCase() + "\\..*")) {
					continue;
				}
				
				// �������MBDP_BRANCHƥ�䵽UPDATE MBDP_BRANCH_INF�����
				if (!text.matches(".*\\s+" + tbname.toUpperCase() + "\\s*")
						&&!text.toUpperCase().matches(".*\\s+" + tbname.toUpperCase() + "\\s+.*"))  {
					continue;
				}
				
				updateflag = 1;
				// ����һ�У�ƴ�ӵ�һ��
				stmtp.setInt(1, result.getInt("LINE")-1);
				resultp = stmtp.executeQuery();
				if (resultp.next()) {
					textp = resultp.getString(1).toUpperCase().trim();
					resultp.close();
				} else {
					throw new Exception("��ȡ" + pacname +"��" + (result.getInt(1) - 1) + "�г���");
				}
				
				text = textp + " " + text;
				
//				if (text.contains("DELETE") || text.contains("INSERT") || text.contains("UPDATE") || text.contains("MERGE")) {
				if ( isUpdate(text, tbname) ) {
//					tbupdate.add(tbname);
					updateflag = 2;
//					System.out.println(text);
					break;
				}
			}
			
			if (updateflag == 1) {
				if (!tbread.contains(tbname)) {
					tbread.add(tbname);
				}
//				System.out.println("read:"+tbname);
			} else if (updateflag == 2) {
				if (!tbupdate.contains(tbname)) {
					tbupdate.add(tbname);
				}
//				System.out.println("update:"+tbname);
			}
			result.close();
		}
		
		closeSR(stmt, result);
		closeSR(stmtp, resultp);
		
		tblist.add(tbread);
		tblist.add(tbupdate);
		return tblist;
	}

	/**
	 * �ж�һ���ı����Ƿ���¹�ָ����
	 * @param text
	 * @param tbname
	 * @return
	 */
	private boolean isUpdate(String text, String tbname) {

		String ta = "DELETE FROM " + tbname.toUpperCase();
		String tb = "DELETE " + tbname.toUpperCase();
		String tc = "TRUNCATE " + tbname.toUpperCase();
		String td = "INSERT INTO " + tbname.toUpperCase();
		String te = "UPDATE " + tbname.toUpperCase();
		String tf = "MERGE INTO " + tbname.toUpperCase();
		String tg = "ALTER TABLE " + tbname.toUpperCase();

		text = text.replaceAll("\\s+", " ").toUpperCase();
		if (text.contains(ta) || text.contains(tb) || text.contains(tc)
				|| text.contains(td) || text.contains(te) || text.contains(tf)
				|| text.contains(tg)) {
			return true;
		}

		return false;
	}
	
	/**
	 * ���Ҳ�����Ƿ����
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public boolean isStepno(String stepno) throws Exception {
		
		String sql = "SELECT * FROM MBDP_B_STEPDEF T WHERE T.STEPNO = '" + stepno + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		if (result.next()) {
			closeSR(stmt, result);
			return true;
		}
		
		closeSR(stmt, result);
		return false;
		
	}
	
	/**
	 * ���ұ�������ͬ��ʣ��Ƿ����
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public boolean isTable(String tbname) throws Exception {
		
		String sql = "SELECT COUNT(1) FROM USER_OBJECTS T WHERE T.OBJECT_TYPE IN ('TABLE', 'SYNONYM') AND T.OBJECT_NAME = '" + tbname + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		if (result.next() && result.getInt(1) == 1) {
			closeSR(stmt, result);
			return true;
		}
		
		closeSR(stmt, result);
		return false;
		
	}
	
	/**
	 * ��ȡ��������
	 * @param conn
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public String getStepType(String stepno) throws Exception {
		
		String sql = "SELECT T.PROG_TYPE FROM MBDP_B_STEPDEF T WHERE T.STEPNO = '" + stepno + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		if (!result.next()) {
			throw new Exception("����" + stepno + "������");
		}
		String proType = result.getString("PROG_TYPE");
		closeSR(stmt, result);
		
		return proType;
		
	}
	
	/**
	 * ��ȡ�������
	 * @param stepno
	 * @return
	 * @throws Exception
	 */
	public String getStepParam(String stepno) throws Exception {
		
		String sql = "SELECT T.PARAMETER FROM MBDP_B_STEPDEF T WHERE T.STEPNO = '" + stepno + "'";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		if (!result.next()) {
			throw new Exception("����" + stepno + "������");
		}
		String param = result.getString("PARAMETER");
		closeSR(stmt, result);
		
		return param;
		
	}
	
	/**
	 * ��ȡ���в����
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getAllStep() throws Exception {
		
		ArrayList<String> stepList = new ArrayList<String>();
		String sql = "SELECT T.STEPNO FROM MBDP_B_STEPDEF T WHERE T.PROG_TYPE IN ('PROC','_ADVPROC','EXP','IMP') ORDER BY T.STEPNO";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while (result.next()) {
			stepList.add(result.getString("STEPNO"));
		}
		closeSR(stmt, result);
		
		return stepList;
		
	}
	
	public void closeSR(Statement stmt, ResultSet result) throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
		if (result != null) {
			result.close();
		}
	}
	public void closeSR(PreparedStatement stmt, ResultSet result) throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
		if (result != null) {
			result.close();
		}
	}
}
