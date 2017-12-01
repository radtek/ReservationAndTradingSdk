package com.icbc.devp.gui.alertmsg;

import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.icbc.devp.gui.factory.ColorFactory;

public class AlertBoard extends JDialog {

	public AlertBoard(){
		super();
		this.setTitle("警告");
		this.setModal(true);
		this.setVisible(false);
		msgLabel = new JLabel();
		msgLabel.setForeground(ColorFactory.getInstance().getColor("RED"));
		this.setLayout(new FlowLayout());
		this.add(msgLabel);
		this.setSize(300,100);
	}
	
	private static AlertBoard _instance = new AlertBoard();
	
	public static AlertBoard getInstance(){
		return _instance;
	}
	
	public void showAlertMsg(String msg, int xPos, int yPos){
		this.setLocation(xPos, yPos);
		//弹出消息
		String line = "<html><body>" + msg + "</body></html>";
		msgLabel.setText(line);
		this.setVisible(true);
	}
	
	private JLabel msgLabel;
}
