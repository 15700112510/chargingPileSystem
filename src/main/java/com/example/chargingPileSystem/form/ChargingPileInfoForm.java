package com.example.chargingPileSystem.form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChargingPileInfoForm {
    /**
     *机床编码
     */
    private String chargingPileId;

    /**
     *id
     */
    private int id;

    /**
     *添加者
     */
    private String createBy;

    /**
     *修改者
     */
    private String updateBy;

    /**
     * status
     */
    private int status;

    /**
     *添加时间
     */
    private Timestamp createTime;

    /**
     *修改时间
     */
    private Timestamp updateTime;


}
