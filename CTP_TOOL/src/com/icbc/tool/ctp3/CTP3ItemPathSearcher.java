package com.icbc.tool.ctp3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class CTP3ItemPathSearcher {
	private static String dbDriverStr = "oracle.jdbc.driver.OracleDriver";
	private static String dbUrl = "jdbc:oracle:thin:@${dbHostIp}:${dbPort}:${dbInstName}";
	private static String dbUser = "";
	private static String dbPass = "";
	private static String itemTableName = "";
	private static String itemIdFd = "";
	private static String itemNameFd = "";
	private static String itemUrlFd = "";
	//private static String qrySql = "SELECT ${itemIdFd},${itemNameFd},${itemUrlFd} FROM ${itemTableName}";
	private static String qrySql =
	"SELECT  ${itemTableName}.${itemIdFd} , ${itemMenuName}.${itemNameFd},${itemTableName}.${itemUrlFd} FROM ${itemTableName}, ${itemMenuName} WHERE ${itemTableName}.${itemIdFd}=${itemMenuName}.${itemIdFd}";
			
	private static String searchPath = "";
	private static String logPath = "src\\config\\resultLog.txt";

	private static ArrayList<ItemObj> itemObjLst = new ArrayList();
	private static HashMap<String, FileObj> fileObjMap = new HashMap();
	private static HashMap<String, FileObj> passFileObjMap = new HashMap();
	private static ArrayList<String> repeatLst = new ArrayList();

	public static String regex_replyPage = "^.*id\\s*=\\s*\"setReplyPage\".*$";
	public static String regex_reqHtmlFlds = "^.*=\\s*utb.getRequiredHtmlFields\\s*.*$";
	public static String regex_fullJspUrl = "^.*=\\s*utb.getFullJspUrl\\s*.*$";

	public static String regex_dseOpName = "^.*dse_operationName\\s*=*.*$";

	public static Pattern pattern_globe = null;
	public static Matcher matcher_globe = null;

	@SuppressWarnings("null")
	public static void main(String[] args) {
		System.out.println("开始读取配置文件：");
		readConfigFile();
		System.out.println("开始从以下目录搜索文件：" + searchPath);
		getWantedFileObjs(searchPath, fileObjMap);
		System.out.println("开始查询菜单开始节点：");
		getWantedItemObjs(itemObjLst);
		System.out.println("开始从菜单开始节点遍历：");
		int j;
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(
					"D:\\MenuLinkedPaths\\" + dbUser + "CTP3关联文件清单.xls"));
			WritableSheet sheet = workbook.createSheet(dbUser + "CTP3关联菜单", 0);
			Label label1 = new Label(0, 0, "CTP3菜单名");
			Label label2 = new Label(1, 0, "关联的菜单");
			//Label label3 = new Label(2, 0, "调用的DSR");
			//Label label4 = new Label(3, 0, "调用的存储过程");
			sheet.addCell(label1);
			sheet.addCell(label2);
			//sheet.addCell(label3);
			//sheet.addCell(label4);
			PrintWriter pw = new PrintWriter(new FileWriter(logPath));
			for (int i = 0; i < itemObjLst.size(); ++i) {
				ItemObj itemObj = (ItemObj) itemObjLst.get(i);
				passFileObjMap.clear();
				System.out.println("开始处理菜单节点：" + itemObj.getItemName());
				pw.write("开始处理菜单节点：" + itemObj.getItemName() + " -> ");
				label1 = new Label(0,i+1,itemObj.getItemName());
				handlerNextFile(fileObjMap, itemObj.getFirstOpOrJsp(), itemObj);
				Iterator iter = passFileObjMap.keySet().iterator();
				j = 1;
				sheet.addCell(label1);
				while (iter.hasNext()) {
					StringBuffer line = new StringBuffer();
					FileObj fileObj = (FileObj) passFileObjMap.get(iter.next());
					System.out.println("菜单：" + fileObj.getItemName());
					pw.write("菜单：" + fileObj.getItemName() + " [ " + fileObj.getFileName()+" ] \r\n");
					line.append("菜单：" + fileObj.getItemName() + " { " + fileObj.getFileName()+" }\n");
					if(fileObj.getDsrNames().size()>0){
						//for(String dsrName:fileObj.getDsrNames())
							pw.write(" 调用DSR: "+fileObj.getDsrNames().toString() + "\r\n");
							line.append(" 调用DSR: { "+fileObj.getDsrNames().toString() + " }\r\n");
							
					}
					if(fileObj.getProcNames().size()>0){
						pw.write(" 调用PROC: "+fileObj.getProcNames().toString() + "\r\n");	
						line.append(" 调用PROC: { "+fileObj.getProcNames().toString() + " }\r\n");
					}
					label2 = new Label(j++,i+1,line.toString());
					sheet.addCell(label2);
				}
				pw.write("\r\n");
				
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void main(String[] args) { System.out.println("开始读取配置文件：");
	 * readConfigFile(); System.out.println("开始从以下目录搜索文件：" + searchPath);
	 * getWantedFileObjs(searchPath, fileObjMap);
	 * System.out.println("开始查询菜单开始节点："); getWantedItemObjs(itemObjLst);
	 * System.out.println("开始从菜单开始节点遍历："); for (int i = 0; i <
	 * itemObjLst.size(); ++i) { ItemObj itemObj = (ItemObj)itemObjLst.get(i);
	 * passFileObjMap.clear(); System.out.println("开始处理菜单节点：" +
	 * itemObj.getItemName()); handlerNextFile(fileObjMap,
	 * itemObj.getFirstOpOrJsp(), itemObj); } System.out.println("开始打印日志："); try
	 * { PrintWriter pw = new PrintWriter(new FileWriter(logPath));
	 * 
	 * Iterator iter = fileObjMap.keySet().iterator(); while (iter.hasNext()) {
	 * FileObj fileObj = (FileObj)fileObjMap.get(iter.next());
	 * System.out.println("打印：" + fileObj.getFileName()); ArrayList itemNames =
	 * fileObj.getItemName(); for (int k = 0; k < itemNames.size(); ++k) {
	 * String line = fileObj.getFileName() + " : " + (String)itemNames.get(k);
	 * pw.write(line + "\r\n"); } } System.out.println("开始打印重复文件：");
	 * pw.write("\r\n以下文件存在重复：\r\n"); for (int j = 0; j < repeatLst.size(); ++j)
	 * { pw.write((String)repeatLst.get(j) + "\r\n"); } pw.close();
	 * System.out.println("操作结果写入文件" + logPath); } catch (IOException e) {
	 * System.out.println("写日志" + logPath + "错误：" + e.getMessage()); } }
	 */

	public static void readConfigFile() {
		File tmpFile = new File(".");
		String tmpFilePath = tmpFile.getAbsolutePath();
		String curDirPath = tmpFilePath.substring(0,
				tmpFilePath.lastIndexOf(File.separator));
		System.out.println("curDirPath=" + curDirPath);
		try {
			FileInputStream fis = new FileInputStream(curDirPath
					+ "\\src\\config\\config.ini");
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String confLine = "";
			while ((confLine = br.readLine()) != null) {
				if (confLine.indexOf("dbHostIp") != -1) {
					String dbHostIp = confLine.substring(
							confLine.indexOf("=") + 1).trim();
					dbUrl = dbUrl.replace("${dbHostIp}", dbHostIp);
					System.out.println("dbHostIp=" + dbHostIp);
				}
				if (confLine.indexOf("dbPort") != -1) {
					String dbPort = confLine.substring(
							confLine.indexOf("=") + 1).trim();
					dbUrl = dbUrl.replace("${dbPort}", dbPort);
					System.out.println("dbPort=" + dbPort);
				}
				if (confLine.indexOf("dbInstName") != -1) {
					String dbInstName = confLine.substring(
							confLine.indexOf("=") + 1).trim();
					dbUrl = dbUrl.replace("${dbInstName}", dbInstName);
					System.out.println("dbInstName=" + dbInstName);
				}
				if (confLine.indexOf("dbUser") != -1) {
					dbUser = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					System.out.println("dbUser=" + dbUser);
				}
				if (confLine.indexOf("dbPass") != -1) {
					dbPass = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					System.out.println("dbPass=" + dbPass);
				}
				if (confLine.indexOf("itemTableName") != -1) {
					itemTableName = confLine.substring(
							confLine.indexOf("=") + 1).trim();
					qrySql = qrySql.replace("${itemTableName}", itemTableName);
					System.out.println("itemTableName=" + itemTableName);
				}
				if (confLine.indexOf("itemIdFd") != -1) {
					itemIdFd = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					qrySql = qrySql.replace("${itemIdFd}", itemIdFd);
					System.out.println("itemIdFd=" + itemIdFd);
				}
				if (confLine.indexOf("itemNameFd") != -1) {
					itemNameFd = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					qrySql = qrySql.replace("${itemNameFd}", itemNameFd);
					System.out.println("itemNameFd=" + itemNameFd);
				}
				if (confLine.indexOf("itemUrlFd") != -1) {
					itemUrlFd = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					qrySql = qrySql.replace("${itemUrlFd}", itemUrlFd);
					System.out.println("itemUrlFd=" + itemUrlFd);
				}
				if (confLine.indexOf("searchPath") != -1) {
					searchPath = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					System.out.println("searchPath=" + searchPath);
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e1) {
			System.out.println("读取配置文件config.ini错误：" + e1.getMessage());
		}
	}

	public static void getWantedFileObjs(String fromPath,
			HashMap<String, FileObj> fileObjMap) {
		File baseD = new File(fromPath);
		if (!baseD.exists())
			return;
		if (baseD.isDirectory()) {
			File[] chidren = baseD.listFiles();
			for (int w = 0; w < chidren.length; ++w) {
				String path = chidren[w].getAbsolutePath();
				getWantedFileObjs(path, fileObjMap);
			}

		} else if (baseD.isFile()) {
			if ("op".equalsIgnoreCase(getFileSuffix(baseD))) {
				readOpFileToObj(baseD, fileObjMap);
			} else if ("jsp".equalsIgnoreCase(getFileSuffix(baseD))){
				readJspFileToObj(baseD, fileObjMap);
			} else if(baseD.getAbsolutePath().indexOf("sql.xml")!=-1){
				
			}
			
		}
	}

	public static void getWantedItemObjs(ArrayList<ItemObj> itemObjLst) {
		try {
			Class.forName(dbDriverStr);
			Connection conn = DriverManager
					.getConnection(dbUrl, dbUser, dbPass);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(qrySql);
			while (rs.next()) {
				ItemObj itemObj = new ItemObj();
				itemObj.setItemId(rs.getString(itemIdFd));
				itemObj.setItemName(rs.getString(itemNameFd));
				String itemUrl = rs.getString(itemUrlFd);
				itemObj.setItemUrl(itemUrl);
				String firstOpOrJspName = "";
				if (itemUrl.indexOf(".flowc") != -1)
					continue;
				if (itemUrl.indexOf("CSReqServlet") != -1) {
					int opNameIdx = itemUrl.indexOf("operationName");
					int equIdx = itemUrl.indexOf("=", opNameIdx);
					int andIdx = itemUrl.indexOf("&", equIdx + 1);
					if (andIdx != -1)
						firstOpOrJspName = itemUrl
								.substring(equIdx + 1, andIdx);
					else
						firstOpOrJspName = itemUrl.substring(equIdx + 1);
				} else {
					int qustIdx = itemUrl.indexOf("?");
					if (qustIdx != -1)
						firstOpOrJspName = itemUrl.substring(0, qustIdx);
					else {
						firstOpOrJspName = itemUrl;
					}
					int lastSxIdx = firstOpOrJspName.lastIndexOf("/");
					if (lastSxIdx != -1) {
						firstOpOrJspName = firstOpOrJspName
								.substring(lastSxIdx + 1);
					}
				}
				System.out.println("firstOpOrJspName : " + firstOpOrJspName);
				itemObj.setFirstOpOrJsp(firstOpOrJspName);
				itemObjLst.add(itemObj);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void handlerNextFile(HashMap<String, FileObj> fileObjMap,
			String tarFileName, ItemObj itemObj) {
		System.out.println("进入：" + tarFileName);
		if (passFileObjMap.get(tarFileName) != null) {
			return;
		}
		FileObj tarFileObj = (FileObj) fileObjMap.get(tarFileName);
		if (tarFileObj != null) {
			tarFileObj.addItemName(itemObj.getItemId() + "_"
					+ itemObj.getItemName());
			System.out.println("已处理：" + tarFileObj.getFilePath());
			passFileObjMap.put(tarFileName, tarFileObj);
			ArrayList linkFileLst = tarFileObj.getLinkFileLst();
			if (linkFileLst.size() > 0) {
				for (int i = 0; i < linkFileLst.size(); ++i) {
					String linkFileName = (String) linkFileLst.get(i);
					handlerNextFile(fileObjMap, linkFileName, itemObj);
				}
				return;
			}
			return;
		}
	}

	public static String getFileSuffix(File file) {
		String path = file.getAbsolutePath();
		int start = path.lastIndexOf(".") + 1;
		int length = path.length();
		return path.substring(start, length);
	}
	
	public static String handleJSP(String str){
		   String[] arr = str.split("/");
		   if(arr.length>1){
			 return arr[arr.length-1];   
		   }else{
			   return str;
		   }
	   }
	
	public static void readOpFileToObj(File file,
			HashMap<String, FileObj> fileObjMap) {
		FileObj fileObj = new FileObj();
		fileObj.setFileType("op");
		fileObj.setFileName(file.getName());
		fileObj.setFilePath(file.getAbsolutePath());
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file.getAbsolutePath());

			// 下面开始读取
			Element root = doc.getDocumentElement(); // 获取根元素           
			NodeList items = root.getElementsByTagName("opstep");
			for (int i = 0; i < items.getLength(); i++) {
				Element ss = (Element) items.item(i);
				//处理链向JSP页面
				boolean isJSP = ss.getAttribute("type").toLowerCase().indexOf("page")==-1 ? false : true;
				//处理链向存储过程文件
				boolean isPROC = ss.getAttribute("type").toLowerCase().indexOf("procedure")==-1? false : true;
				//处理DSR文件
				boolean isDSR = ss.getAttribute("type").toLowerCase().indexOf("dsr")==-1?false:true;
				
				NodeList inputsItem = ss.getElementsByTagName("inputs");
				for (int j = 0; j < inputsItem.getLength(); ++j) {
					Element sta = (Element) inputsItem.item(j);
					NodeList states = sta.getElementsByTagName("input");
					if(isPROC){//处理链向存储过程文件
						Element state = (Element) states.item(0);
						fileObj.addProcNames(state.getAttribute("src").substring(1, state.getAttribute("src").length()-1));
					}
					else if(isJSP){//处理链向JSP页面
					  for (int k = 0; k < states.getLength(); ++k) {
						Element state = (Element) states.item(k);
						fileObj.addLinkFile(handleJSP(state.getAttribute("src")));
					  }
					}else if(isDSR){//处理DSR文件
						for (int k = 0; k < states.getLength(); ++k) {
							Element state = (Element) states.item(k);
							if(state.getAttribute("name").toLowerCase().indexOf("tradeid")!=-1)
									//result.getRelJSP().add(state.getAttribute("src").substring(1, state.getAttribute("src").length()-1));
						 	  fileObj.addDsrNames(state.getAttribute("src").substring(1, state.getAttribute("src").length()-1));
						    }	
						}
					}
			}
			String fileName = fileObj.getFileName();
			String opName = fileName.substring(0, fileName.indexOf("."));
			if (fileObjMap.get(opName) == null)
				fileObjMap.put(opName, fileObj);
			else {
				repeatLst.add(opName);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!matchesPattern(regex_replyPage, line))
					continue;
				int pageNameIdx = line.indexOf("pageName");
				if (pageNameIdx != -1) {
					int firstQutIdx = line.indexOf("\"", pageNameIdx);
					if (firstQutIdx != -1) {
						int secQutIdx = line.indexOf("\"", firstQutIdx + 1);
						if (secQutIdx != -1) {
							String pageName = line.substring(firstQutIdx + 1,
									secQutIdx).trim();
							int sxIdx = pageName.lastIndexOf("/");
							if (sxIdx != -1) {
								pageName = pageName.substring(sxIdx + 1);
								System.out.println("replyPageName : "
										+ pageName);
								if (!"".equals(pageName)) {
									fileObj.addLinkFile(pageName);
								}
							}
						}
					}
				}
			}

			String fileName = fileObj.getFileName();
			String opName = fileName.substring(0, fileName.indexOf("."));
			if (fileObjMap.get(opName) == null)
				fileObjMap.put(opName, fileObj);
			else {
				repeatLst.add(opName);
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("读取文件" + file.getPath() + "错误：" + e.getMessage());
		}
		*/
	}

	public static void readJspFileToObj(File file,
			HashMap<String, FileObj> fileObjMap) {
		FileObj fileObj = new FileObj();
		fileObj.setFileType("jsp");
		fileObj.setFileName(file.getName());
		fileObj.setFilePath(file.getAbsolutePath());
		if(fileObj.getFileName().equals("error.jsp"))return ;
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				if (matchesPattern(regex_reqHtmlFlds, line)) {
					int reqHtmlFldsIdx = line.indexOf("getRequiredHtmlFields");
					if (reqHtmlFldsIdx != -1) {
						int dhIdx = line.indexOf(",", reqHtmlFldsIdx);
						if (dhIdx != -1) {
							int firstQutIdx = line.indexOf("\"", dhIdx);
							if (firstQutIdx != -1) {
								int secQutIdx = line.indexOf("\"",
										firstQutIdx + 1);
								if (secQutIdx != -1) {
									String opName = line.substring(
											firstQutIdx + 1, secQutIdx).trim();
									System.out.println("opName : " + opName);
									if (!"".equals(opName)) {
										fileObj.addLinkFile(opName);
									}
								}
							}
						}
					}
				}
				if (matchesPattern(regex_fullJspUrl, line)) {
					int fullJspUrlIdx = line.indexOf("getFullJspUrl");
					if (fullJspUrlIdx != -1) {
						int firstQutIdx = line.indexOf("\"", fullJspUrlIdx);
						if (firstQutIdx != -1) {
							int secQutIdx = line.indexOf("\"", firstQutIdx + 1);
							if (secQutIdx != -1) {
								String pageName = line.substring(
										firstQutIdx + 1, secQutIdx).trim();
								int sxIdx = pageName.lastIndexOf("/");
								int sxidx_end = pageName.lastIndexOf("?");
								if ((sxIdx != -1) && (sxidx_end == -1)) {
									pageName = pageName.substring(sxIdx + 1);
									System.out.println("pageName : " + pageName);
									if (!"".equals(pageName))
										fileObj.addLinkFile(pageName);
								} else if ((sxIdx != -1) && (sxidx_end != -1)) {
									pageName = pageName.substring(sxIdx + 1,
											sxidx_end);
									System.out
											.println("pageName : " + pageName);
									if (!"".equals(pageName))
										fileObj.addLinkFile(pageName);
								} else {
									System.out
											.println("pageName : " + pageName);
									fileObj.addLinkFile(pageName);
								}
							}
						}
					}
				}
				if (matchesPattern(regex_dseOpName, line)) {
					int dseOpNameIdx = line.indexOf("dse_operationName");
					if (dseOpNameIdx != -1) {
						int firstQutIdx = line.indexOf("=", dseOpNameIdx);
						if (firstQutIdx != -1) {
							int secQutIdx = line.indexOf("\"", firstQutIdx + 1);
							if (secQutIdx != -1) {
								String opName = line.substring(firstQutIdx + 1,
										secQutIdx).trim();
								if ((opName == null) || ("".equals(opName))) {
									int secQutIdx_end = line.indexOf("\"",
											secQutIdx + 1);
									opName = line.substring(secQutIdx + 1,
											secQutIdx_end).trim();
								}
								System.out.println("opName : " + opName);
								if (!"".equals(opName)) {
									fileObj.addLinkFile(opName);
								}
							}
						}
					}
				}
			}
			String fileName = fileObj.getFileName();
			if (fileObjMap.get(fileName) == null)
				fileObjMap.put(fileName, fileObj);
			else {
				repeatLst.add(fileName);
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("读取文件" + file.getPath() + "错误：" + e.getMessage());
		}
	}

	public static synchronized boolean matchesPattern(String regex, String str) {
		pattern_globe = Pattern.compile(regex);
		matcher_globe = pattern_globe.matcher(str);

		return matcher_globe.find();
	}
}