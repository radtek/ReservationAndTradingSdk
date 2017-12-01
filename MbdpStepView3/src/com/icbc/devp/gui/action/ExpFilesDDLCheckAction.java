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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.icbc.devp.bean.db.DBMetaBean;
import com.icbc.devp.bean.db.env.DbConnectionBean;
import com.icbc.devp.expFileddlChk.ExpFilesWithDBTable;
import com.icbc.devp.gui.factory.DBMetaLoader;
import com.icbc.devp.gui.factory.DbConnectionLoader;
import com.icbc.devp.gui.pane.MbdpSplitPane;
import com.icbc.devp.gui.toolbar.ChooseDbEnvAction;
import com.icbc.devp.gui.toolbar.DbcfgDialog;
import com.icbc.devp.gui.toolbar.ExpDbTableConfigAction;
import com.icbc.devp.gui.toolbar.ExpDbTablecfgDialog;
import com.icbc.devp.object.file.MscImportDsObject;
import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.ftp.FTPOperator;
import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.util.WriteExcel;
import com.icbc.devp.tool.utilZip.ZipUtil;

/**
 * TODO 类的描述：。 
 * <pre>
 *
 * </pre>
 *
 * <pre>
 *     kfzx-yanyj
 *     2016-12-01
 * </pre> 
 */
public class ExpFilesDDLCheckAction implements ActionListener {
	
	private JTabbedPane tabPane;
	
	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints cons;
	//private JRadioButton coverRb;
	//private JRadioButton appendRb;
	private JRadioButton updateDBToPROCYesRb;//更新存储过程与关联数据库表关系-是
	private JRadioButton updateDBToPROCNoRb; //更新存储过程与关联数据库表关系-否
	private JButton chooseFile; //选择压缩文件
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
	private JLabel dbInsLabel;    //Ins标签
	private JTextField dbInsText; //Ins输入框
	private JButton resetBtn; //重置按钮
	private JLabel dbTableLabel; //导出文件配置表
	//private JTextField dbTableText; 
	private JButton dbTableColCfgBtn;//表字段设置
	private static String tabName = "表结构变更与文件生成关联检查";
	
	//链接信息放在这儿
	private HashMap<String, DBMetaBean> envMap;
     //下拉框
	private JComboBox envBox; //导出文件配置表输入域
	
	private ExpDbTablecfgDialog setDialog; //设置对话框
	private String ip;
	private String path;
	private String dbIns;//数据实例
	private String userName;
	private String password;
	private String dbExpTable, dbCol1Name, dbCol2Name;
	boolean isClob;
	private int update = 0;
	private String dbTable;
	private String dbTableFileNameCol , dbTableParamCol; //导出文件配置表的字段
	/*
	private String olocalPath = "result" + File.separator + "tmp";
	private String localPath = olocalPath;
	private String relFileName = "FileTableRel.csv";
	private String configPath = "config/imp_config";
	*/
	public ExpFilesDDLCheckAction(MbdpSplitPane sp) {
		super();
		this.tabPane = sp.getViewTabPane();
		//this.initAction();
		
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
		this.initDbConfig();
		/**
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
				pathLabel.setText("数据库根目录:");
				pathText.setText("/u02/cbms/cbmsbatch/");
			}
		});
		*/
		
		this.updateDBToPROCYesRb = new JRadioButton("全量更新（更新存储过程与关联数据库表关系）",false);
		this.updateDBToPROCNoRb = new JRadioButton("不需更新（不更新存储过程与关联数据库表关系）",true);
		
		this.rbGroup = new ButtonGroup();
		this.rbGroup.add(updateDBToPROCYesRb);
		this.rbGroup.add(updateDBToPROCNoRb);
		
		this.ipLabel = new JLabel("数据库IP:", JLabel.TRAILING);
		this.pathLabel = new JLabel("DDL文件路径:", JLabel.TRAILING);		
		this.userLabel    = new JLabel("用户名:", JLabel.TRAILING);
		this.passwordLabel    = new JLabel("密码:", JLabel.TRAILING);
		this.dbInsLabel = new JLabel("实例名:",JLabel.TRAILING);
		this.dbTableLabel = new JLabel("导出文件配置表",JLabel.TRAILING);
		
		this.ipText = new JTextField("122.16.77.219");
		this.pathText = new JTextField("");
		this.userText = new JTextField("batch");
		this.passwordText = new JTextField("sys123");
		this.dbInsText = new JTextField("npbmsdb");
		//this.dbTableText = new JTextField("MSC_BATCHEXP_PARAM");
		this.dbTableColCfgBtn = new JButton("表字段设置");
		
		//弹出字段设置框
		ExpDbTableConfigAction expDbTableConfigAction = new ExpDbTableConfigAction();
		this.dbTableColCfgBtn.addActionListener(expDbTableConfigAction);
		
		//把envBox 和 envMap加载进来
		setDialog = new ExpDbTablecfgDialog();
		setDialog.setDbBox(this.envBox);
		setDialog.setDbEnvMap(this.envMap);
		this.setDialog.init();
		expDbTableConfigAction.setDialog(setDialog);
		
		this.chooseFile = new JButton("选择文件");
		this.chooseFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
		       JFileChooser jfc = new JFileChooser();
		       jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		       jfc.showDialog(new JLabel(),"文件选择");
		       File file = jfc.getSelectedFile();
		       if(file.isFile()){
		    	   pathText.setText(file.getAbsolutePath().toString().trim());
		       }
		       
			}
		});
		
		this.updateBtn = new JButton("检查导出文件对应数据库表与DDL关系");
		this.updateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 输入校验
				if (ipText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入数据库IP地址","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					ip = ipText.getText();
					if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
						JOptionPane.showMessageDialog(null,"请输入有效IP地址","",JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				if (pathText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入或选择" + pathLabel.getText(),"",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					path = pathText.getText();
				}
				
				if (userText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入数据库服务器登录用户名","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					userName = userText.getText();
				}
				
				if (passwordText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"请输入数据库服务器登录密码","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					password = passwordText.getText();
				}
				
				if(dbInsText.getText().equals("")){
					JOptionPane.showMessageDialog(null,"请输入数据库实例名","",JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					dbIns = dbInsText.getText();
				}
				
				//检查数据库链接是否正确
				ConnectionManager conn= new ConnectionManager();
				boolean connectFlag = conn.initDBConnection(ip,dbIns,userName,password,"1521");
				
				if(!connectFlag){
					JOptionPane.showMessageDialog(null,"数据库链接不正确请检查","",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(envBox.getSelectedItem() == null){
					JOptionPane.showMessageDialog(null,"请选择数据库表","",JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					DBMetaBean dbMeta = envMap.get((envBox.getSelectedItem().toString()));
					dbExpTable = dbMeta.getTableName();
					dbCol1Name = dbMeta.getFileNameColumn();
					dbCol2Name = dbMeta.getTableNameColumn();
					isClob = (dbMeta.getIsClob() == "0" ? true : false);
				}
					
			    /**
				 * 开新的线程处理
				*/

				Thread process = new Thread(new UpdateFileTableRelThread(
						ip,userName,password,dbIns, dbExpTable
						, dbCol1Name, dbCol2Name, isClob));
				process.start();
				
				
			}
			
		});

		this.resetBtn = new JButton("重置");
		this.resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ipText.setText("");
				userText.setText("");
				pathText.setText("");
				passwordText.setText("");
				dbInsText.setText("");
				//dbTableText.setText("");
			}
		});

		this.cons.insets = new Insets(10,5,10,5);
		int hindex = 0;
		
		setLocate(this.cons, 1, hindex, 1,1);
		this.cons.anchor = GridBagConstraints.WEST;
		this.layout.setConstraints(this.updateDBToPROCYesRb,this.cons);
		this.panel.add(this.updateDBToPROCYesRb);
		
		setLocate(this.cons, 1, ++hindex, 1,1 );
		this.layout.setConstraints(this.updateDBToPROCNoRb,this.cons);
		this.panel.add(this.updateDBToPROCNoRb);
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.cons.fill = GridBagConstraints.BOTH;
		this.layout.setConstraints(this.ipLabel, this.cons);
		this.panel.add(this.ipLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		cons.ipady = 5;
		this.layout.setConstraints(this.ipText, this.cons);	
		this.panel.add(this.ipText);
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.cons.fill = GridBagConstraints.BOTH;
		this.layout.setConstraints(this.pathLabel, this.cons);
		this.panel.add(this.pathLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		this.layout.setConstraints(this.pathText, this.cons);	
		this.panel.add(this.pathText);
		
		setLocate(this.cons, 3, hindex ,1, 1);
		this.layout.setConstraints(this.chooseFile,this.cons);
		this.panel.add(this.chooseFile);
		
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
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.layout.setConstraints(this.dbInsLabel, this.cons);
		this.panel.add(this.dbInsLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		this.layout.setConstraints(this.dbInsText , this.cons);
		this.panel.add(this.dbInsText);
		
		setLocate(this.cons, 0, ++hindex, 1, 1);
		this.layout.setConstraints(this.dbTableLabel, this.cons);
		this.panel.add(this.dbTableLabel);
		
		setLocate(this.cons, 1, hindex, 2, 1);
		this.layout.setConstraints(this.envBox,this.cons);
		this.panel.add(this.envBox);
		
		setLocate(this.cons, 3, hindex, 1, 1);
		this.layout.setConstraints(this.dbTableColCfgBtn,this.cons);
		this.panel.add(this.dbTableColCfgBtn);

		setLocate(this.cons, 0, ++hindex, 3, 1);
		this.cons.fill = GridBagConstraints.NONE;
		this.cons.anchor = GridBagConstraints.CENTER;
		this.layout.setConstraints(this.updateBtn, this.cons);	
		this.panel.add(this.updateBtn);
		
		setLocate(this.cons, 3,hindex,1,1);
		this.cons.anchor = GridBagConstraints.EAST;
		this.layout.setConstraints(this.resetBtn,this.cons);
		this.panel.add(this.resetBtn);
		
	}
	
	private void setLocate(GridBagConstraints conn, int grix, int griy, int griWidth, int griHeight) {
		conn.gridx = grix;
		conn.gridy = griy;
		conn.gridwidth = griWidth;
		conn.gridheight = griHeight;
	}
	
	private void initDbConfig(){
		this.envMap = DBMetaLoader.getInstance().loadDbConfig();
		this.envBox = new JComboBox();
		DBMetaBean bean;
		ArrayList<String> itemList = new ArrayList<String>();
		for(Iterator<DBMetaBean> it = this.envMap.values().iterator(); it.hasNext();){
			bean = it.next();
			itemList.add(bean.getTableID());
			System.out.println(bean.getTableID());
		}
		Collections.sort(itemList);
		for(int i=0; i<itemList.size(); i++){
			this.envBox.addItem(itemList.get(i));
		}
	}
		
	class UpdateFileTableRelThread implements Runnable {

		private String dbUrl = "jdbc:oracle:thin:@122.16.45.196:1521:MSCDB";
		private String dbUser = "msc";
		private String dbPass = "msc"; 
		private String dbInstName = "MSCDB";
		private String dbExpTable = "MSC_BATCHEXP_PARAM";
		private String dbCol1Name = "FILE_NAME";
		private String dbCol2Name = "SQLSTR";
		private boolean isClob = false;
		
		public UpdateFileTableRelThread(String dbUrl,String dbUser,String dbPass,String dbInstName,String dbExpTable
				,String dbCol1Name,String dbCol2Name,boolean isClob) {
			this.dbUrl = dbUrl;
			this.dbUser = dbUser;
		    this.dbPass = dbPass;
		    this.dbInstName = dbInstName;
		    this.dbExpTable = dbExpTable;
		    this.dbCol1Name = dbCol1Name;
		    this.dbCol2Name = dbCol2Name;
		    this.isClob = isClob;
		}

		/**
		 * 把比较的步骤信息展示出来
		 */
		@Override
		public void run() {		
			/* 把文件解压缩，然后获取有DDL修改的表名*/
			ZipUtil zipUtilDBTables = new ZipUtil();
			zipUtilDBTables.unzip(path);
			updateBtn.setText("处理zip文件......");
			updateBtn.setEnabled(false);
			HashSet<String> Tables = zipUtilDBTables.getTableNames();
			/* 生成DDL表对应的Bin文件*/			
			updateBtn.setText("检查DDL与数据库、输出文件的关系");
			ExpFilesWithDBTable expFilesWithDBTable = new ExpFilesWithDBTable(dbUrl, dbInstName, dbUser, dbPass, "1521",dbExpTable,
					dbCol1Name,dbCol2Name,isClob);
			//updateBtn.setText("检查成功！");
			
			JOptionPane.showMessageDialog(null,"检查成功!结果文件在当前目录下"+dbInstName+"_DDLRefFiles.xls","",JOptionPane.INFORMATION_MESSAGE);
			
			HashMap<String, HashSet<String>> result = expFilesWithDBTable.getDBTabletoFile();
			
			for(String res : result.keySet()){
				System.out.print(res + ":");
				for(String ele : result.get(res)){
					System.out.print(ele + " ");
				}
				System.out.println("");
			}
			
			WriteExcel writeExcel = new WriteExcel();
			
			HashMap<String, HashSet<String>> Ans = new HashMap<String,HashSet<String>>();
			
			for(String table : Tables){
				if(result.containsKey(table)){
					Ans.put(table, result.get(table));
				}else{
					Ans.put(table,new HashSet<String>());
				}
			}
			
			try {
				writeExcel.WriteDDLdbTableRefFile(dbInstName, Ans ,result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			updateBtn.setText("检查导出文件对应数据库表与DDL关系");
			updateBtn.setEnabled(true);
		}
		
	}


}
