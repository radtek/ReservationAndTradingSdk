package com.icbc.devp.tool.telnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Calendar;

import org.apache.commons.net.telnet.TelnetClient;



/**
 * Telnet操作器,基于commons-net-2.2.jar
 * @author kfzx-yanyj
 *
 */
public class TelnetOperator {
	
	private String prompt = ">";	//结束标识字符串,Windows中是>,Linux中是#
	private char promptChar = '>';	//结束标识字符
	private String serverPrompt = "$";
	private TelnetClient telnet;
	private InputStream in;		// 输入流,接收返回信息
	private PrintStream out;	// 向服务器写入 命令
	PrintWriter fileOut;
	
	/*public static void main(String[] args){
		TelnetOperator batchServer = new TelnetOperator();
		if (batchServer.login("122.16.93.53", 23, "mscbatch", "mscbatch")) {
			batchServer.writeCMD("cd /u02/cbms/batch/config");
			System.out.println(batchServer.readUntil(batchServer.serverPrompt));
			batchServer.writeCMD("ls");
			System.out.println(batchServer.readUntil(batchServer.serverPrompt));
		} else {
			System.out.println("登录失败。。。");
		}
		
		
	}*/
	
	/**
	 * @param termtype	协议类型：VT100、VT52、VT220、VTNT、ANSI
	 * @param prompt	结果结束标识
	 */
	public TelnetOperator(String termtype,String prompt,PrintWriter pw){
		telnet = new TelnetClient(termtype);
		setPrompt(prompt);
		Calendar d =  Calendar.getInstance();
		this.fileOut = pw;
	}
	/*
	 public TelnetOperator(String termtype,String type,String cur){
		telnet = new TelnetClient(termtype);
	}
	*/
	public TelnetOperator(String termtype){
		telnet = new TelnetClient(termtype);
		try {
			fileOut = new PrintWriter("D:\\" + "FTPFileTransfer.log");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TelnetOperator(){
		telnet = new TelnetClient();
		try {
			fileOut = new PrintWriter("log" + File.separator + "FTPFileTransfer.log");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void login(String ip , int port){
		try{
			telnet.connect(ip,port);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * 登录到目标主机
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 */
	public boolean login(String ip, int port, String username, String password){
		try {
			telnet.connect(ip, port);
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());
			String ts = readUntil("login:");
			System.out.println(ts);
			fileOut.write(ts+"\r\n");
			writeCMD(username);
			ts = readUntil("Password:");
			System.out.println(ts);
			fileOut.write(ts + "\r\n");
			writeCMD(password);
			String HOST_IP = InetAddress.getLocalHost().toString();
			//int idx = HOST_IP.indexOf('/');
			String rs = new String(readUntil("Last login:").getBytes("ISO-8859-1"),"GBK");
			System.out.println(rs);
			fileOut.write(rs+"\r\n");
			if (username.indexOf("uds")!=-1){
				System.out.println(readUntil("]"));
			}
			else{
			  System.out.println(readUntil(serverPrompt));
			}
			if(rs!=null&&rs.contains("Login Failed")){
//				throw new RuntimeException("登录失败");
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 读取分析结果
	 * 
	 * @param pattern	匹配到该字符串时返回结果
	 * @return
	 */
	public String readUntil(String pattern) {
		StringBuffer sb = new StringBuffer();
//		System.out.println("pattern="+pattern);
		try {
			char lastChar = (char)-1;
			boolean flag = pattern!=null&&pattern.length()>0;
			if(flag)
				lastChar = pattern.charAt(pattern.length() - 1);
			char ch;
			int code = -1;
			while ((code = in.read()) != -1) {
				ch = (char)code;
				sb.append(ch);
				
				//匹配到结束标识时返回结果
				if (flag) {
					if (ch == lastChar && sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}else{
					//如果没指定结束标识,匹配到默认结束标识字符时返回结果
					if(ch == promptChar){
						String rs = new String(sb.toString().getBytes("ISO-8859-1"),"GBK");
					    System.out.print(rs);
						return sb.toString();
					}
				}
				//登录失败时返回结果
				if(sb.toString().contains("Login Failed")){
					String rs = new String(sb.toString().getBytes("ISO-8859-1"),"GBK");
					System.out.print(rs);
					return sb.toString();
				}
				
				if(sb.toString().lastIndexOf(pattern)!=-1){
					String rs = new String(sb.toString().getBytes("ISO-8859-1"),"GBK");
					System.out.print(rs);
				    return sb.toString();
				}
				//String rs = new String(sb.toString().getBytes("ISO-8859-1"),"GBK");
				//System.out.print(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 控制台输入命令
	 * 
	 * @param value
	 */
	public void writeCMD(String value) {
		try {
			out.println(value);
			System.out.println(value);
			fileOut.write(value+"\r\n");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发送命令,返回执行结果
	 * 
	 * @param command
	 * @return
	 */
	public String sendUDSNCommand(String command) {
		try {
			writeCMD(command);
			String rs = readUntil("]");
			rs = new String(rs.getBytes("ISO-8859-1"),"GBK");
			System.out.println(rs);
			fileOut.write(rs+"\r\n");
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 发送命令,返回执行结果
	 * 
	 * @param command
	 * @return
	 */
	public String sendCommand(String command) {
		try {
			writeCMD(command);
			String rs = readUntil(serverPrompt);
			rs = new String(rs.getBytes("ISO-8859-1"),"GBK");
			System.out.println(rs);
			fileOut.write(rs+"\r\n");
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送FTP命令，返回执行结果
	 * @param command
	 * @return
	 */
	
	public String sendFTPCommand(String command){
		try{
			writeCMD(command);
			String rs = readUntil(prompt);
			rs = new String(rs.getBytes("ISO-8859-1"),"GBK");
			System.out.println(rs);
			fileOut.write(rs+"\r\n");
			return rs;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 关闭连接
	 */
	public void distinct(){
		try {
			if (out != null)
				out.close();
			if (in != null)
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if(telnet!=null&&!telnet.isConnected())
				telnet.disconnect();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPrompt(String prompt) {
		if(prompt!=null){
			this.prompt = prompt;
			this.promptChar = prompt.charAt(prompt.length()-1);
		}
	}
	
	
	
}
