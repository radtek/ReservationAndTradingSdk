package com.icbc.devp.gui.toolbar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.icbc.devp.bean.db.env.DbConnectionBean;
import com.icbc.devp.gui.factory.DbConnectionLoader;
import com.icbc.devp.gui.factory.IconFactory;

public class DbcfgDialog extends JDialog implements WindowListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DbcfgDialog(){
		this.setTitle("维护数据库链接");
		this.setModal(true);
		this.setSize(460, 300);
		this.dbBox = new JComboBox();
		this.dbBox.addActionListener(new DbCfgInfoBoxAction(this));
		this.setIconImage(IconFactory.getInstance().getIcon("SETTING").getImage());
		this.addWindowListener(this);
	}

	/**************************窗口方法区**********************************/
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		if(this.relAction == null){
			return;
		}
		System.out.println("lalal");
		if(!this.relAction.deleteList.isEmpty()){
			String key = this.dbBox.getSelectedItem() == null ? null : this.dbBox.getSelectedItem().toString();
			for(int i=0; i<relAction.deleteList.size(); i++){
				this.dbBox.addItem(relAction.deleteList.get(i));
			}
			relAction.deleteList.clear();
			this.showDbConfigInfo(key);
			relAction.deleteList.clear();
			this.resetBtnStatus();
			return;
		}
		this.setFieldEditable(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		if(this.dbBox == null || this.dbBox.getItemCount() == 0){
			return;
		}
		this.dbBox.setSelectedIndex(0);
	}

	/**************************窗口方法区**********************************/
	
	public void showInfo(){
		DbConnectionBean bean = this.dbEnvMap.get(this.dbBox.getSelectedItem());
		if(bean == null){
			this.setVisible(true);
			return;
		}
		this.idField.setText(bean.getId());
		this.sidField.setText(bean.getSid());
		this.portField.setText(bean.getPort());
		this.ipField.setText(bean.getIp());
		this.userField.setText(bean.getDbUser());
		this.pwdField.setText(bean.getDbPwd());
		//取消编辑模式
		this.idField.setEditable(false);
		this.sidField.setEditable(false);
		this.portField.setEditable(false);
		this.ipField.setEditable(false);
		this.userField.setEditable(false);
		this.pwdField.setEditable(false);
		this.setVisible(true);
	}
	
	public void init(){
		int fieldSize = 18;
		this.cons = new GridBagConstraints();
		this.layout = new GridBagLayout();
		this.setLayout(this.layout);
		cons.anchor = GridBagConstraints.EAST;
		//然后开始设计
		this.cons.gridx = 0;
		this.cons.gridy = 0;
		this.cons.gridwidth = 1;
		this.cons.gridheight = 1;
		JLabel label = new JLabel("数据库连接: ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		//筛选信息
		this.cons.gridx = 1;
		this.layout.setConstraints(this.dbBox, cons);
		this.add(this.dbBox);
		//新增接口
		this.cons.gridx = 2;
		this.cons.gridwidth =1;
		this.deleteBtn = new JButton("删  除");
		this.layout.setConstraints(this.deleteBtn, cons);
		this.add(this.deleteBtn);
		//维护接口
		this.cons.gridx = 3;
		this.cons.gridwidth = 1;
		this.mdfBtn = new JButton("编  辑");
		this.layout.setConstraints(this.mdfBtn, cons);
		this.add(this.mdfBtn);
		//显示ID***********************************************
		this.cons.gridx = 0;
		this.cons.gridy = 1;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		//走起***********************************************
		this.cons.gridy ++;
		label = new JLabel("标识ID: ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		//显示
		this.cons.gridx = 1;
		this.idField = new JTextField();
		this.idField.setColumns(fieldSize);
		this.layout.setConstraints(this.idField, cons);
		this.add(this.idField);
		//空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy ++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		//再来***********************************************
		this.cons.gridy ++;
		this.cons.gridx = 0;
		label = new JLabel("数据库IP: ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		
		this.cons.gridx = 1;
		this.ipField = new JTextField();
		this.ipField.setColumns(fieldSize);
		this.layout.setConstraints(this.ipField, cons);
		this.add(this.ipField);
		
		this.cons.gridx = 2;
		label = new JLabel("  端  口: ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		this.cons.gridx ++;
		this.portField = new JTextField();
		this.portField.setColumns(10);
		this.layout.setConstraints(this.portField, cons);
		this.add(this.portField);
		//空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy ++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		//***********************************************
		this.cons.gridx = 0;
		this.cons.gridy ++;
		label = new JLabel("  实例名: ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		
		this.cons.gridx = 1;
		this.sidField = new JTextField();
		this.sidField.setColumns(fieldSize);
		this.layout.setConstraints(this.sidField, cons);
		this.add(this.sidField);
		//空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy ++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		//***************************************************
		this.cons.gridy ++;
		this.cons.gridx = 0;
		label = new JLabel("用户名: ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		this.cons.gridx ++;
		this.userField = new JTextField();
		this.userField.setColumns(fieldSize);
		this.layout.setConstraints(this.userField, cons);
		this.add(this.userField);
		
		this.cons.gridx ++;
		label = new JLabel("  登录密码: ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		
		this.cons.gridx ++;
		this.pwdField = new JTextField();
		this.pwdField.setColumns(10);
		this.layout.setConstraints(this.pwdField, cons);
		this.add(this.pwdField);
		//新的一行
		//空白***********************************************
		this.cons.gridx = 0;
		
		this.cons.gridy ++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		//***************************************************
		this.cons.gridx = 0;
		this.cons.gridy ++;
		this.msgLabel = new JLabel("");
		this.cons.gridwidth = 2;
		this.layout.setConstraints(this.msgLabel, cons);
		this.add(this.msgLabel);
		
		this.cons.gridx = 2;
		this.cons.gridwidth = 1;
		this.cancelBtn = new JButton("取  消");
		this.layout.setConstraints(this.cancelBtn, cons);
		this.add(this.cancelBtn);


		this.cons.gridx ++;
		this.saveBtn = new JButton("保  存");
		this.layout.setConstraints(this.saveBtn, cons);
		this.add(this.saveBtn);
		
		this.setResizable(false);
		
		//加监听
		relAction = new BtnRelAction(this);
		this.deleteBtn.addActionListener(relAction);
		this.saveBtn.addActionListener(relAction);
		this.cancelBtn.addActionListener(relAction);
		this.mdfBtn.addActionListener(relAction);
	}
	
	public void setVisible(boolean flag){
		this.msgLabel.setText("");
		this.resetBtnStatus();
		super.setVisible(flag);
	}
	
	//重置
	public void resetBtnStatus(){
		if(this.cancelBtn == null){
			return;
		}
		this.cancelBtn.setEnabled(false);
		this.saveBtn.setEnabled(false);
		this.mdfBtn.setEnabled(true);
		this.deleteBtn.setEnabled(true);
	}
	
	/**TODO:设置BOX，显示现有的信息*/
	public void setDbBox(JComboBox dbBox){
		this.synBox = dbBox;	//用来同步的
		for(int i=0; i<dbBox.getItemCount(); i++){
			this.dbBox.addItem(dbBox.getItemAt(i));
		}
	}
	/**TODO:设置MAP*/
	public void setDbEnvMap(HashMap<String, DbConnectionBean> dbEnvMap){
		this.dbEnvMap = dbEnvMap;
	}


	public void showDbConfigInfo(String key){
		this.setFieldEditable(false);
		if(this.dbEnvMap == null){
			return;
		}
		if(key == null){
			//新增用的
			this.idField.setText(null);
			this.ipField.setText(null);
			this.sidField.setText(null);
			this.userField.setText(null);
			this.pwdField.setText(null);
			this.portField.setText(null);
			this.dbBox.setSelectedItem(null);
		}else{
			DbConnectionBean bean = this.dbEnvMap.get(key);
			if(bean == null){
				return;
			}
			this.idField.setText(bean.getId());
			this.ipField.setText(bean.getIp());
			this.sidField.setText(bean.getSid());
			this.userField.setText(bean.getDbUser());
			this.pwdField.setText(bean.getDbPwd());
			this.portField.setText(bean.getPort());
		}
	}
	
	private void setFieldEditable(boolean flag){
		if(this.idField == null){
			return;
		}
		this.idField.setEditable(flag);
		this.ipField.setEditable(flag);
		this.sidField.setEditable(flag);
		this.userField.setEditable(flag);
		this.pwdField.setEditable(flag);
		this.portField.setEditable(flag);
	}
	
	//设置按钮是否可以编辑
	public void setBtnEditable(boolean flag){
		if(this.deleteBtn == null){
			return;
		}
		this.deleteBtn.setEnabled(flag);
		this.mdfBtn.setEnabled(flag);
		this.cancelBtn.setEnabled(flag);
		this.saveBtn.setEnabled(flag);
	}
	
	//设置错误信息
	public void showErrMsg(String msg){
		this.msgLabel.setForeground(errColor);
		this.msgLabel.setText(msg);
	}
	
	private Color errColor = new Color(255,0,0);
	private Color blackColor = new Color(0,0,0);
	private JComboBox dbBox;	//db的信息
	private JComboBox synBox;
	private HashMap<String, DbConnectionBean> dbEnvMap;
	//功能ID
	private JButton mdfBtn;	//编辑按钮
	private JButton deleteBtn;
	private JButton cancelBtn;
	private JButton saveBtn;	//保存按钮
	//编辑区域
	private JTextField idField;
	private JTextField ipField;
	private JTextField sidField;
	private JTextField userField;
	private JTextField pwdField;
	private JTextField portField;
	//提示信息
	private JLabel msgLabel;
	//layout
	private GridBagLayout layout;
	private GridBagConstraints cons;
	private BtnRelAction relAction;

	//显示box中数据库连接的动作
	private class DbCfgInfoBoxAction implements ActionListener {

		public DbCfgInfoBoxAction(DbcfgDialog dlg){
			this.dlg = dlg;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JComboBox box = (JComboBox)e.getSource();
			dlg.showDbConfigInfo(box.getSelectedItem() == null ? null : box.getSelectedItem().toString());
			dlg.resetBtnStatus();
		}
		
		private DbcfgDialog dlg;
	}
	
	//四个按钮之间的关联关系
	private class BtnRelAction implements ActionListener{
		public BtnRelAction(DbcfgDialog dlg){
			this.dlg = dlg;
			this.deleteList = new ArrayList<String>();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			this.dlg.msgLabel.setForeground(blackColor);
			// TODO Auto-generated method stub
			if(this.dlg.deleteBtn == e.getSource()){
				Object obj = dlg.dbBox.getSelectedItem();
				this.deleteList.add(obj.toString());
				dlg.dbBox.removeItem(obj);
				if(this.dlg.dbBox.getItemCount() == 0){
					this.dlg.deleteBtn.setEnabled(false);
				}
				this.dlg.cancelBtn.setEnabled(true);
				this.dlg.saveBtn.setEnabled(true);
				this.dlg.msgLabel.setText("");
				//保存方法
				return;
			}
			if(this.dlg.mdfBtn == e.getSource()){
				this.dlg.mdfBtn.setEnabled(false);
				this.dlg.saveBtn.setEnabled(true);
				this.dlg.deleteBtn.setEnabled(false);
				this.dlg.cancelBtn.setEnabled(true);
				//编辑方法
				dlg.setFieldEditable(true);
				this.dlg.msgLabel.setText("");
			}
			if(this.dlg.cancelBtn == e.getSource()){
				String key = dlg.dbBox.getSelectedItem() == null ? null : dlg.dbBox.getSelectedItem().toString();
				if(!this.deleteList.isEmpty()){
					for(int i=0; i<this.deleteList.size(); i++){
						dlg.dbBox.addItem(this.deleteList.get(i));
					}
					this.deleteList.clear();
				}
				dlg.showDbConfigInfo(key);
				this.deleteList.clear();
				this.dlg.resetBtnStatus();
				this.dlg.msgLabel.setText("");
				return;
			}
			//保存按钮按下来
			if(this.dlg.saveBtn == e.getSource()){
				this.dbList.clear();
//				String key;
				for(int i=0; i<this.deleteList.size(); i++){
					try{
						this.dlg.synBox.removeItem(this.deleteList.get(i));
					}catch(Exception xe){
						xe.printStackTrace();
					}
				}
				this.deleteList.clear();
				//下一步
				String editItem = this.dlg.idField.getText();
				if("".equals(editItem) || "".equals(this.dlg.ipField.getText().trim()) ||
				   "".equals(this.dlg.userField.getText().trim()) || "".equals(this.dlg.pwdField.getText().trim()) ||
				   "".equals(this.dlg.portField.getText().trim()) || "".equals(this.dlg.sidField.getText().trim())){
					dlg.showErrMsg("ERROR: 请填写完毕所有信息o(>_<)o");
				}
				if(editItem.length() > 15){
					dlg.showErrMsg("ERROR:标识ID不能超过15位(>_<)");
					return;
				}
				if(editItem.indexOf("--")!=-1){
					dlg.showErrMsg("ERROR:标识ID不能带有\"--\"。");
					return;
				}
				DbConnectionBean bean;
				bean = this.dlg.dbEnvMap.get(editItem);
				if(bean == null){
					bean = new DbConnectionBean();
					bean.setId(editItem.trim());
					bean.setDbPwd(this.dlg.pwdField.getText().trim());
					bean.setDbUser(this.dlg.userField.getText().trim());
					bean.setSid(this.dlg.sidField.getText().trim());
					bean.setIp(this.dlg.ipField.getText().trim());
					bean.setPort(this.dlg.portField.getText().trim());
					this.dlg.dbBox.addItem(editItem);
					this.dlg.synBox.addItem(editItem);
				}else{
					bean.setId(editItem.trim());
					bean.setDbPwd(this.dlg.pwdField.getText().trim());
					bean.setDbUser(this.dlg.userField.getText().trim());
					bean.setSid(this.dlg.sidField.getText().trim());
					bean.setIp(this.dlg.ipField.getText().trim());
					bean.setPort(this.dlg.portField.getText().trim());
					boolean exists =false;
					//判断有没有同名的
					for(int i=0; i<this.dlg.dbBox.getItemCount(); i++){
						if(dlg.dbBox.getItemAt(i).toString().equals(editItem)){
							exists = true;
							break;
						}
					}
					if(!exists){
						this.dlg.synBox.addItem(editItem);
						this.dlg.dbBox.addItem(editItem);
					}
				}
				this.dlg.dbEnvMap.put(editItem, bean);
				this.dlg.setFieldEditable(false);
				this.dlg.resetBtnStatus();
				
				for(int i=0; i<this.dlg.dbBox.getItemCount(); i++){
					editItem = dlg.dbBox.getItemAt(i).toString();
					bean = dlg.dbEnvMap.get(editItem);
					if(bean == null){
						continue;
					}
					this.dbList.add(bean);
				}
				if(!DbConnectionLoader.getInstance().writeConfig(dbList)){
					this.dlg.showErrMsg("ERROR:写文件时候失败o(T.T)o");
					return;
				}
				this.dlg.msgLabel.setText("SUCCESS: 保存成功。");
				this.dlg.dbBox.setSelectedItem(editItem);
			}
		}
		private DbcfgDialog dlg;
		
		private ArrayList<String> deleteList;
		ArrayList<DbConnectionBean> dbList = new ArrayList<DbConnectionBean>();
	}

}
