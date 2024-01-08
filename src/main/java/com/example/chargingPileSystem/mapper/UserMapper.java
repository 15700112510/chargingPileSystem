package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.form.LoginForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserInfo queryUserByPhone(long userPhone);

    public LoginForm queryPile(String userName);

    public int register(UserInfo userInfo);

    public String queryUserOpenId(String chargingPileId);
}
