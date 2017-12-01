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
	private String TableID; //区分不同数据库表
	private String TableName; //数据库表名称
	private String FileNameColumn; //文件名字段
	private String TableNameColumn; //数据库表名相关字段
	private String isClob; //是否Clob字段
	
	
}
