/*
 * �������� 2008-2-4
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.icbc.devp.tool.xml;


/**
 * @author shilei
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public interface IXMLReader {
	public static String DEBUG_VER = "<#NOVA+ V1.1.0#PRS V2.8.0#F-PBMS V2.14.01.0#>";
	public static String DEBUG_PRG_VER = "<#LQH#070319#N#0000#>";

	 
	public XMLSaveContext read(String fileName);	

}
