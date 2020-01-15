package com.sjtu.edu.cn.iot.Entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ExtendInfo {
    @JSONField(name = "联系方式")
    private String contactMethod;

    @JSONField(name = "联系地址")
    private String contactAddress;
}
