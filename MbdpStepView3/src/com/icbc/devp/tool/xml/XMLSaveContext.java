/*
 * 创建日期 2008-10-4
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.icbc.devp.tool.xml;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author shilei
 *
 * 
 * 保存一个文件的XML信息,只能保存最多两级的信息
 * 本类保存一级信息，并且要求nodeName在范围内唯一
 */
public class XMLSaveContext {
 
	Vector saveNodes=new Vector();
	String name;
	public XMLSaveContext(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
	public void addSaveNode(XMLSaveNode node){
		this.saveNodes.add(node);
	}

	public XMLSaveNode[] getSaveNodesByName(String nodeName){
		ArrayList nodes=new ArrayList();
		XMLSaveNode node=null;
		for(int i=0;i<this.saveNodes.size();i++){
			node=(XMLSaveNode)this.saveNodes.get(i);
			if (node.getNodeName().equalsIgnoreCase(nodeName))
			    nodes.add(node);
		}
		XMLSaveNode[] rtNodes=new XMLSaveNode[nodes.size()];
		for(int i=0;i<rtNodes.length;i++){
			rtNodes[i]=(XMLSaveNode)nodes.get(i);
		}
		return rtNodes;
	}
}
