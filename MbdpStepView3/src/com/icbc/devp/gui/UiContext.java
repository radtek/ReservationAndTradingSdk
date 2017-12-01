package com.icbc.devp.gui;

import java.util.HashMap;

import javax.swing.JComponent;

import org.w3c.dom.Document;

import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.tool.xml.XMLConfigParser;
import com.icbc.devp.tool.xml.XMLLoader;
import com.icbc.devp.tool.xml.XMLSaveContext;
import com.icbc.devp.tool.xml.XMLSaveNode;

/**TODO: 保存所有对象的链接，通过这里可以直接存取到*/
public class UiContext {
	
	public UiContext(){
		this.comMap = new HashMap<String, JComponent>();
		this.valueMap = new HashMap<String,String>();
		this.widthSplit = 30;
		this.heightSplit = 60;
		this.loadConfig();
	}
	
	public static UiContext _instance = new UiContext();
	
	public static UiContext getInstance(){
		return _instance;
	}
	
	/**TODO:记录了ID和组件的对照关系*/
	public boolean recordId(String id, JComponent com){
		if(this.comMap.get(id) != null){
			return false;
		}
		this.comMap.put(id, com);
		return true;
	}
	
	/**TODO:获取组件*/
	public JComponent getComponent(String id){
		return this.comMap.get(id);
	}
	
	public void setDbManager(ConnectionManager dbManager){
		this.dbManager = dbManager;
	}
	
	public void putValue(String key, String value){
		this.valueMap.put(key, value);
	}
	
	public String getValue(String key){
		return this.valueMap.get(key);
	}
	
	public ConnectionManager getDbManager(){
		return this.dbManager;
	}
	
	/**TODO:获取线条入口固定长度*/
	public int getLineInLength(){
		return 40;
	}
	
	/**TODO:节点宽度*/
	public int getNodeWidth(){
		return 200;
	}
	
	/**TODO:节点高度*/
	public int getNodeHeight(){
		return 56;
	}
	
	/**TODO:宽度间隔*/
	public int getWidthSplit(){
		return widthSplit;
	}
	
	/**TODO:高度间隔*/
	public int getHeightSplit(){
		return this.heightSplit;
	}
	
	/**TODO:加载XML配置文件*/
	public boolean loadConfig(){
		try{
			String path = EnvUtil.getInstance().getRootPath()+"/config/gui/GuiFrameCfg.xml";
			XMLLoader xmlLoader = new XMLLoader();
			Document doc = xmlLoader.load(path);
			XMLSaveContext ctx = (new XMLConfigParser()).parse(doc.getDocumentElement());
			XMLSaveNode[] nodes = ctx.getSaveNodesByName("VIEWCFG");
			XMLSaveNode node = nodes[0];
			String widthVal = node.getChildNodesByName("CELL_WIDTH_SPLIT")[0].getNodeValue().trim();
			String heightVal = node.getChildNodesByName("CELL_HEIGHT_SPLIT")[0].getNodeValue().trim();
			this.widthSplit = Integer.parseInt(widthVal);
			this.heightSplit = Integer.parseInt(heightVal);
			if(this.widthSplit < 20){
				this.widthSplit = 20;
			}
			if(this.heightSplit < 10){
				this.heightSplit = 10;
			}
			return true;
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}	
		
	}
	
	
	//值对象
	private HashMap<String, JComponent> comMap;
	private ConnectionManager dbManager;
	private HashMap<String, String> valueMap;
	
	private int widthSplit;
	private int heightSplit;
	
//	private XMLLoader xmlLoader;
//	private XMLConfigParser xmlParser;
}
