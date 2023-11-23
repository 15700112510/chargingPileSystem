package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserInfo queryUserOpenId(String UserOpenId);
    public int insertChargingPlieId(String UserOpenId,String chargingPlieId);
}
