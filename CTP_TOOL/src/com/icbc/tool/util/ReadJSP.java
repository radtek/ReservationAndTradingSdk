package com.icbc.tool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.icbc.tool.ctp3.FileObj;
import com.icbc.tool.db.CTPBean;

public class ReadJSP {

	/**
	 * @author kfzx-yanyj
	 * @value 分析CTP4 的FLOWC
	 */

	final String FLOWC = ".flowc";
	final String FLOWACTION = "flowActionName=";
	final String jspInclude = "<jsp:include page=";

	/**
	 * @author kfzx-yanyj
	 * @value 分析CTP3页面链接关系
	 */

	String regex_replyPage = "^.*id\\s*=\\s*\"setReplyPage\".*$";
	String regex_reqHtmlFlds = "^.*=\\s*utb.getRequiredHtmlFields\\s*.*$";
	String regex_fullJspUrl = "^.*=\\s*utb.getFullJspUrl\\s*.*$";
	String regex_dseOpName = "^.*dse_operationName\\s*=*.*$";
	/* 匹配平台接口 */
	String regex_httpUrl = "^.*http:\\s*com.icbc.cte.cs.servlet.WithoutSessionReqServlet\\s*.*$";

	Pattern pattern_globe1 = Pattern.compile(regex_replyPage);
	Pattern pattern_globe2 = Pattern.compile(regex_reqHtmlFlds);
	Pattern pattern_globe3 = Pattern.compile(regex_fullJspUrl);
	Pattern pattern_globe4 = Pattern.compile(regex_dseOpName);
	Matcher matcher_globe = null;


	
	
	// 处理普通的CTP4 JSP页面
	public CTPBean handleJSP(String str) { 
		CTPBean tmp = new CTPBean();
		int strLen = str.length();
		int j, idx = str.indexOf(FLOWC);
		if (idx != -1) {
			// if(!matchesPattern(regex_httpUrl, str)){
			j = idx - 1;
			while (j > 0 && str.charAt(j) != '\"')
				--j;
			tmp.setFlowcName(str.substring(j + 1, idx + 6));
			idx = str.indexOf(FLOWACTION);
			j = idx + 16;
			try {
				while (j < strLen && str.charAt(j) != '\"'
						&& str.charAt(j) != ' ')
					++j;
				tmp.setFileID(str.substring(idx + 16, j));
			} catch (Exception ex) {

			}
			return tmp;
		}

		return  null;
	}

	public CTPBean handleIncludeJSP(String str) {
		CTPBean tmp = new CTPBean();
		int j, idx = str.indexOf(jspInclude);
		if (idx != -1) {
			idx += (jspInclude.length() + 2);
			j = idx + 1;
			int strLen = str.length();
			while (j < strLen && str.charAt(j) != '\"') {
				++j;
			}
			String fileName = str.substring(idx, j);
			tmp.setFileName(fileName.replace('/', '\\'));
			// System.out.println(fileName);
			return tmp;
		}
		return null;
	}

	// 添加处理CTP3的页面调用
	public List<CTPBean> readJSP(String fileName) throws Exception {
		List<CTPBean> result = new ArrayList<CTPBean>();
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				fileName));
		BufferedReader br = new BufferedReader(reader);
		String line = null;
		while ((line = br.readLine()) != null) { // JSP 页面分析
			CTPBean tmp = handleJSP(line);
			if (tmp != null && !result.contains(tmp)) {
				result.add(tmp);
			} /*else {
                tmp = new CTPBean();
				if (matchesPattern(pattern_globe2, line)) {
					int reqHtmlFldsIdx = line.indexOf("getRequiredHtmlFields");
					if (reqHtmlFldsIdx != -1) {
						int dhIdx = line.indexOf(",", reqHtmlFldsIdx);
						if (dhIdx != -1) {
							int firstQutIdx = line.indexOf("\"", dhIdx);
							if (firstQutIdx != -1
									&& firstQutIdx + 1 < line.length()) {
								int secQutIdx = line.indexOf("\"",
										firstQutIdx + 1);
								if (secQutIdx != -1
										&& secQutIdx + 1 < line.length()) {
									String opName = line.substring(
											firstQutIdx + 1, secQutIdx).trim();
									System.out.println("opName : " + opName);
									if (!opName.isEmpty()) {
										tmp.setCtp3OpName(opName.toLowerCase());
									}
								}
							}
						}
					}
				}
				else if (matchesPattern(pattern_globe3, line)) {
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
									if (pageName != null)
										tmp.setFileName(pageName);
								} else if ((sxIdx != -1) && (sxidx_end != -1)) {
									pageName = pageName.substring(sxIdx + 1,
											sxidx_end);
									System.out.println("pageName : " + pageName);
									if (!"".equals(pageName))
										tmp.setFileName(pageName);
								} else {
									System.out.println("pageName : " + pageName);
									tmp.setFileName(pageName);
								}
							}
						}
					}
				}else if (matchesPattern(pattern_globe4, line)) {
					int dseOpNameIdx = line.indexOf("dse_operationName");
					if (dseOpNameIdx != -1) {
						//System.out.println(line);
						int firstQutIdx = line.indexOf("=", dseOpNameIdx);
						if (firstQutIdx != -1
								&& firstQutIdx + 1 < line.length()) {
							int secQutIdx = line.indexOf("\"", firstQutIdx + 1);
							if (secQutIdx != -1) {
								String opName = line.substring(firstQutIdx + 1,
										secQutIdx).trim();
								if ((opName == null) || ("".equals(opName))
										&& secQutIdx + 1 < line.length()) {
									int secQutIdx_end = line.indexOf("\"",
											secQutIdx + 1);
									if (secQutIdx_end != -1) {
										opName = line.substring(secQutIdx + 1,
												secQutIdx_end).trim();
									}
								}
								if(!opName.isEmpty()){
								  String[] opNameArr = opName.split("&");
								  opName = opNameArr[0];
								  tmp.setCtp3OpName(opName);
								  System.out.println(tmp.getCtp3OpName());
								}
							}
						}
					}
				}
			}*/
			if (tmp != null && !result.contains(tmp)) {
				result.add(tmp);
			}
		}

		return result;
	}

	public  synchronized boolean matchesPattern(Pattern pattern_globe, String str) {
		matcher_globe = pattern_globe.matcher(str);
		boolean result =  matcher_globe.find();
		matcher_globe.reset();
		return result;
	}
}
