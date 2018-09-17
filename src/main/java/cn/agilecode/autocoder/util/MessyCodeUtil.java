/**  
 * @Title: MessyCodeUtil.java
 * @Package com.qingmayun.qmart.util
 * @Description: 
 * @author Elisa  
 * @date 2014-12-18 上午10:44:51
 * @version 深圳市惠捷通科技有限公司  
 */
package cn.agilecode.autocoder.util;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;


/**
 * @ClassName: MessyCodeUtil
 * @Description: 乱码工具类
 */
public class MessyCodeUtil
{

	private final static Logger log = Logger.getLogger(MessyCodeUtil.class);

	public static String isoToUtf8(String str)
	{
		log.info("转之前：" + str);
		try
		{
			str = new String(str.getBytes("ISO8859-1"), "UTF-8");
			log.info("转之后：" + str);
			return str;
		} catch (UnsupportedEncodingException e)
		{
			log.error(e.toString(),e);
			return null;
		}
	}

}
