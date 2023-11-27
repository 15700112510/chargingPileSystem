package com.example.chargingPileSystem.Service.impl;

import com.example.chargingPileSystem.Service.UserService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public R<?> login(String userOpenId) {
        UserInfo userInfo = userMapper.queryUser(userOpenId);
        if (userInfo != null) {
            if (userInfo.getChargingPileId() != null) {
                return R.ok(userInfo.getChargingPileId());
            }else
                return R.fail(ErrorEnum.CHARGING_PLIE_ID_NO_EXIST_ERROR, "没有绑定充电桩");
        } else {
            // 用户不存在
            return R.fail(ErrorEnum.USERNAME_NO_EXIST_ERROR, "用户不存在");
        }
    }
    @Override
    public R<?> register(String userOpenId,String chargingPileId){
     //   userMapper.insertChargingPlieId(userOpenId,chargingPileId);
        return R.ok();
    }
}
