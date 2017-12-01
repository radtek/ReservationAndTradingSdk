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
 * TODO 类的描述：。 
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
	private static String tabName = "更新导入文件和接口表对照关系";
	
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
		
		// 判断这个tab是否存在
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
		
		this.appendRb = new JRadioButton("增量更新（有新增导入文件时请连接测试环境进行增量更新，可指定到数据源）", true);
		this.appendRb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				pathLabel.setText("配置文件路径:");
				pathText.setText("/u02/cbms/cbmsbatch/config/imp_config/cbms/day/alm001/");
			}
		});
		this.coverRb = new JRadioButton("全量更新（功能和流程环境导入配置文件有错漏，建议连接模测环境进行全量更新）", false);
		this.coverRb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pathLabel.setText("批量根目录:");
				pathText.setText("/u02/cbms/cbmsbatch/");
			}
		});
		this.rbGroup = new ButtonGroup();
		this.rbGroup.add(appendRb);
		this.rbGroup.add(coverRb);
		
		this.ipLabel = new JLabel("批量服务器IP:", JLabel.TRAILING);
		this.pathLabel = new JLabel("配置文件路径:", JLabel.TRAILING);
		this.userLabel    = new JLabel("用户名:", JLabel.TRAILING);
		this.passwordLabel    = new JLabel("密码:", JLabel.TRAILING);
		
		this.ipText = new JTextField("122.16.93.53");
		this.pathText = new JTextField("/u02/cbms/cbmsbatch/config/imp_config/cbms/day/alm001/");
		this.userText = new JTextField("mscbatch");
		this.passwordText = new JTextField("mscbatch");
		
		this.updateBtn = new JButton("更新导入文件和接口表对照关系");
		this.updateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 输入校验
				if (ipText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入批量服务器IP地址","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					ip = ipText.getText();
					if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
						JOptionPane.showMessageDialog(null,"请输入有效IP地址","",JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				if (pathText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入" + pathLabel.getText(),"",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					path = pathText.getText();
					if (coverRb.isSelected() == true) {	// 全量更新
						if (path.charAt(path.length()-1) == '/') {
							path = path + configPath;
						} else {
							path = path + "/" + configPath;
						}
					} else {	// 增量更新
						int tindex = path.indexOf("config/imp_config");
						if (tindex == -1) {
							JOptionPane.showMessageDialog(null,"输入的配置文件路径应包含config/imp_config","",JOptionPane.ERROR_MESSAGE);
							return;
						}
						localPath = olocalPath + path.substring(tindex + 17);
					}
				}
				
				if (userText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入批量服务器登录用户名","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					userName = userText.getText();
				}
				
				if (passwordText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入批量服务器登录密码","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					password = passwordText.getText();
				}
				// 登录服务器
				FTPOperator ftpOper = new FTPOperator();
				if (!ftpOper.login(ip, userName, password)) {
					JOptionPane.showMessageDialog(null,"登录失败！","",JOptionPane.ERROR_MESSAGE);
					Log.getInstance().error("登录失败！IP = " + ip + ", username = " + userName + ", password = " + password);
					ftpOper.logoff();
					return;
				}
				Log.getInstance().info("登录成功！IP = " + ip + ", username = " + userName + ", password = " + password);
				if (!ftpOper.isDirectoryExist(path)) {
					JOptionPane.showMessageDialog(null,"路径" + path + "不存在，请重新输入！","",JOptionPane.ERROR_MESSAGE);
					ftpOper.logoff();
					return;
				}
				File tmpDir = new File(olocalPath);
				if (tmpDir.exists() && !deleteDir(tmpDir)) {
					JOptionPane.showMessageDialog(null,"创建临时目录" + olocalPath + "失败！","",JOptionPane.ERROR_MESSAGE);
					ftpOper.logoff();
					return;
				}
				tmpDir = new File(localPath);
				if (!tmpDir.exists() && !tmpDir.mkdirs()) {
					JOptionPane.showMessageDialog(null,"创建临时目录" + localPath + "失败！","",JOptionPane.ERROR_MESSAGE);
					ftpOper.logoff();
					return;
				}
				
				File rfile = new File("FileTableRel.csv");
				// 下载文件，更新对照关系
				if (update == 1) {
					JOptionPane.showMessageDialog(null,"后台正在更新","",JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (rfile.exists()) {
						int op = JOptionPane.showConfirmDialog(null, "导入文件和接口表对照关系“FileTableRel.csv”已存在，确认要更新吗？", "确认",JOptionPane.YES_NO_OPTION);
						if (op == JOptionPane.NO_OPTION) {
							ftpOper.logoff();
							return;
						}
					}
					Thread updateFTRel = new Thread(new UpdateFileTableRelThread(ftpOper));
					updateFTRel.start();
					updateBtn.setText("更新中......");
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
	 * 删除fname及其下的所有文件 
	 * @param fname
	 * @return true-删除成功，false-删除失败
	 */
	private boolean deleteDir(File fname) {
		boolean result = true;
		File[] fileList = null;
		if (fname.isDirectory()) {	// 当前文件为目录
			fileList = fname.listFiles();
			if (fileList == null) {	// 目录为空
				result = fname.delete();
			} else {	// 目录非空
				for (int i = 0; i < fileList.length; ++i) {
					deleteDir(fileList[i]);
				}
				result = fname.delete();
			}
		} else {	// 当前文件为文件
			result = fname.delete();
		}
		if (result == false) {
			Log.getInstance().errorAndLog("删除" + fname + "失败！");
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
			updateBtn.setText("开始从批量服务器下载导入配置文件...");
			if (ftpOper.downloadAll(path, localPath)) {
				// 导入配置下载完成，开始生成对照关系
				updateBtn.setText("下载完成，开始生成导入文件和接口表对照关系...");
				try{
					MscImportDsObject obj = new MscImportDsObject();
					int loadResult = obj.loadAllConfigFile(olocalPath, ".xml");
					if (loadResult == -1) {
						JOptionPane.showMessageDialog(null,"导入文件和接口表对照关系“FileTableRel.csv”更新失败","",JOptionPane.ERROR_MESSAGE);
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
								disMeg = "导入文件和接口表对照关系“FileTableRel.csv”已更新完毕";
							} else {
								
								disMeg = "导入文件和接口表对照关系“FileTableRel.csv”已更新完毕，但部分导入配置文件加载失败(log" + File.separator + "error" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".err)";
							}
							System.out.println(disMeg);
							updateBtn.setText("更新成功！");
							JOptionPane.showMessageDialog(null,disMeg,"",JOptionPane.INFORMATION_MESSAGE);
							update = 2;
						} else {
							JOptionPane.showMessageDialog(null,"导入文件和接口表对照关系“FileTableRel.csv”更新失败","",JOptionPane.ERROR_MESSAGE);
							update = -1;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"导入文件和接口表对照关系“FileTableRel.csv”更新失败","",JOptionPane.ERROR_MESSAGE);
					update = -1;
					Log.getInstance().exception(e);
				}
			} else {
				JOptionPane.showMessageDialog(null,"从批量服务器下载导入配置文件失败！","",JOptionPane.ERROR_MESSAGE);
				update = -1;
			}
			updateBtn.setText("更新导入文件和表的对照关系");
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
