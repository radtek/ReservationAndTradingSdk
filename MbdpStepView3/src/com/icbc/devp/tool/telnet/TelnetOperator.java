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
 * Telnet������,����commons-net-2.2.jar
 * @author kfzx-yanyj
 *
 */
public class TelnetOperator {
	
	private String prompt = ">";	//������ʶ�ַ���,Windows����>,Linux����#
	private char promptChar = '>';	//������ʶ�ַ�
	private String serverPrompt = "$";
	private TelnetClient telnet;
	private InputStream in;		// ������,���շ�����Ϣ
	private PrintStream out;	// �������д�� ����
	PrintWriter fileOut;
	
	/*public static void main(String[] args){
		TelnetOperator batchServer = new TelnetOperator();
		if (batchServer.login("122.16.93.53", 23, "mscbatch", "mscbatch")) {
			batchServer.writeCMD("cd /u02/cbms/batch/config");
			System.out.println(batchServer.readUntil(batchServer.serverPrompt));
			batchServer.writeCMD("ls");
			System.out.println(batchServer.readUntil(batchServer.serverPrompt));
		} else {
			System.out.println("��¼ʧ�ܡ�����");
		}
		
		
	}*/
	
	/**
	 * @param termtype	Э�����ͣ�VT100��VT52��VT220��VTNT��ANSI
	 * @param prompt	���������ʶ
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
	 * ��¼��Ŀ������
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
//				throw new RuntimeException("��¼ʧ��");
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ��ȡ�������
	 * 
	 * @param pattern	ƥ�䵽���ַ���ʱ���ؽ��
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
				
				//ƥ�䵽������ʶʱ���ؽ��
				if (flag) {
					if (ch == lastChar && sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}else{
					//���ûָ��������ʶ,ƥ�䵽Ĭ�Ͻ�����ʶ�ַ�ʱ���ؽ��
					if(ch == promptChar){
						String rs = new String(sb.toString().getBytes("ISO-8859-1"),"GBK");
					    System.out.print(rs);
						return sb.toString();
					}
				}
				//��¼ʧ��ʱ���ؽ��
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
	 * ����̨��������
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
	 * ��������,����ִ�н��
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
	 * ��������,����ִ�н��
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
	 * ����FTP�������ִ�н��
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
	 * �ر�����
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
