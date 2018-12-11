package com.wxy.bigdata.common.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtils {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	private static final int BUFFER_SIZE = 4*1024;
	private static final Collection<?> nullData = new ArrayList<>();
	private static int TOKEN_COUNT = 0;
	private synchronized static int incTokenCount() {
		TOKEN_COUNT++;
		if(TOKEN_COUNT > 999999) {
			TOKEN_COUNT = 0;
		}
		return TOKEN_COUNT;
	}
	
	public static String generateToken() {
		DecimalFormat df = new DecimalFormat("000000");
		String left = df.format(incTokenCount());
		
		long ctime = System.currentTimeMillis();
		String right =Long.toHexString(ctime);
		
		return left+right;
	}
	
	public static boolean isNumeric(String str)
	{
		for (int i = 0; i < str.length(); i++)
		{
			//System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}


	
	public static boolean tokenTimeout(String token, int timeout) {
		int millis = timeout*1000;
		
		String hextime = token.substring(6);
		long tokenTime = Long.valueOf(hextime, 16);

		if(System.currentTimeMillis()-tokenTime > millis) {
			return true;
		}
		
		return false;
	}
	
	public static String generateUUID(boolean withSeparator) {
		String uuid = UUID.randomUUID().toString();
		if(!withSeparator) {
			uuid = uuid.replace("-", "");
		}
		return uuid;
		
	}


	
	public static Date parseDate(String dateStr, String format) {
		if(StringUtils.isEmpty(format)){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {

		}
		
		return date;
	}

	public static String formatDate(Date date, String format) {
		try{
			if(StringUtils.isEmpty(format)){
				format = "yyyy-MM-dd HH:mm:ss";
			}
	
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}catch(Exception e) {
			logger.error("Date("+date+") format exception: ", e);
		}
		return null;
	}

	public static String dateConvert(String date,int addDay){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = "";
		try {
			Date newDate = sdf.parse(date);
			Calendar cl = Calendar.getInstance();
			cl.setTime(newDate);
			cl.add(Calendar.DATE, addDay);
			dateStr = sdf.format(cl.getTime());
			return dateStr;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return dateStr;
	}
	
	public static byte[] mergeBytes(byte[] data1, byte[] data2) {
		byte[] bytes = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, bytes, 0, data1.length);
		System.arraycopy(data2, 0, bytes, data1.length, data2.length);
		
		return bytes;
	}
	
	public static byte[] subBytes(byte[] bytes, int offset, int size) {
		byte[] result = new byte[size];
		System.arraycopy(bytes, offset, result, 0, size);
		
		return result;
	}
	
	public static byte[] InputStream2ByteArray(InputStream in){
		byte[] r = null;
		ByteArrayOutputStream outStream = null;
		try{
			outStream = new ByteArrayOutputStream();
			
			byte[] data = new byte[BUFFER_SIZE];
			int count = -1;
			while((count = in.read(data,0,BUFFER_SIZE)) != -1) {
				outStream.write(data, 0, count);
			}
			 
			data = null;
			
			r = outStream.toByteArray();
		}catch(IOException e) {
			logger.error("InputStream2Byte exception", e);
		}finally{
			try {
				if(outStream != null) {
					outStream.close();
					outStream = null;
				}
			} catch (IOException e) {
				logger.error("InputStream2Byte close output stream exception", e);
			}
		}
		
		return r;
	}
	
	public static byte[] loadByteArray(String filename) {  
		FileChannel fc = null;  
		RandomAccessFile randomAccessFile = null;
		MappedByteBuffer byteBuffer = null;
		try{  
			randomAccessFile = new RandomAccessFile(filename,"r");
			fc = randomAccessFile.getChannel();  
			byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();  
			byte[] result = new byte[(int)fc.size()];  
			if (byteBuffer.remaining() > 0) {  
				byteBuffer.get(result, 0, byteBuffer.remaining());  
			}
			
			return result;  
		}catch (IOException e) {
			logger.error("CommonUtil.loadByteArray", e);
		}finally{  
			try{
				if(randomAccessFile != null) {
					randomAccessFile.close();
					randomAccessFile = null;
				}
				if(fc != null) {
					fc.close();
					fc = null;
				}
				if(byteBuffer != null) {
					byteBuffer.clear();
					byteBuffer = null;
				}
				
				System.gc();
			}catch (IOException e) {
				logger.info("CommonUtil.loadByteArray", e);
			}  
		}  
		
		return null;
	}
	
	public static boolean saveFile(String file, byte[] content) {
		FileOutputStream os =null;
		try{
			File f = new File(file);
			String path = f.getParent();
			f = new File(path);
			if(!f.exists()) {
				if(!f.mkdirs()){
					return false;
				}
			}
			
		    os = new FileOutputStream(file);
			os.write(content);
			
		}catch(IOException e) {
			logger.info("Save file ("+file+") exception: ", e);
			
			return false;
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
			}
		}
		return true;
	}
	
	public static boolean deleteFile(String file) {
		try{
			File f = new File(file);
			File path = f.getParentFile();
			boolean r = f.delete();
			if(!r) {
				r = f.delete();
			}
			if(r) {
				deleteEmptyFolder(path);
			}
			return r;
		}catch(Exception e) {
			logger.info("Delete file ("+file+") exception:", e);
		}
		return false;
	}
	
	private static void deleteEmptyFolder(File path) {
		File parent = path.getParentFile();
		if(path.delete()){
			deleteEmptyFolder(parent);
		}
	}

	private static int formateScale(Integer sc) {
		return (sc == null || sc.intValue() <= 0) ? 2 : sc.intValue();
	}

	public static String mathDataFormate(float f, Integer sc) {
		BigDecimal bg = new BigDecimal(f);
		int scale = formateScale(sc);
		Double f1 = bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1.toString();
	}

	public static String mathDataFormate(double f, Integer sc) {
		BigDecimal bg = new BigDecimal(f);
		int scale = formateScale(sc);
		Double f1 = bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1.toString();
	}

	public static String mathDataFormate(BigDecimal bg, Integer sc) {
		int scale = formateScale(sc);
		Double f1 = bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1.toString();
	}

	/**
	 * 将带有指数形式的String对象,转换为不带指数形式的字符串返回
	 * @param str 指数形式的对象
	 * @param scale 指定的数值精度
	 * @return
	 */
	public static String mathDataFormate(String str,int scale){
		return new BigDecimal(str).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	public static void removeNullData(Collection<?> data) {
		if (nullData.size() <= 0) {
			nullData.add(null);
		}
		if (data != null && data.size() > 0) {
			data.removeAll(nullData);
		}
	}
	
	/**
	 * <p>检测一个对象是否为空, 如果为字符串则会剔除空格进行验证.</p>
	 * <p style="color:red;">字符串数据 <code>""</code> 同样被视为 <code>null</code> .</p>
	 * @param checkObj 需要被检测的对象.
	 * @return 如果对象为空返回 <code>true</code> 不为空返回 <code>false</code> .
	 * */
	public static boolean isNull(Object checkObj) {
		if (checkObj == null) {
			return true;
		}
		
		if (String.class.isAssignableFrom(checkObj.getClass())) {
			if (checkObj.toString().trim().length() < 1 || 
					"".equals(checkObj.toString().trim()))
				return true;
		}
		
		return false;
	}
	
	//转换map中的数组为逗号分割的字符串,转移非法字符
	public static Map<String, Object> parseArrayInMapToString(Map<String, Object> map) {
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = map.get(key);
			if (value instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) value;
				StringBuilder sb = new StringBuilder();
				for (Object j : jsonArray) {
					sb.append(",").append(j);
				}
				if (sb.length() > 1) {
					value = sb.substring(1);
				} else {
					value = "";
				}
			}
			value = parseSqlValue(value);
			map.put(key, String.valueOf(value));
		}
		return map;
	}
	
	//转换map中的数组为逗号分割的字符串,转移非法字符
	public static JSONObject parseArrayInMapToString(JSONObject map) {
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = map.get(key);
			if (value instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) value;
				StringBuilder sb = new StringBuilder();
				for (Object j : jsonArray) {
					sb.append(",").append(j);
				}
				if (sb.length() > 1) {
					value = sb.substring(1);
				} else {
					value = "";
				}
			}
			value = parseSqlValue(value);
			map.put(key, String.valueOf(value));
		}
		return map;
	}
	
	//转化sql中的非法字符
	private static Object parseSqlValue(Object value) {
		if (value instanceof String) {
			value = ((String)value).replace("\'", "\\'");
			return value;
		}
		return value;
	}
	


	/**
	* 获得该月第一天
	* @param year
	* @param month
	* @return
	*/
	public static String getFirstDayOfMonth(int year,int month){
	        Calendar cal = Calendar.getInstance();
	        //设置年份
	        cal.set(Calendar.YEAR,year);
	        //设置月份
	        cal.set(Calendar.MONTH, month);
	        //获取某月最小天数
	        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
	        //设置日历中月份的最小天数
	        cal.set(Calendar.DAY_OF_MONTH, firstDay);
	        //格式化日期
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String firstDayOfMonth = sdf.format(cal.getTime());
	        return firstDayOfMonth;
	    }

/**
* 获得该月最后一天
* @param year
* @param month
* @return
*/
public static String getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
}

    /**
     *
     * @param date String类型  "yyyy-MM"
     * @return String类型 "yyyy-MM-dd"
     */
    public static String getLastDayOfMonth(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CommonUtils.parseDate(date, "yyyy-MM"));
        calendar.add(Calendar.MONTH,1);
        calendar.add(Calendar.DATE,-1);
        Date eDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(eDate);
    }
    /**
     * 判断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 数字格式化
     * 保留几位小数
     * 
     * @return
     */
    public static BigDecimal BigDecimalFormat(int digit,String downOrUp,String nums){
    	try {
    		BigDecimal decimal = new BigDecimal(nums);
    		if("DOWN".equals(downOrUp)){
    			decimal=decimal.setScale(digit,BigDecimal.ROUND_HALF_DOWN);
    		}else{
    			decimal=decimal.setScale(digit,BigDecimal.ROUND_HALF_UP);
    		}
    		return decimal;
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    	
    }
    /**
     * 计算两个时间相差天数
     * @param sdate
     * @param edate
     * @return
     */
    public static int daysBetween(Date sdate,Date edate){     
        Calendar cal = Calendar.getInstance();     
        cal.setTime(edate);     
        long etime = cal.getTimeInMillis();                  
        cal.setTime(sdate);     
        long stime = cal.getTimeInMillis();          
        long between_days=(etime-stime)/(1000*3600*24);     
        return Integer.parseInt(String.valueOf(between_days));            
    }  
    /**
     * 计算两个时间相差月数
     * @param sdate
     * @param edate
     * @return
     */
    public static int calculateMonthIn(Date sdate, Date edate) {
    	Calendar cals = new GregorianCalendar();
    	cals.setTime(sdate);
    	Calendar cale = new GregorianCalendar();
    	cale.setTime(edate);
    	int c =
    	(cale.get(Calendar.YEAR) - cals.get(Calendar.YEAR)) * 12 + cale.get(Calendar.MONTH)
    	- cals.get(Calendar.MONTH);
    	return c;
    }

	public static String generate16UUID(boolean withSeparator) {
		String uuid = UUID.randomUUID().toString();
		if(!withSeparator) {
			uuid = uuid.replace("-", "").substring(16);
		}
		return uuid;

	}
	/**
	 * 判断文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean judeFileExists(String path) {
		boolean flag= false;
		File file=new File(path);
		if (file.exists()) {
		       flag=true;
		} else {
			 flag=true;        
		}
		return flag;
	}
	 
    
}
