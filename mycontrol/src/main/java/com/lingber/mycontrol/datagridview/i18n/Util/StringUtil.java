package com.lingber.mycontrol.datagridview.i18n.Util;

public class StringUtil {
    public static int string2int(String str) {
        return string2int(str, 0);
    }

    public static int string2int(String str, int def) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
        }
        return def;
    }

    public static Double string2Double(String str){
        try {
            return  Double.parseDouble(str);
        }catch (Exception e) {
        }
        return 0D;
    }

    public static void main(String[] args) {
        System.out.println(string2Double("66.546512"));
    }
}
