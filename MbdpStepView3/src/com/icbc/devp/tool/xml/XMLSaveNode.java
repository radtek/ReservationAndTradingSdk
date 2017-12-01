/*
 * 创建日期 2008-10-4
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.icbc.devp.tool.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author shilei
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class XMLSaveNode {
	public static String DEBUG_VER = "<#NOVA V2.6.0#CBS V2.6.0#F-MSC V1.4.0.0#>";
	public static String DEBUG_PRG_VER = "<#LQH#070319#N#0000#>";
 
	private HashMap attributes=new HashMap();
	private Vector childNodes=new Vector();
	private XMLSaveNode parentNode;
	private String nodeName;
	private String nodeValue;
	public XMLSaveNode(String name,String value){
		this(name,value,null);
	}
	public XMLSaveNode(String name,String value,XMLSaveNode parentNode){
		this.nodeName=name;
		this.nodeValue=value;
		this.parentNode=parentNode;
	}
	public void setParent(XMLSaveNode parentNode){
		this.parentNode=parentNode;
	}
	public XMLSaveNode getParent(){
		return this.parentNode;
	}
	public int getChildCount(){
		return this.childNodes.size();
	}
	public XMLSaveNode getChildAt(int index){
		return (XMLSaveNode)this.childNodes.get(index);
	}
	public XMLSaveNode[] getChildNodesByName(String nodeName){
		ArrayList chdNodes=new ArrayList();
		XMLSaveNode node=null;
		for(int i=0;i<this.childNodes.size();i++){
			node=(XMLSaveNode)this.childNodes.get(i);
			if (node.getNodeName().equalsIgnoreCase(nodeName)){
				chdNodes.add(node);
			}
		}
		XMLSaveNode[] rtNodes=new XMLSaveNode[chdNodes.size()];
		for(int i=0;i<chdNodes.size();i++)
			rtNodes[i]=(XMLSaveNode)chdNodes.get(i);
		return rtNodes;    
	}
	public void addChildNode(XMLSaveNode childNode){
		if (childNode!=null)
			childNode.setParent(this);
		this.childNodes.add(childNode);
	}
	public void addAttribute(String attrName,String attrValue){
		attributes.put(attrName,attrValue);
	}
	public String getAttributeValueByName(String attrName){
		return (String)this.attributes.get(attrName);
	}
	/**
	 * @return
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @return
	 */
	public String getNodeValue() {
		return nodeValue;
	}

}
