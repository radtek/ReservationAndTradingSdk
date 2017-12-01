package com.icbc.tool.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class OPGBean {
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getOPGtype() {
		return OPGtype;
	}
	public void setOPGtype(String oPGtype) {
		OPGtype = oPGtype;
	}
	public List<String> getOpName() {
		return opName;
	}
	public void setOpName(List<String> opName) {
		this.opName = opName;
	} 
	public List<String> getOpDesc() {
		return opDesc;
	}
	public void setOpDesc(List<String> opDesc) {
		this.opDesc = opDesc;
	}
	
	public HashMap<String, String> getOpContent() {
		return opContent;
	}
	
	public HashMap<String, HashSet<String>> getProcedureRefTable() {
		return procedureRefTable;
	}
	
	public void setProcedureRefTable(
			HashMap<String, HashSet<String>> procedureRefTable) {
		this.procedureRefTable = procedureRefTable;
	}
	
	public void setOpContent(HashMap<String, String> opContent) {
		this.opContent = opContent;
	}
	
	String displayName = ""; //opg 在Flowc的名字
	String OPGtype = ""; //opg类型
	List<String> opName = new ArrayList<String>(); //存储过程的名字
	List<String> opDesc = new ArrayList<String>(); //op描述
	HashMap<String,String> opContent = new HashMap(); //记录op涉及的关联接口
	HashMap<String, HashSet<String>> procedureRefTable = new HashMap<String,HashSet<String>>();//OPG存储过程对应的数据库表

}
