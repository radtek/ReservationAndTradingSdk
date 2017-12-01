package com.icbc.main;

/**
 * @author kfzx-yanyj
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import jxl.*;
import jxl.write.*;

import com.icbc.tool.db.CTPBean;
import com.icbc.tool.db.OPGBean;
import com.icbc.tool.util.WriteExcel;

public class SearchPath {

	List<CTPBean> result = new ArrayList<CTPBean>();
	int Steps;
	PrintWriter fileOut;
	List<ArrayList<String>> LinkedPath = new ArrayList<ArrayList<String>>();

	public int getIDFromFileName(String fileName) {
		ReadFiles t = new ReadFiles();
		fileName = t.DealStr(fileName);
		for (CTPBean res : result) {
			if (res.getFileName() != null && res.getFileName().equals(fileName))
				return res.getID();
		}
		return -1;
	}

	public int getIDFromdisplayName(String dp) {
		for (CTPBean res : result) {
			if (res.getDisplayName() != null && res.getDisplayName().equals(dp)) {
				return res.getID();
			}
		}
		return -1;
	}

	public int getIDFromItemName(String itemName) {
		itemName = new String(itemName.getBytes());
		fileOut.write(itemName + "\r\n");
		String temp;
		for (CTPBean res : result) {
			if (res.getItemName() != null) {
				temp = new String(res.getItemName().getBytes());
				if (temp.equals(itemName) || temp.indexOf(itemName) != -1) {
					return res.getID();
				}
			}
		}
		return -1;
	}

	/**
	 * @param pckgName
	 * @return pckgName对应的OPG ID
	 */
	public HashSet<Integer> getOPGIDFromPCKGName(String pckgName) {
		HashSet<Integer> relOPG = new HashSet<Integer>();
		for (CTPBean res : result) {
			if (res.getFileName() == null)
				continue;
			if (res.getFileName().toLowerCase().indexOf("opg") != -1) {
				List<OPGBean> opgSteps = res.getOpgBean();
				if (opgSteps == null)
					continue;
				for (OPGBean opgstep : opgSteps) {
					HashMap<String, String> opContent = opgstep.getOpContent();
					// System.out.println(opgstep.getDisplayName()+":" +
					// opContent);
					if (opContent.containsValue(pckgName)) {
						relOPG.add(res.getID());
					}
				}
			}
		}
		return relOPG;
	}

	/**
	 * <b>后向路径</b>
	 * 
	 * @param ID
	 * @param paths
	 */

	public void DFSNext(int ID, ArrayList<Integer> paths) {
		if (result.get(ID).getNextID().isEmpty()
				|| result.get(ID).getNextID() == null) {
			int cnt, idx, SZ = paths.size();
			ArrayList<String> linked = new ArrayList<String>();
			for (cnt = 0; cnt < SZ - 1; ++cnt) {
				idx = paths.get(cnt);
				/*
				 * System.out.print(" Steps." + (cnt + 1) + " ID = " + idx + ":"
				 * + result.get(idx).getFileName() + " -->>");
				 */
				fileOut.write(" Steps." + (cnt + 1) + " ID = " + idx + ":"
						+ result.get(idx).getFileName() + " -->>");
				linked.add(result.get(idx).getFileName());
			}
			idx = paths.get(cnt);
			fileOut.write(" Steps." + (cnt + 1) + " ID = " + idx + ":"
					+ result.get(idx).getFileName() + " 第  " + (++Steps)
					+ " 条路径\r\n");
			linked.add(result.get(idx).getFileName());
			//LinkedPath.add(linked);
			return;
		} else {
			for (int idx : result.get(ID).getNextID()) {
				if (result.get(idx).getFileName() == null)
					continue;
				if (!paths.contains(idx)) {
					if (!RelID.contains(idx)) {
						RelID.add(idx);
					}
					paths.add(idx);
					DFSNext(idx, paths);
					paths.remove(paths.size() - 1);
				}
			}
		}
	}

	/**
	 * <b>前向路径</b>
	 * 
	 * @param ID
	 * @param paths
	 */

	public void DFSPred(int ID, ArrayList<Integer> paths) {
		if (result.get(ID).getPredID().isEmpty()
				|| result.get(ID).getPredID() == null) {
			ArrayList<String> linked = new ArrayList<String>();
			int cnt, idx, SZ = paths.size();
			for (cnt = SZ - 1; cnt > 0; --cnt) {
				idx = paths.get(cnt);
				/*
				 * System.out.print(" Steps." + (SZ - cnt) + " ID = " + idx +
				 * ":" + result.get(idx).getFileName() + " -->>");
				 */
				fileOut.write(" Steps." + (SZ - cnt) + " ID = " + idx + ":"
						+ result.get(idx).getFileName() + " -->>");
				linked.add(result.get(idx).getFileName());
			}
			idx = paths.get(cnt);
			/*
			 * System.out.println(" Steps." + (SZ) + " ID = " + idx + ":" +
			 * result.get(idx).getFileName() + " 第  " + (++Steps) + " 条路径 ");
			 */
			fileOut.write(" Steps." + (cnt + 1) + " ID = " + idx + ":"
					+ result.get(idx).getFileName() + " 第  " + (++Steps)
					+ " 条路径\r\n");
			linked.add(result.get(idx).getFileName());
			LinkedPath.add(linked);
			return;
		} else {
			for (int idx : result.get(ID).getPredID()) {
				if (result.get(idx).getFileName() == null)
					continue;
				if (!paths.contains(idx)) {
					if (!RelID.contains(idx)) {
						RelID.add(idx);
					}
					paths.add(idx);
					DFSPred(idx, paths);
					paths.remove(paths.size() - 1);
				}
			}
		}
	}

	/**
	 * <b>关联文件</b>
	 * 
	 * @param RelID
	 * @param EBMitemName
	 */
	public void ShowRelID(List<Integer> RelID, String EBMitemName) {
		int len = RelID.size();
		List<CTPBean> retObj = new ArrayList<CTPBean>();
		for (int i = 0; i < len; ++i) {
			retObj.add(result.get(RelID.get(i)));
		}
		WriteExcel wr = new WriteExcel();
		try {
			wr.WriteExcel(retObj, EBMitemName);
			System.out.println("Create Excel Succeed!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b> 关联ID名字
	 * 
	 * @param RelID
	 */
	public void ShowNextID(List<Integer> RelID) {
		int len = RelID.size();
		List<CTPBean> retObj = new ArrayList<CTPBean>();
		for (int i = 0; i < len; ++i) {
			retObj.add(result.get(RelID.get(i)));
			if (result.get(RelID.get(i)).getOpgBean() != null) {
				for (OPGBean opg : result.get(RelID.get(i)).getOpgBean()) {
					System.out.print(opg.getDisplayName());
					if (opg.getOpName() == null)
						continue;
					for (String str : opg.getOpName())
						System.out.print(str + " ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * 
	 * @param pckgID
	 * @return 是否生成相关联的文件ID
	 * @throws Exception
	 */
	public boolean handlePCKG(HashSet<Integer> pckgIDList, String pckgName)
			throws Exception {
		if (pckgIDList == null)
			return false;
		WriteExcel wr = new WriteExcel();
		for (int pckgID : pckgIDList) {
			LinkedPath = new ArrayList<ArrayList<String>>();
			System.out.println(pckgName + " is No. " + pckgID);
			ArrayList<Integer> paths = new ArrayList<Integer>();
			RelID = new ArrayList<Integer>();
			RelID.add(pckgID);
			paths.add(pckgID);
			DFSPred(pckgID, paths);
			ShowRelID(RelID, pckgName);
			wr.WriteLinkedExcel(LinkedPath, pckgName);
		}
		return true;
	}

	/**
	 * 生成全量的关联文件清单 以及菜单关联路径 20150417
	 * 
	 * @throws Exception
	 */
	public void searchALLFile() throws Exception {
		int ID;
		String fileName = "";
		WriteExcel wr = new WriteExcel();
		ArrayList<Integer> paths = new ArrayList<Integer>();
		//LinkedPath = new ArrayList<ArrayList<String>>();
		RelID = new ArrayList<Integer>();
		for (CTPBean ctp : result) {
			if (ctp.getFileName() != null &&(ctp.getFileName().toLowerCase().indexOf(".opg")!=-1 
					|| ctp.getFileName().toLowerCase().indexOf(".jsp")!=-1)) {
				//LinkedPath.clear();
				paths.clear();
				ID = getIDFromFileName(ctp.getFileName());
				fileName = ctp.getFileName();
				if(fileName.toLowerCase().indexOf(".jsp")!=-1){
					if(ctp.getItemName()!=null)
						fileName = ctp.getItemName();
				}	
				if (ID != -1) {
					Steps = 1;
					RelID.clear();
					RelID.add(ID);
					paths.add(ID);
					DFSNext(ID, paths);
					ShowRelID(RelID, handleFileName(fileName));
					wr.WriteLinkedExcel(LinkedPath,
							handleFileName(fileName));
					System.out.println(fileName + " SuccessFul! ");
				}
			}
		}
	}

	/**
	 * 处理vjson opg保留文件名 需以它为Excel文件名的情况
	 * 
	 * @param fileName
	 * @return
	 */
	public String handleFileName(String fileName) {
		if (fileName.indexOf("\\") != -1) {
			fileName = fileName.replace("\\", "_");
		}
		if (fileName.indexOf(".") != -1) {
			fileName = fileName.replace(".", "_");
		}
		if (fileName.indexOf("^^")!=-1){
			fileName = fileName.replace("^^", "_");
					
		}
		return fileName;
	}

	/**
	 * 通过页面菜单名字获取关联菜单以及相关的链接路径
	 * 
	 * @param searchPath
	 * @param EBMitemName
	 * @param pckgName
	 * @return
	 * @throws Exception
	 */
	public boolean work(String searchPath, String EBMitemName, String pckgName)
			throws Exception {
		// readConfigFile();
		int ID, pckgID;
		result = new ReadFiles().ReadFiles(searchPath);
		ArrayList<Integer> paths = new ArrayList<Integer>();
		EBMitemName = new String(EBMitemName.getBytes());
		fileOut = new PrintWriter("D:\\output.txt");
		ID = getIDFromItemName(EBMitemName);
		RelID = new ArrayList<Integer>();
		HashSet<Integer> pckgIDList = getOPGIDFromPCKGName(pckgName);
		boolean pckgFlag = handlePCKG(pckgIDList, pckgName);
		WriteExcel wr = new WriteExcel();
		System.out.println(EBMitemName + " is No. " + ID);
		LinkedPath = new ArrayList<ArrayList<String>>();
		if (ID != -1) {
			Steps = 1;
			RelID = new ArrayList<Integer>();
			RelID.add(ID);
			paths.add(ID);
			DFSNext(ID, paths);
			ShowRelID(RelID, EBMitemName);
			//wr.WriteLinkedExcel(LinkedPath, EBMitemName);
		} else {
			fileOut.write(EBMitemName + "\r\n");
			fileOut.write("应用中无此菜单\r\n");
			fileOut.close();
			return false || pckgFlag;
		}
		
		// ShowNextID(result.get(ID).getNextID());
		// ShowNextID(result.get(ID).getPredID());
		// 生成全量菜单
		searchALLFile();

		fileOut.close();
		return true || pckgFlag;
	}

	/**
	 * GUI界面，读取配置文件
	 */
	public void readConfigFile() {
		File tmpFile = new File(".");
		String tmpFilePath = tmpFile.getAbsolutePath();
		String curDirPath = tmpFilePath.substring(0,
				tmpFilePath.lastIndexOf(File.separator));
		System.out.println("curDirPath=" + curDirPath);
		try {
			FileInputStream fis = new FileInputStream(curDirPath
					+ "\\config.ini");
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String confLine = "";
			while ((confLine = br.readLine()) != null) {
				if (confLine.indexOf("searchPath") != -1) {
					searchPath = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					System.out.println(searchPath);
				}
				if (confLine.indexOf("EBMitemName") != -1) {
					EBMitemName = confLine.substring(confLine.indexOf("=") + 1)
							.trim();
					System.out.println(EBMitemName);
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e1) {
			System.out.println("读取config.ini配置错误" + e1.getMessage());
		}
	}

	private String searchPath = "";
	private String EBMitemName = "";
	private List<Integer> RelID = null;

}
