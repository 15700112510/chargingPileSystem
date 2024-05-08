package com.example.chargingPileSystem.Service.jsapi.impl;

import cn.hutool.core.date.DateUtil;
import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.Service.jsapi.PaymentService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.constant.PaymentConstant;
import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.mapper.PaymentMapper;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {


    @Resource
    private WxPayService wxPayService;
    @Resource
    private PaymentMapper paymentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ChargingService chargingService;

    /**
     * 创建预订单
     */
    @Override
    public Object createPreOrder(PaymentOrder paymentOrder) {
        if (paymentOrder.getUserOpenid() == null || paymentOrder.getUserOpenid().equals("")) {
            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR, "用户openid为空");
        } else if (userMapper.queryUserByUserOpenid(paymentOrder.getUserOpenid()) == null) {
            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR, "用户openid为不存在");
        }
        if (paymentOrder.getAmount() == null || paymentOrder.getAmount().equals("")
                || paymentOrder.getAmount().equals("0"))
            return R.fail(ErrorEnum.AMOUNT_EMPTY_ERROR, "金额为空");
        if (paymentOrder.getOutTradeNo() == null || paymentOrder.getOutTradeNo().equals(""))
            return R.fail(ErrorEnum.OUT_TRADE_NO_EMPTY_ERROR, "商户订单号为空");

        String outTradeNo = paymentOrder.getOutTradeNo();
        String userOpenid = paymentOrder.getUserOpenid();
        int amount = paymentOrder.getAmount();
        String chargingPileId = paymentOrder.getChargingPileId();
        if (chargingPileId == null || chargingPileId.equals("")) {
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
        orderRequest.setNotifyUrl(PaymentConstant.PAY_CALLBACK_URL);
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
        paymentMapper.insertPreOrder(outTradeNo, userOpenid, chargingPileId, amount, PaymentConstant.PAY_PROCESSING);
        return wxPayMpOrderResult;
    }

    /**
     * 支付回调
     *
     * @param xmlData
     */
    @Override
    public void PayCallback(String xmlData) {

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
        WxPayOrderNotifyV3Result.Amount price = parsedResult.getAmount();


        //判断是否预订单是否存在
        PaymentOrder paymentOrder = paymentMapper.selectByOrderNo(outTradeNo);
        if (paymentOrder == null) {
            log.info("支付回调订单不存在");
            return;
        }
        if (paymentOrder.getStatus() == PaymentConstant.PAY_SUCCESS) {
            log.info("支付回调订单已支付");
            return;
        }

        //创建充电记录ID
        String chargingRecordId = outTradeNo;

        paymentOrder.setTransactionId(transactionId);
        paymentOrder.setChargingRecordId(chargingRecordId);
        paymentOrder.setStatus(PaymentConstant.PAY_SUCCESS);
        //更新订单支付成功状态
        paymentMapper.updatePay(paymentOrder);

        //开启充电桩
        try {
            chargingService.openPile(paymentOrder.getChargingPileId());
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("开始退款");
            Thread.sleep(3000);
            redRefundPay(paymentMapper.selectByOrderNo(outTradeNo), paymentOrder.getAmount());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 退款
     *
     * @param paymentOrder
     * @param refundAmount
     * @throws WxPayException
     */
    @Override
    public void redRefundPay(PaymentOrder paymentOrder, int refundAmount) throws WxPayException {
        if (paymentOrder == null) {
            throw new RuntimeException("支付订单不存在");
        } else if (paymentOrder.getStatus() == PaymentConstant.PAY_REFUND_SUCCESS) {
            System.out.println("退款订单已退款");
            return;
        }
        //原订单支付金额
        int amount = paymentOrder.getAmount();
        String outRefundNo = "return_" + paymentOrder.getOutTradeNo();
        //微信支付-申请退款请求参数
        WxPayRefundV3Request orderRequest = new WxPayRefundV3Request();
        WxPayRefundV3Request.Amount am = new WxPayRefundV3Request.Amount();
        //原订单金额
        am.setTotal(amount);
        am.setCurrency("CNY");
        //退款金额 注意：退款金额，单位为分，只能为整数，不能超过原订单支付金额。
        am.setRefund(refundAmount);
        //金额信息
        orderRequest.setAmount(am);
        //transaction_id:微信支付订单号
        orderRequest.setTransactionId(paymentOrder.getTransactionId());
        //商户订单号
        orderRequest.setOutRefundNo(outRefundNo);
        paymentOrder.setOutRefundNo(outRefundNo);
        orderRequest.setNotifyUrl(PaymentConstant.PAY_REFUND_CALLBACK_URL);

        //调用微信V3退款API
        WxPayRefundV3Result result = wxPayService.refundV3(orderRequest);
        String status = result.getStatus();
        switch (status) {
            case "SUCCESS":
                System.out.println("退款成功");
                paymentOrder.setStatus(PaymentConstant.PAY_SUCCESS);
                //更新订单支付成功状态
                paymentMapper.updatePay(paymentOrder);
                break;
            case "CLOSED":
                System.out.println("退款关闭");
                paymentOrder.setStatus(PaymentConstant.PAY_REFUND_FAIL);
                //更新订单支付关闭状态
                paymentMapper.updatePay(paymentOrder);
                break;
            case "PROCESSING":
                System.out.println("退款处理中");
                paymentOrder.setStatus(PaymentConstant.PAY_REFUND_PROCESSING);
                //更新订单支付成功状态
                paymentMapper.updatePay(paymentOrder);
                break;
            case "ABNORMAL":
                System.out.println("退款异常");
                paymentOrder.setStatus(PaymentConstant.PAY_REFUND_FAIL);
                //更新订单支付成功状态
                paymentMapper.updatePay(paymentOrder);
                break;
            default:
                System.out.println("退款失败");
                paymentOrder.setStatus(PaymentConstant.PAY_REFUND_FAIL);
                //更新订单支付成功状态
                paymentMapper.updatePay(paymentOrder);
                break;
        }
    }

    /**
     * 退款回调
     *
     * @param xmlData
     */
    @Override
    public void redRefundNotify(String xmlData) {
        try {
            WxPayRefundNotifyV3Result refundResult = wxPayService.parseRefundNotifyV3Result(xmlData, null);
            String orderId = refundResult.getResult().getOutTradeNo();//拿到订单号获取订单
            PaymentOrder paymentOrder = paymentMapper.selectByOrderNo(orderId);
            if (paymentOrder == null) {
                System.out.println("订单不存在");
                return;
            } else if (paymentOrder.getStatus() == PaymentConstant.PAY_REFUND_SUCCESS) {
                System.out.println("订单已退款");
                return;
            }
            paymentOrder.setStatus(PaymentConstant.PAY_REFUND_SUCCESS);
            paymentMapper.updatePay(paymentOrder);
            System.out.println("退款成功" + orderId);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
    }

}
