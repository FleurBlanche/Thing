package com.sjtu.edu.cn.iot.Entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Item {
    @JSONField(name = "物名")
    private String name;

    @JSONField(name = "物功能表")
    private String tableAddress;

    @JSONField(name = "物类型")
    private String deviceType;

    public Item(String name, String tableAddress, String type){
        this.name = name;
        this.tableAddress = tableAddress;
        this.deviceType = type;
    }
}
