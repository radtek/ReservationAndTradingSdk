package com.icbc.devp.bean.file;

/**TODO:���˵���xml��FILE_SUMMARY����Ϣ��bean*/
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

	/*��Ա����*/
	private String fileName;	    //�ļ�ID����ӦFILENAME
	private String tableName;	    //������
	private String loadMethod;	    //���뷽ʽ��Ĭ��append
	private String fileType;	    //�ļ����ͣ�������FIXLENGTH���ָ�����SEPERATOR
	private int    length;		    //�ļ�����
	private String sepNum;		    //�ָ������루��ֵ��
	private boolean isNecessary;    //�Ƿ����
	private String appFlag;		    //Ӧ�ñ�ʾ��������F-PBMS
	private String srcInterface;    //Դ�ӿ����ƣ���Сд����
	private String targetInterface; //Ŀ��ӿ����ƣ���Сд����
	private String floor;		    //����
	private String interfaceType;	//�ӿ����ͣ�PT����ZJ
	private String PARALLEL;	//�Ƿ�֧�ֲ�������
}
