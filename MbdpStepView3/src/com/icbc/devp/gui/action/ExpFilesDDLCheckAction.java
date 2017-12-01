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
 * TODO ����������� 
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
	private JRadioButton updateDBToPROCYesRb;//���´洢������������ݿ���ϵ-��
	private JRadioButton updateDBToPROCNoRb; //���´洢������������ݿ���ϵ-��
	private JButton chooseFile; //ѡ��ѹ���ļ�
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
	private JLabel dbInsLabel;    //Ins��ǩ
	private JTextField dbInsText; //Ins�����
	private JButton resetBtn; //���ð�ť
	private JLabel dbTableLabel; //�����ļ����ñ�
	//private JTextField dbTableText; 
	private JButton dbTableColCfgBtn;//���ֶ�����
	private static String tabName = "��ṹ������ļ����ɹ������";
	
	//������Ϣ�������
	private HashMap<String, DBMetaBean> envMap;
     //������
	private JComboBox envBox; //�����ļ����ñ�������
	
	private ExpDbTablecfgDialog setDialog; //���öԻ���
	private String ip;
	private String path;
	private String dbIns;//����ʵ��
	private String userName;
	private String password;
	private String dbExpTable, dbCol1Name, dbCol2Name;
	boolean isClob;
	private int update = 0;
	private String dbTable;
	private String dbTableFileNameCol , dbTableParamCol; //�����ļ����ñ���ֶ�
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
		this.initDbConfig();
		/**
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
				pathLabel.setText("���ݿ��Ŀ¼:");
				pathText.setText("/u02/cbms/cbmsbatch/");
			}
		});
		*/
		
		this.updateDBToPROCYesRb = new JRadioButton("ȫ�����£����´洢������������ݿ���ϵ��",false);
		this.updateDBToPROCNoRb = new JRadioButton("������£������´洢������������ݿ���ϵ��",true);
		
		this.rbGroup = new ButtonGroup();
		this.rbGroup.add(updateDBToPROCYesRb);
		this.rbGroup.add(updateDBToPROCNoRb);
		
		this.ipLabel = new JLabel("���ݿ�IP:", JLabel.TRAILING);
		this.pathLabel = new JLabel("DDL�ļ�·��:", JLabel.TRAILING);		
		this.userLabel    = new JLabel("�û���:", JLabel.TRAILING);
		this.passwordLabel    = new JLabel("����:", JLabel.TRAILING);
		this.dbInsLabel = new JLabel("ʵ����:",JLabel.TRAILING);
		this.dbTableLabel = new JLabel("�����ļ����ñ�",JLabel.TRAILING);
		
		this.ipText = new JTextField("122.16.77.219");
		this.pathText = new JTextField("");
		this.userText = new JTextField("batch");
		this.passwordText = new JTextField("sys123");
		this.dbInsText = new JTextField("npbmsdb");
		//this.dbTableText = new JTextField("MSC_BATCHEXP_PARAM");
		this.dbTableColCfgBtn = new JButton("���ֶ�����");
		
		//�����ֶ����ÿ�
		ExpDbTableConfigAction expDbTableConfigAction = new ExpDbTableConfigAction();
		this.dbTableColCfgBtn.addActionListener(expDbTableConfigAction);
		
		//��envBox �� envMap���ؽ���
		setDialog = new ExpDbTablecfgDialog();
		setDialog.setDbBox(this.envBox);
		setDialog.setDbEnvMap(this.envMap);
		this.setDialog.init();
		expDbTableConfigAction.setDialog(setDialog);
		
		this.chooseFile = new JButton("ѡ���ļ�");
		this.chooseFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
		       JFileChooser jfc = new JFileChooser();
		       jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		       jfc.showDialog(new JLabel(),"�ļ�ѡ��");
		       File file = jfc.getSelectedFile();
		       if(file.isFile()){
		    	   pathText.setText(file.getAbsolutePath().toString().trim());
		       }
		       
			}
		});
		
		this.updateBtn = new JButton("��鵼���ļ���Ӧ���ݿ����DDL��ϵ");
		this.updateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ����У��
				if (ipText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"���������ݿ�IP��ַ","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					ip = ipText.getText();
					if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
						JOptionPane.showMessageDialog(null,"��������ЧIP��ַ","",JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				if (pathText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"�������ѡ��" + pathLabel.getText(),"",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					path = pathText.getText();
				}
				
				if (userText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"���������ݿ��������¼�û���","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					userName = userText.getText();
				}
				
				if (passwordText.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"���������ݿ��������¼����","",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					password = passwordText.getText();
				}
				
				if(dbInsText.getText().equals("")){
					JOptionPane.showMessageDialog(null,"���������ݿ�ʵ����","",JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					dbIns = dbInsText.getText();
				}
				
				//������ݿ������Ƿ���ȷ
				ConnectionManager conn= new ConnectionManager();
				boolean connectFlag = conn.initDBConnection(ip,dbIns,userName,password,"1521");
				
				if(!connectFlag){
					JOptionPane.showMessageDialog(null,"���ݿ����Ӳ���ȷ����","",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(envBox.getSelectedItem() == null){
					JOptionPane.showMessageDialog(null,"��ѡ�����ݿ��","",JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					DBMetaBean dbMeta = envMap.get((envBox.getSelectedItem().toString()));
					dbExpTable = dbMeta.getTableName();
					dbCol1Name = dbMeta.getFileNameColumn();
					dbCol2Name = dbMeta.getTableNameColumn();
					isClob = (dbMeta.getIsClob() == "0" ? true : false);
				}
					
			    /**
				 * ���µ��̴߳���
				*/

				Thread process = new Thread(new UpdateFileTableRelThread(
						ip,userName,password,dbIns, dbExpTable
						, dbCol1Name, dbCol2Name, isClob));
				process.start();
				
				
			}
			
		});

		this.resetBtn = new JButton("����");
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
		 * �ѱȽϵĲ�����Ϣչʾ����
		 */
		@Override
		public void run() {		
			/* ���ļ���ѹ����Ȼ���ȡ��DDL�޸ĵı���*/
			ZipUtil zipUtilDBTables = new ZipUtil();
			zipUtilDBTables.unzip(path);
			updateBtn.setText("����zip�ļ�......");
			updateBtn.setEnabled(false);
			HashSet<String> Tables = zipUtilDBTables.getTableNames();
			/* ����DDL���Ӧ��Bin�ļ�*/			
			updateBtn.setText("���DDL�����ݿ⡢����ļ��Ĺ�ϵ");
			ExpFilesWithDBTable expFilesWithDBTable = new ExpFilesWithDBTable(dbUrl, dbInstName, dbUser, dbPass, "1521",dbExpTable,
					dbCol1Name,dbCol2Name,isClob);
			//updateBtn.setText("���ɹ���");
			
			JOptionPane.showMessageDialog(null,"���ɹ�!����ļ��ڵ�ǰĿ¼��"+dbInstName+"_DDLRefFiles.xls","",JOptionPane.INFORMATION_MESSAGE);
			
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
			
			updateBtn.setText("��鵼���ļ���Ӧ���ݿ����DDL��ϵ");
			updateBtn.setEnabled(true);
		}
		
	}


}
