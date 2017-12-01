package com.icbc.tool.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.icbc.tool.db.OPGBean;

public class ReadOPG {

	public List<OPGBean> readOPG(String file) throws Exception {
		List<OPGBean> result = new ArrayList<OPGBean>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			Element root = doc.getDocumentElement(); // 获取根元素
			NodeList items = root.getElementsByTagName("processDefine");
			for (int i = 0; i < items.getLength(); i++) {
				Element ss = (Element) items.item(i);
				NodeList names = ss.getElementsByTagName("opstep");
				for (int j = 0; j < names.getLength(); ++j) {
					Element e = (Element) names.item(j);
					OPGBean tmp = new OPGBean();
					/*if(e.getAttribute("type").equals("com.icbc.ctp.jdbc.tc.opsteps.TransactionBeginOpStep"))
						continue;*/
					if(e.getAttribute("type").indexOf("Procedure")==-1)continue;
					tmp.setDisplayName(e.getAttribute("displayName"));
					tmp.setOPGtype(OPGtypeHandler(e.getAttribute("type")));
					tmp.getOpContent().put(tmp.getOPGtype(),tmp.getDisplayName());
					
					NodeList ops = e.getElementsByTagName("inputs"); // ��ȡ�������
					for (int k = 0; k < ops.getLength(); ++k) {
						Element op = (Element) ops.item(k);
						List<String> opName = new ArrayList<String>();
						List<String> opDesc = new ArrayList<String>();
						NodeList input = op.getElementsByTagName("input");
						for(int w = 0 ; w<input.getLength();++w){
						    Element opIn = (Element)input.item(w);
							if (opIn.getAttribute("src").indexOf("PROC") != -1) {
								// System.out.print(opIn.getAttribute("src") +
								// " ^ " + opIn.getAttribute("desc") + "|");
								// System.out.println();
								int lenSrc = opIn.getAttribute("src").length();
								int idx = opIn.getAttribute("src").lastIndexOf(
										'.');
								idx = (idx == -1) ? 0 : idx;
								if (lenSrc > 1) {
									opName.add(opIn.getAttribute("src")
											.substring(idx + 1, lenSrc - 1));
								} else {
									opName.add(opIn.getAttribute("src")
											.substring(idx + 1));
								}
								opDesc.add(opIn.getAttribute("desc"));
							}else{
								/**
								 * @method 记住调用的主机接口,需要写处理主机接口名
								 */
								if(tmp.getOPGtype().toLowerCase().indexOf("dsr")!=-1){
									opName.add(opIn.getAttribute("src"));
									opDesc.add(opIn.getAttribute("desc"));
									tmp.getOpContent().put(opIn.getAttribute("desc"),opIn.getAttribute("src"));
									//System.out.println(tmp.getOpContent());
								}
							}
						}
					 tmp.setOpName(opName);
					 tmp.setOpDesc(opDesc);
				   }
					if(tmp!=null){
						result.add(tmp);
						/*System.out.println( tmp.getOPGtype()+"^"+tmp.getDisplayName());
						if(tmp.getOpName()!=null){
						   int lenTmp = tmp.getOpName().size();
						   for(int tt=0;tt<lenTmp;++tt)
						    	System.out.print(tmp.getOpName().get(tt) + "^" + tmp.getOpDesc() + " ");
					  }
						System.out.println();*/
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public String OPGtypeHandler(String str){
		int idx = str.lastIndexOf('.');
		return str.substring(idx+1);
	}
	
}
