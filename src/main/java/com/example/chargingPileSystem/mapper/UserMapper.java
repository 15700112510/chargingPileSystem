package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserInfo queryUser(String userName);

    public int register(String userName,String chargingPlieId);

    public String queryUserOpenId(String chargingPileId);
}
