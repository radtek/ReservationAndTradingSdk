package com.icbc.devp.gui.factory;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;

import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.tool.xml.XMLConfigParser;
import com.icbc.devp.tool.xml.XMLLoader;
import com.icbc.devp.tool.xml.XMLSaveContext;
import com.icbc.devp.tool.xml.XMLSaveNode;

/**TODO:存储颜色的地方*/
public class ColorFactory {

	public ColorFactory(){
		colorMap = new HashMap<String, Color>();
		this.initColor();
	}
	
	private static ColorFactory _instance = new ColorFactory();
	
	public static ColorFactory getInstance(){
		return _instance;
	}
	
	public Color getColor(String key){
		return this.colorMap.get(key);
	}
	
	private void initColor(){
		File file = new File(EnvUtil.getInstance().getRootPath(), "config/gui/color/ColorCfg.xml");
		if(!file.isFile()){
			Log.getInstance().error("[ColorFactory.initColor]没有找到文件："+file.getAbsolutePath());
			return;
		}
		XMLLoader xmlLoader = new XMLLoader();
		Document doc = xmlLoader.load(file.getAbsolutePath());
		XMLConfigParser xmlParser = new XMLConfigParser();
		XMLSaveContext ctx = xmlParser.parse(doc.getDocumentElement());
		XMLSaveNode[] nodes = ctx.getSaveNodesByName("COLOR");
		XMLSaveNode node;
		Color color;
		int red;
		int green;
		int blue;
		String id;
		for(int i=0; i<nodes.length; i++){
			try{
				node  = nodes[i];
				id    = node.getAttributeValueByName("ID");
				red   = Integer.parseInt(node.getAttributeValueByName("RED"));
				green = Integer.parseInt(node.getAttributeValueByName("GREEN"));
				blue  = Integer.parseInt(node.getAttributeValueByName("BLUE"));
				color = new Color(red,green,blue);
				this.colorMap.put(id, color);
			}catch(Exception e){
				Log.getInstance().error("[ColorFactory.initColor]加载颜色信息过程发生异常，文件："+file.getAbsolutePath());
				return;
			}
		}
	}
	
	private HashMap<String, Color> colorMap;
	
}
