package com.icbc.devp.gui.entry;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;



import com.icbc.devp.gui.UiContext;
import com.icbc.devp.gui.action.ExpFilesDDLCheckAction;
import com.icbc.devp.gui.action.ExpRelStepAction;
import com.icbc.devp.gui.action.FindStepAction;
import com.icbc.devp.gui.action.UpdateFileTableRelAction;
import com.icbc.devp.gui.frame.MbdpFrame;
import com.icbc.devp.gui.pane.MbdpSplitPane;
import com.icbc.devp.gui.toolbar.ViewToolPane;
import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.util.EnvUtil;


public class Start {

	public static void main(String[] args){
		ConnectionManager dbManager = null;
		try{
			Log.initErrorLog();
			Log.initOPLog();
			File dir = new File(EnvUtil.getInstance().getRootPath(), "sql");
			if(!dir.isDirectory()){
				dir.mkdirs();
			}
			Font font = new Font("仿宋", Font.PLAIN, 15);
			Font picFont = new Font("宋体", Font.PLAIN, 14);
			Start.initGlobalFont(font, picFont);
			dbManager = new ConnectionManager();
			UiContext.getInstance().setDbManager(dbManager);
			
			MbdpFrame frame = new MbdpFrame("MBDP批量信息查询 V0.6");
			
			MbdpSplitPane sp = new MbdpSplitPane();
			sp.initParam(frame.getSize());

			ViewToolPane pane = new ViewToolPane(sp);
			
			frame.add(pane, BorderLayout.NORTH);
			frame.add(sp, BorderLayout.CENTER);
			
			// lxc
			frame.setJMenuBar(initMenu(sp));		
			
			frame.setVisible(true);
			
			
		}catch(Exception e){
			Log.getInstance().exception(e);
			e.printStackTrace();
		}finally{
			try{
				dbManager.closeDBConnection();
			}catch(Exception xx){
				Log.getInstance().exception(xx);
			}
		}
	}
	
	private static void initGlobalFont(Font font, Font picFont){
		FontUIResource fontRes = new FontUIResource(font);
		FontUIResource picRes  = new FontUIResource(picFont);
		Object key;
		Object value;
		for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
			key = keys.nextElement();
			value = UIManager.get(key);
			if(value instanceof FontUIResource){
				if("Panel.font".equals(key)){
					UIManager.put(key, picRes);
				}else{
					UIManager.put(key, fontRes);
				}
			}
		}
	}
	
	private static JMenuBar initMenu(MbdpSplitPane sp) {
		
		JMenuBar mb = new JMenuBar();
		JMenu searchMenu = new JMenu("查询");
		JMenu expMenu = new JMenu("导出");
		JMenu updateMenu = new JMenu("更新");

		mb.add(searchMenu);
		mb.add(expMenu);
		mb.add(updateMenu);
		JMenuItem findStep = new JMenuItem("查询访问表的步骤");
		findStep.addActionListener(new FindStepAction(sp));
		searchMenu.add(findStep);
		JMenuItem expRelStep = new JMenuItem("导出全量未配置关联关系的关联步骤");
		expRelStep.addActionListener(new ExpRelStepAction());
		expMenu.add(expRelStep);
		JMenuItem updateFileTableRel = new JMenuItem("更新导入文件和接口表对照关系");
		updateFileTableRel.addActionListener(new UpdateFileTableRelAction(sp));
		updateMenu.add(updateFileTableRel);
		
		JMenuItem expFilesDDLCheck = new JMenuItem("表结构变更与文件生成关联检查");
		expFilesDDLCheck.addActionListener(new ExpFilesDDLCheckAction(sp));
		expMenu.add(expFilesDDLCheck);
		
		return mb;
	}
}
