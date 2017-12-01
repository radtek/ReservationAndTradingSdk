package com.icbc.devp.gui.factory;

import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;

import org.w3c.dom.Document;

import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.tool.xml.XMLConfigParser;
import com.icbc.devp.tool.xml.XMLLoader;
import com.icbc.devp.tool.xml.XMLSaveContext;
import com.icbc.devp.tool.xml.XMLSaveNode;

public class IconFactory {

	private static IconFactory _instance = new IconFactory();
	
	public static IconFactory getInstance(){
		return _instance;
	}
	
	public IconFactory(){
		this.xmlLoader = new XMLLoader();
		this.xmlParser = new XMLConfigParser();
		iconMap = new HashMap<String, ImageIcon>();
		String path = EnvUtil.getInstance().getRootPath() + File.separator + "config/gui/stepinfo/IconRelConfig.xml";
		this.loadIcons(path, EnvUtil.getInstance().getRootPath());
	}
	
	/**TODO:����ICON��Ϣ*/
	public boolean loadIcons(String configPath, String rootPath){
		try{
			Document doc = xmlLoader.load(configPath);
			XMLSaveContext ctx = this.xmlParser.parse(doc.getDocumentElement());
			XMLSaveNode[] nodes = ctx.getSaveNodesByName("ICONREL");
			XMLSaveNode node;
			//��ȡ��Ŀ¼
			ImageIcon icon;
			String id;
			String relPath;
			//ѭ����ȡ����
			for(int i=0; i<nodes.length;i++){
				node = nodes[i];
				
				try{
					id = node.getAttributeValueByName("PROG_TPYE");
					relPath = node.getAttributeValueByName("ICON_PATH");
					icon = new ImageIcon(rootPath + File.separator + relPath);
					//��ȡ
					this.iconMap.put(id, icon);
				}catch(Exception xe){
					xe.printStackTrace();
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			Log.getInstance().exception(e);
			return false;
		}
	}
	
	/**TODO:���ݴ����ID��ȡ��Ӧ��ICON*/
	public ImageIcon getIcon(String id){
		if(id == null){
			return null;
		}
		return this.iconMap.get(id);
	}
	
	//ICON��ID������Ӧ��ͼƬ�Ķ��չ�ϵ
	private HashMap<String, ImageIcon> iconMap;
	private XMLLoader xmlLoader;
	private XMLConfigParser xmlParser;
}
