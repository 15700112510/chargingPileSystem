package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户充值操作
 * @author kongling
 * @package com.suda.platform.controller.app
 * @date 2019-06-17  11:21
 * @project charging_pile_cloud
 */
@RestController
@RequestMapping("/charging/app/WxPay")
@Slf4j
public class AppWxPayController {


    @Autowired
    WxPayService wxPayService;
    @Autowired
    private ChargingService stockUserChargeService;
    @Resource
    private UserMapper userMapper;

    /**
     * 用户小程序充值
     */

    @PostMapping( "/wxAppPay")
    public Object wxPay(StockUserCharge charge) throws WxPayException {
        System.out.println("用户充值参数："+charge);
        if (charge.getUserPhone() == null || charge.getUserPhone().equals("") ){
            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR,"用户openid为空");
        }else if (userMapper.queryUserByPhone(charge.getUserPhone()) == null ){
            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR,"用户openid为不存在");
        }
        Object result= stockUserChargeService.createUserCharge(charge);

        return result;
    }




    /**
     * 微信充值回调
     */
    @ResponseBody
    @RequestMapping("/wxBack")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("进入回调");
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            // 结果正确
            String orderId = result.getOutTradeNo();
            String tradeNo = result.getTransactionId();
            String deviceInfo = result.getDeviceInfo();
            String openId = result.getOpenid();
            int totalfeee = result.getTotalFee();

//            //自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
//            StockUserCharge stockUserCharge = stockUserChargeService.getOne(new QueryWrapper<StockUserCharge>()
//            .eq("swift_no",orderId));
//            if(stockUserCharge.getWithdrawStatus() == WithdrawStatusEnum.STATUS_4.getCode().byteValue()){
//               //更新付款状态 用户付款成功
//                stockUserCharge.setTranId(tradeNo);
//                stockUserChargeService.withdrawStatusSuccess(stockUserCharge);
//            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

}
