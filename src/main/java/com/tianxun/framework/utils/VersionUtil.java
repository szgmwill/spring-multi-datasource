package com.tianxun.framework.utils;

public class VersionUtil {
        
    /**
     * 判断是否符合匹配规则
     * @param rule 0 为包含 1为排除 
     * @param ruleStr
     * @param matchStr
     * @return
     */
    public static boolean isMatchRule (int ruleType , String ruleStr , String ver , String uid){
        if(0 == ruleType){
            if(ruleStr.indexOf("ver_" + ver) > -1 || ruleStr.indexOf("uid_" + uid) > -1 
                    || ruleStr.indexOf("mix_" + ver + "_" + uid) > -1){
                return true;
            }else{
                return false;
            }
        }
        if(1 == ruleType){
            if(ruleStr.indexOf("ver_" + ver) > -1 || ruleStr.indexOf("uid_" + uid) > -1 
                    || ruleStr.indexOf("mix_" + ver + "_" + uid) > -1){
                return false;                
            }else{
                return true;
            }
        }
        return false;
    }
    
    //判断version1是不是 大于或者等于 version2
    public static boolean compareVersion(String version1 , String version2){
        if(version1.equals(version2)){
            return true;
        }
        
        String[] version1Arr = version1.split("\\.");
        String[] version2Arr = version2.split("\\.");        
        try{
            for(int i = 0; i < version1Arr.length; i++){
                if(version2Arr.length < i){
                    return false;
                }
                if(Integer.parseInt(version1Arr[i]) > Integer.parseInt(version2Arr[i])){
                    return true;
                }else if(Integer.parseInt(version1Arr[i]) < Integer.parseInt(version2Arr[i])){
                    return false;
                }
            }
        }catch (Exception e) {
            return false;
        }        
        return false;
    }
}
