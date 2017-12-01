package com.icbc.tool.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.icbc.main.ReadFiles;
import com.icbc.tool.db.CTPBean;
import com.icbc.tool.db.ReadDBTable;

public class ReadFlowc {

	/**
	 * @description flowc�ļ��У�<view>��ǩ���� jsp �� vjson��ʽ�ļ��� <subop>��ǩ���� op�ļ�
	 * @param file
	 * @return flowc�ļ�������jsp subop vjson�ļ������ӹ�ϵ
	 * @throws Exception
	 */
    
	public String getFlowcName(String str){
		String flowcName="";
		int idx = str.lastIndexOf(".flowc");
		while(idx>=0 && str.charAt(idx)!='\\') idx--;
		if(idx>=0){
			flowcName = str.substring(idx+1, str.length());
		}
		return flowcName;
	}
	public List<CTPBean> readXMLFile(String file) throws Exception {
		List<CTPBean> result = new ArrayList<CTPBean>();
		int ID = ReadFiles.IDCount;
		//System.out.println("ID=" + ID);
		String FlowcName = getFlowcName(file);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			//获取Flowc的根节点
			Element root = doc.getDocumentElement(); 
			
			/**
			 * @return 处理.jsp or .vjson
			 */
			
			NodeList items = root.getElementsByTagName("view");
			for (int i = 0; i < items.getLength(); i++) {
				Element ss = (Element) items.item(i);
				CTPBean tmp = new CTPBean();
				tmp.setFileID(ss.getAttribute("id")); 
				tmp.setDisplayName(ss.getAttribute("displayName")); 
				tmp.setFlowcName(FlowcName);
				NodeList names = ss.getElementsByTagName("param");
				for (int j = 0; j < names.getLength(); ++j) {
					Element e = (Element) names.item(j);
					if ((e.getAttribute("name")).equals("pageName")) {
						tmp.setFileName(e.getAttribute("value").replace('/', '\\'));
						if(tmp.getFileName().charAt(0)=='/' || tmp.getFileName().charAt(0)=='\\'){
							tmp.setFileName(tmp.getFileName().substring(1).replace('/', '\\'));
						}
						break;
					} else if ((e.getAttribute("name")).equals("templateName")) {
						tmp.setFileName(e.getAttribute("value").replace('/', '\\'));
						if(tmp.getFileName().charAt(0)=='/'|| tmp.getFileName().charAt(0)=='\\'){
							tmp.setFileName(tmp.getFileName().substring(1).replace('/', '\\'));
						}
						break;
					}
				}

				NodeList statesItem = ss.getElementsByTagName("states");
				List<CTPBean> nextFileList = new ArrayList<CTPBean>();
				for (int j = 0; j < statesItem.getLength(); ++j) {
					Element sta = (Element) statesItem.item(j);
					NodeList states = sta.getElementsByTagName("state");
					if (states.getLength() > 0) {
						for (int k = 0; k < states.getLength(); ++k) {
							CTPBean nextFile = new CTPBean();
							Element e = (Element) states.item(k);
							String nextStr = e.getAttribute("next");			
							if (nextStr != null) {
								nextFile.setFileID(nextStr);
								nextFile.setFlowcName(tmp.getFlowcName());
							}
							if(nextFile!=null){
								nextFileList.add(nextFile);
							}
						}
					}
				}
				if(tmp!=null && nextFileList!=null){
					tmp.setNextFile(nextFileList);
				}

				if (tmp != null) {
					tmp.setID(ID++);
					result.add(tmp);
				}
			}

			/**
			 * @return 处理Flowc中的.opg情况
			 */
			
			NodeList opItems = root.getElementsByTagName("subop");	
			for (int i = 0; i < opItems.getLength(); i++) {
				Element op = (Element) opItems.item(i);
				CTPBean tmp = new CTPBean();
				if(op!=null){
					tmp.setFileID(op.getAttribute("id")); // ��ȡFileID������ļ�����Ӧ��ݿ���FileID
				    tmp.setDisplayName(op.getAttribute("displayName"));
				    tmp.setFileName((op.getAttribute("type")+".opg").replace('/', '\\'));
				    if(tmp.getFileName().charAt(0)=='\\'|| tmp.getFileName().charAt(0)=='\\'){
						tmp.setFileName(tmp.getFileName().substring(1));
					}
				    tmp.setFlowcName(FlowcName);
				}
				NodeList statesItem = op.getElementsByTagName("states");// ��ȡ�����ļ����������ļ���״̬
				List<CTPBean> nextFileList = new ArrayList<CTPBean>();
			    for (int j = 0; j < statesItem.getLength(); ++j) {
				   Element sta = (Element) statesItem.item(j);
				   NodeList states = sta.getElementsByTagName("state");		
				     if (states.getLength() > 0) {
					    for (int k = 0; k < states.getLength(); ++k) {
						   Element e = (Element) states.item(k);
						   CTPBean nextFile = new CTPBean();
						   String nextStr = e.getAttribute("next");
						   if (nextStr != null) {
								nextFile.setFileID(nextStr);
								nextFile.setFlowcName(tmp.getFlowcName());
							}
							if(nextFile!=null){
								nextFileList.add(nextFile);
							}
					   }
				   }
			 }
			 if(tmp!=null && nextFileList!=null){
					tmp.setNextFile(nextFileList);
			  }
			 if(tmp!=null){
				 tmp.setID(ID++);
				 result.add(tmp);
			 }
		   }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if(result!=null){
		  for(CTPBean res : result){
			  if(res.getNextFile().size()<=0)continue;
			  for(CTPBean re : res.getNextFile()){
				  for(CTPBean obj: result){
					  if(re.getFileID().equals(obj.getFileID()) && re.getFlowcName().equals(obj.getFlowcName())){
						  res.getNextID().add(obj.getID());
						  obj.getPredID().add(res.getID());
						 // System.out.println(obj.getID());
						  break;
					  }
				  }
			  }
		  }
	   }
		
	   return result;
	}
	
}
