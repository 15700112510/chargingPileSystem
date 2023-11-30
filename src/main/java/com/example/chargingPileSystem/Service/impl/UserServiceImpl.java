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
    public R<?> login(String userName) {
        UserInfo userInfo = userMapper.queryUser(userName);
        if (userInfo != null) {
            if (userInfo.getChargingPileId() != null) {
                System.out.println(userMapper.queryPile(userName));
                return R.ok(userMapper.queryPile(userName));
            }else
                return R.fail(ErrorEnum.CHARGING_PLIE_ID_NO_CONNECT_ERROR, "充电桩未连接");
        } else {
            // 用户不存在
            return R.fail(ErrorEnum.USERNAME_NO_EXIST_ERROR, "用户不存在");
        }
    }

    @Override
    public R<?> register(String userName,String chargingPileId){
        userMapper.register(userName,chargingPileId);
        userMapper.flush(userName);
        return R.ok();
    }
}
