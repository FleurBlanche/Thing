package com.sjtu.edu.cn.iot.Entity;

public class Thing {
    private String interfaceVersion = "v1.0";
    private String encodingStandard = "utf-8";
    private BasicInfo basicInfo;
    private PropertyInfo propertyInfo;
    private ServiceInfo serviceInfo;
    private ExtendInfo extendInfo;

    public String toJson() {
        //转变未json字符串
        StringBuilder jsonBuff = new StringBuilder();
        jsonBuff.append("{");
        jsonBuff.append("encoding standard")


        return jsonBuff.toString();
    }
}
