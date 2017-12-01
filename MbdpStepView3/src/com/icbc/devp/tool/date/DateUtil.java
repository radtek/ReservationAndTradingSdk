/*
 * �������� 2008-10-18
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.icbc.devp.tool.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class DateUtil {
 
	/**
	 * �õ���ǰ����(yyyymmdd)
	 * @author Administrator
	 */
	public static String getCurrdate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(calendar.getTime());
	}

	/**�õ���ǰ����
	 * @param String format
	 * ���ڸ�ʽ
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
	 * У�����ڸ�ʽ(yyyymmdd)
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
			//Log.getInstance().error("У�����ڸ�ʽ�쳣��" + e.toString());
			return false;
		}
		return isDate;
	}
	
	/**
	 * �ù������ں������ļ��ӳ�������ƥ�������ļ�����
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
		//��ǰ�µ����һ��
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
	 * �жϴ���������ַ���YYYYMMDD�Ƿ��Ǹ��µ����һ��
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
	 *���ݴ���������ַ������Ƽ����Ӻ��ʱ��ֵ������������
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

	/**TODO:2012.6.19���жϴ���������Ƿ�Ϊ�Ϸ��������ַ�����
	 * @param String strDate
	 * ��У��������ַ���
	 * @param String format
	 * ����������ַ�����ʽ�����������ݴ���ĸ�ʽ����У�飬���磺yyyy-MM-dd��yyyyMM��
	 * @return ���ϸ�ʽ����true�����򷵻�false
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
	
	/**TODO:�����봫������ƫ�����ɸ��·ݵ�����ֵ��
	 * @param strDate
	 * ��������ڣ�������������ڼ���ƫ��������ֵ
	 * @param format
	 * ��������ֵ�ĸ�ʽ������yyyyMM��yyyy-MM-dd��
	 * @param int months
	 * ƫ���·ݵĸ�����������ʾ��ǰƫ�������ʾ���ƫ��
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

	/**TODO:�����봫������ƫ�������������ֵ��
	 * @param strDate
	 * ��������ڣ�������������ڼ���ƫ��������ֵ
	 * @param format
	 * ��������ֵ�ĸ�ʽ������yyyyMM��yyyy-MM-dd��
	 * @param int months
	 * ƫ���������������ʾ��ǰƫ�������ʾ���ƫ��
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

	/**TODO:��ȡ�������һ������
	 * @author kfzx-luoha 2012.9.19*/
	public static String getCurrMonthEndDay(String monthdate,String fmtStr)
    {
    	//��ǰ�µ����һ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tempDate = monthdate+"-01";//������workdate���ڸ�ʽ����ȡmonthdateƴ������01�ţ���Ϊworkdate����������
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
	
	/**TODO:�������ַ���ת��Ϊ���ڶ���
	 * @param String dateStr
	 * �����ַ���
	 * @param String format
	 * �����ַ����ĸ�ʽ�����磺yyyy-MM-dd*/
	public static Date convert2Date(String dateStr, String format)
	    throws Exception{
		return (new SimpleDateFormat(format)).parse(dateStr);
	}
	
	/**TODO:������������֮������������
	 * @param String beginDate
	 * ��ʼ����
	 * @param String endDate
	 * ��������
	 * @param String format
	 * �����ַ����ĸ�ʽ������yyyy-MM-dd
	 * @return int �����������������beginDate��endDate��ͷ��ظ�����
	 * @throws Exception
	 * �������������ַ�����ʽ��һ�£��׳��쳣��������δ֪�쳣Ҳ�׳���
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
			//�������������
			Long millSec = (date2.getTime() - date1.getTime()) / (1000*3600*24);
			//תΪString֮����תΪint����
			String tmpStr = String.valueOf(millSec);
			int result = Integer.parseInt(tmpStr);
			return result;
		}catch(Exception e){
			errinfo = tag + "Exception����������("+beginDate+")��("+endDate+")֮������������̷����쳣��"+e.getMessage();
			throw new Exception(errinfo);
		}
	}
	
	/**TODO:������ֵ��ʽ��Ϊָ���ַ�����ʽ���ַ�������
	 * @param Date date
	 * �����ַ���
	 * @param String format
	 * ���ص������ַ����ĸ�ʽ*/
	public static String convert2Str(Date date,
			                       String format)
	  throws Exception{
		String tag = "[DateUtil.parseDate]";
		String errinfo;
		try{
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			return fmt.format(date);
		}catch(Exception e){
			errinfo = tag + "Exception����\""+format+"\"��ʽ��������\""+date+"\"���̷����쳣��"+e.getMessage();
			throw new Exception(errinfo);
		}
	}
}
