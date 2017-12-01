package com.icbc.devp.gui.toolbar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.icbc.devp.bean.db.DBMetaBean;
import com.icbc.devp.bean.db.env.DbConnectionBean;
import com.icbc.devp.gui.factory.DBMetaLoader;
import com.icbc.devp.gui.factory.DbConnectionLoader;
import com.icbc.devp.gui.factory.IconFactory;



public class ExpDbTablecfgDialog extends JDialog implements WindowListener {

	/**
	 * 导出文件配置的页面
	 * kfzx-yanyj
	 * 2016/12/13
	 */
	
	private static final long serialVersionUID = 11L;

	public ExpDbTablecfgDialog() {
		this.setTitle("导出文件配置表的字段");
		this.setModal(true);
		this.setSize(560, 400);
		this.dbBox = new JComboBox();
		this.dbBox.addActionListener(new DbMetaCfgInfoBoxAction(this));		
		this.setIconImage(IconFactory.getInstance().getIcon("SETTING")
				.getImage());
		this.addWindowListener(this);
	}

	/************************** 窗口方法区 **********************************/
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
		if (this.relAction == null) {
			return;
		}
		System.out.println("koko");

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
		this.YesClobBtn.setSelected(false);
		this.NoClobBtn.setSelected(true);
	}

	/************************** 窗口方法区 **********************************/

	public void init() {
		int fieldSize = 18;
		this.cons = new GridBagConstraints();
		this.layout = new GridBagLayout();
		this.setLayout(this.layout);
		cons.anchor = GridBagConstraints.EAST;
		// 然后开始设计
		this.cons.gridx = 0;
		this.cons.gridy = 0;
		this.cons.gridwidth = 1;
		this.cons.gridheight = 1;
		JLabel label = new JLabel("数据库表: ");
		this.layout.setConstraints(label, cons);
		this.add(label);

		this.cons.gridx = 1;
		this.layout.setConstraints(this.dbBox, cons);
		this.add(this.dbBox);

		this.cons.gridx = 2;
		this.cons.gridwidth = 1;
		this.mdfBtn = new JButton("编 辑");
		this.layout.setConstraints(this.mdfBtn, cons);
		this.add(this.mdfBtn);

		this.cons.gridx = 3;
		this.cons.gridwidth = 1;
		this.deleteBtn = new JButton("删 除");
		this.layout.setConstraints(this.deleteBtn, cons);
		this.add(this.deleteBtn);

		// 空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		// 再来***********************************************

		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("ID:       ");
		this.layout.setConstraints(label, cons);
		this.add(label);

		this.cons.gridx = 1;
		this.idField = new JTextField();
		this.idField.setColumns(fieldSize);
		this.layout.setConstraints(this.idField, cons);
		this.add(this.idField);

		// 空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		// 再来***********************************************

		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("数据库表名: ");
		this.layout.setConstraints(label, cons);
		this.add(label);

		this.cons.gridx = 1;
		this.TableNameField = new JTextField();
		this.TableNameField.setColumns(fieldSize);
		this.layout.setConstraints(this.TableNameField, cons);
		this.add(this.TableNameField);

		// 空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		// 再来***********************************************

		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("文件名字段: ");
		this.layout.setConstraints(label, cons);
		this.add(label);

		// 显示
		this.cons.gridx = 1;
		this.FileNameColField = new JTextField();
		this.FileNameColField.setColumns(fieldSize);
		this.layout.setConstraints(this.FileNameColField, cons);
		this.add(this.FileNameColField);

		// 空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		// 再来***********************************************

		this.cons.gridy++;
		this.cons.gridx = 0;
		label = new JLabel("表相关字段: ");
		this.layout.setConstraints(label, cons);
		this.add(label);

		this.cons.gridx = 1;
		this.TabelNameColField = new JTextField();
		this.TabelNameColField.setColumns(fieldSize);
		this.layout.setConstraints(this.TabelNameColField, cons);
		this.add(this.TabelNameColField);

		// 空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		// 再来***********************************************

		this.cons.gridy++;
		this.cons.gridx = 0;
		this.cons.gridwidth = 1;
		label = new JLabel("表相关字段Clob: ");
		this.layout.setConstraints(label, cons);
		this.add(label);

		this.cons.gridx = 1;
		this.cons.gridwidth = 1;
		cons.anchor = GridBagConstraints.WEST;
		this.YesClobBtn = new JRadioButton("是");
		this.layout.setConstraints(this.YesClobBtn, cons);
		this.add(this.YesClobBtn);
		this.YesClobBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NoClobBtn.setSelected(false);
				YesClobBtn.setSelected(true);
			}
		});

		this.cons.gridy++;
		this.cons.gridx = 1;
		this.cons.gridwidth = 1;
		this.NoClobBtn = new JRadioButton("否");
		this.NoClobBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YesClobBtn.setSelected(false);
				NoClobBtn.setSelected(true);
			}
		});
		this.layout.setConstraints(this.NoClobBtn, cons);
		this.add(this.NoClobBtn);

		// 空白***********************************************
		this.cons.gridx = 0;
		this.cons.gridy++;
		label = new JLabel("       ");
		this.layout.setConstraints(label, cons);
		this.add(label);
		// 再来***********************************************

		this.cons.gridy++;
		this.cons.gridx = 0;
		this.msgLabel = new JLabel("");
		this.cons.gridwidth = 2;
		this.layout.setConstraints(this.msgLabel, cons);
		this.add(this.msgLabel);

		this.cons.gridx = 2;
		this.cons.gridwidth = 1;
		this.saveBtn = new JButton("保  存");
		this.layout.setConstraints(this.saveBtn, cons);
		this.add(this.saveBtn);

		this.cons.gridx = 3;
		this.cancelBtn = new JButton("取  消");
		this.layout.setConstraints(this.cancelBtn, cons);
		this.add(this.cancelBtn);

		// 加监听
		relAction = new BtnRelAction(this);
		this.deleteBtn.addActionListener(relAction);
		this.saveBtn.addActionListener(relAction);
		this.cancelBtn.addActionListener(relAction);
		this.mdfBtn.addActionListener(relAction);

	}

	/** TODO:设置BOX，显示现有的信息 */
	public void setDbBox(JComboBox dbBox) {
		this.synBox = dbBox; // 用来同步的
		for (int i = 0; i < dbBox.getItemCount(); i++) {
			this.dbBox.addItem(dbBox.getItemAt(i));
		}
	}

	// 重置
	public void resetBtnStatus() {
		if (this.cancelBtn == null) {
			return;
		}
		this.cancelBtn.setEnabled(false);
		this.saveBtn.setEnabled(false);
		this.mdfBtn.setEnabled(true);
		this.deleteBtn.setEnabled(true);
	}

	/** TODO:设置MAP */
	public void setDbEnvMap(HashMap<String, DBMetaBean> dbEnvMap) {
		this.dbEnvMap = dbEnvMap;
	}

	public void showDbConfigInfo(String key) {
		//System.out.println("key="+key);
		this.setFieldEditable(false);
		if (this.dbEnvMap == null) {
			return;
		}
		if (key == null) {
			// 新增用的
			this.idField.setText(null);
			this.FileNameColField.setText(null);
			this.TabelNameColField.setText(null);
			this.TableNameField.setText(null);
			this.YesClobBtn.setSelected(false);
			this.NoClobBtn.setSelected(true);
			this.dbBox.setSelectedItem(null);
		} else {
			DBMetaBean bean = this.dbEnvMap.get(key);
			if (bean == null) {
				return;
			}
			this.idField.setText(bean.getTableID());
			this.TableNameField.setText(bean.getTableName());
			this.TabelNameColField.setText(bean.getTableNameColumn());
			this.FileNameColField.setText(bean.getFileNameColumn());
			if(bean.getIsClob().equals("0")){
				this.NoClobBtn.setSelected(true);
				this.YesClobBtn.setSelected(false);
			}else{
				this.NoClobBtn.setSelected(false);
				this.YesClobBtn.setSelected(true);
			}
		}
	}

	private void setFieldEditable(boolean flag) {
		if (this.idField == null) {
			return;
		}
		this.idField.setEditable(flag);
		this.TableNameField.setEditable(flag);
		this.TabelNameColField.setEditable(flag);
		this.FileNameColField.setEditable(flag);

	}

	// 设置按钮是否可以编辑
	public void setBtnEditable(boolean flag) {
		if (this.deleteBtn == null) {
			return;
		}
		this.deleteBtn.setEnabled(flag);
		this.mdfBtn.setEnabled(flag);
		// this.cancelBtn.setEnabled(flag);
		this.saveBtn.setEnabled(flag);
	}

	// 设置错误信息
	public void showErrMsg(String msg) {
		this.msgLabel.setForeground(errColor);
		this.msgLabel.setText(msg);
	}

	public void showInfo() {
		DBMetaBean bean = this.dbEnvMap.get(this.dbBox.getSelectedItem());
		if(bean == null){
			this.setVisible(true);
			return;
		}
		this.idField.setText(bean.getTableID());
		this.FileNameColField.setText(bean.getFileNameColumn());
		this.TabelNameColField.setText(bean.getTableNameColumn());
		this.TableNameField.setText(bean.getTableName());
		if(bean.getIsClob().equals("0")){
			this.NoClobBtn.setSelected(true);
			this.YesClobBtn.setSelected(false);
		}else{
			this.NoClobBtn.setSelected(false);
			this.YesClobBtn.setSelected(true);
		}
		//取消编辑模式
		this.idField.setEditable(false);
		this.TableNameField.setEditable(false);
		this.TabelNameColField.setEditable(false);
		this.FileNameColField.setEditable(false);
		this.setVisible(true);
	}

	/*
	 * private void initDbConfig(){
//		this.envMap = new HashMap<String, DbConnectionBean>();
//		loader = new DbConnectionLoader();
		this.envMap = DBMetaLoader.getInstance().loadDbConfig();
		this.envBox = new JComboBox();
		DBMetaBean bean;
		ArrayList<String> itemList = new ArrayList<String>();
		for(Iterator<DBMetaBean> it = this.envMap.values().iterator(); it.hasNext();){
			bean = it.next();
			itemList.add(bean.getTableID());
//			this.envBox.addItem(bean.getId());
		}
		Collections.sort(itemList);
		for(int i=0; i<itemList.size(); i++){
			this.envBox.addItem(itemList.get(i));
		}
	}
	*/
	
	
	
	private Color errColor = new Color(255, 0, 0);
	private Color blackColor = new Color(0, 0, 0);
	private JComboBox dbBox; // 数据库表配置
	private JComboBox synBox;
	private HashMap<String, DBMetaBean> dbEnvMap;
    //下拉框
	private JComboBox envBox; //导出文件配置表输入域
	// 功能ID
	private JButton deleteBtn; // 删除按钮
	private JButton mdfBtn; // 修改按钮
	private JButton cancelBtn; // 取消按钮

	private JRadioButton YesClobBtn; // 是Clob
	private JRadioButton NoClobBtn; // 否Clob
	private JButton saveBtn; // 保存按钮

	// 编辑区域
	private JTextField idField;// 数据库表标识ID
	private JTextField FileNameColField; // 文件名字段输入域
	private JTextField TabelNameColField; // 表名字段输入域
	private JTextField TableNameField; // 数据库表名

	// 字段Label
	private JLabel FileNameColLabel; // 文件名字段
	private JLabel TabelNameColLabel; // 表名字段
	private JLabel isClobLabel; // Clob字段判断
	private JLabel msgLabel; // 消息显示Label

	// layout
	private GridBagLayout layout;
	private GridBagConstraints cons;
	private BtnRelAction relAction;

	// 四个按钮之间的关联关系
	private class BtnRelAction implements ActionListener {
		public BtnRelAction(ExpDbTablecfgDialog dlg) {
			this.dlg = dlg;
			this.deleteList = new ArrayList<String>();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (this.dlg.saveBtn == e.getSource()) {
				this.dbList.clear();
				// String key;
				for (int i = 0; i < this.deleteList.size(); i++) {
					try {
						this.dlg.synBox.removeItem(this.deleteList.get(i));
					} catch (Exception xe) {
						xe.printStackTrace();
					}
				}
				this.deleteList.clear();
				// 下一步
				String editItem = this.dlg.idField.getText();
				if ("".equals(editItem)
						|| "".equals(this.dlg.FileNameColField.getText().trim())
						|| "".equals(this.dlg.TabelNameColField.getText().trim())
						|| "".equals(this.dlg.TableNameField.getText().trim())
						//|| "".equals(this.dlg.t.getText().trim())
						) {
					dlg.showErrMsg("ERROR: 请填写完毕所有信息o(>_<)o");
				}
				if (editItem.length() > 15) {
					dlg.showErrMsg("ERROR:标识ID不能超过15位(>_<)");
					return;
				}
				if (editItem.indexOf("--") != -1) {
					dlg.showErrMsg("ERROR:标识ID不能带有\"--\"。");
					return;
				}
				DBMetaBean bean;
				bean = this.dlg.dbEnvMap.get(editItem);
				if (bean == null) {
					bean = new DBMetaBean();
					bean.setTableID(this.dlg.idField.getText().trim());
					bean.setTableName(this.dlg.TableNameField.getText().trim());
					bean.setFileNameColumn(this.dlg.FileNameColField.getText().trim());
					bean.setTableNameColumn(this.dlg.TabelNameColField.getText().trim());
					bean.setIsClob(this.dlg.YesClobBtn.isSelected() ? "1" : "0");
					this.dlg.dbBox.addItem(editItem);
					this.dlg.synBox.addItem(editItem);
				} else {
					bean = new DBMetaBean();
					bean.setTableID(this.dlg.idField.getText().trim());
					bean.setTableName(this.dlg.TableNameField.getText().trim());
					bean.setFileNameColumn(this.dlg.FileNameColField.getText().trim());
					bean.setTableNameColumn(this.dlg.TabelNameColField.getText().trim());
					bean.setIsClob(this.dlg.YesClobBtn.isSelected() ? "1" : "0");
					boolean exists = false;
					// 判断有没有同名的
					for (int i = 0; i < this.dlg.dbBox.getItemCount(); i++) {
						if (dlg.dbBox.getItemAt(i).toString().equals(editItem)) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						this.dlg.synBox.addItem(editItem);
						this.dlg.dbBox.addItem(editItem);
					}
				}
				this.dlg.dbEnvMap.put(editItem, bean);
				this.dlg.setFieldEditable(false);
				this.dlg.resetBtnStatus();

				for (int i = 0; i < this.dlg.dbBox.getItemCount(); i++) {
					editItem = dlg.dbBox.getItemAt(i).toString();
					bean = dlg.dbEnvMap.get(editItem);
					if (bean == null) {
						continue;
					}
					this.dbList.add(bean);
				}
				if (!DBMetaLoader.getInstance().writeConfig(dbList)) {
					this.dlg.showErrMsg("ERROR:写文件时候失败o(T.T)o");
					return;
				}
				this.dlg.msgLabel.setText("SUCCESS: 保存成功。");
				this.dlg.dbBox.setSelectedItem(editItem);
			} else if (this.dlg.mdfBtn == e.getSource()) {
				this.dlg.mdfBtn.setEnabled(false);
				this.dlg.saveBtn.setEnabled(true);
				this.dlg.deleteBtn.setEnabled(false);
				this.dlg.cancelBtn.setEnabled(true);
				// 编辑方法
				dlg.setFieldEditable(true);
				this.dlg.msgLabel.setText("");
			} else if (this.dlg.deleteBtn == e.getSource()) {
				
				Object obj = dlg.dbBox.getSelectedItem();
				this.deleteList.add(obj.toString());
				dlg.dbBox.removeItem(obj);
				if (this.dlg.dbBox.getItemCount() == 0) {
					this.dlg.deleteBtn.setEnabled(false);
				}
				this.dlg.cancelBtn.setEnabled(true);
				this.dlg.saveBtn.setEnabled(true);
				this.dlg.msgLabel.setText("");
				// 删除方法
				return;
			}
		}

		private ExpDbTablecfgDialog dlg;
		private ArrayList<String> deleteList;
		ArrayList<DBMetaBean> dbList = new ArrayList<DBMetaBean>();
	}

	//显示box中数据库连接的动作
	private class DbMetaCfgInfoBoxAction implements ActionListener {

			public DbMetaCfgInfoBoxAction(ExpDbTablecfgDialog dlg){
				this.dlg = dlg;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox box = (JComboBox)e.getSource();
				dlg.showDbConfigInfo(box.getSelectedItem() == null ? null : box.getSelectedItem().toString());
				dlg.resetBtnStatus();
			}
			
			private ExpDbTablecfgDialog dlg;
		}
}
