package com.icbc.devp.gui.frame;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class MbdpFrame extends JFrame {

	public MbdpFrame(){
		this.initFrame();
	}
	
	public MbdpFrame(String title){
		this.initFrame();
		this.setTitle(title);
	}
	
	
	//初始化框架大小，退出方式等
	private void initFrame(){
		Insets inset = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		Dimension d  = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.frameSize = new Dimension((d.width - inset.left - inset.right)*3/5, (d.height - inset.top - inset.bottom)*3/5);
		this.setSize(this.frameSize);
		
		location = new Point((d.width - inset.left - inset.right)/5, (d.height - inset.top - inset.bottom)/5);
		this.setLocation(this.location);
		
		
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	private static final long serialVersionUID = 20140831L;
	
	//窗口大小
	private Dimension frameSize;
	private Point location;	//窗口起始位置

}
