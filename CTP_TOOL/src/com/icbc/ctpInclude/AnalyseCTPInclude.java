package com.icbc.ctpInclude;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyseCTPInclude {

	List<String> fileList = new ArrayList<String>();
	List<String> pathFiles = new ArrayList<String>();

	int total;
	
	HashMap<String, HashSet<String>> ctpDict = new HashMap<String, HashSet<String>>();

	public static void main(String[] args) throws Exception {
		new AnalyseCTPInclude().work();
	}

	/**
	 * 
	 * @param arr
	 * @return js文件名
	 */
	public HashSet<String> HandleTags(String[] arr) {
		HashSet<String> res = new HashSet<String>();
		for (String ele : arr) {
			int idx = ele.lastIndexOf('/');
			String temp = ele.substring(idx + 1, ele.length());
			idx = temp.indexOf(".js");
			temp = temp.substring(0, idx);
			res.add(temp);
		}
		return res;
	}

	/**
	 * @since CTP5.0 CTP组件标签的导入的js
	 * @throws IOException
	 */
	public void initCTP() throws IOException {
		String curPath = System.getProperty("user.dir") + File.separator
				+ "IncludeTagsConstance.txt";
		BufferedReader in = new BufferedReader(new FileReader(curPath));
		String s, key;
		int nameIdx;
		try {
			while ((s = in.readLine()) != null) {
				s = s.trim();
				if ((nameIdx = s.indexOf("_JS")) != -1) {
					int idx = s.indexOf('=');
					String ctpTab = s.substring(idx + 1).trim();
					ctpTab = ctpTab.substring(1, ctpTab.length() - 2);
					key = s.substring(0, nameIdx).toLowerCase();
					String[] ctpTabs = ctpTab.split(",");
					ctpDict.put(key, HandleTags(ctpTabs));
				}
			}
			// System.out.println(ctpDict.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void work() throws Exception {
		initCTP();
		//D:\\CCViews\\kfzx-yanyj_N2CBMS1610\\vobs\\V_N2CBMS\\Cmp_N2CBMS-WEB\\CBMSWeb\\WebContent
		getPathFile("D:\\GitWorkspace\\pbms\\NPBMS\\WEB\\pbms_baseWeb\\WebContent");
		for (String fileName : pathFiles) {
			//checkCTPInclude(fileName);
			/*if (checkTag(fileName)==false){
				System.out.println(fileName);
			}*/
			checkZoneORBranchID(fileName);
		}
	}

	/**
	 * 1) '<%= zoneid %>'
	 * 2) '<%= branchid %>'
	 * 3) '<%= accnoid %>'
	 * @param fileName
	 * @throws IOException
	 */
	public void checkZoneORBranchID(String fileName) throws IOException{
		try {
			boolean flag = false;
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String s ,tmpS;
			Matcher matcher;
			Pattern pattern1 = Pattern.compile("^.*<%=.*zoneid.*%>$");
			Pattern pattern2 = Pattern.compile("^.*<%=.*(brchid|branchid).*%>$");
			Pattern pattern3 = Pattern.compile("^.*<%=.*accno(id|).*%>$");
			while ((s = in.readLine()) != null) {
				tmpS = s;
				s = s.toLowerCase();
				matcher = pattern1.matcher(s);
				if (matcher.matches()) {
				   if(s.indexOf("'<")==-1 && s.indexOf("\"<")==-1){
					flag=true;
					System.out.println(tmpS);
				   }
				}
				matcher = pattern2.matcher(s);
				if (matcher.matches()) {
					   if(s.indexOf("'<")==-1 && s.indexOf("\"<")==-1){
						flag=true;
						System.out.println(tmpS);
					   }
				}
				matcher = pattern3.matcher(s);
				if (matcher.matches()) {
					   if(s.indexOf("'<")==-1 && s.indexOf("\"<")==-1){
						flag=true;
						System.out.println(tmpS);
					   }
				}
			}
	
			  if(flag)
				System.out.println(fileName);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param fileName
	 *            文件名
	 * @return <ctp:include items=""> 与 new ctp.* 或 ctp.组件名的方法 是否引入正确
	 * @throws Exception 
	 */
	public void checkCTPInclude(String fileName) throws Exception {
		HashSet<String> includePages = new HashSet<String>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		Pattern pattern1 = Pattern.compile("^.*ctp:include\\s+items=.*$");
		Pattern pattern2 = Pattern.compile("^.*new\\s+ctp.*$");
		Pattern pattern3 = Pattern.compile("^.*include\\s+file=.*$");
		Set<String> ctp = new HashSet<String>();
		Matcher matcher;
		String s;
		boolean flagCTPInclude = false;
		try {
			while ((s = in.readLine()) != null) {
				matcher = pattern1.matcher(s);
				if (matcher.matches()) {
					flagCTPInclude = true;
					int firstIdx = s.indexOf("items=") + "items=".length() + 1;
					int secondIdx = firstIdx;
					while (secondIdx < s.length()) {
						if (s.charAt(secondIdx) == '\"'
								|| s.charAt(secondIdx) == '\'') {
							break;
						}
						++secondIdx;
					}
					String sCTP = s.substring(firstIdx, secondIdx);
					String[] ctpElements = sCTP.split(",");
					for (String ele : ctpElements) {
						// System.out.print(ele.trim()+" + ");
						if (ele.equals("")) {
							System.out.println(fileName + " 存在空引入 ");
							continue;
						}
						HashSet<String> tags = ctpDict.get(ele.trim());
						if (tags != null) {
							// System.out.println(tags.toString());
							for (String tag : tags) {
								ctp.add(tag);
							}
						} else {
							System.out.println(fileName + " ** " + ele + " 存在异常引用! ");
						}
					}
				}

				matcher = pattern2.matcher(s);
				if (matcher.matches()) {
					int firstIndex = s.indexOf("new ctp") + 3;
					while (s.charAt(firstIndex) == ' '
							&& firstIndex < s.length())
						++firstIndex;
					int secondIndex = firstIndex + 1;
					while (secondIndex < s.length()) {
						if (s.charAt(secondIndex) == '(')
							break;
						++secondIndex;
					}
					String ctpUI = s.substring(firstIndex, secondIndex).trim();
					// System.out.println(ctpUI);

					if (flagCTPInclude) {
						// System.out.println(ctpUI);
						if (!ctp.contains(ctpUI)) {
							System.out.print(fileName + " ** "
									+ ctp.toString() + " ** " + ctpUI + " ");
							fileList.add(fileName + "**" + ctpUI);
							// break;
							if (!includePages.isEmpty()) {
								if (FindIncludePages(ctpUI, includePages)) {
									System.out.println(" 存在迭代引用!");
								}else{
									System.out.println(" 存在异常引用! ");
								}
							}else{
								System.out.println(" 存在异常引用! ");
							}
						}
					}
				}
				/**
				 * 递归查找Include Items
				 * 
				 * */
				matcher = pattern3.matcher(s);
				if (matcher.matches()) {
					int idx = s.indexOf('=');
					String temp = s.substring(idx + 1).trim();
					temp = handlePages(temp);
					if (temp.toLowerCase().indexOf("head") == -1) {
						includePages.add(temp);
						//System.out.println(temp);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean FindIncludePages(String ctpUI, HashSet<String> includePages)
			throws Exception {
		for (String pages : includePages) {
			for (String fileName : pathFiles) {
				if (fileName.indexOf(pages) != -1) {
					try {
						BufferedReader in = new BufferedReader(new FileReader(
								fileName));
						Pattern pattern1 = Pattern
								.compile("^.*ctp:include\\s+items=.*$");
						Matcher matcher;
						String s;
						while ((s = in.readLine()) != null) {
							matcher = pattern1.matcher(s);
							if (matcher.matches()) {
								int firstIdx = s.indexOf("items=")
										+ "items=".length() + 1;
								int secondIdx = firstIdx;
								while (secondIdx < s.length()) {
									if (s.charAt(secondIdx) == '\"'
											|| s.charAt(secondIdx) == '\'') {
										break;
									}
									++secondIdx;
								}
								String sCTP = s.substring(firstIdx, secondIdx);
								String[] ctpElements = sCTP.split(",");
								for (String ele : ctpElements) {
									// System.out.print(ele.trim()+" + ");
									if (ele.equals("")) {
										System.out.println(fileName + "存在空引入");
										continue;
									}
									HashSet<String> tags = ctpDict.get(ele
											.trim());
									if (tags != null) {
										// System.out.println(tags.toString());
										for (String tag : tags)
											if (tag.equals(ctpUI)) {
											    System.out.print(fileName+"**" + pages);
												return true;
											}
									}
								}
							}

						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return false;
	}

	public boolean isLetter(char ch) {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z')
			return true;
		else
			return false;
	}

	public String handlePages(String str) {
		int i = 0;
		while (!isLetter((str.charAt(i))) && i < str.length())
			++i;
		str = str.substring(i);
		while (str.charAt(i) != '"' && i < str.length())
			++i;
		str = str.substring(0, i);
		str = str.replace('/', '\\');
		return str;

	}

	public void getPathFile(String fileName) {
		File f = new File(fileName);
		if (f.isDirectory()) {
			File[] fileList = f.listFiles();
			if (fileList.length > 0) {
				for (File file : fileList) {
					if (file.isDirectory())
						getPathFile(file.getAbsolutePath());
					else if (file.toString().toLowerCase().indexOf(".jsp") != -1) {
						pathFiles.add(file.getAbsolutePath());
					}
				}
			}
		} else if (fileName.toLowerCase().indexOf(".jsp") != -1) {
			pathFiles.add(fileName);
		}
	}
	
	public boolean checkTag(String fileName) throws IOException{
		String s;
		boolean htmlRec=false;
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		int match =0 ;
		 String startTag = "<!--";
		 String endTag = "-->";
		 String htmlTag = "<html>";		
		while ((s = in.readLine()) != null) {
			if(s.toLowerCase().indexOf(htmlTag)!=-1){
				htmlRec = true;
			}
			if(!htmlRec)continue;
			if(s.indexOf(startTag)!=-1){
				match=match+1;
			}
			if(s.indexOf(endTag)!=-1){
				match=match-1;
			}
			/*if (match>1 || match<-1){
				System.out.print("match="+match+" in ");
				return false;
			}*/
		}
		if(match==0)return true;
		else{
			System.out.print("match="+match+" in ");
			return false;
		}
	}
}