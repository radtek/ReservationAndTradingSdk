package com.icbc.tool.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CTPBean {
	public String getFlowcName() {
		return flowcName;
	}

	public void setFlowcName(String flowcName) {
		this.flowcName = flowcName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public List<Integer> getNextID() {
		return nextID;
	}

	public void setNextID(List<Integer> nextID) {
		this.nextID = nextID;
	}
	
	public List<CTPBean> getNextFile() {
		return nextFileID;
	}

	public void setNextFile(List<CTPBean> nextFileID) {
		this.nextFileID = nextFileID;
	}
	
	public List<Integer> getPredID() {
		return predID;
	}

	public void setPredID(List<Integer> predID) {
		this.predID = predID;
	}

	public List<CTPBean> getNextFileID() {
		return nextFileID;
	}

	public void setNextFileID(List<CTPBean> nextFileID) {
		this.nextFileID = nextFileID;
	}
	
	public List<OPGBean> getOpgBean() {
		return opgBean;
	}

	public void setOpgBean(List<OPGBean> opgBean) {
		this.opgBean = opgBean;
	}
	public String getCtp3OpName() {
		return ctp3OpName;
	}

	public void setCtp3OpName(String ctp3OpName) {
		this.ctp3OpName = ctp3OpName;
	}
	
	public HashSet<String> getDsrName() {
		return dsrName;
	}

	public void setDsrName(HashSet<String> dsrName) {
		this.dsrName = dsrName;
	}

	private int ID = 0;
	private List<Integer> predID = new ArrayList<Integer>(); //双向链表的前驱指针，用ID标志
	private List<Integer> nextID = new ArrayList<Integer>(); //双向链表的后继指针，用ID标志
	private List<CTPBean> nextFileID = new ArrayList<CTPBean>(); //辅助存储后继的文件
	private String flowcName = null; //flowc名称
	private String ctp3OpName = "";
	private String itemName = null; //菜单名称
	private String fileName = null; //菜单对应的文件名称
	private String fileID = null; //菜单对应在flowc中的名称
	private String displayName = null; //菜单对应在flowc显示的名字
	private List<OPGBean> opgBean = null; //存储OPG文件中的opstep
	private HashSet<String> dsrName = null; //DSR接口名字
	
}
