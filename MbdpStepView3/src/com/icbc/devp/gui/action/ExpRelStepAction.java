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
				JOptionPane.showMessageDialog(null,"���Ƚ�������","",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return;
			}
		}
		File rfile = new File("result" + File.separator + "�����ϵ�����.csv");
		if (exp == 0) {
			if (rfile.exists()) {
				int op = JOptionPane.showConfirmDialog(null, "��result\\�����ϵ�����.csv���Ѵ��ڣ�ȷ��Ҫ����������", "ȷ��",JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.NO_OPTION) {
					return;
				}
			}
			Thread expRelStep = new Thread(new ExpRelStepThread());
			expRelStep.start();
			JOptionPane.showMessageDialog(null,"��̨��ʼ�����������裬���Ժ��ڡ�result\\�����ϵ�����.csv���鿴�������","",JOptionPane.INFORMATION_MESSAGE);
		} else if (exp == 1) {
			JOptionPane.showMessageDialog(null,"��̨���ڼ����������裬�Ѽ�����" + curr + "/" + totalNum + "�����裬���Ժ��ڡ�result\\�����ϵ�����.csv���鿴�������","",JOptionPane.INFORMATION_MESSAGE);
		} else if (exp == 2) {
			JOptionPane.showMessageDialog(null,"���������Ѽ�����ϣ����ڡ�result\\�����ϵ�����.csv���鿴�������","",JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	
	
	class ExpRelStepThread implements Runnable {

		@Override
		public void run() {
			
			exp = 1;
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter("result" + File.separator + "�����ϵ�����.csv"));
				writer.write("�����,,�������裨����ţ�������");
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
					writer.write(stepno+",ͬ����Դ");
					if (rlist.get(0).size() == 0) {
						writer.write(",��");
					} else {
						for (int j = 0; j < rlist.get(0).size(); ++j) {
							stepBean = rlist.get(0).get(j);
							writer.write("," + stepBean.getStepno() + "��" + stepBean.getTbname());
						}
					}
					writer.newLine();
					writer.write(",��������Դ");
					if (rlist.get(1).size() == 0) {
						writer.write(",��");
					} else {
						for (int j = 0; j < rlist.get(1).size(); ++j) {
							stepBean = rlist.get(1).get(j);
							writer.write("," + stepBean.getStepno() + "��" + stepBean.getTbname());
						}
					}
					writer.newLine();
					writer.flush();
				}
				writer.close();

			} catch (Exception e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			exp = 2;
			
		}
		
	}

}
