/*
 * 创建日期 2008-10-4
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.icbc.devp.tool.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author 史忠钦
 *
 * 
 * 
 */
public class XMLConfigParser {
 

	public XMLSaveNode parseNode(Node node){
		String value=node.getNodeValue();
		if ((value==null)&&(node.getFirstChild()!=null)){
			if (node.getFirstChild().getNodeType()==Node.TEXT_NODE)
				value=node.getFirstChild().getNodeValue();
		}
		XMLSaveNode rtNode=new XMLSaveNode(node.getNodeName(),value);
		for(int y=0;y<node.getAttributes().getLength();y++){//属性
			String attrName=node.getAttributes().item(y).getNodeName();
			String attrValue=node.getAttributes().item(y).getNodeValue();
			if (attrName.length()>0){
				rtNode.addAttribute(attrName,attrValue);
			}
		}
		NodeList childes=node.getChildNodes();
		for(int i=0;i<childes.getLength();i++){
			if (childes.item(i).getNodeType()!=Node.ELEMENT_NODE) continue;
			XMLSaveNode newNode=this.parseNode(childes.item(i));
			rtNode.addChildNode(newNode);
		}
		return rtNode;
	}
	public XMLSaveContext parse(Node root){
		NodeList nodes=root.getChildNodes();
		XMLSaveContext ctx=new XMLSaveContext(root.getNodeName());
		XMLSaveNode saveNode=null;
		for(int i=0;i<nodes.getLength();i++){
			Node node=nodes.item(i);
			if (node.getNodeType()!=Node.ELEMENT_NODE) continue;
			saveNode=this.parseNode(node);
			ctx.addSaveNode(saveNode);
		}
		return ctx;
	}
}
