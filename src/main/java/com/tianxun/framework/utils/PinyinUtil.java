/**
 * 
 */
package com.tianxun.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;



/**
 * 
 * 拼音工具类
 * 
 * @author michael.chen
 *
 */
public class PinyinUtil {
    
    public static final Logger logger = Logger.getLogger(PinyinUtil.class);
    
    /**
     * 
     * 返回给定（包含简繁体汉字的）字符串的拼音(小写)和拼音缩写(大写)
     * 
     * @param str
     * @param strsEn
     * @return
     */
    public static String[] toPinyin(String str, String...strsEn) {
        
        String[] strsPinyin = null;
        
        if(StringUtils.isEmpty(str)) {
            return strsPinyin; 
        }
        
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        
        int nLen = str.length();
        String strPinyin = "";
        String strShortPinyin = "";
        for(int i=0; i<nLen; i++) {
            try {
                String[] arrPinyin = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i), outputFormat);
                if(arrPinyin == null) {
                    continue;
                }
                String pinyin = null;
                if(strsEn.length > 0 && StringUtils.isNotBlank(strsEn[0])) {
                    pinyin = chooseStr(arrPinyin, strsEn[0]);                    
                } else {
                    pinyin = arrPinyin[0];    
                }                
                strPinyin += pinyin;
                strShortPinyin += pinyin.substring(0, 1).toUpperCase();
            } catch(Exception e) {
                ExceptionLogUtil.logExceptionInfo(logger, e);
            }
        }
        
        strsPinyin = new String[]{strPinyin, strShortPinyin};
        return strsPinyin;
    }
    
    private static String chooseStr(String[] arrPinyin, String strsEn) {
        String strs = strsEn.toLowerCase(); 
        for(String pinyin : arrPinyin) {
            if(strs.contains(pinyin)) {
                return pinyin;
            }
        }
        return arrPinyin[0];
    }
    
    public static void main(String[] args) {
    	System.out.println(toPinyin("顾国庆")[1]);
    }
}
