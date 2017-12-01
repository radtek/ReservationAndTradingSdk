package com.icbc.main;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EBMMenuViewer {

	protected Shell shell;
	private Text txt_MenuName;
	private String fileList;
	private Text txt_PCKG;
	private Text txt_tableName;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//EBMMenuViewer window = new EBMMenuViewer();
			//window.open();
			boolean flag = new SearchPath().work("D:\\CCViews\\kfzx-linyg_N2BAPP1701\\vobs\\V_N2BAPP","预审批客户确认","EI44540");
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(621, 484);
		shell.setText("\u83DC\u5355\u94FE\u8DEF\u5206\u6790\u5DE5\u5177\u00A9\u73E0\u6D77\u6D4B\u8BD5\u90E8");
		
		Label lb_File = new Label(shell, SWT.NONE);
		lb_File.setBounds(29, 60, 105, 17);
		lb_File.setText("\u9879\u76EE\u6587\u4EF6\u6240\u5728\u76EE\u5F55");
		
		final Label lb_FileName = new Label(shell, SWT.NONE);
		lb_FileName.setBounds(140, 60, 163, 17);
		
		Button btn_File = new Button(shell, SWT.NONE);
		btn_File.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shell);
				dialog.setText("选择文件路径");
				String saveFile = dialog.open();
				if (saveFile != null) {
					File directory = new File(saveFile);
					System.out.println(directory.getPath());
					fileList = directory.getPath();
					lb_FileName.setText(fileList);
				}
			}
		});
		
		btn_File.setBounds(333, 55, 80, 27);
		btn_File.setText("\u9009\u62E9");
		
		Label lb_MenuName = new Label(shell, SWT.NONE);
		lb_MenuName.setBounds(29, 115, 61, 17);
		lb_MenuName.setText("\u83DC\u5355\u540D\u79F0");
		
		final Label lb_Result = new Label(shell, SWT.NONE);
		lb_Result.setBounds(134, 278, 380, 40);
		
		txt_MenuName = new Text(shell, SWT.BORDER);
		txt_MenuName.setBounds(137, 112, 174, 23);
		
		Label lb_PCKG = new Label(shell, SWT.NONE);
		lb_PCKG.setBounds(29, 174, 77, 17);
		lb_PCKG.setText("\u63A5\u53E3/\u5B58\u50A8\u8FC7\u7A0B");
		
		txt_PCKG = new Text(shell, SWT.BORDER);
		txt_PCKG.setBounds(137, 171, 174, 23);
		
		Button btn_Enter = new Button(shell, SWT.NONE);
		btn_Enter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					lb_Result.setText("正在检查"+txt_MenuName.getText());
					boolean resFlag = new SearchPath().work(lb_FileName.getText(),txt_MenuName.getText(),txt_PCKG.getText());
					if(resFlag){
						lb_Result.setText("生成菜单路径："+"D:\\" +  txt_MenuName.getText() +".xls \n"
							+ " 关联文件清单在 D:\\"+ txt_MenuName.getText() + "关联文件.xls \n" );
					}
					else{
						lb_Result.setText("没有此菜单");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btn_Enter.setBounds(133, 343, 80, 27);
		btn_Enter.setText("\u786E\u5B9A");
		
		Button btn_Cancel = new Button(shell, SWT.NONE);
		btn_Cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btn_Cancel.setBounds(333, 343, 80, 27);
		btn_Cancel.setText("\u53D6\u6D88");	
		
		Label lb_tableName = new Label(shell, SWT.NONE);
		lb_tableName.setBounds(29, 243, 61, 17);
		lb_tableName.setText("数据库表名");
		
		txt_tableName = new Text(shell, SWT.BORDER);
		txt_tableName.setBounds(140, 240, 174, 23);
	}
}
