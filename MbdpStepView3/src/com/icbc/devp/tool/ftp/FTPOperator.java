package com.icbc.devp.tool.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.icbc.devp.tool.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

/**
 * Apache commons-net ����һ�ѣ�����FTP�ͻ��˹������ĺ��ò�
 * 
 * @author : kfzx-yanyj��2015-8-31 14:00:38��
 * 
 * <�����䣬�жϷ������İ汾������ϵͳ������ 2016/08/23>
 *         <p>
 */
public class FTPOperator {

	private ArrayList<String> pathFiles = new ArrayList<String>();// �ļ����������ļ�
	PrintWriter fileOut;
	private FTPClient ftpClient;

	public FTPOperator(PrintWriter pw) {
		this.fileOut = pw;
	}

	/*public static void main(String[] args) throws Throwable {
		//new FTPOperator().FTPUpload("122.16.13.245", "mscbatch", "mscbatch","D:\\UDS_TEMP", "/u02/cbms/cbmsbatch/log/");
		new FTPOperator().FTPDownload("107.252.78.128", "udsn", "Qwer1234",
				"D:\\UDS_TEMP", "/app7/F-CBMS/target/MSC/FPM/008/20240920/");
	}*/

	public FTPOperator() {
		try {
			fileOut = new PrintWriter("log" + File.separator + "FTPFileTransfer.log");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �ݹ���������ļ�
	 * 
	 * @param fileName
	 */
	public void getPathFile(String fileName) {
		File f = new File(fileName);
		if (f.isDirectory()) {
			File[] fileList = f.listFiles();
			if (fileList.length > 0) {
				for (File file : fileList) {
					if (file.isDirectory()) {
						getPathFile(file.getAbsolutePath());
					} else {
						// System.out.println(file.getAbsolutePath());
						pathFiles.add(file.getAbsolutePath());
					}
				}
			}
		} else {
			pathFiles.add(fileName);
		}
	}


	/**
	 * ���ر����ļ���
	 * 
	 * @param fileName
	 * @return
	 */
	public String getFileName(String fileName) {
		return fileName.substring(fileName.lastIndexOf('\\') + 1,
				fileName.length());
	}
	/**
	 * ���ط������ļ���
	 * @param fileName
	 * @return
	 */
	public String getServerFileName(String fileName){
		return fileName.substring(fileName.lastIndexOf('/') + 1,
				fileName.length());
	}

	public String setDir(String dir){
		System.out.println(dir.charAt(dir.length()-1));
		if (!dir.endsWith(File.separator))
			return dir.concat(File.separator);
		else return dir;
	}
	/**
	 * FTP�ϴ������ļ�����
	 * 
	 * @throws Exception
	 */
	public void FTPUpload(String ip, String username, String pwd,
			String srcDirs, String remoteDir) throws Exception {
		FTPClient ftpClient = new FTPClient();
		pathFiles = new ArrayList<String>();
		FileInputStream fis = null;
		File srcFile;
		try {
			ftpClient.connect(ip);
			ftpClient.login(username, pwd);
			getPathFile(srcDirs);
			for (String srcDir : pathFiles) {
				srcFile = new File(srcDir);
				fis = new FileInputStream(srcFile);
				// �����ϴ�Ŀ¼
				ftpClient.changeWorkingDirectory(remoteDir);
				fileOut.write(ftpClient.getReplyString()+"\n");
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("GBK");
				// �����ļ����ͣ������ƣ�
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				fileOut.write(ftpClient.getReplyString()+"\n");
				ftpClient.storeFile(getFileName(srcDir), fis);
				fileOut.write(ftpClient.getReplyString()+"\n");
				System.out.println(ftpClient.getReplyString());
				fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fileOut.write(e.getMessage() + "\n");
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				fileOut.write(e.getMessage() + "\n");
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
	}

	public boolean login(String ip, String username, String pwd) {
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(ip);
			return ftpClient.login(username, pwd);
		} catch (Exception e) {
			fileOut.write("����ʧ��!\nIP:" + ip + "\n�û���:" + username + "\n����:" + pwd + "\n" + e.getMessage() + "\n");
			return false;
		}
	}
	
	/**
	 * FTP���ص����ļ�����
	 * 
	 * @throws Throwable
	 */
	public boolean FTPDownload(String remoteDirs, String srcDirs) {
		
		FileOutputStream fos = null;

		try {
			/*for (FTPFile f : ftpClient.listFiles(remoteDirs)) {
                System.out.println(f.getRawListing());
                //System.out.println(f.toFormattedString());
            }*/
//			ftpClient
			for (String remoteFileName : ftpClient.listNames(remoteDirs)) {
				ftpClient.setBufferSize(1024);
				// �����ļ����ͣ������ƣ�
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				System.out.println(setDir(srcDirs)+getServerFileName(remoteFileName));
				fos = new FileOutputStream(setDir(srcDirs)+getServerFileName(remoteFileName));
				ftpClient.retrieveFile(remoteFileName, fos);
				System.out.println(ftpClient.getStatus());
				fos.close();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			fileOut.write("�����ļ�ʧ�ܣ�\n" + e.getMessage() + "\n");
//			throw new RuntimeException("FTP�ͻ��˳���", e);
			return false;
		}
	}
	
	/**
	 * ����remoteDirs·���µ������ļ���������Ŀ¼�µ��ļ��� 
	 * @param remoteDirs Դ�ļ�·��
	 * @param srcDirs ���ش��·��
	 * @return ���سɹ�����true�����򷵻�false
	 */
	public boolean downloadAll(String remoteDirs, String srcDirs) {
		FileOutputStream fos = null;
		
		try {
			for (FTPFile remoteFile : ftpClient.listFiles(remoteDirs)) {
				ftpClient.setBufferSize(1024);
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				String nremoteDir = remoteDirs + "/" + remoteFile.getName();
				String nsrcDir = srcDirs + "/"  + remoteFile.getName();
				System.out.println("current file = " + nsrcDir);
				if (nsrcDir.equals("result\tmp/cbms/day/ccrm/C_CCRMSCD137.xml")) {
					System.out.println("current file = " + nsrcDir);
				}
				if (remoteFile.isFile()) {
					fos = new FileOutputStream(nsrcDir);
					ftpClient.retrieveFile(nremoteDir, fos);
					fos.close();
				} else if (remoteFile.isDirectory()) {
					String fname = remoteFile.getName();
					System.out.println(fname);
					File dir = new File(nsrcDir);
					dir.mkdirs();
					downloadAll(nremoteDir, nsrcDir);
				}
				System.out.println(ftpClient.getStatus());
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fileOut.write("�����ļ�ʧ�ܣ�\n" + e.getMessage() + "\n");
			return false;
		}
	}

	public void logoff() {
		try {
			ftpClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			fileOut.write(e.getMessage() + "\n");
			throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
		}
	}
	
	public boolean isDirectoryExist(String pathname) {
		try {
			return ftpClient.changeWorkingDirectory(pathname);
		} catch (IOException e) {
				e.printStackTrace();
				fileOut.write(e.getMessage() + "\n");
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
	
	public static void main(String[] args) throws Exception {
//		System.out.println(File.pathSeparator);
//		System.out.println(File.pathSeparatorChar);
//		System.out.println(File.separator);
//		System.out.println(File.separatorChar);
		FTPOperator ftpOper = new FTPOperator();
		System.out.println(ftpOper.login("122.16.93.53", "mscbatch", "mscbatch"));
//		System.out.println(ftpOper.isDirectoryExist("1234"));
		ftpOper.downloadAll("/u02/cbms/cbmsbatch/config/imp_config/cbms/", "tmp");
//		ftpOper.FTPDownload("/u02/cbms/cbmsbatch/config/imp_config/cbms/day/aam/", "tmp");
		ftpOper.logoff();
	}
}