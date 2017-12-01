package com.icbc.devp.tool.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.icbc.devp.tool.log.Log;

public class FileUtil {
	public FileUtil(){
		
	}
	/**TODO:读取CSV文件，并返回数组列表，每个元素是一个数组，就是通过分割逗号之后的内容
	 * @param String filePath
	 * csv文件的绝对路径*/
	public ArrayList<String[]> readCsvFile(String filePath)
	   throws Exception{
		File file = new File(filePath);
		if(!file.isFile()){
			throw new Exception("[FileUtil.readCsvFile]文件"+filePath+"不存在！");
		}
		//读取文件开始
		ArrayList<String[]> list = new ArrayList<String[]>();
		String[] array;
		String dataLine;
		LineNumberReader reader = new LineNumberReader(new FileReader(file));
		while((dataLine = reader.readLine())!=null){
			array = dataLine.split(",");
			list.add(array);
		}
		reader.close();
		return list;
	}
	
	/**TODO:读取文件，将每一行都返回，返回时一个列表，按顺序读取*/
	public static ArrayList<String> readFile(String filePath)
	  throws Exception{
		File file = new File(filePath);
		if(!file.isFile()){
			throw new Exception("[FileUtil.readFile]文件"+filePath+"不存在！");
		}
		//读取文件开始
		ArrayList<String> list = new ArrayList<String>();
		String dataLine;
		LineNumberReader reader = new LineNumberReader(new FileReader(file));
		while((dataLine = reader.readLine())!=null){
			list.add(dataLine);
		}
		reader.close();
		return list;
	}

	/**TODO:读取文件，将每一行都返回，返回时一个列表，按顺序读取*/
	public static ArrayList<String> readUtfFile(String filePath)
	  throws Exception{
		File file = new File(filePath);
		if(!file.isFile()){
			throw new Exception("[FileUtil.readFile]文件"+filePath+"不存在！");
		}
		//读取文件开始
		ArrayList<String> list = new ArrayList<String>();
		String dataLine;
		FileInputStream inputStream = null;
		InputStreamReader inReader = null;
		BufferedReader bufreader = null;
		try{
			inputStream = new FileInputStream(filePath);
			inReader = new InputStreamReader(inputStream, "UTF-8");
			bufreader = new BufferedReader(inReader);
			while((dataLine = bufreader.readLine())!=null){
				list.add(dataLine);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(bufreader != null){
				bufreader.close();
			}
			if(inReader != null){
				inReader.close();
			}
			if(inputStream != null){
				inputStream.close();
			}
		}
		///////////////////////////
//		LineNumberReader reader = new LineNumberReader(new FileReader(file));

//		reader.close();
		return list;
	}
	/**TODO:写文件，将传入的列表写入到文件中，列表中每一个元素将被视为一行。
	 * @param String filePath
	 * 目标文件的绝对路径。
	 * @param boolean append
	 * true-增量更新，false-全量更新。
	 * @return 成功写入文件返回true，否则返回false。*/
	public static boolean writeFile(String filePath, boolean append,
			                        ArrayList<String> lineList){
		String tag = "[FileUtil.writeFile]";
		String errinfo;
		BufferedOutputStream out = null;
		byte[] bytes;
		String line;
		try{
			out = new BufferedOutputStream(new FileOutputStream(filePath, append));
			if(lineList == null){
				//为空直接返回true
				return true;
			}
			//不为空，写入
			for(int i=0;i<lineList.size();i++){
				line = lineList.get(i) + "\r\n";
				bytes = line.getBytes();
				out.write(bytes);
				out.flush();
			}
			return true;
		}catch(Exception e){
			errinfo = tag + "Exception:写入文件\""+filePath+"\"过程发生异常："+ e.getMessage();
			Log.getInstance().error(errinfo);
			Log.getInstance().exception(e);
			return false;
		}finally{
			//关闭文件
			if(out != null){
				try{
					out.close();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}
	}

	/**TODO:写文件，将传入的列表写入到文件中，列表中每一个元素将被视为一行。
	 * @param String filePath
	 * 目标文件的绝对路径。
	 * @return 成功写入文件返回true，否则返回false。*/
	public static boolean writeUtfFile(String filePath,
			                           ArrayList<String> lineList){
		String tag = "[FileUtil.writeFile]";
		String errinfo;
		FileOutputStream out = null;
		PrintWriter fileWriter = null;
		BufferedWriter writer = null;
		
//		byte[] bytes;
		String line;
		try{
//			out = new BufferedOutputStream(new FileOutputStream(filePath));
			out = new FileOutputStream(new File (filePath));
			fileWriter = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
			writer = new BufferedWriter(fileWriter);
			if(lineList == null){
				//为空直接返回true
				return true;
			}
			//不为空，写入
			for(int i=0;i<lineList.size();i++){
				line = lineList.get(i) + "\r\n";
//				bytes = line.getBytes();
				writer.write(line);
				writer.flush();
			}
			return true;
		}catch(Exception e){
			errinfo = tag + "Exception:写入文件\""+filePath+"\"过程发生异常："+ e.getMessage();
			Log.getInstance().error(errinfo);
			Log.getInstance().exception(e);
			return false;
		}finally{
			//关闭文件
			if(out != null){
				try{
					writer.close();
					fileWriter.close();
					out.close();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}
	}
	
	/**TODO:扫描指定目录中指定后缀的文件，返回文件的绝对路径。
	 * @param String filePath
	 * 要扫描的目录
	 * @param String lastName
	 * 文件名后缀*/
	public static ArrayList<String> scanFiles(String filePath,
			                                  String lastName)
        throws Exception{
		String msg;
		String tag = "[FileUtil.scanFiles]";
		try{
			ArrayList<String> fileList = new ArrayList<String>();
			File dir = new File(filePath);
			ArrayList<String> tmpList;
			if(dir.isFile()){
				if(filePath.endsWith(lastName)){
					fileList.add(filePath);
				}
				return fileList;
			}else{
				File[] files = dir.listFiles();
				//循环读取
				for(int i=0; i<files.length; i++){
					if(files[i].isFile()){
						if(files[i].getAbsolutePath().endsWith(lastName)){
							fileList.add(files[i].getAbsolutePath());
						}
					}else{
						tmpList = scanFiles(files[i].getAbsolutePath(),lastName);
						fileList.addAll(tmpList);
					}
				}
			}
			return fileList;
		}catch(Exception e){
			msg = tag + "[Exception]" + e.getMessage();
			System.out.println(msg);
			throw new Exception(e);
		}
	}
	
	/**TODO:追加内容到文件里面*/
	public static boolean writeFileByMode(String filePath,
			                              ArrayList<String> lineList,
			                              boolean isAppend){
//		String msg = "";
//		String tag = "[FileUtil.writeFileByMode]";
		FileWriter writer = null;
		try{
			if(lineList == null || lineList.isEmpty()){
				return true;
			}
			writer = new FileWriter(filePath, isAppend);
			for(int i=0; i<lineList.size(); i++){
				writer.append("\r\n"+lineList.get(i));
			}
			return true;
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}finally{
			try{
				if(writer != null){
					writer.close();
				}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}

	/**TODO:追加内容到文件里面*/
	public static boolean writeFileByMode(String filePath,
			                              String line,
			                              boolean isAppend){
//		String msg = "";
//		String tag = "[FileUtil.writeFileByMode]";
		FileWriter writer = null;
		try{
			if(line == null){
				return true;
			}
			writer = new FileWriter(filePath, isAppend);
			writer.append("\r\n"+line);
			return true;
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}finally{
			try{
				if(writer != null){
					writer.close();
				}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]){
		try{
			String path = "E:\\【珠海一部工作】\\【需求变更】\\5月版投产后方案\\附件\\code\\";
			ArrayList<String> list = FileUtil.scanFiles(path, "");
			for(int i=0;i<list.size();i++){
				System.out.println(list.get(i));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
