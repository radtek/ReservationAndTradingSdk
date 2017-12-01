/*
 * �������� 2008-10-4
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.icbc.devp.tool.xml;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author shilei
 *
 * 
 * ����һ���ļ���XML��Ϣ,ֻ�ܱ��������������Ϣ
 * ���ౣ��һ����Ϣ������Ҫ��nodeName�ڷ�Χ��Ψһ
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
