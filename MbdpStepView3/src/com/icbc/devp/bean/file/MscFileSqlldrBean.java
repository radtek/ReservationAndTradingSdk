package com.icbc.devp.bean.file;

/**TODO:法人框架里面负责sqlldr导入参数的BEAN*/
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

	private boolean isDirect;	//direct参数，默认false
	private boolean isParallel; //是否支持并行导入，默认true
	private String  silentMode; //包括 header 和 feedBack??
	private boolean isSkipIndex; //是否维护索引导入，默认false
	private int errors;			//容错记录数，默认50
	private String charset;		//字符集，默认：ZHS16GBK
}
