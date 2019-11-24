package com.javateam019.util;

/**
 * String tool class
 */
public class StringUtil {

    /**
     * judge if input is empty
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (str ==null || "".equals(str.trim())){
            return true;
        }else{
            return  false;
        }
    }

    /**
     * judge if input is not empty
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        if(str!= null && !"".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }


}
