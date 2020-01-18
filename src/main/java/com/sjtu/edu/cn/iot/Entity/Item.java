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

    @JSONField(name = "所有人")
    private String owner;

    public Item(String name, String tableAddress, String type, String user){
        this.name = name;
        this.tableAddress = tableAddress;
        this.deviceType = type;
        this.owner = user;
    }
}
