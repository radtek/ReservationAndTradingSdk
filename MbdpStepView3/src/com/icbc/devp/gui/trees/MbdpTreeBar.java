package com.icbc.devp.gui.trees;

import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.icbc.devp.gui.factory.IconFactory;
import com.icbc.devp.gui.info.DataLoader;
import com.icbc.devp.tool.log.Log;

public class MbdpTreeBar{

	public MbdpTreeBar(String TreeName){
		node = new CtlTreeNode(TreeName);
		
	}
	
	/**TODO:初始化节点信息*/
	public boolean initTreeNode(JTabbedPane tabPane, DataLoader loader){
		if(!node.initCtlTree()){
//			System.out.println("Init tree node fail!");
			Log.getInstance().error("Init tree node fail!");
			return false;
		}
		tree = new JTree(node.getRootNode());
		TreeMouseActionListener ls = new TreeMouseActionListener(tabPane, loader, node);
		tree.addMouseListener(ls);
		DefaultTreeCellRenderer render = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
		render.setLeafIcon(IconFactory.getInstance().getIcon("TREE_LEAF"));
		render.setClosedIcon(IconFactory.getInstance().getIcon("CLOSE_FOLDER"));
		render.setOpenIcon(IconFactory.getInstance().getIcon("OPEN_FOLDER"));
		//设置
		treeUi = (BasicTreeUI)this.tree.getUI();
		treeUi.setCollapsedIcon(IconFactory.getInstance().getIcon("CFLODER"));
		return true;
	}
	
	public JTree getCtlTree(){
		return tree;
	}
	
	private CtlTreeNode node;
	private JTree tree;
	private BasicTreeUI treeUi;
}
