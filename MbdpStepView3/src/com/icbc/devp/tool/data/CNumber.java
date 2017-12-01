package com.icbc.devp.tool.data;

public class CNumber {

	/**TODO:判断传入的字符串是否数字，是返回true，否则返回false。*/
	public static boolean isNumber(String str){
		try{
			Double.parseDouble(str.trim());
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
