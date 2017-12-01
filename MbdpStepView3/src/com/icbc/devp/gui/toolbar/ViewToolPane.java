package com.icbc.devp.gui.toolbar;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.icbc.devp.bean.db.env.DbConnectionBean;
import com.icbc.devp.gui.action.ExpRelStepAction;
import com.icbc.devp.gui.action.FindStepAction;
import com.icbc.devp.gui.factory.DbConnectionLoader;
import com.icbc.devp.gui.factory.IconFactory;
import com.icbc.devp.gui.pane.MbdpSplitPane;

public class ViewToolPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ViewToolPane(MbdpSplitPane pane){
		this.init(pane);
		this.initAction();
		this.action.setPane(pane);
	}

	public void init(MbdpSplitPane pane){
		//变量初始化
		this.initDbConfig();
//		this.layout = new FlowLayout(FlowLayout.LEFT);
//		this.setLayout(this.layout);
		this.borderLayout = new BorderLayout();
		this.setLayout(this.borderLayout);
		this.westPanel = new JPanel();
		this.westPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(this.westPanel, BorderLayout.WEST);
		choseDbLabel = new JLabel("选择数据库:  ");
		this.westPanel.add(this.choseDbLabel);
		//查找活动号

		this.westPanel.add(this.envBox);
		choseEnvBtn = new JButton("建立连接");
		this.choseEnvBtn.setToolTipText("连接选择的数据库");
		this.choseEnvBtn.setIcon(IconFactory.getInstance().getIcon("SETTING"));
		this.westPanel.add(this.choseEnvBtn);
		this.setBtn = new JButton();
		this.setBtn.setIcon(IconFactory.getInstance().getIcon("SETTING1"));
		this.setBtn.setToolTipText("设置数据库连接");
		this.westPanel.add(this.setBtn);
		
		//
		MdfDbConfigAction setAction = new MdfDbConfigAction();
		this.setBtn.addActionListener(setAction);
		setDialog = new DbcfgDialog();
		setDialog.setDbBox(this.envBox);
		setDialog.setDbEnvMap(this.envMap);
		this.setDialog.init();
		setAction.setDialog(setDialog);
		
		//左边的，查询步骤号
		this.eastPanel = new JPanel();
		this.eastPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(this.eastPanel, BorderLayout.EAST);

		JLabel label = new JLabel("输入关键字: ");
		this.eastPanel.add(label);
		
		this.searchText = new JTextField();
		this.searchText.setColumns(15);
		this.eastPanel.add(this.searchText);
		
		this.searchBtn = new SearchBtn("查  找", this.searchText, pane.getViewTabPane());
		this.eastPanel.add(this.searchBtn);
		
		//lxc
//		this.southPanel = new JPanel();
//		this.southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		this.add(this.southPanel, BorderLayout.SOUTH);
		
		
//		this.findStepBtn = new JButton("查找访问表的步骤");
//		this.southPanel.add(this.findStepBtn);
//		findStepBtn.addActionListener(new FindStepAction(pane.getViewTabPane()));
		
//		this.expBtn = new JButton("导出");
//		this.expBtn.setToolTipText("导出全量未配置关联关系的关联步骤");
//		this.southPanel.add(expBtn);
//		expBtn.addActionListener(new ExpRelStepAction());
		
	}

	private void initDbConfig(){
//		this.envMap = new HashMap<String, DbConnectionBean>();
//		loader = new DbConnectionLoader();
		this.envMap = DbConnectionLoader.getInstance().loadDbConfig();
		this.envBox = new JComboBox();
		DbConnectionBean bean;
		ArrayList<String> itemList = new ArrayList<String>();
		for(Iterator<DbConnectionBean> it = this.envMap.values().iterator(); it.hasNext();){
			bean = it.next();
			itemList.add(bean.getId());
//			this.envBox.addItem(bean.getId());
		}
		Collections.sort(itemList);
		for(int i=0; i<itemList.size(); i++){
			this.envBox.addItem(itemList.get(i));
		}
	}
	
	private void initAction(){
		this.action = new ChooseDbEnvAction();
		this.action.setBox(this.envBox);
		this.action.setDbEnvMap(this.envMap);
		this.choseEnvBtn.addActionListener(this.action);
	}
	
//	public void setDbManager(ConnectionManager dbManager){
//		this.action.setDbManager(dbManager);
//	}
	
	public void setPane(MbdpSplitPane pane){
		this.action.setPane(pane);
	}
	
	private JTextField searchText;
	private SearchBtn searchBtn;
	private JLabel choseDbLabel;
	//BorderLayout
	private BorderLayout borderLayout;
	private JPanel westPanel;
	private JPanel eastPanel;
	
	//链接信息放在这儿
	private HashMap<String, DbConnectionBean> envMap;
	//下拉框
	private JComboBox envBox;
//	private DbConnectionLoader loader;
	private JButton choseEnvBtn;
	//走
	private ChooseDbEnvAction action;
	//设置
	private JButton setBtn;
	//维护对话框
	private DbcfgDialog setDialog;
	
	//查询访问表的步骤lxc
	private JButton findStepBtn;
	
	//导出全量未配置关联关系的关联步骤
	private JButton expBtn;
	
	private JPanel southPanel;
	
}