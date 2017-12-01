package com.icbc.devp.gui.trees;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

import com.icbc.devp.bean.db.MbdpCtlBean;
import com.icbc.devp.gui.info.DataLoader;
import com.icbc.devp.gui.info.StepInfoTree;
import com.icbc.devp.gui.tabpane.PicTabPane;
import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.tool.log.Log;

public class LoadStepdefAction implements ActionListener {

	public LoadStepdefAction(CtlTreeNode treeNode){
//		this.stepTreeMap = new HashMap<String, StepInfoTree>();
		this.treeNode = treeNode;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.loadStepInfo();
	}
	
	public void loadStepInfo(){
		//判断是否已经有这个tab了，有的话不要再次初始化，只需要重新加载即可
		if(this.tabPane == null){
			return;
		}
		if(this.tabName == null){
			return;
		}
		//开始
		int index = this.tabPane.indexOfTab(this.tabName);
		StepInfoPane tmpPane = null;
		PicTabPane picPane;
		if(index == -1){
			tmpPane = new StepInfoPane();
			picPane = new PicTabPane(tmpPane);
			this.tabPane.add(this.tabName, picPane);
			this.tabPane.setSelectedComponent(picPane);
		}else{
			this.tabPane.remove(index);
			tmpPane = new StepInfoPane();
			picPane = new PicTabPane(tmpPane);
			this.tabPane.add(this.tabName, picPane);
			this.tabPane.setSelectedComponent(picPane);
		}
		try{

			StepInfoTree tree = tmpPane.getInfoTree();
			if(tree == null){
				tree = new StepInfoTree();
			}else{
				tree.clear();
			}
			if(!this.loader.loadStepInfoBySql(tree, this.app, this.cycle, this.dataSource)){
				return;
			}
			if(!this.loader.loadRelationInfoBySql(tree, this.app, this.cycle, this.dataSource)){
				return;
			}
			tree.calNodesHeight();
			if(!tmpPane.requestFocusInWindow()){
				Log.getInstance().error("获取焦点失败。");
			}
			tmpPane.setInfoTree(tree);
			tmpPane.initStepLocation();
		}catch(Exception ec){
			ec.printStackTrace();
		}
	}
	
	public void setNodeName(String nodeName){
//		this.nodeName = nodeName;
		MbdpCtlBean bean = this.treeNode.getCtl(nodeName);
		if(bean == null){
			Log.getInstance().error("[LoadStepdefAction.setNodeName]无法匹配输入的信息："+nodeName);
			return;
		}
		tabName = bean.getApp()+"_"+bean.getCycle()+"_"+bean.getDatasource();
		app = bean.getApp();
		cycle = bean.getCycle();
		dataSource = bean.getDatasource();
	}
	
	/**TODO:获取到TAB容器*/
	public JTabbedPane getTabPane() {
		return tabPane;
	}

	/**TODO:设置TAB容器*/
	public void setTabPane(JTabbedPane tabPane) {
		this.tabPane = tabPane;
	}

//	/**TODO:设置连接管理*/
//	public void setConManager(ConnectionManager dbManager){
//		this.dbManager = dbManager;
//	}
	
	public void setLoader(DataLoader loader){
		this.loader = loader;
	}

	//变量
	private DataLoader loader;
	private String app;
	private String cycle;
	private String dataSource;
	private String tabName;
//	private HashMap<String, StepInfoTree> stepTreeMap;	//记录了批量表示和步骤树的关系
	private CtlTreeNode treeNode;
	//画图的地方
	private JTabbedPane tabPane;
}
