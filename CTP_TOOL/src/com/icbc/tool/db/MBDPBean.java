package com.icbc.tool.db;

import java.util.HashSet;

public class MBDPBean {

	public String getCYCLE() {
		return CYCLE;
	}

	public void setCYCLE(String cYCLE) {
		CYCLE = cYCLE;
	}

	public String getSTEP_NO() {
		return STEP_NO;
	}

	public void setSTEP_NO(String sTEP_NO) {
		STEP_NO = sTEP_NO;
	}

	public String getPROG_TYPE() {
		return PROG_TYPE;
	}

	public void setPROG_TYPE(String pROG_TYPE) {
		PROG_TYPE = pROG_TYPE;
	}

	public HashSet<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(HashSet<String> tableNames) {
		this.tableNames = tableNames;
	}

	public String getDATA_SOURCE() {
		return DATA_SOURCE;
	}

	public void setDATA_SOURCE(String dATA_SOURCE) {
		DATA_SOURCE = dATA_SOURCE;
	}

	public String getPROG() {
		return PROG;
	}

	public void setPROG(String pROG) {
		PROG = pROG;
	}

	private String DATA_SOURCE;
	private String PROG;
	private String CYCLE;
	private String STEP_NO;
	private String PROG_TYPE;
	private HashSet<String> tableNames = null; // 步骤对应的表清单
}
