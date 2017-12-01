package com.icbc.devp.gui.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;

import com.icbc.devp.bean.db.env.DbConnectionBean;
import com.icbc.devp.tool.file.FileUtil;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.tool.xml.XMLConfigParser;
import com.icbc.devp.tool.xml.XMLLoader;
import com.icbc.devp.tool.xml.XMLSaveContext;
import com.icbc.devp.tool.xml.XMLSaveNode;

public class DbConnectionLoader {

	public DbConnectionLoader(){
		cfgPath = EnvUtil.getInstance().getRootPath();
		loader = new XMLLoader();
		parser = new XMLConfigParser();
	}
	
	private static DbConnectionLoader _instance = new DbConnectionLoader();
	
	public static DbConnectionLoader getInstance(){
		return _instance;
	}
	
	public HashMap<String, DbConnectionBean> loadDbConfig(){
		String dbCfgPath = new File(cfgPath, "config/db/env/DbConfig.xml").getAbsolutePath();
		Document doc = this.loader.load(dbCfgPath);
		XMLSaveContext ctx = parser.parse(doc.getDocumentElement());
		XMLSaveNode[] nodes = ctx.getSaveNodesByName("CONNECTION");
		XMLSaveNode node;
		HashMap<String, DbConnectionBean> envMap = new HashMap<String, DbConnectionBean>();
		DbConnectionBean bean;
		for(int i=0; i<nodes.length; i++){
			node = nodes[i];
			bean = new DbConnectionBean();
			bean.setId(node.getAttributeValueByName("ID"));
			bean.setDbPwd(node.getChildNodesByName("PWD")[0].getNodeValue());
			bean.setDbUser(node.getChildNodesByName("USER")[0].getNodeValue());
			bean.setIp(node.getChildNodesByName("IP")[0].getNodeValue());
			bean.setPort(node.getChildNodesByName("PORT")[0].getNodeValue());
			bean.setSid(node.getChildNodesByName("SID")[0].getNodeValue());
			envMap.put(bean.getId(), bean);
		}
		return envMap;
	}
	
	/**TODO:更新些文件*/
	public boolean writeConfig(ArrayList<DbConnectionBean> dbList){
		if(dbList == null){
			return false;
		}
		String dbCfgPath = new File(cfgPath, "config/db/env/DbConfig.xml").getAbsolutePath();
		ArrayList<String> lineList = new ArrayList<String>();
		lineList.add("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
		lineList.add("");
		lineList.add("<DATABASE>");
		DbConnectionBean bean;
		for(int i=0; i<dbList.size(); i++){
			bean = dbList.get(i);
			lineList.add("    <CONNECTION ID=\""+bean.getId()+"\">");
			lineList.add("        <IP>"+bean.getIp()+"</IP>");
			lineList.add("        <SID>"+bean.getSid()+"</SID>");
			lineList.add("        <USER>"+bean.getDbUser()+"</USER>");
			lineList.add("        <PWD>"+bean.getDbPwd()+"</PWD>");
			lineList.add("        <PORT>"+bean.getPort()+"</PORT>");
			lineList.add("    </CONNECTION>");
			lineList.add("");
		}
		lineList.add("</DATABASE>");
		return FileUtil.writeFile(dbCfgPath, false, lineList);
	}
	
	private String cfgPath;
	private XMLLoader loader;
	private XMLConfigParser parser;
}
