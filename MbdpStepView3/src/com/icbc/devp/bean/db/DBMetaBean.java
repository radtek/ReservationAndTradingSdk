package com.icbc.devp.bean.db;

public class DBMetaBean {

	public String getTableID() {
		return TableID;
	}
	public void setTableID(String tableID) {
		TableID = tableID;
	}
	public String getTableName() {
		return TableName;
	}
	public void setTableName(String tableName) {
		TableName = tableName;
	}
	public String getFileNameColumn() {
		return FileNameColumn;
	}
	public void setFileNameColumn(String fileNameColumn) {
		FileNameColumn = fileNameColumn;
	}
	public String getTableNameColumn() {
		return TableNameColumn;
	}
	public void setTableNameColumn(String tableNameColumn) {
		TableNameColumn = tableNameColumn;
	}
	public String getIsClob() {
		return isClob;
	}
	public void setIsClob(String isClob) {
		this.isClob = isClob;
	}
	private String TableID; //���ֲ�ͬ���ݿ��
	private String TableName; //���ݿ������
	private String FileNameColumn; //�ļ����ֶ�
	private String TableNameColumn; //���ݿ��������ֶ�
	private String isClob; //�Ƿ�Clob�ֶ�
	
	
}
