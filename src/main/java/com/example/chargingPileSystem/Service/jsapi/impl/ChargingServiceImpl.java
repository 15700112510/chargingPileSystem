package com.example.chargingPileSystem.Service.jsapi.impl;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingMapper;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;

@Service
public class ChargingServiceImpl implements ChargingService {
    @Resource
    private WxPayService wxPayService;
    @Resource
    private ChargingMapper chargingMapper;
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;
    @Resource
    private UserMapper userMapper;

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

        WxPayUnifiedOrderRequest orderRequest =new WxPayUnifiedOrderRequest();
        String orderNum = System.currentTimeMillis()+ "";
        //用户openid
        orderRequest.setOpenid(charge.getUserPhone());
        //订单号
        orderRequest.setOutTradeNo(charge.getSwiftNo());
        //交易类型（小程序）
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);

        //orderRequest.setSpbillCreateIp(HttpRequestUtil.getIpAddr(request));
        //订单金额
        orderRequest.setTotalFee(charge.getFee());
        //订单描述
        orderRequest.setBody("充电付款");
        //随机字符串，确保每次付款请求都不同
        orderRequest.setNonceStr(String.valueOf(System.currentTimeMillis())+String.valueOf(new Random().nextInt(1000)));
        System.out.println("微信回调函数"+wxPayService.getPayBaseUrl());
        orderRequest.setNotifyUrl(wxPayService.getPayBaseUrl());
        WxPayMpOrderResult wxPayMpOrderResult = wxPayService.createOrder(orderRequest);

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

}
