package com.icbc.devp.gui.toolbar;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;

import com.icbc.devp.bean.db.env.DbConnectionBean;
import com.icbc.devp.gui.UiContext;
import com.icbc.devp.gui.info.DataLoader;
import com.icbc.devp.gui.pane.MbdpSplitPane;
import com.icbc.devp.gui.trees.MbdpTreeBar;
import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.util.EnvUtil;

public class ChooseDbCfgAction implements ActionListener {

	public ChooseDbCfgAction(){
		this.treeMap = new HashMap<String, MbdpTreeBar>();
		loader = new DataLoader();
		this.dbManager = UiContext.getInstance().getDbManager();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		 this.box.getSelectedItem().toString();
		Point p = new Point(10,10);
		EnvUtil.convertToScreenPoint( p, (JButton)e.getSource());
		try{
			 
			 String dbId = this.box.getSelectedItem().toString();
			 DbConnectionBean bean = this.dbEnvMap.get(dbId);
			 if(bean == null){
				 return;
			 }
			 if(this.dbManager != null){
				 try{
					 this.dbManager.closeDBConnection();
				 }catch(Exception xe){
					 xe.printStackTrace();
				 }
			 }
			 if(!this.dbManager.initDBConnection(bean.getIp(), bean.getSid(), bean.getDbUser(), bean.getDbPwd(), bean.getPort())){
				 Log.getInstance().error("[ChooseDbEnvAction]连接数据库失败，请检查配置信息是否正确。");
				 return;
			 }
			 tree = this.treeMap.get(dbId);
			 if(tree == null){
				 tree = new MbdpTreeBar(dbId+":批量工作流信息");
			 }
			 if(tree.initTreeNode(this.pane.getViewTabPane(), loader)){
				this.pane.setCtlTree(tree.getCtlTree());
			 }
		}catch(Exception ex){
			Log.getInstance().error("[ChooseDbEnvAction]建立数据库联机失败，原因："+ex.getMessage());
			Log.getInstance().exception(ex);
		}
	}
	
	

	public MbdpTreeBar getTree() {
		return tree;
	}
	public void setTree(MbdpTreeBar tree) {
		this.tree = tree;
	}
	public MbdpSplitPane getPane() {
		return pane;
	}
	public void setPane(MbdpSplitPane pane) {
		this.pane = pane;
	}
	public JComboBox getBox() {
		return box;
	}
	public void setBox(JComboBox box) {
		this.box = box;
	}
	public HashMap<String, DbConnectionBean> getDbEnvMap() {
		return dbEnvMap;
	}
	public void setDbEnvMap(HashMap<String, DbConnectionBean> dbEnvMap) {
		this.dbEnvMap = dbEnvMap;
	}

	private MbdpTreeBar tree;
	private MbdpSplitPane pane;
	private JComboBox box;
	private HashMap<String, DbConnectionBean> dbEnvMap;
	private ConnectionManager dbManager;
	private HashMap<String, MbdpTreeBar> treeMap;
	private DataLoader loader;
}
