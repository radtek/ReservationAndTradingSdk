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
			Font font = new Font("����", Font.PLAIN, 15);
			Font picFont = new Font("����", Font.PLAIN, 14);
			Start.initGlobalFont(font, picFont);
			dbManager = new ConnectionManager();
			UiContext.getInstance().setDbManager(dbManager);
			
			MbdpFrame frame = new MbdpFrame("MBDP������Ϣ��ѯ V0.6");
			
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
		JMenu searchMenu = new JMenu("��ѯ");
		JMenu expMenu = new JMenu("����");
		JMenu updateMenu = new JMenu("����");

		mb.add(searchMenu);
		mb.add(expMenu);
		mb.add(updateMenu);
		JMenuItem findStep = new JMenuItem("��ѯ���ʱ�Ĳ���");
		findStep.addActionListener(new FindStepAction(sp));
		searchMenu.add(findStep);
		JMenuItem expRelStep = new JMenuItem("����ȫ��δ���ù�����ϵ�Ĺ�������");
		expRelStep.addActionListener(new ExpRelStepAction());
		expMenu.add(expRelStep);
		JMenuItem updateFileTableRel = new JMenuItem("���µ����ļ��ͽӿڱ���չ�ϵ");
		updateFileTableRel.addActionListener(new UpdateFileTableRelAction(sp));
		updateMenu.add(updateFileTableRel);
		
		JMenuItem expFilesDDLCheck = new JMenuItem("��ṹ������ļ����ɹ������");
		expFilesDDLCheck.addActionListener(new ExpFilesDDLCheckAction(sp));
		expMenu.add(expFilesDDLCheck);
		
		return mb;
	}
}
