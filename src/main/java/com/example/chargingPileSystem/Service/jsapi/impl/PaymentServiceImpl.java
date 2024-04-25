package com.example.chargingPileSystem.Service.jsapi.impl;

import cn.hutool.core.date.DateUtil;
import com.example.chargingPileSystem.Service.jsapi.PaymentService;
import com.example.chargingPileSystem.constant.PaymentConstant;
import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.example.chargingPileSystem.mapper.PaymentMapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private PaymentMapper paymentMapper;
    /**
     * 创建预订单
     */
    @Override
    public Object createPreOrder(PaymentOrder paymentOrder) {
        //        if (charge.getUserOpenid() == null || charge.getUserOpenid().equals("") ){
//            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR,"用户openid为空");
//        }else if (userMapper.queryUserByPhone(charge.getUserOpenid()) == null ){
//            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR,"用户openid为不存在");
//        }

        WxPayService wxPayService = getWxPayService(wxPayConfig);

        String outTradeNo = paymentOrder.getOutTradeNo();
        String userOpenid = paymentOrder.getUserOpenid();
        int amount = paymentOrder.getAmount();
        String chargingPileId = paymentOrder.getChargingPileId();
        if (chargingPileId ==null || chargingPileId.equals("")){
            chargingPileId = "null";
        }

        //参数注入
        WxPayUnifiedOrderV3Request orderRequest = new WxPayUnifiedOrderV3Request();
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(userOpenid);
        WxPayUnifiedOrderV3Request.Amount preAmount = new WxPayUnifiedOrderV3Request.Amount();
        preAmount.setTotal(amount);
        preAmount.setCurrency("CNY");
        orderRequest.setDescription("充电付款");
        orderRequest.setOutTradeNo(outTradeNo);
        Date nowDate = new Date();
        Date dateAfter = new Date(nowDate.getTime() + 300000);
        String format = DateUtil.format(dateAfter, "yyyy-MM-dd'T'HH:mm:ssXXX");
        orderRequest.setTimeExpire(format);
        orderRequest.setAmount(preAmount);
        orderRequest.setPayer(payer);

        //创建预订单
        WxPayUnifiedOrderV3Result.JsapiResult wxPayMpOrderResult;
        try {
            wxPayMpOrderResult = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, orderRequest);
        } catch (WxPayException e) {
            paymentMapper.insertPreOrder(outTradeNo, userOpenid, chargingPileId, amount, PaymentConstant.PAY_FAIL);
            log.info("创建订单异常" + e.getMessage());
            throw new RuntimeException(e);
        }
        //数据库插入预订单数据
        paymentMapper.insertPreOrder(outTradeNo, userOpenid, chargingPileId,  amount, PaymentConstant.PAY_PROCESSING);
        return wxPayMpOrderResult;
    }



    @Override
    public void PayCallback(String xmlData) {
        WxPayService wxPayService = getWxPayService(wxPayConfig);

        // 解析支付回调
        WxPayOrderNotifyV3Result result;
        try {
            result = wxPayService.parseOrderNotifyV3Result(xmlData, null);
        } catch (WxPayException e) {
            log.info("解析支付回调异常" + e.getMessage());
            throw new RuntimeException(e);
        }
        WxPayOrderNotifyV3Result.DecryptNotifyResult parsedResult = result.getResult();

        //获取订单信息
        String userOpenid = parsedResult.getPayer().getOpenid();
        String outTradeNo = parsedResult.getOutTradeNo();
        String transactionId = parsedResult.getTransactionId();

        //判断是否预订单是否存在
        PaymentOrder paymentOrder = paymentMapper.selectByOrderNo(userOpenid,outTradeNo);
        if (paymentOrder == null){
            log.info("支付回调订单不存在");
            return;
        }
        if (paymentOrder.getStatus() == 0){
            log.info("支付回调订单已支付");
            return;
        }

        String chargingRecordId = outTradeNo;

        //更新订单支付成功状态
        paymentMapper.updateOrderStatus(outTradeNo, transactionId, chargingRecordId, PaymentConstant.PAY_SUCCESS);
    }

    @Override
    public void redRefundPay(PaymentOrder paymentOrder,int refundAmount) throws WxPayException {
        WxPayService wxPayService = getWxPayService(wxPayConfig);
        //原订单支付金额
        int amount = paymentOrder.getAmount();
        //微信支付-申请退款请求参数
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        WxPayRefundV3Request.Amount am = new WxPayRefundV3Request.Amount();
        //原订单金额
        am.setTotal(amount);
        am.setCurrency("CNY");
        //退款金额 注意：退款金额，单位为分，只能为整数，不能超过原订单支付金额。
        am.setRefund(refundAmount);
        //金额信息
        request.setAmount(am);
        //transaction_id:微信支付订单号
        request.setTransactionId(paymentOrder.getTransactionId());
        //商户订单号
        request.setOutRefundNo("return_"+paymentOrder.getOutTradeNo());
//        request.setNotifyUrl(WxConfig.refund_notify_url);

        //调用微信V3退款API
        WxPayRefundV3Result result = wxPayService.refundV3(request);
        String status = result.getStatus();
        switch (status) {
            case "SUCCESS":
                paymentMapper.updateOrderStatus(outTradeNo, transactionId, chargingRecordId, PaymentConstant.PAY_SUCCESS);
                break;
            case "CLOSED":
                paymentMapper.updateOrderStatus(outTradeNo, transactionId, chargingRecordId, PaymentConstant.PAY_SUCCESS);
                break;
            case "PROCESSING":
                paymentMapper.updateOrderStatus(outTradeNo, transactionId, chargingRecordId, PaymentConstant.PAY_SUCCESS);
                break;
            case "ABNORMAL":
                paymentMapper.updateOrderStatus(outTradeNo, transactionId, chargingRecordId, PaymentConstant.PAY_SUCCESS);
                break;
            default:
                paymentMapper.updateOrderStatus(outTradeNo, transactionId, chargingRecordId, PaymentConstant.PAY_SUCCESS);
                break;
        }
    }


    /**
     * 创建微信支付服务对象
     */
    public WxPayService getWxPayService(WxPayConfig wxPayConfig) {
        WxPayService payService = new WxPayServiceImpl();
        payService.setConfig(wxPayConfig);
        return payService;
    }
}
