package com.debug.mooc.dubbo.two.server.data;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: corey
 * @Date: 2019/4/1 14:21
 **/
@Data
@ToString
public class DubboRecordResponse implements Serializable{

    private Integer code;

    private String msg;

    private Integer data;
}