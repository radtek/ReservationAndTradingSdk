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
		//�ж��Ƿ��Ѿ������tab�ˣ��еĻ���Ҫ�ٴγ�ʼ����ֻ��Ҫ���¼��ؼ���
		if(this.tabPane == null){
			return;
		}
		if(this.tabName == null){
			return;
		}
		//��ʼ
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
				Log.getInstance().error("��ȡ����ʧ�ܡ�");
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
			Log.getInstance().error("[LoadStepdefAction.setNodeName]�޷�ƥ���������Ϣ��"+nodeName);
			return;
		}
		tabName = bean.getApp()+"_"+bean.getCycle()+"_"+bean.getDatasource();
		app = bean.getApp();
		cycle = bean.getCycle();
		dataSource = bean.getDatasource();
	}
	
	/**TODO:��ȡ��TAB����*/
	public JTabbedPane getTabPane() {
		return tabPane;
	}

	/**TODO:����TAB����*/
	public void setTabPane(JTabbedPane tabPane) {
		this.tabPane = tabPane;
	}

//	/**TODO:�������ӹ���*/
//	public void setConManager(ConnectionManager dbManager){
//		this.dbManager = dbManager;
//	}
	
	public void setLoader(DataLoader loader){
		this.loader = loader;
	}

	//����
	private DataLoader loader;
	private String app;
	private String cycle;
	private String dataSource;
	private String tabName;
//	private HashMap<String, StepInfoTree> stepTreeMap;	//��¼��������ʾ�Ͳ������Ĺ�ϵ
	private CtlTreeNode treeNode;
	//��ͼ�ĵط�
	private JTabbedPane tabPane;
}
