package com.lingber.mycontrol.datagridview;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author :ShuboLin
 * @CreatTime: 2020-06-02 15:21.
 * @Description: 工具类
 */

public class LingberUtil<T> {

    /**根据字段名称获取字段值**/
    public static String getFieldValueByName(String name, Object object){
        if (object == null||name == null){
            return null;
        }
        Field[] fields = object.getClass().getDeclaredFields();
        String[] types1={"int","java.lang.String","boolean","char","float","double","long","short","byte"};
        String[] types2={"java.lang.Integer","java.lang.String","java.lang.Boolean","java.lang.Character","java.lang.Float","java.lang.Double","java.lang.Long","java.lang.Short","java.lang.Byte"};
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            // 字段名
            if(fields[j].getName().equals(name)){
                // 字段值
                for(int i=0;i<types1.length;i++){
                    if(fields[j].getType().getName().equalsIgnoreCase(types1[i])|| fields[j].getType().getName().equalsIgnoreCase(types2[i])){
                        try {
                            if(fields[j].get(object)!=null){
                                return fields[j].get(object)+"";
                            }else{
                                return null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @author ShuboLin
     * @CreatTime 2020-06-02 17:52
     * @Param1 数据源
     * @Param2 排第几个
     * @Param3 排序方法
     * @Description 排序数组
     */
    public static void arrSort(List<String[]> list, final int sortIndex, final String sortType){
        Collections.sort(list, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                int ret = 0;
                if((o1.length==o2.length)&&sortIndex<=o1.length){
                    if(o1[sortIndex]!=null&&o2[sortIndex]!=null){
                        if(sortType!=null&&"asce".equals(sortType)){
                            // 升序
                            ret = o1[sortIndex].compareTo(o2[sortIndex]);
                        }else{
                            // 降序
                            ret = o2[sortIndex].compareTo(o1[sortIndex]);
                        }
                    }
                }
                return ret;
            }
        });
    }

    /**
     * @author ShuboLin
     * @CreatTime 2020-06-02 17:52
     * @Param1 数据源
     * @Param2 字段名
     * @Param3 排序方法
     * @Description 排序对象
     */
    public static void objectSort(List<Object> list, final String fieldName, final String sortType){
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                int ret = 0;
                if(sortType!=null&&fieldName!=null&&"asce".equals(sortType)){
                    // 升序
                    ret = getFieldValueByName(fieldName, o1).compareTo(LingberUtil.getFieldValueByName(fieldName, o2));
                }else{
                    // 降序
                    ret = getFieldValueByName(fieldName, o2).compareTo(LingberUtil.getFieldValueByName(fieldName, o1));
                }
                return ret;
            }
        });
    }
}
