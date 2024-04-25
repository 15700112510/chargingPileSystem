package com.example.chargingPileSystem.domain;

import lombok.Data;
import java.util.Date;

/**
 * <p>
 * 用户出入金表
 * </p>
 *
 * @author kongling
 * @since 2019-06-12
 */
@Data
public class StockUserCharge {
    //流水号
    private String outTradeNo;
    //用户openid
    private String userOpenid;
    //金额
    private int fee;
    //充电桩id
    private String  chargingPileId;

//    //创建时间
//    private Date createTime;

}
