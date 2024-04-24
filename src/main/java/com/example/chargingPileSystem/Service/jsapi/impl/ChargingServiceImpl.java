package com.example.chargingPileSystem.Service.jsapi.impl;

import cn.hutool.core.date.DateUtil;
import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingMapper;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.example.chargingPileSystem.util.wechatUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class ChargingServiceImpl implements ChargingService {

    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private ChargingMapper chargingMapper;
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;

    @Override
    public R<?> state(String chargingPileId) {

        int stage = chargingMapper.queryStage(chargingPileId);
//        System.out.println(stage);
//        return R.ok();
        if (!(stage == 12)) {
            return R.fail(ErrorEnum.CHARGING_PLIE_ID_NO_CONNECT_ERROR,"充电桩未连接");
        }
        StateForm stateForm = chargingMapper.queryChargingPileState(chargingPileId);
        System.out.println(stateForm);
        return R.ok(stateForm);
    }

    //获取充电桩Status  0未插枪  1插枪但是没有任何动作 2插枪有命令下发充电但是未充电（车内预约充电状态/充满自停状态） 3正常充电状态  4离线
    @Override
    public R<?> status(String userOpenId) {
        int status = chargingPileInfoMapper.queryStatus(userOpenId);
        return  R.ok(status);
    }

    //创建预定单
    @Override
    public Object createUserCharge(StockUserCharge charge) throws WxPayException {
        WxPayService wxPayService = getWxPayService(wxPayConfig);
        System.out.println("创建预定单");

        WxPayUnifiedOrderV3Request orderRequest =new WxPayUnifiedOrderV3Request ();
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(charge.getUserOpenid());
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(charge.getFee());
        amount.setCurrency("CNY");
        orderRequest.setDescription("充电付款");
        orderRequest.setOutTradeNo(charge.getOutTradeNo());
        Date nowDate = new Date();
        Date dateAfter = new Date(nowDate.getTime() + 300000);
        String format = DateUtil.format(dateAfter, "yyyy-MM-dd'T'HH:mm:ssXXX");
        orderRequest.setTimeExpire(format);
        orderRequest.setNotifyUrl("");
        orderRequest.setAmount(amount);
        orderRequest.setPayer(payer);

//        System.out.println("微信回调函数"+wxPayService.getPayBaseUrl());
//        orderRequest.setNotifyUrl(wxPayService.getPayBaseUrl());
        log.info("创建订单");


        WxPayUnifiedOrderV3Result.JsapiResult wxPayMpOrderResult = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, orderRequest);
        System.out.println("创建订单完成");


//        addChargeRecord(null,stockUser.getId(), charge.getFee(),
//                stockUserCapitalFund.getStockCode(),PayTypeEnum.STATUS_1, WithdrawStatusEnum.STATUS_4, orderNum);

        return  wxPayMpOrderResult;
    }

    //付款成功
    @Override
    public void withdrawStatusSuccess(StockUserCharge stockUserCharge) {
//        stockUserCharge.setWithdrawStatus(WithdrawStatusEnum.STATUS_2.getCode().intValue());
//        int b= baseMapper.update(stockUserCharge,new UpdateWrapper<StockUserCharge>()
//                .eq("id",stockUserCharge.getId())
//                .eq("withdraw_status",WithdrawStatusEnum.STATUS_4.getCode().intValue()));
//        if(b>0){
//            //更新用户资产
//            StockUserCapitalFund stockUserCapitalFund = stockUserCapitalFundService.getOne(new QueryWrapper<StockUserCapitalFund>()
//                    .eq("stock_user_id",stockUserCharge.getStockUserId()));
//
//            //更新账户资产
//            int j =  stockUserCapitalFundService.updateRechargeByCodeId(stockUserCapitalFund.getId(),stockUserCharge.getFee());
//            //资产流水
//            if(j>0) {
//                stockUserMoneyDetailService.addUserMoneyDetail(
//                        stockUserCapitalFund.getStockUserId(),
//                        stockUserCharge.getFee(),
//                        stockUserCapitalFund.getUsableFund(),
//                        WaterTypeEnum.STATUS_2.getCode(),
//                        FinancialTypeEnum.TYPE_2, "用户微信充值",
//                        stockUserCharge.getId(),
//                        stockUserCapitalFund.getStockCode());
//            }
//        }
    }
    public WxPayService getWxPayService(WxPayConfig wxPayConfig) {
        System.out.println("wxPayService 开始创建");
        WxPayService payService = new WxPayServiceImpl();
        System.out.println("wxPayService 构造方法结束");
        payService.setConfig(wxPayConfig);
        System.out.println("wxPayService 创建完成");
        System.out.println("wx04a5a6484e9716c2".equals(payService.getConfig().getAppId()));
        return payService;
    }

}
