/**
 * 
 */
package com.icbc.devp.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.icbc.devp.relstep.RelStep;
import com.icbc.devp.relstep.RelStepBean;

/**
 * @author kfzx-liangxch
 *
 */
public class ExpRelStepAction implements ActionListener {

	private int exp = 0;
	private RelStep relStep = null;
	private int totalNum = 0;
	private int curr = 0;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (relStep == null) {
			try {
				relStep = new RelStep();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"请先建立连接","",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return;
			}
		}
		File rfile = new File("result" + File.separator + "步骤关系检查结果.csv");
		if (exp == 0) {
			if (rfile.exists()) {
				int op = JOptionPane.showConfirmDialog(null, "“result\\步骤关系检查结果.csv”已存在，确认要重新生成吗？", "确认",JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.NO_OPTION) {
					return;
				}
			}
			Thread expRelStep = new Thread(new ExpRelStepThread());
			expRelStep.start();
			JOptionPane.showMessageDialog(null,"后台开始检索关联步骤，请稍后在“result\\步骤关系检查结果.csv”查看检索结果","",JOptionPane.INFORMATION_MESSAGE);
		} else if (exp == 1) {
			JOptionPane.showMessageDialog(null,"后台正在检索关联步骤，已检索完" + curr + "/" + totalNum + "个步骤，请稍后在“result\\步骤关系检查结果.csv”查看检索结果","",JOptionPane.INFORMATION_MESSAGE);
		} else if (exp == 2) {
			JOptionPane.showMessageDialog(null,"关联步骤已检索完毕，请在“result\\步骤关系检查结果.csv”查看检索结果","",JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	
	
	class ExpRelStepThread implements Runnable {

		@Override
		public void run() {
			
			exp = 1;
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter("result" + File.separator + "步骤关系检查结果.csv"));
				writer.write("步骤号,,关联步骤（步骤号，关联表）");
				writer.newLine();
				ArrayList<String> stepList = relStep.getAllStep();
				String stepno = null;
				ArrayList<ArrayList<RelStepBean>> rlist = null;
				RelStepBean stepBean = null;
				totalNum = stepList.size();
				curr = 0;
				for (curr = 0; curr < totalNum; ++curr) {
					stepno = stepList.get(curr);
					rlist = relStep.relStepFilteredSep(stepno);
					if (rlist == null) {
						continue;
					}
					if (rlist.get(0).size() == 0 && rlist.get(1).size() == 0) {
						continue;
					}
					writer.write(stepno+",同数据源");
					if (rlist.get(0).size() == 0) {
						writer.write(",无");
					} else {
						for (int j = 0; j < rlist.get(0).size(); ++j) {
							stepBean = rlist.get(0).get(j);
							writer.write("," + stepBean.getStepno() + "，" + stepBean.getTbname());
						}
					}
					writer.newLine();
					writer.write(",其他数据源");
					if (rlist.get(1).size() == 0) {
						writer.write(",无");
					} else {
						for (int j = 0; j < rlist.get(1).size(); ++j) {
							stepBean = rlist.get(1).get(j);
							writer.write("," + stepBean.getStepno() + "，" + stepBean.getTbname());
						}
					}
					writer.newLine();
					writer.flush();
				}
				writer.close();

			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			exp = 2;
			
		}
		
	}

}
