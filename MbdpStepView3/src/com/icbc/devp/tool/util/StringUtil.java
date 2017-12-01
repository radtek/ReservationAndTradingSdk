package com.icbc.devp.tool.util;

public class StringUtil {

	/**TODO:�󲹿ո���ĳ��ֵ
	 * @param String input
	 * Ҫ����������ַ���
	 * @param int length
	 * �������ܳ���
	 * @param char chr
	 * ���������ַ�
	 * @return String
	 * �������ַ���*/
	public static String leftFill(String input,
			                int length,
			                char chr){
		String tmpStr = input==null?"":input;
		//�ж��Ƿ���Ҫ����
		int srcLen = tmpStr.getBytes().length;
		if(srcLen >= length){
			//�����룬ֱ�ӷ���
			return tmpStr;
		}
		//�����Ҫ���뿩
		StringBuffer buf = new StringBuffer(length+1);
		for(int i=srcLen; i<length; i++){
			buf.append(chr);
		}
		buf.append(tmpStr);
		return buf.toString();
	}
	
	/**TODO:�Ҳ��ո���ĳ����ֵ
	 * @param String input
	 * Ҫ���д�����ַ���
	 * @param int length
	 * �������ܳ���
	 * @param char chr
	 * ���������ַ�
	 * @return String �������ַ���*/
	public static String rightFill(String input,
			                 int length,
			                 char chr){
		String tmpStr = input;
		if(tmpStr == null){
			tmpStr = "";
		}
		//����
		int srcLen = tmpStr.getBytes().length;
		if(srcLen >= length){
			//����Ҫ����
			return tmpStr;
		}
		//������
		StringBuffer buf = new StringBuffer(length+1);
		buf.append(tmpStr);
		for(int i=srcLen; i<length; i++){
			buf.append(chr);
		}
		//�㶨�������ַ���
		return buf.toString();
	}
}
