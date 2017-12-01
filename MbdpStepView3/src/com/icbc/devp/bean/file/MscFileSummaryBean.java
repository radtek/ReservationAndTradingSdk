package com.icbc.devp.bean.file;

/**TODO:法人导入xml中FILE_SUMMARY的信息的bean*/
public class MscFileSummaryBean {

	public MscFileSummaryBean(){
		
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getLoadMethod() {
		return loadMethod;
	}
	public void setLoadMethod(String loadMethod) {
		this.loadMethod = loadMethod;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getSepNum() {
		return sepNum;
	}
	public void setSepNum(String sepNum) {
		this.sepNum = sepNum;
	}
	public boolean isNecessary() {
		return isNecessary;
	}
	public void setNecessary(boolean isNecessary) {
		this.isNecessary = isNecessary;
	}
	public String getAppFlag() {
		return appFlag;
	}
	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}
	public String getSrcInterface() {
		return srcInterface;
	}
	public void setSrcInterface(String srcInterface) {
		this.srcInterface = srcInterface;
	}
	public String getTargetInterface() {
		return targetInterface;
	}
	public void setTargetInterface(String targetInterface) {
		this.targetInterface = targetInterface;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public void setPARALLEL(String PARALLEL) {
		this.PARALLEL = PARALLEL;
	}
	public String getPARALLEL() {
		return PARALLEL;
	}

	/*成员变量*/
	private String fileName;	    //文件ID，对应FILENAME
	private String tableName;	    //表名称
	private String loadMethod;	    //导入方式，默认append
	private String fileType;	    //文件类型，定长：FIXLENGTH，分隔符：SEPERATOR
	private int    length;		    //文件长度
	private String sepNum;		    //分隔符代码（数值）
	private boolean isNecessary;    //是否必须
	private String appFlag;		    //应用标示符，比如F-PBMS
	private String srcInterface;    //源接口名称，大小写敏感
	private String targetInterface; //目标接口名称，大小写敏感
	private String floor;		    //场次
	private String interfaceType;	//接口类型，PT或者ZJ
	private String PARALLEL;	//是否支持并发导入
}
