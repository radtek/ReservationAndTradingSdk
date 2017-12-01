package com.icbc.devp.gui.action;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.util.EnvUtil;

public class SaveFlowImageAction implements ActionListener {

	public SaveFlowImageAction(StepInfoPane panel){
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Dimension d = panel.getSize();
//		System.out.println(d.width+"-->"+d.height);
		BufferedImage img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		
//		Image imx;
		Graphics g = img.getGraphics();
		g.setColor(white);
		g.fillRect(0, 0, d.width, d.height);
		this.panel.drawImage(g);
		String filename;
		try{
			filename = this.panel.getInfoTree().getRoot().getInfo().getIdPrefix()+"image.jpg";
		}catch(Exception e1){
			Log.getInstance().exception(e1);
			filename = "image.jpg";
		}
		
//		System.out.println()
		File dir = new File(EnvUtil.getInstance().getRootPath(), "image");
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		try{
			ImageIO.write(img, "jpeg", new File(dir, filename));
			
		}catch(Exception xe){
			Log.getInstance().exception(xe);
		}
	}

	private StepInfoPane panel;
	private Color white = new Color(255,255,255);
}
