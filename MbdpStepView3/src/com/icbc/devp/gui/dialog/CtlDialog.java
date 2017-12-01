package com.icbc.devp.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.icbc.devp.bean.db.MbdpCtlBean;
import com.icbc.devp.gui.factory.ColorFactory;
import com.icbc.devp.gui.factory.IconFactory;
import com.icbc.devp.gui.info.DataLoader;
import com.icbc.devp.gui.info.DbDataUpdate;

public class CtlDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//��Ա����
	private JTextField workdateField;
	private JTextField rundayField;
	private JComboBox statusBox;
	private JComboBox pretTypeBox;
	private JLabel workdateLabel;
	private JLabel rundayLabel;
	private JLabel statusLabel;
	private JLabel pretLabel;
	private JLabel msgLabel;

	private MbdpCtlBean ctl;
	//�༭��ťɶ��
	private JButton editBtn;
	private JButton refreshBth;
	private DbDataUpdate dbUpdate = new DbDataUpdate();
	//layout
	private GridBagLayout layout;
	private GridBagConstraints cons;
	
	
	public CtlDialog(){
		cons = new GridBagConstraints();
		layout = new GridBagLayout();
		this.setLayout(layout);
		this.setSize(500, 160);
		this.setModal(true);
		this.init();
		this.setVisible(false);
		this.setResizable(false);
		this.setIconImage(IconFactory.getInstance().getIcon("BOARD_SH").getImage());
	}
	
	public void showCtlInfo(MbdpCtlBean ctl, int x, int y){
		this.ctl = ctl;
		this.setTitle(this.ctl.getName());
		this.workdateField.setText(this.ctl.getWorkdate());
		this.statusBox.setSelectedItem(this.ctl.getStatusDesc());
		this.rundayField.setText(this.ctl.getMonRunDay());
		this.pretTypeBox.setSelectedItem(this.ctl.getPretType());
		this.setLocation(x, y);
		this.msgLabel.setText("");
		this.setVisible(true);
	}

	
	//��Ա����
	private void init(){
		//һ������
		this.workdateLabel = new JLabel("��������: ");
		cons.weightx = 100.0;
		cons.weighty = 100.0;
		cons.anchor = GridBagConstraints.CENTER;
		cons.fill   = GridBagConstraints.BOTH;
		//label��ʼ��
		this.workdateLabel = new JLabel("��������:", JLabel.CENTER);
		this.rundayLabel   = new JLabel("�� �� ��:" , JLabel.CENTER);
		this.statusLabel   = new JLabel("����״̬:", JLabel.CENTER);
		this.pretLabel     = new JLabel("Ԥ �� ��:" , JLabel.CENTER);
		this.msgLabel      = new JLabel("",JLabel.LEFT);
		//���������Ϣ��
		this.workdateField = new JTextField("");
		this.workdateField.setColumns(15);
		this.rundayField   = new JTextField("");
		this.rundayField.setColumns(10);
		this.statusBox     = new JComboBox();
		this.statusBox.addItem("1-��ʼ");
		this.statusBox.addItem("2-������");
		this.statusBox.addItem("4-�ж�");
		//ѡ��������
		this.pretTypeBox   = new JComboBox();
		this.pretTypeBox.addItem("UDS");
		this.pretTypeBox.addItem("UDSN");
		this.pretTypeBox.addItem("GFT");
		//�޸İ�ť
		this.editBtn = new JButton("�� ��");
		this.editBtn.setIcon(IconFactory.getInstance().getIcon("APPLY"));
		this.editBtn.addActionListener(this);
		this.refreshBth = new JButton("ˢ ��");
		this.refreshBth.setIcon(IconFactory.getInstance().getIcon("REFRESH"));
		this.refreshBth.addActionListener(this);
		//��һ��λ
		cons.gridx = 0;
		cons.gridy = 0;
		
		this.layout.setConstraints(this.workdateLabel, cons);
		cons.gridx ++;
		this.layout.setConstraints(this.workdateField, cons);
		cons.gridx ++;
		this.layout.setConstraints(this.statusLabel, cons);
		cons.gridx ++;
		this.layout.setConstraints(this.statusBox, cons);
		cons.gridx = 0;
		cons.gridy = 1;
		this.layout.setConstraints(this.rundayLabel, cons);
		cons.gridx ++;
		this.layout.setConstraints(this.rundayField, cons);
		cons.gridx ++;
		this.layout.setConstraints(this.pretLabel, cons);
		cons.gridx ++;
		this.layout.setConstraints(this.pretTypeBox, cons);
		//��һ��
		cons.gridy ++;
		cons.gridx = 3;
		JLabel label = new JLabel("", JLabel.CENTER);
		this.layout.setConstraints(label, cons);
		cons.gridy ++;
		cons.gridx = 0;
		cons.gridwidth = 2;
		this.layout.setConstraints(this.msgLabel, cons);
		cons.gridx = 2;
		cons.gridwidth = 1;
		this.layout.setConstraints(this.refreshBth, cons);
		cons.gridx = 3;
		this.layout.setConstraints(this.editBtn, cons);
		
		//�Ӷ�����ȥ
		this.add(workdateLabel);
		this.add(workdateField);
		this.add(statusLabel);
		this.add(statusBox);
		this.add(rundayLabel);
		this.add(rundayField);
		this.add(pretLabel);
		this.add(pretTypeBox);
		this.add(label);
		this.add(this.msgLabel);
		this.add(this.refreshBth);
		this.add(editBtn);
	}
	
	/**TODO:����MBDP_B_CTL�������*/
	public void actionPerformed(ActionEvent e) {
		if(this.ctl == null){
			return;
		}
		this.msgLabel.setText("");
		
		if(e.getSource() == this.editBtn){
			String status = (String)this.statusBox.getSelectedItem();
			if(status != null){
				status = status.substring(0, 1);
			}else{
				return;
			}
			
			String tmpStatus = this.ctl.getStatus();
			String tmpWorkdate = this.ctl.getWorkdate();
			String runday = this.ctl.getMonRunDay() == null ? "" : this.ctl.getMonRunDay();
			String pretType = this.ctl.getPretType();
			
			this.ctl.setStatus(status);
			this.ctl.setWorkdate(this.workdateField.getText());
			this.ctl.setMonRunDay(this.rundayField.getText());
			this.ctl.setPretType((String)this.pretTypeBox.getSelectedItem());
			if(!dbUpdate.updateCtlInfo(this.ctl)){
				this.msgLabel.setForeground(ColorFactory.getInstance().getColor("RED"));
				this.msgLabel.setText(" ���µ����ݿ�ʧ��o(>_<)o");
				//����
				this.ctl.setStatus(tmpStatus);
				this.ctl.setWorkdate(tmpWorkdate);
				this.ctl.setMonRunDay(runday);
				this.ctl.setPretType(pretType);
			}else{
				this.msgLabel.setForeground(ColorFactory.getInstance().getColor("BLACK"));
				this.msgLabel.setText(" SUCC�����³ɹ��� ");
			}
			return;
		}
		
		if(e.getSource() == this.refreshBth){
			MbdpCtlBean bean = DataLoader.refreshCtl(this.ctl.getApp(), this.ctl.getCycle(), this.ctl.getDatasource());
			if(bean == null){
				this.msgLabel.setForeground(ColorFactory.getInstance().getColor("RED"));
				this.msgLabel.setText(" ˢ��ʧ�� o(>_<)o");
			}else{
				this.ctl.setMonRunDay(bean.getMonRunDay());
				this.ctl.setPretType(bean.getPretType());
				this.ctl.setStatus(bean.getStatus());
				this.ctl.setWorkdate(bean.getWorkdate());
				this.workdateField.setText(this.ctl.getWorkdate());
				this.statusBox.setSelectedItem(this.ctl.getStatusDesc());
				this.rundayField.setText(this.ctl.getMonRunDay());
				this.pretTypeBox.setSelectedItem(this.ctl.getPretType());
			}
		}

	}

}
