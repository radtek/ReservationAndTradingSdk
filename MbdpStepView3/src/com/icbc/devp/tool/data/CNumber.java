package com.icbc.devp.tool.data;

public class CNumber {

	/**TODO:�жϴ�����ַ����Ƿ����֣��Ƿ���true�����򷵻�false��*/
	public static boolean isNumber(String str){
		try{
			Double.parseDouble(str.trim());
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
