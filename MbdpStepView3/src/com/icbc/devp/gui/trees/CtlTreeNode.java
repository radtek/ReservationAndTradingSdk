package com.icbc.devp.gui.trees;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import com.icbc.devp.bean.db.MbdpCtlBean;
import com.icbc.devp.dbstep.DbDataLoader;

public class CtlTreeNode{

	public CtlTreeNode(){
		root = new DefaultMutableTreeNode();
	}

	public CtlTreeNode(String treeName){
		root = new DefaultMutableTreeNode(treeName);
	}
	
	public void setTreeName(String treeName){
		root.setUserObject(treeName);
	}
	
	private void initTreeInfo(ArrayList<MbdpCtlBean> beanList){
		String nodeName;
		MbdpCtlBean bean;
		DefaultMutableTreeNode node;
		this.ctlMap.clear();
		for(int i=0;i<beanList.size();i++){
			bean = beanList.get(i);
			nodeName = bean.getName();
			node = new DefaultMutableTreeNode(nodeName);
			this.root.add(node);
			this.ctlMap.put(nodeName, bean);
		}
	}
	
	public MbdpCtlBean getCtl(String key){
		return this.ctlMap.get(key);
	}
	
	public boolean initCtlTree(){
		DbDataLoader loader = new DbDataLoader();
		try{
			ArrayList<MbdpCtlBean> beanList = loader.loadCtlInfoBySql();
			if(beanList == null){
				return false;
			}
			this.initTreeInfo(beanList);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			loader.release();
		}
	}
	
	public DefaultMutableTreeNode getRootNode(){
		return root;
	}

//	private ConnectionManager conManager;
	private DefaultMutableTreeNode root;
	private static final long serialVersionUID = 20140831L;
	
//	private JPopupMenu poppuMenu;
	private HashMap<String, MbdpCtlBean> ctlMap = new HashMap<String, MbdpCtlBean>();
}
