/**
 * 
 */
package com.icbc.devp.gui.action;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.icbc.devp.gui.pane.MbdpSplitPane;
import com.icbc.devp.object.file.MscImportDsObject;
import com.icbc.devp.tool.ftp.FTPOperator;
import com.icbc.devp.tool.log.Log;

/**
 * TODO ����������� 
 * <pre>
 *
 * </pre>
 *
 * <pre>
 * modify by kfzx-liangxch on 2016-8-29
 *    fix->1.
 *         2.
 * </pre> 
 */
public class UpdateFileTableRelAction implements ActionListener {
	
	private JTabbedPane tabPane;
	
	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints cons;
	private JRadioButton coverRb;
	private JRadioButton appendRb;
	private ButtonGroup rbGroup;
	private JLabel ipLabel;
	private JTextField ipText;
	private JLabel pathLabel;
	private JTextField pathText;
	private JLabel userLabel;
	private JTextField userText;
	private JLabel passwordLabel;
	private JTextField passwordText; 
	private JButton updateBtn;
	private static String tabName = "���µ����ļ��ͽӿڱ���չ�ϵ";
	
	private String ip;
	private String path;
	private String userName;
	private String password;
	private int update = 0;
	private String olocalPath = "result" + File.separator + "tmp";
	private String localPath = olocalPath;
	private String relFileName = "FileTableRel.csv";
	private String configPath = "config/imp_config";
	
	public UpdateFileTableRelAction(MbdpSplitPane sp) {
		super();
		this.tabPane = sp.getViewTabPane();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// �ж����tab�Ƿ����
		int index = this.tabPane.indexOfTab(tabName);
		if (index != -1) {
			tabPane.setSelectedIndex(index);
			return;
		}
		
		cons = new GridBagConstraints();
		layout = new GridBagLayout();
		this.panel = new JPanel();
		this.panel.setLayout(layout);
		init();
		tabPane.add(tabName,panel);
		tabPane.setSelectedComponent(panel);
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	private void init() {
		
		this.appendRb = new JRadioButton("�������£������������ļ�ʱ�����Ӳ��Ի��������������£���ָ��������Դ��", true);
		this.appendRb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				pathLabel.setText("�����ļ�·��:");
				pathText.setText("/u02/cbms/cbmsbatch/config/imp_config/cbms/day/alm001/");
			}
		});
		this.coverRb = new JRadioButton("ȫ�����£����ܺ����̻������������ļ��д�©����������ģ�⻷������ȫ�����£�", false);
		this.coverRb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pathLabel.setText("������Ŀ¼:");
				pathText.setText("/u02/cbms/cbmsbatch/");
			}
		});
		this.rbGroup = new ButtonGroup();
		this.rbGroup.add(appendRb);
		this.rbGroup.add(coverRb);
		
		this.ipLabel = new JLabel("����������IP:", JLabel.TRAILING);
		this.pathLabel = new JLabel("�����ļ�·��:", JLabel.TRAILING);
		this.userLabel    = new JLabel("�û���:", JLabel.TRAILING);
		this.passwordLabel    = new JLabel("����:", JLabel.TRAILING);
		
		this.ipText = new JTextField("122.16.93.53");
		this.pathText = new JTextField("/u02/cbms/cbmsbatch/config/imp_config/cbms/day/alm001/");
		this.userText = new JTextField("mscbatch");
		this.passwordText = new JTextField("mscbatch");
		
		this.updateBtn = new JButton("���µ����ļ��ͽӿڱ���չ�ϵ");
		this.updateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ����У��
				if (ipText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"����������������IP��ַ","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					ip = ipText.getText();
					if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
						JOptionPane.showMessageDialog(null,"��������ЧIP��ַ","",JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				if (pathText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"������" + pathLabel.getText(),"",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					path = pathText.getText();
					if (coverRb.isSelected() == true) {	// ȫ������
						if (path.charAt(path.length()-1) == '/') {
							path = path + configPath;
						} else {
							path = path + "/" + configPath;
						}
					} else {	// ��������
						int tindex = path.indexOf("config/imp_config");
						if (tindex == -1) {
							JOptionPane.showMessageDialog(null,"����������ļ�·��Ӧ����config/imp_config","",JOptionPane.ERROR_MESSAGE);
							return;
						}
						localPath = olocalPath + path.substring(tindex + 17);
					}
				}
				
				if (userText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"������������������¼�û���","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					userName = userText.getText();
				}
				
				if (passwordText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"������������������¼����","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					password = passwordText.getText();
				}
				// ��¼������
				FTPOperator ftpOper = new FTPOperator();
				if (!ftpOper.login(ip, userName, password)) {
					JOptionPane.showMessageDialog(null,"��¼ʧ�ܣ�","",JOptionPane.ERROR_MESSAGE);
					Log.getInstance().error("��¼ʧ�ܣ�IP = " + ip + ", username = " + userName + ", password = " + password);
					ftpOper.logoff();
					return;
				}
				Log.getInstance().info("��¼�ɹ���IP = " + ip + ", username = " + userName + ", password = " + password);
				if (!ftpOper.isDirectoryExist(path)) {
					JOptionPane.showMessageDialog(null,"·��" + path + "�����ڣ����������룡","",JOptionPane.ERROR_MESSAGE);
					ftpOper.logoff();
					return;
				}
				File tmpDir = new File(olocalPath);
				if (tmpDir.exists() && !deleteDir(tmpDir)) {
					JOptionPane.showMessageDialog(null,"������ʱĿ¼" + olocalPath + "ʧ�ܣ�","",JOptionPane.ERROR_MESSAGE);
					ftpOper.logoff();
					return;
				}
				tmpDir = new File(localPath);
				if (!tmpDir.exists() && !tmpDir.mkdirs()) {
					JOptionPane.showMessageDialog(null,"������ʱĿ¼" + localPath + "ʧ�ܣ�","",JOptionPane.ERROR_MESSAGE);
					ftpOper.logoff();
					return;
				}
				
				File rfile = new File("FileTableRel.csv");
				// �����ļ������¶��չ�ϵ
				if (update == 1) {
					JOptionPane.showMessageDialog(null,"��̨���ڸ���","",JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (rfile.exists()) {
						int op = JOptionPane.showConfirmDialog(null, "�����ļ��ͽӿڱ���չ�ϵ��FileTableRel.csv���Ѵ��ڣ�ȷ��Ҫ������", "ȷ��",JOptionPane.YES_NO_OPTION);
						if (op == JOptionPane.NO_OPTION) {
							ftpOper.logoff();
							return;
						}
					}
					Thread updateFTRel = new Thread(new UpdateFileTableRelThread(ftpOper));
					updateFTRel.start();
					updateBtn.setText("������......");
					updateBtn.setEnabled(false);
				}
			}
			
		});

//		Insets lbInset = new Insets(10,30,10,2);
//		Insets textInset = new Insets(10,2,10,70);
		this.cons.insets = new Insets(10,5,10,5);
		int hindex = 0;
		
		setLocate(this.cons, 1, hindex, 1, 1);
		this.cons.anchor = GridBagConstraints.WEST;
		this.layout.setConstraints(this.appendRb, this.cons);
		this.panel.add(this.appendRb);
		
		setLocate(this.cons, 1, ++hindex, 1, 1);
		this.layout.setConstraints(this.coverRb, this.cons);
		this.panel.add(this.coverRb);
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.cons.fill = GridBagConstraints.BOTH;
		this.layout.setConstraints(this.ipLabel, this.cons);
		this.panel.add(this.ipLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		cons.ipady = 5;
		this.layout.setConstraints(this.ipText, this.cons);	
		this.panel.add(this.ipText);
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.layout.setConstraints(this.pathLabel, this.cons);
		this.panel.add(this.pathLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		this.layout.setConstraints(this.pathText, this.cons);	
		this.panel.add(this.pathText);
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.layout.setConstraints(this.userLabel, this.cons);
		this.panel.add(this.userLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		this.layout.setConstraints(this.userText, this.cons);	
		this.panel.add(this.userText);
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.layout.setConstraints(this.passwordLabel, this.cons);
		this.panel.add(this.passwordLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		this.layout.setConstraints(this.passwordText, this.cons);	
		this.panel.add(this.passwordText);
		
		setLocate(this.cons, 0, ++hindex, 3, 1);
		this.cons.fill = GridBagConstraints.NONE;
		this.cons.anchor = GridBagConstraints.CENTER;
		this.layout.setConstraints(this.updateBtn, this.cons);	
		this.panel.add(this.updateBtn);
		
		/*this.cons.anchor = GridBagConstraints.EAST; 
		this.layout.setConstraints(this.appendRb, this.cons);
		this.panel.add(this.appendRb);
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.cons.anchor = GridBagConstraints.WEST; 
		this.layout.setConstraints(this.coverRb, this.cons);
		this.panel.add(this.coverRb);
		
		this.cons.anchor = GridBagConstraints.CENTER; 
		this.cons.insets = lbInset;
		this.cons.fill = GridBagConstraints.BOTH;
		this.cons.weightx = 0;
		this.cons.gridwidth = GridBagConstraints.RELATIVE;
		this.layout.setConstraints(this.ipLabel, this.cons);
		this.panel.add(this.ipLabel);

		cons.insets = textInset;
		cons.ipady = 5;
		this.cons.weightx = 1;
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.layout.setConstraints(this.ipText, this.cons);	
		this.panel.add(this.ipText);

		cons.insets = lbInset;
		this.cons.weightx = 0;
		this.cons.gridwidth = GridBagConstraints.RELATIVE;
		this.layout.setConstraints(this.pathLabel, this.cons);
		this.panel.add(this.pathLabel);

		cons.insets = textInset;
		this.cons.weightx = 1;
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.layout.setConstraints(this.pathText, this.cons);	
		this.panel.add(this.pathText);

		cons.insets = lbInset;
		this.cons.weightx = 0;
		this.cons.gridwidth = GridBagConstraints.RELATIVE;
		this.layout.setConstraints(this.userLabel, this.cons);
		this.panel.add(this.userLabel);

		cons.insets = textInset;
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.cons.weightx = 1;
		this.layout.setConstraints(this.userText, this.cons);	
		this.panel.add(this.userText);
		
		cons.insets = lbInset;
		this.cons.weightx = 0;
		this.cons.gridwidth = GridBagConstraints.RELATIVE;
		this.layout.setConstraints(this.passwordLabel, this.cons);
		this.panel.add(this.passwordLabel);

		cons.insets = textInset;
		this.cons.weightx = 1;
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.layout.setConstraints(this.passwordText, this.cons);	
		this.panel.add(this.passwordText);
		this.cons.weightx = 0;
		this.cons.fill = GridBagConstraints.NONE;
		this.layout.setConstraints(this.updateBtn, this.cons);	
		this.panel.add(this.updateBtn);*/
	}
	
	private void setLocate(GridBagConstraints conn, int grix, int griy, int griWidth, int griHeight) {
		conn.gridx = grix;
		conn.gridy = griy;
		conn.gridwidth = griWidth;
		conn.gridheight = griHeight;
	}
	
	/**
	 * ɾ��fname�����µ������ļ� 
	 * @param fname
	 * @return true-ɾ���ɹ���false-ɾ��ʧ��
	 */
	private boolean deleteDir(File fname) {
		boolean result = true;
		File[] fileList = null;
		if (fname.isDirectory()) {	// ��ǰ�ļ�ΪĿ¼
			fileList = fname.listFiles();
			if (fileList == null) {	// Ŀ¼Ϊ��
				result = fname.delete();
			} else {	// Ŀ¼�ǿ�
				for (int i = 0; i < fileList.length; ++i) {
					deleteDir(fileList[i]);
				}
				result = fname.delete();
			}
		} else {	// ��ǰ�ļ�Ϊ�ļ�
			result = fname.delete();
		}
		if (result == false) {
			Log.getInstance().errorAndLog("ɾ��" + fname + "ʧ�ܣ�");
		}
		return result;
	}
	
	class UpdateFileTableRelThread implements Runnable {
		
		private FTPOperator ftpOper;
		public UpdateFileTableRelThread(FTPOperator ftpOp) {
			ftpOper = ftpOp;
		}

		@Override
		public void run() {
			update = 1;
			updateBtn.setText("��ʼ���������������ص��������ļ�...");
			if (ftpOper.downloadAll(path, localPath)) {
				// ��������������ɣ���ʼ���ɶ��չ�ϵ
				updateBtn.setText("������ɣ���ʼ���ɵ����ļ��ͽӿڱ���չ�ϵ...");
				try{
					MscImportDsObject obj = new MscImportDsObject();
					int loadResult = obj.loadAllConfigFile(olocalPath, ".xml");
					if (loadResult == -1) {
						JOptionPane.showMessageDialog(null,"�����ļ��ͽӿڱ���չ�ϵ��FileTableRel.csv������ʧ��","",JOptionPane.ERROR_MESSAGE);
						update = -1;
					} else {
						boolean isAppend = true;
						if (coverRb.isSelected() == true) {
							isAppend = false;
						}
						if(obj.writeFileTableRel(relFileName, isAppend, olocalPath)){
//						if (obj.writeXmlInfo(relFileName)) {
							String disMeg;
							if (loadResult == 0) {
								disMeg = "�����ļ��ͽӿڱ���չ�ϵ��FileTableRel.csv���Ѹ������";
							} else {
								
								disMeg = "�����ļ��ͽӿڱ���չ�ϵ��FileTableRel.csv���Ѹ�����ϣ������ֵ��������ļ�����ʧ��(log" + File.separator + "error" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".err)";
							}
							System.out.println(disMeg);
							updateBtn.setText("���³ɹ���");
							JOptionPane.showMessageDialog(null,disMeg,"",JOptionPane.INFORMATION_MESSAGE);
							update = 2;
						} else {
							JOptionPane.showMessageDialog(null,"�����ļ��ͽӿڱ���չ�ϵ��FileTableRel.csv������ʧ��","",JOptionPane.ERROR_MESSAGE);
							update = -1;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"�����ļ��ͽӿڱ���չ�ϵ��FileTableRel.csv������ʧ��","",JOptionPane.ERROR_MESSAGE);
					update = -1;
					Log.getInstance().exception(e);
				}
			} else {
				JOptionPane.showMessageDialog(null,"���������������ص��������ļ�ʧ�ܣ�","",JOptionPane.ERROR_MESSAGE);
				update = -1;
			}
			updateBtn.setText("���µ����ļ��ͱ�Ķ��չ�ϵ");
			updateBtn.setEnabled(true);
			ftpOper.logoff();
		}
		
	}

//	public static void main(String[] args) {
//		UpdateFileTableRelAction aa = new UpdateFileTableRelAction();
//		File tmpDir = new File(aa.localPath);
//		if (tmpDir.exists()) {
//			System.out.println(aa.deleteDir(tmpDir));
//		}
//		System.out.println("done");
//	}
}
