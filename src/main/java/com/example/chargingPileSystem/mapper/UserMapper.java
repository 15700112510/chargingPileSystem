package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.form.LoginForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 根据手机号查询用户信息
    public UserInfo queryUserByPhone(long userPhone);

    // 查询桩主信息
    public LoginForm queryPile(String userName);


    // 注册用户
    public int register(UserInfo userInfo);

    // 查询用户openid
    public String queryUserOpenId(String chargingPileId);
}
