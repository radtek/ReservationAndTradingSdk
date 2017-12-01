/*
 * �������� 2008-9-24
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.icbc.devp.tool.xml;




import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.icbc.devp.tool.log.Log;

/**
 *
 * 
 *
 */
public class XMLLoader {
 
	public Document load(String fileName){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(fileName));           
			return doc;
		}catch(Exception ex){
			ex.printStackTrace();
			Log.getInstance().errorAndLog("[XMLLoader.load]ERROR:�ļ�"+fileName+"����ʧ��!" + ex.getMessage());
			return null;
		}
	}
}
