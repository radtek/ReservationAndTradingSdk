package com.icbc.devp.gui.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;

import com.icbc.devp.bean.db.DBMetaBean;
import com.icbc.devp.bean.db.env.DbConnectionBean;
import com.icbc.devp.tool.file.FileUtil;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.tool.xml.XMLConfigParser;
import com.icbc.devp.tool.xml.XMLLoader;
import com.icbc.devp.tool.xml.XMLSaveContext;
import com.icbc.devp.tool.xml.XMLSaveNode;

public class DBMetaLoader {

	public DBMetaLoader(){
		cfgPath = EnvUtil.getInstance().getRootPath();
		loader = new XMLLoader();
		parser = new XMLConfigParser();
	}
	
	private static DBMetaLoader _instance = new DBMetaLoader();
	
	public static DBMetaLoader getInstance(){
		return _instance;
	}
	
	public HashMap<String, DBMetaBean> loadDbConfig(){
		String dbCfgPath = new File(cfgPath, "config/db/env/DBMetaConfig.xml").getAbsolutePath();
		Document doc = this.loader.load(dbCfgPath);
		XMLSaveContext ctx = parser.parse(doc.getDocumentElement());
		XMLSaveNode[] nodes = ctx.getSaveNodesByName("TABLE");
		XMLSaveNode node;
		HashMap<String, DBMetaBean> envMap = new HashMap<String, DBMetaBean>();
		DBMetaBean bean;
		for(int i=0; i<nodes.length; i++){
			node = nodes[i];
			bean = new DBMetaBean();
			bean.setTableID(node.getAttributeValueByName("ID"));
			bean.setTableName(node.getChildNodesByName("TableName")[0].getNodeValue());
			bean.setFileNameColumn(node.getChildNodesByName("FileNameColumn")[0].getNodeValue());
			bean.setTableNameColumn(node.getChildNodesByName("TableNameColumn")[0].getNodeValue());
			bean.setIsClob(node.getChildNodesByName("isClob")[0].getNodeValue());
			envMap.put(bean.getTableID(), bean);
		}
		return envMap;
	}
	
	/**TODO:更新些文件*/
	public boolean writeConfig(ArrayList<DBMetaBean> dbList){
		if(dbList == null){
			return false;
		}
		String dbCfgPath = new File(cfgPath, "config/db/env/DBMetaConfig.xml").getAbsolutePath();
		ArrayList<String> lineList = new ArrayList<String>();
		lineList.add("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
		lineList.add("");
		lineList.add("<TABLES>");
		DBMetaBean bean;
		for(int i=0; i<dbList.size(); i++){
			bean = dbList.get(i);
			lineList.add("    <TABLE ID=\""+bean.getTableID()+"\">");
			lineList.add("        <TableName>"+bean.getTableName()+"</TableName>");
			lineList.add("        <FileNameColumn>"+bean.getFileNameColumn()+"</FileNameColumn>");
			lineList.add("        <TableNameColumn>"+bean.getTableNameColumn()+"</TableNameColumn>");
			lineList.add("        <isClob>"+bean.getIsClob()+"</isClob>");
			lineList.add("    </TABLE>");
			lineList.add("");
		}
		lineList.add("</TABLES>");
		return FileUtil.writeFile(dbCfgPath, false, lineList);
	}
	
	private String cfgPath;
	private XMLLoader loader;
	private XMLConfigParser parser;
}
