package com.icbc.devp.tool.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.icbc.devp.tool.date.DateUtil;
import com.icbc.devp.tool.util.EnvUtil;



/**
 * ��־
 * Ԥ����
 * ��顢���뵽�����ļ�
 * ���������������������
 * @author kfzx-wangch01
 *
 */
public final class Log {

	public static final  Logger opLog = Logger.getLogger("MBDPVIEW-OP");	
	public static final  Logger errLog = Logger.getLogger("MBDPVIEW-ERR");
	/**
	 * Singleton instance.
	 */
	private static final Log INSTANCE = new Log();
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * Utility Constructor.
	 */
	private Log() {
		super();
	}

	/**
	 * Get the singleton instance.
	 * @return  Singleton.
	 */
	public static Log getInstance() {
		return INSTANCE;
	}
	

	/**
	 * ��ʼ��������־
	 * @param path
	 * @param todo
	 * @param date
	 * @throws SecurityException
	 * @throws IOException
	 */
	public static void initOPLog() throws SecurityException, IOException {
			Handler[] handlers = opLog.getHandlers();
			for(int i=0;i<handlers.length;i++)
			{
				handlers[i].flush();
				handlers[i].close();
			}
			String path = EnvUtil.getInstance().getRootPath() + File.separator + "log";
			String todo = "Oper";
			String date = DateUtil.getCurrdate("yyyyMMdd");
			File logdir = new File(path);
			File f = new File(path, todo+date+".log");
			if (!logdir.exists()) logdir.mkdirs();
			FileHandler fh  = new FileHandler(f.getCanonicalPath(),true);
			fh.setFormatter(new SimpleFormatter());
			opLog.addHandler(fh);
			opLog.setUseParentHandlers(false);
	}
	
	/**
	 * ��ʼ��������־
	 * @param path
	 * @param todo
	 * @param date
	 * @throws SecurityException
	 * @throws IOException
	 */
	public static void initErrorLog() throws SecurityException, IOException {
		Handler[] handlers = errLog.getHandlers();
		for(int i=0;i<handlers.length;i++)
		{
			handlers[i].flush();
			handlers[i].close();
		}
		String path = EnvUtil.getInstance().getRootPath() + File.separator + "log";
		File logdir = new File(path);
		String todo = "error";
		String date = DateUtil.getCurrdate("yyyyMMdd");;
		File f = new File(path, todo+date+".err");
		if (!logdir.exists()) logdir.mkdirs();
		FileHandler fh =  new FileHandler(f.getCanonicalPath(),true);
		fh.setFormatter(new SimpleFormatter());
		errLog.addHandler(fh);
		errLog.setUseParentHandlers(false);
	}

	/**
	 * ���ݴ����err�ļ���
	 * Log a error msg.
	 * @param msg   Message.
	 */
	public void error(String msg) {
		errLog.log(Level.SEVERE, msg);
	}

	/**
	 * ���ݴ����log�ļ���
	 * Log an info msg.
	 * @param msg   Message.
	 */
	public void info(String msg) {
		opLog.log(Level.INFO, msg);
	}
	
	/**��Exception��¼����*/
	public void exception(Exception e){
		StackTraceElement[] traces = e.getStackTrace();
		String line = "";
		for(int i=0; i<traces.length; i++){
			line += traces[i].toString()+"\r\n";
		}
		errLog.log(Level.SEVERE, line);
	}
	/**TODO:��¼��־��ͬʱ�������Ļ*/
	public void errorAndLog(String msg){
		String dateStr = format.format(new Date());
		String errmsg = "["+ dateStr + "][ERROR]"+msg;
		System.out.println(errmsg);
		errLog.log(Level.WARNING, errmsg);
	}
	/**
	 * ���ݴ����err�ļ���
	 * Log an exception msg.
	 * @param e Exception.
	 */
	public void throwing(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		errLog.log(Level.WARNING, sw.toString());
	}	
		
}
