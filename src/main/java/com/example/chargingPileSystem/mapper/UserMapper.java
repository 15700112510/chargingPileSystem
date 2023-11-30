package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.form.LoginForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserInfo queryUser(String userName);
    public String flush(String userName);

    public LoginForm queryPile(String userName);

    public int register(String userName,String chargingPileId);

    public String queryUserOpenId(String chargingPileId);
}
