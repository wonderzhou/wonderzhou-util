/**
 * 
 */
package com.apusic.skynet.data.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * @author Zhouqi
 * @date 2016年1月15日 上午10:55:48
 * @version V1.0
 */
@SuppressWarnings("unchecked")
public class RegexValidateUtil {
	static boolean flag = false;
	static String regex = "";

	public static boolean check(String str, String regex) {
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	 /**
     * 匹配URL地址
     */
    public final static boolean isUrl(String str) {
        return check(str, "[a-zA-z]+://[^\\s]*");
    }
    
    /**
     * 匹配密码，以字母开头，长度在6-12之间，只能包含字符、数字和下划线。
     */
    public final static boolean isPwd(String str) {
        return check(str, "^[a-zA-Z]\\w{6,12}$");
    }
	
	 /**
     * 验证字符，只能包含中文、英文、数字、下划线等字符。
     */
    public final static boolean stringCheck(String str) {
        return check(str, "^[a-zA-Z0-9\u4e00-\u9fa5-_]+$");
    }
    
    /**
     * 判断英文字符(a-zA-Z)
     */
    public final static boolean isEnglish(String text){
        return check(text, "^[A-Za-z]+$"); 
    }
    
    /**
     * 判断中文字符(包括汉字和符号)
     */
    public final static boolean isChineseChar(String text){
        return check(text, "^[\u0391-\uFFE5]+$");
    }
    
    /**
     * 匹配汉字
     */
    public final static boolean isChinese(String text){
        return check(text, "^[\u4e00-\u9fa5]+$");
    }
    
    /**
     * 是否包含中英文特殊字符，除英文"-_"字符外
     */
    public static boolean isContainsSpecialChar(String text) {
        if(StringUtils.isEmpty(text)) return false;
        String[] chars={"[","`","~","!","@","#","$","%","^","&","*","(",")","+","=","|","{","}","'",
                ":",";","'",",","[","]",".","<",">","/","?","~","！","@","#","￥","%","…","&","*","（","）",
                "—","+","|","{","}","【","】","‘","；","：","”","“","’","。","，","、","？","]"};
        for(String ch : chars){
            if(text.contains(ch)) return true;
        }
        return false;
    }
    
    /**
     * 过滤中英文特殊字符，除英文"-_"字符外
     */
    public static String stringFilter(String text) {
        String regExpr="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
        Pattern p = Pattern.compile(regExpr);
        Matcher m = p.matcher(text);
        return m.replaceAll("").trim();     
    }

	/**
	 * 验证邮箱
	 */
	public static boolean checkEmail(String email) {
		String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		return check(email, regex);
	}

	/**
	 * 验证手机号码
	 * 
	 * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
	 * 联通号码段:130、131、132、136、185、186、145 电信号码段:133、153、180、189
	 * 
	 * @param cellphone
	 * @return
	 */
	public static boolean checkCellphone(String cellphone) {
//		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-3]|[5-9])|(17[0-3]|[5-9]))|((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[[0-3]|5-9])|(17[0-3]|[5-9])-)\\d{8}$";
		String regex = "^((1)|(1-)|(01))\\d{10}$";
		return check(cellphone, regex);
	}

	/**
	 * 验证固话号码
	 * 
	 * @param telephone
	 * @return
	 */
	public static boolean checkTelephone(String telephone) {
		String regex = "^(\\d{3}\\d{7,8})|(\\d{4}\\d{7,8})|(\\d{3}-\\d{7,8})|(\\d{4}-\\d{7,8})|(\\d{7,8})$";
		return check(telephone, regex);
	}

	/**
	 * 验证传真号码
	 * 
	 * @param fax
	 * @return
	 */
	public static boolean checkFax(String fax) {
		String regex = "^(0\\d{2}\\d{8}(\\d{1,4})?)|(0\\d{3}\\d{7,8}(\\d{1,4})?)$";
		return check(fax, regex);
	}

	/**
	 * 验证QQ号码
	 * 
	 * @param QQ
	 * @return
	 */
	public static boolean checkQQ(String QQ) {
		String regex = "^[1-9][0-9]{4,} $";
		return check(QQ, regex);
	}
	public final static boolean isNull(Object[] objs){
        if(objs==null||objs.length==0) return true;
        return false;
    }
    
    public final static boolean isNull(Integer integer){
        if(integer==null||integer==0) return true;
        return false;
    }
    
    public final static boolean isNull(Collection collection){
        if(collection==null||collection.size()==0) return true;
        return false;
    }
    
    public final static boolean isNull(Map map){
        if(map==null||map.size()==0) return true;
        return false;
    }
    
    public final static boolean isNull(String str){
        return str == null || "".equals(str.trim()) || "null".equals(str.toLowerCase());
    }
    
    
    public final static boolean isNull(Long longs){
        if(longs==null||longs==0) return true;
        return false;
    }
    
    public final static boolean isNotNull(Long longs){
        return !isNull(longs);
    }
    
    public final static boolean isNotNull(String str){
        return !isNull(str);
    }
    
    public final static boolean isNotNull(Collection collection){
        return !isNull(collection);
    }
    
    public final static boolean isNotNull(Map map){
        return !isNull(map);
    }
    
    public final static boolean isNotNull(Integer integer){
        return !isNull(integer);
    }
    
    public final static boolean isNotNull(Object[] objs){
        return !isNull(objs);
    }
}
