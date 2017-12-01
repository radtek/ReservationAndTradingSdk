package com.icbc.tool.ctp3;

import java.util.ArrayList;

public class FileObj {
	ArrayList<String> itemName = new ArrayList();
	private String fileType = "";
	private String fileName = "";
	private String filePath = "";
	private FileObj preFile = null;
	ArrayList<String> linkFileLst = new ArrayList();
	private ArrayList<String> dsrNames = new ArrayList();
	private ArrayList<String> procNames = new ArrayList();
	
	public ArrayList<String> getDsrNames() {
		return this.dsrNames;
	}

	public ArrayList<String> getProcNames() {
		return this.procNames;
	}
	public void addDsrNames(String dsrName) {
		this.dsrNames.add(dsrName);
	}

	public void setDsrNames(ArrayList<String> dsrNames) {
		this.dsrNames = dsrNames;
	}

	public void addProcNames(String procName) {
		this.procNames.add(procName);
	}

	public void setProcNames(ArrayList<String> procNames) {
		this.procNames = procNames;
	}

	
	public ArrayList<String> getItemName() {
		return this.itemName;
	}

	public void setItemName(ArrayList<String> itemName) {
		this.itemName = itemName;
	}

	public void addItemName(String itemName) {
		this.itemName.add(itemName);
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public FileObj getPreFile() {
		return this.preFile;
	}

	public void setPreFile(FileObj preFile) {
		this.preFile = preFile;
	}

	public ArrayList<String> getLinkFileLst() {
		return this.linkFileLst;
	}

	public void setLinkFileLst(ArrayList<String> linkFileLst) {
		this.linkFileLst = linkFileLst;
	}

	public void addLinkFile(String linkFile) {
		this.linkFileLst.add(linkFile);
	}
}