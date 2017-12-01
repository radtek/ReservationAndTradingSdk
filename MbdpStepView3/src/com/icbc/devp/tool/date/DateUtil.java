/*
 * 创建日期 2008-10-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.icbc.devp.tool.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class DateUtil {
 
	/**
	 * 得到当前日期(yyyymmdd)
	 * @author Administrator
	 */
	public static String getCurrdate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(calendar.getTime());
	}

	/**得到当前日期
	 * @param String format
	 * 日期格式
	 * @author kfzx-luoha 2012.7.20*/
	public static String getCurrdate(String format){
		try{
			SimpleDateFormat formater = new SimpleDateFormat(format);
			return formater.format(new Date());
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * 校验日期格式(yyyymmdd)
	 * @author Administrator
	 */
	public static boolean isDate(String strDate) {		
		String tempstr;
		try{
			if (strDate.indexOf("-") == -1){
				tempstr = strDate;
			}else{
				tempstr =
					strDate.substring(0, strDate.indexOf("-"))
						+ strDate.substring(
							strDate.indexOf("-") + 1,
							strDate.lastIndexOf("-"))
						+ strDate.substring(strDate.lastIndexOf("-") + 1);
			}
		}catch(Exception e){
			return false;
		}

		boolean isDate = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		Date date = null;
		try {
			date = format.parse(tempstr);
			if (format.format(date).equalsIgnoreCase(tempstr)) {
				isDate = true;
			}
		} catch (ParseException e) {
			//Log.getInstance().error("校验日期格式异常：" + e.toString());
			return false;
		}
		return isDate;
	}
	
	/**
	 * 用工作日期和数据文件延迟天数来匹配数据文件日期
	 * @param workdate
	 * @param adddays
	 * @return
	 */
	public static String getMatchDate(String workdate, String adddays){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(workdate));
		} catch (Exception e) {
			return "";
		}
		
		if("*".equals(adddays)) {
			return getCurrMonthEndDay(c.getTime());
		}
		
		int i = Integer.parseInt(adddays);
		c.add(Calendar.DATE, i);
		sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(c.getTime());
	}
	
	public static String getCurrMonthEndDay(Date date) {
		//当前月的最后一天
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		c.setTime(date);
		c.add(Calendar.MONTH,1);       
		c.set(Calendar.DAY_OF_MONTH,1);       
		c.add(Calendar.DAY_OF_MONTH,-1);   
		String current_month_day_end=sf.format(c.getTime());  
		return current_month_day_end;
	}	
	
	/*
	 * 判断传入的日期字符串YYYYMMDD是否是该月的最后一天
	 */
	public static boolean isLastDay(String date)
	{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date in=sf.parse(date);
		    String lastDay=DateUtil.getCurrMonthEndDay(in);
		    if(date.equalsIgnoreCase(date))
		    	return true;
		    return false;	
		} catch (ParseException e) {
			
			return false;
		}
	}

	/*
	 *根据传入的日期字符串和推荐或延后的时间值计算最终日期
	 */
	public static String getDateAfterMonths(String date,int months)
	{
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
			c.add(Calendar.MONTH, months);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
		
	}
	
	public static String getDateAfterDays(String date,int days)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
			c.add(Calendar.DAY_OF_MONTH, days);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

	/**TODO:2012.6.19，判断传入的日期是否为合法的日期字符串。
	 * @param String strDate
	 * 待校验的日期字符串
	 * @param String format
	 * 传入的日期字符串格式，方法将根据传入的格式进行校验，例如：yyyy-MM-dd，yyyyMM等
	 * @return 符合格式返回true，否则返回false
	 * @author kfzx-luoha*/
	public static boolean isDate(String strDate,
			                     String format){
		if(strDate==null || format==null){
			return false;
		}
		try{
			SimpleDateFormat formater = new SimpleDateFormat(format);
			formater.parse(strDate);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**TODO:计算与传入日期偏差若干个月份的日期值。
	 * @param strDate
	 * 传入的日期，将根据这个日期计算偏差后的日期值
	 * @param format
	 * 传入日期值的格式，比如yyyyMM，yyyy-MM-dd等
	 * @param int months
	 * 偏差月份的个数，负数表示向前偏差，正数表示向后偏差
	 * @author kfzx-luoha*/
	public static String calOtherMonth(String strDate,
			                           String format,
			                           int months)
	   throws Exception{
		SimpleDateFormat formater = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(formater.parse(strDate));
		c.add(Calendar.MONTH, months);
		return formater.format(c.getTime());
	}

	/**TODO:计算与传入日期偏差若干天的日期值。
	 * @param strDate
	 * 传入的日期，将根据这个日期计算偏差后的日期值
	 * @param format
	 * 传入日期值的格式，比如yyyyMM，yyyy-MM-dd等
	 * @param int months
	 * 偏差的天数，负数表示向前偏差，正数表示向后偏差
	 * @author kfzx-luoha*/
	public static String calOtherDate(String strDate,
			                          String format,
			                          int days)
	   throws Exception{
		SimpleDateFormat formater = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(formater.parse(strDate));
		c.add(Calendar.DATE, days);
		return formater.format(c.getTime());
	}

	/**TODO:获取当月最后一个日期
	 * @author kfzx-luoha 2012.9.19*/
	public static String getCurrMonthEndDay(String monthdate,String fmtStr)
    {
    	//当前月的最后一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tempDate = monthdate+"-01";//月批量workdate日期格式化，取monthdate拼上日期01号，即为workdate所在月首日
		Calendar c  = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(tempDate));
		} catch (Exception e) {
            e.printStackTrace();
		}
    	c.add(Calendar.MONTH,1);       
        c.set(Calendar.DAY_OF_MONTH,1);       
        c.add(Calendar.DAY_OF_MONTH,-1);   
        SimpleDateFormat sdf1 = new SimpleDateFormat(fmtStr);
        String current_month_day_end=sdf1.format(c.getTime());  
    	 return current_month_day_end;
    }
	
	/**TODO:将日期字符串转换为日期对象。
	 * @param String dateStr
	 * 日期字符串
	 * @param String format
	 * 日期字符串的格式，比如：yyyy-MM-dd*/
	public static Date convert2Date(String dateStr, String format)
	    throws Exception{
		return (new SimpleDateFormat(format)).parse(dateStr);
	}
	
	/**TODO:计算两个日期之间相差的天数。
	 * @param String beginDate
	 * 开始日期
	 * @param String endDate
	 * 结束日期
	 * @param String format
	 * 日期字符串的格式，比如yyyy-MM-dd
	 * @return int 相差的日期天数，如果beginDate比endDate大就返回负数。
	 * @throws Exception
	 * 如果传入的日期字符串格式不一致，抛出异常。（其他未知异常也抛出）
	 * @author kfzx-luoha/2012.11.5*/
	public static int calDayCount(String beginDate,
			                      String endDate,
			                      String format)
	  throws Exception{
		String tag = "[DateUtil.calDayCount]";
		String errinfo;
		try{
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			Date date1 = fmt.parse(beginDate);
			Date date2 = fmt.parse(endDate);
			//计算相隔的天数
			Long millSec = (date2.getTime() - date1.getTime()) / (1000*3600*24);
			//转为String之后再转为int返回
			String tmpStr = String.valueOf(millSec);
			int result = Integer.parseInt(tmpStr);
			return result;
		}catch(Exception e){
			errinfo = tag + "Exception，计算日期("+beginDate+")和("+endDate+")之间相差天数过程发生异常："+e.getMessage();
			throw new Exception(errinfo);
		}
	}
	
	/**TODO:将日期值格式化为指定字符串格式的字符串返回
	 * @param Date date
	 * 日期字符串
	 * @param String format
	 * 返回的日期字符串的格式*/
	public static String convert2Str(Date date,
			                       String format)
	  throws Exception{
		String tag = "[DateUtil.parseDate]";
		String errinfo;
		try{
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			return fmt.format(date);
		}catch(Exception e){
			errinfo = tag + "Exception，以\""+format+"\"格式解析日期\""+date+"\"过程发生异常："+e.getMessage();
			throw new Exception(errinfo);
		}
	}
}
