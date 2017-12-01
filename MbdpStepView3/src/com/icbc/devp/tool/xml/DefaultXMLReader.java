/*
 * �������� 2008-2-4
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.icbc.devp.tool.xml;

import org.w3c.dom.Document;

/**
 * @author shilei
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class DefaultXMLReader implements IXMLReader {
	public static String DEBUG_VER = "<#NOVA+ V1.1.0#PRS V2.8.0#F-PBMS V2.14.01.0#>";
	public static String DEBUG_PRG_VER = "<#LQH#070319#N#0000#>";
 
	private Document doc;
	/* ���� Javadoc��
	 * @see com.icbc.batch.core.IXMLReader#read(java.lang.String)
	 */
	public XMLSaveContext read(String fileName) {
		doc=(new XMLLoader()).load(fileName);
		XMLSaveContext ctx=(new XMLConfigParser()).parse(doc.getDocumentElement());		
		return ctx;
	}
	public Document getDocument() {//tmp
		return doc;
	}
}
