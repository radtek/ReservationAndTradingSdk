package com.icbc.devp.tool.util;

public class StringUtil {

	/**TODO:左补空格至某个值
	 * @param String input
	 * 要处理的输入字符串
	 * @param int length
	 * 补齐后的总长度
	 * @param char chr
	 * 用于填充的字符
	 * @return String
	 * 补齐后的字符串*/
	public static String leftFill(String input,
			                int length,
			                char chr){
		String tmpStr = input==null?"":input;
		//判断是否需要补齐
		int srcLen = tmpStr.getBytes().length;
		if(srcLen >= length){
			//不补齐，直接返回
			return tmpStr;
		}
		//否则就要补齐咯
		StringBuffer buf = new StringBuffer(length+1);
		for(int i=srcLen; i<length; i++){
			buf.append(chr);
		}
		buf.append(tmpStr);
		return buf.toString();
	}
	
	/**TODO:右补空格至某个数值
	 * @param String input
	 * 要进行处理的字符串
	 * @param int length
	 * 补齐后的总长度
	 * @param char chr
	 * 用于填充的字符
	 * @return String 补齐后的字符串*/
	public static String rightFill(String input,
			                 int length,
			                 char chr){
		String tmpStr = input;
		if(tmpStr == null){
			tmpStr = "";
		}
		//补齐
		int srcLen = tmpStr.getBytes().length;
		if(srcLen >= length){
			//不需要补齐
			return tmpStr;
		}
		//否则补齐
		StringBuffer buf = new StringBuffer(length+1);
		buf.append(tmpStr);
		for(int i=srcLen; i<length; i++){
			buf.append(chr);
		}
		//搞定，返回字符串
		return buf.toString();
	}
}
