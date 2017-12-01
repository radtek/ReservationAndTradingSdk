package com.icbc.devp.bean.file;

/**TODO:���˿�����渺��sqlldr���������BEAN*/
public class MscFileSqlldrBean {

	public MscFileSqlldrBean(){
		isDirect = false;
		isParallel = true;
		silentMode = "HEADER";
		isSkipIndex = false;
		errors = 50;
		charset = "ZHS16GBK";
	}
	
	public boolean isDirect() {
		return isDirect;
	}
	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}
	public boolean isParallel() {
		return isParallel;
	}
	public void setParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}
	public String getSilentMode() {
		return silentMode;
	}
	public void setSilentMode(String silentMode) {
		this.silentMode = silentMode;
	}
	public boolean isSkipIndex() {
		return isSkipIndex;
	}
	public void setSkipIndex(boolean isSkipIndex) {
		this.isSkipIndex = isSkipIndex;
	}
	public int getErrors() {
		return errors;
	}
	public String getParallel() {
		if(isParallel){
			return "true";
		}
		else{
			return "false";
		}
	}
	public void setErrors(int errors) {
		this.errors = errors;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}

	private boolean isDirect;	//direct������Ĭ��false
	private boolean isParallel; //�Ƿ�֧�ֲ��е��룬Ĭ��true
	private String  silentMode; //���� header �� feedBack??
	private boolean isSkipIndex; //�Ƿ�ά���������룬Ĭ��false
	private int errors;			//�ݴ��¼����Ĭ��50
	private String charset;		//�ַ�����Ĭ�ϣ�ZHS16GBK
}
