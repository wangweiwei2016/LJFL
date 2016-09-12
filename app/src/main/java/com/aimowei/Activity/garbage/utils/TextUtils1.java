package com.aimowei.Activity.garbage.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils1 {
	// 将textview中的字符全角化。即将所有的数字、字母及标点
		//全部转为全角字符，使它们与汉字同占两个字节，这样就可以避
		//免由于占位导致的排版混乱问题了。
		// 半角转为全角的代码如下，只需调用即可。
		public static String ToDBC(String input) {
			char[] c = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == 12288) {
					c[i] = (char) 32;
					continue;
				}
				if (c[i] > 65280 && c[i] < 65375)
					c[i] = (char) (c[i] - 65248);
			}
			return new String(c);
		}
		/** 
		     * 去除特殊字符或将所有中文标号替换为英文标号 
		     *  
		     * @param str 
		     * @return 
		     */  
		    public static String stringFilter(String str) {  
	        str = str.replaceAll("【", "[").replaceAll("】", "]")  
		               .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号  
	        String regEx = "[『』]"; // 清除掉特殊字符  
		       Pattern p = Pattern.compile(regEx);  
		       Matcher m = p.matcher(str);  
		       return m.replaceAll("").trim();  
		    } 

}
