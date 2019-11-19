package com.belongfeng.ssm.sms_service;

import lombok.Data;

/**
 * 发送结果
 * <p>
 * Created by zengxf on 2019/8/5.
 */
@Data
public class SendResult {

    Sign sign;
    String errorMsg;
    int httpStatus;
    ApiResult res;

    /*** 与第三方网站通信结果标志 */
    public enum Sign {ERROR, SUCCESS}

    public interface ApiResult {
        /*** 第三方网站发送是否 OK */
        boolean sendOk();
    }

    public boolean sendOk() {
        return res != null && res.sendOk();
    }

}
