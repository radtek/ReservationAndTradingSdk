package com.icbc.tool.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.icbc.tool.db.SQLXMLBean;

public class ReadCQL {

	public HashMap<String,String> readCQL(String fileName){
		HashMap<String,String> result = new HashMap<String,String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fileName);
            Element root = doc.getDocumentElement(); // 获取根元素
			NodeList items = root.getElementsByTagName("procedure");
			for (int i = 0; i < items.getLength(); i++) {
				Element ss = (Element) items.item(i);
			    //SQLXMLBean e = new SQLXMLBean();
			    //e.setProcedureID(ss.getAttribute("id"));
			    //e.setProcedureName(ss.getAttribute("procedureName"));
				result.put(ss.getAttribute("id"),ss.getAttribute("procedureName"));
			}
		}catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception exx) {
			exx.printStackTrace();
		}
		return result;
	}
}
