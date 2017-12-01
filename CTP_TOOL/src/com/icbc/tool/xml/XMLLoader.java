package com.icbc.tool.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class XMLLoader {
 
	public Document load(String fileName){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(fileName));           
			return doc;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
}
