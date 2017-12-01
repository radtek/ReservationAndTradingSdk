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
		//������ʼ��
		this.initDbConfig();
//		this.layout = new FlowLayout(FlowLayout.LEFT);
//		this.setLayout(this.layout);
		this.borderLayout = new BorderLayout();
		this.setLayout(this.borderLayout);
		this.westPanel = new JPanel();
		this.westPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(this.westPanel, BorderLayout.WEST);
		choseDbLabel = new JLabel("ѡ�����ݿ�:  ");
		this.westPanel.add(this.choseDbLabel);
		//���һ��

		this.westPanel.add(this.envBox);
		choseEnvBtn = new JButton("��������");
		this.choseEnvBtn.setToolTipText("����ѡ������ݿ�");
		this.choseEnvBtn.setIcon(IconFactory.getInstance().getIcon("SETTING"));
		this.westPanel.add(this.choseEnvBtn);
		this.setBtn = new JButton();
		this.setBtn.setIcon(IconFactory.getInstance().getIcon("SETTING1"));
		this.setBtn.setToolTipText("�������ݿ�����");
		this.westPanel.add(this.setBtn);
		
		//
		MdfDbConfigAction setAction = new MdfDbConfigAction();
		this.setBtn.addActionListener(setAction);
		setDialog = new DbcfgDialog();
		setDialog.setDbBox(this.envBox);
		setDialog.setDbEnvMap(this.envMap);
		this.setDialog.init();
		setAction.setDialog(setDialog);
		
		//��ߵģ���ѯ�����
		this.eastPanel = new JPanel();
		this.eastPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(this.eastPanel, BorderLayout.EAST);

		JLabel label = new JLabel("����ؼ���: ");
		this.eastPanel.add(label);
		
		this.searchText = new JTextField();
		this.searchText.setColumns(15);
		this.eastPanel.add(this.searchText);
		
		this.searchBtn = new SearchBtn("��  ��", this.searchText, pane.getViewTabPane());
		this.eastPanel.add(this.searchBtn);
		
		//lxc
//		this.southPanel = new JPanel();
//		this.southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		this.add(this.southPanel, BorderLayout.SOUTH);
		
		
//		this.findStepBtn = new JButton("���ҷ��ʱ�Ĳ���");
//		this.southPanel.add(this.findStepBtn);
//		findStepBtn.addActionListener(new FindStepAction(pane.getViewTabPane()));
		
//		this.expBtn = new JButton("����");
//		this.expBtn.setToolTipText("����ȫ��δ���ù�����ϵ�Ĺ�������");
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
	
	//������Ϣ�������
	private HashMap<String, DbConnectionBean> envMap;
	//������
	private JComboBox envBox;
//	private DbConnectionLoader loader;
	private JButton choseEnvBtn;
	//��
	private ChooseDbEnvAction action;
	//����
	private JButton setBtn;
	//ά���Ի���
	private DbcfgDialog setDialog;
	
	//��ѯ���ʱ�Ĳ���lxc
	private JButton findStepBtn;
	
	//����ȫ��δ���ù�����ϵ�Ĺ�������
	private JButton expBtn;
	
	private JPanel southPanel;
	
}