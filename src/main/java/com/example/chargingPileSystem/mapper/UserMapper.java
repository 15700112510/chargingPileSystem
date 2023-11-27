package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserInfo queryUser(String UserOpenId);
    public int insertChargingPileId(String UserOpenId,String chargingPlieId);
    public String queryUserOpenId(String chargingPileId);
}
