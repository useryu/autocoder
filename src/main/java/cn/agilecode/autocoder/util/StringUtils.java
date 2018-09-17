package cn.agilecode.autocoder.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * 字符串校验工具类
* @ClassName: StringUtil
* @Description:
 */
public class StringUtils
{
	private  static  final Logger  log=Logger.getLogger(StringUtils.class);
	
	public static final String nvl(String src, String defaultValue)
	{
		if ((src != null) && (src.length() > 0))
		{
			return src;
		}
		return defaultValue;
	}

	public static final String nvl(String src)
	{
		return nvl(src, "");
	}

	/**
	 * 判断是否为null，或长度等于0
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isEmpty(String str)
	{
		if (str == null || str.trim().equals(""))
			return true;

		return false;
	}
	
	

	/***
	 * 获取小于数值的字符串
	 * 
	 * **/
	public static final String getMaxLengthStr(String str,int length ){
		if(str==null){
			return "";
		}
		int l=str.length();
		if(l<length){
			return str.substring(0,str.length());
		}else{
			return str.substring(0, length);
		}
	}
	
	
	/**
	 * url encode
	 * 
	 * @param str
	 * @return
	 */
	public static final String urlEncode(String str, String charSet)
	{
		if (str == null)
			return null;

		try
		{
			return URLEncoder.encode(str, charSet);
		} catch (UnsupportedEncodingException e)
		{
			log.error("urlEncode error", e);
			return null;
		}
	}
	
	/**
	 * url encode
	 * 
	 * @param str
	 * @return
	 */
	public static final String urlDecode(String str, String charSet)
	{
		String re="";
		if (str == null)
			return null;
		try{
		 re=URLDecoder.decode(str, charSet);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return re;
	}

	public static void main(String[] args) {
		System.out.println(urlDecode("%E6%9D%9F%E5%B8%A6%E7%BB%93%E5%8F%91","utf-8"));
	}
}