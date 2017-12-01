package com.icbc.devp.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicBorders;

import com.icbc.devp.gui.factory.ColorFactory;
import com.icbc.devp.gui.factory.IconFactory;
import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.gui.info.StepInfoTree;
import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.tool.date.DateUtil;
import com.icbc.devp.tool.file.FileUtil;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.txtdraw.base.Info;

public class StepDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StepDialog(StepInfoPane pane){
		cons = new GridBagConstraints();
		layout = new GridBagLayout();
		this.setLayout(layout);
		this.setSize(500, 400);
		this.init();
		this.setModal(true);
		this.pane = pane;
//		this.infoList = new ArrayList<StepInfo>();
	}
	
	public void setInfo(StepInfo info){
		this.info = info;
		this.msgLabel.setText("");
		Info stepinf = this.info.getInfo();
		this.setTitle(this.info.getInfo().getStepno()+"步骤信息");
		this.stepnoText.setText(stepinf.getStepno());
		this.typeText.setText(stepinf.getProcType());
		this.descText.setText(stepinf.getProcDesc());
		this.progText.setText(stepinf.getProc());
		this.parameterArea.setText(stepinf.getParameter());
		ArrayList<StepInfo> tmpList = info.getFList();
		String line="";
		for(int i=0; i<tmpList.size();i++){
			line += tmpList.get(i).getInfo().getStepno();
			if(i < (tmpList.size()+1)){
				line += "; ";
			}
		}
		this.fatherArea.setText(line);
		tmpList = info.getSList();
		line="";
		for(int i=0; i<tmpList.size();i++){
			line += tmpList.get(i).getInfo().getStepno();
			if(i < (tmpList.size()+1)){
				line += "; ";
			}
		}
		this.stepnoText.setEditable(this.info.isNewNode());
		this.sonArea.setText(line);
		this.enableBox.setSelected(info.isEnable());
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		this.msgLabel.setForeground(ColorFactory.getInstance().getColor("BLACK"));
		if(e.getSource() == this.saveBtn){
			ArrayList<String[]> fstepList;
			ArrayList<String[]> sstepList;
			ArrayList<StepInfo> fList = new ArrayList<StepInfo>();
			ArrayList<StepInfo> sList = new ArrayList<StepInfo>();
			
			if(this.info.isNewNode()){
				if(!this.info.getInfo().getStepno().equals(this.stepnoText.getText().trim())){
					this.pane.getInfoTree().remove(this.info.getId());
					this.info.getInfo().setStepno(this.stepnoText.getText().trim());
					this.pane.getInfoTree().setNode(this.info);
				}
			}
			if(!this.getStepinfo(this.fatherArea.getText(), fList)){
				return;
			}else{
				fstepList = this.info.compareList(fList, true);
			}
			if(!this.getStepinfo(this.sonArea.getText(), sList)){
				return;
			}else{
				sstepList = this.info.compareList(sList, false);
			}
			if(!this.writeRelSqlToDisk(fstepList)){
				this.msgLabel.setText("保存失败-1");
				return;
			}
			if(!this.writeRelSqlToDisk(sstepList)){
				this.msgLabel.setText("保存失败-2");
				return;
			}
			this.info.getInfo().setProc(this.progText.getText());
			this.info.getInfo().setProcDesc(this.descText.getText());
			this.info.getInfo().setProcType(this.typeText.getText());
			this.info.getInfo().setParameter(this.parameterArea.getText());
			this.info.getInfo().setStepno(this.stepnoText.getText());
			//更新
			this.info.setFList(fList);
			this.info.setSList(sList);
			int height = 1;
			for(int i=0;i<fList.size();i++){
				fList.get(i).addSonNode(this.info);
				if(fList.get(i).getHeight() > height){
					height = fList.get(i).getHeight();
				}
			}
			this.info.setHeight(height+1);
			for(int i=0;i<sList.size();i++){
				sList.get(i).addFahterNode(this.info);
			}
			this.info.repaintImage();
			this.pane.repaint();
			//保存动作，先判断下标记的那些步骤号是否都存在，不存在就要嗝屁了
			StepInfoTree tree = this.pane.getInfoTree();
			File file = new File(EnvUtil.getInstance().getRootPath(), "sql"+File.separator+"MBDP_B_STEPDEF_"+tree.getIdPrefix()+DateUtil.getCurrdate()+".sql");
			if(FileUtil.writeFileByMode(file.getAbsolutePath(), this.info.getInfo().getInsertSql(), true)){
				this.msgLabel.setText("保存成功!");
			}else{
				this.msgLabel.setForeground(ColorFactory.getInstance().getColor("RED"));
				this.msgLabel.setText("保存失败-3");
			}
			return;
		}
		if(e.getSource() == this.cancleBtn){
			//取消动作
			
			return;
		}
	}
	
	private boolean writeRelSqlToDisk(ArrayList<String[]> stepList){
		if(stepList == null || stepList.isEmpty()){
			return true;
		}
		ArrayList<String> lineList = new ArrayList<String>();
		String[] lines;
		String line;
		//添加处理的
		for(int i=0; i<stepList.size(); i++){
			lines = stepList.get(i);
			line = "DELETE FROM MBDP_B_STEPREL T WHERE T.APP='"+this.info.getInfo().getApp()
			       +"' AND T.CYCLE='"+this.info.getInfo().getCycle()+"' AND T.DATA_SOURCE='"+this.info.getInfo().getDataSource()
			       +"' AND T.STEPNO_P='"+lines[0]+"' AND T.STEPNO='"+lines[1]+"';";
			lineList.add(line);
			if("0".equals(lines[2])){
				line = "INSERT INTO MBDP_B_STEPREL(APP, CYCLE, DATA_SOURCE, STEPNO_P, STEPNO) VALUES('"
					   + this.info.getInfo().getApp() +"', '"+this.info.getInfo().getCycle()+"', '"+this.info.getInfo().getDataSource()+"', '"
					   + lines[0]+"', '" + lines[1] + "');";
				lineList.add(line);
			}
		}
		File file = new File(EnvUtil.getInstance().getRootPath(), "sql"+File.separator+"MBDP_B_STEPREL_"+this.pane.getInfoTree().getIdPrefix()+DateUtil.getCurrdate()+".sql");
		return FileUtil.writeFileByMode(file.getAbsolutePath(), lineList, true);
	}
	
	public void showInfo(int x,int y){
		this.setLocation(x, y);
		this.setVisible(true);
	}
	
	private void init(){
		this.stepnoLabel = new JLabel("步骤ID:");
		this.typeLabel   = new JLabel("构件类型:");
		this.progLabel   = new JLabel("程    序:");
		this.parameterLabel = new JLabel("入    参:");
		this.descLabel = new JLabel("程序描述:");
		this.fatherLabel = new JLabel("前继步骤:",JLabel.CENTER);
		this.sonLabel    = new JLabel("后继步骤:",JLabel.CENTER);
		
		this.stepnoLabel.setHorizontalAlignment(JLabel.CENTER);
		this.typeLabel.setHorizontalAlignment(JLabel.CENTER);
		this.progLabel.setHorizontalAlignment(JLabel.CENTER);
		this.parameterLabel.setHorizontalAlignment(JLabel.CENTER);
		this.descLabel.setHorizontalAlignment(JLabel.CENTER);
		//还有显示
		this.stepnoText = new JTextField();
		this.typeText   = new JTextField();
		this.progText   = new JTextField();
		this.parameterArea = new JTextArea();
		this.descText = new JTextField();

		
		this.fatherArea = new JTextArea();
		this.sonArea    = new JTextArea();
		
//		this.descText.setEditable(false);
//		this.stepnoText.setEditable(false);
//		this.typeText.setEditable(false);
//		this.progText.setEditable(false);
//		this.parameterArea.setEditable(false);
		this.parameterArea.setAutoscrolls(true);
		//开始
		this.parameterArea.setLineWrap(true);	//自动换行
		this.parameterArea.setWrapStyleWord(true);	//不断字
		this.fatherArea.setLineWrap(true);
		this.fatherArea.setWrapStyleWord(true);
		this.sonArea.setLineWrap(true);
		this.sonArea.setWrapStyleWord(true);
		this.sonArea.setBorder(BasicBorders.getTextFieldBorder());
		this.parameterArea.setBorder(BasicBorders.getTextFieldBorder());
		this.fatherArea.setBorder(BasicBorders.getTextFieldBorder());
		this.fatherArea.setRows(3);
		this.sonArea.setRows(3);
		this.parameterArea.setRows(4);
		this.sonArea.setAutoscrolls(true);
		this.fatherArea.setAutoscrolls(true);
		this.parameterArea.setAutoscrolls(true);
		this.sonJsp = new JScrollPane(this.sonArea);
		this.fatherJsp = new JScrollPane(this.fatherArea);
		this.paramJsp = new JScrollPane(this.parameterArea);
		//开始加进去
		cons.weightx = 100;
		cons.weighty = 100;
//		cons.gridwidth = 1;
		cons.gridx = 0;
		cons.gridy = 0;
		cons.fill = GridBagConstraints.BOTH;
		cons.anchor = GridBagConstraints.CENTER;
		this.layout.setConstraints(this.stepnoLabel, cons);
		cons.gridy = 1;
		this.layout.setConstraints(this.descLabel, cons);
		cons.gridy = 2;
		this.layout.setConstraints(this.typeLabel, cons);
		cons.gridy = 3;
		this.layout.setConstraints(this.progLabel, cons);
		cons.gridy = 4;
		this.layout.setConstraints(this.parameterLabel, cons);
		cons.gridy = 5;
		this.layout.setConstraints(this.fatherLabel, cons);
		cons.gridy = 6;
		this.layout.setConstraints(this.sonLabel, cons);
		//
		this.add(this.stepnoLabel);
		this.add(this.typeLabel);
		this.add(this.descLabel);
		this.add(this.progLabel);
		this.add(this.parameterLabel);
		this.add(this.fatherLabel);
		this.add(this.sonLabel);
		
		cons.gridx = 1;
		cons.gridy = 0;
		cons.gridwidth = 4;
		this.layout.setConstraints(this.stepnoText, cons);
		cons.gridy = 1;
		this.layout.setConstraints(this.descText, cons);
		cons.gridy = 2;
		this.layout.setConstraints(this.typeText, cons);
		cons.gridy = 3;
		this.layout.setConstraints(this.progText, cons);
		cons.gridy = 4;
		this.layout.setConstraints(this.paramJsp, cons);
		cons.gridy = 5;
		this.layout.setConstraints(this.fatherJsp, cons);
		cons.gridy = 6;
		this.layout.setConstraints(this.sonJsp, cons);
		
		this.add(this.stepnoText);
		this.add(this.typeText);
		this.add(this.descText);
		this.add(this.progText);
		this.add(this.paramJsp);
		this.add(this.fatherJsp);
		this.add(this.sonJsp);
		
		//加第5行
		this.enableBox = new JCheckBox("启用步骤");
		this.enableBox.setHorizontalAlignment(JCheckBox.LEFT);
		cons.gridx = 1;
		cons.gridy = 7;
		cons.gridwidth = 1;
		this.layout.setConstraints(this.enableBox, cons);
		this.add(this.enableBox);
		//按钮
		this.saveBtn = new JButton("保 存");
		this.cancleBtn = new JButton("取 消");
		this.saveBtn.setIcon(IconFactory.getInstance().getIcon("APPLY"));
		this.cancleBtn.setIcon(IconFactory.getInstance().getIcon("CROSS"));
		this.saveBtn.addActionListener(this);
		this.cancleBtn.addActionListener(this);
		this.msgLabel = new JLabel("  ");
//		this.emptyLabel = new JLabel("  ");
		cons.gridx = 2;
		this.layout.setConstraints(this.msgLabel, cons);
		this.add(msgLabel);
//		cons.gridx ++;
//		this.layout.setConstraints(this.emptyLabel, cons);
//		this.add(emptyLabel);
		
		cons.gridy ++;
		cons.gridwidth = 1;
		//最后两列
		cons.fill = GridBagConstraints.WEST;
//		cons.anchor = GridBagConstraints.CENTER;
		cons.gridx = 3;
		this.layout.setConstraints(this.saveBtn, cons);
		cons.gridx = 4;
		this.layout.setConstraints(this.cancleBtn, cons);
		this.add(this.saveBtn);
		this.add(this.cancleBtn);
	}
	
	//判断节点是否存在，如果存在保存在传入的列表中
	private boolean getStepinfo(String line,
			                    ArrayList<StepInfo> infoList){
		if(line == null || "".equals(line)){
			infoList.clear();
			return true;
		}
		String[] stepno = line.split(";");
		String id;
		StepInfo inf;
		infoList.clear();
		String str;
		StepInfoTree tree = this.pane.getInfoTree();
		for(int i=0; i<stepno.length; i++){
			str = stepno[i].trim();
			if("".equals(str)){
				continue;
			}
			id  = tree.getIdPrefix()+str;
			inf = tree.getNode(id);
			if(inf == null){
				this.msgLabel.setForeground(ColorFactory.getInstance().getColor("RED"));
				this.msgLabel.setText(str+"不存在！");
				return false;
			}
			infoList.add(inf);
		}
		return true;
	}
	
	private StepInfo info;
	private StepInfoPane pane;
	private GridBagLayout layout;
	private GridBagConstraints cons;
	//JLabel
	private JLabel typeLabel;
	private JLabel stepnoLabel;
	private JLabel progLabel;
	private JLabel parameterLabel;
	private JLabel descLabel;
	private JLabel fatherLabel;
	private JLabel sonLabel;
	//TextField
	private JTextField typeText;
	private JTextField stepnoText;
	private JTextField progText;
	private JTextArea parameterArea;
	private JTextField descText;
	private JLabel msgLabel;
	//父子节点放在这里
	private JTextArea fatherArea;
	private JTextArea sonArea;
	private JScrollPane sonJsp;
	private JScrollPane fatherJsp;
	private JScrollPane paramJsp;
	//勾选框
	private JCheckBox enableBox;
	//按钮
	private JButton saveBtn;
	private JButton cancleBtn;
//	private JButton clearBtn;	//清空缓存
	//对话框宽度
//	private int diaWidth = 400;
}
