package com.belongfeng.ssm.sms_service;

/**
 * 第三方网站短信发送服务类
 * <p>
 * Created by zengxf on 2019/8/5.
 */
public interface SmsService {

    String domain();

    SendResult send(String mobile);

}
