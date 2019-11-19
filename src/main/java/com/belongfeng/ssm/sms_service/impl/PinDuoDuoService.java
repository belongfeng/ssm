package com.belongfeng.ssm.sms_service.impl;

import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
public class PinDuoDuoService extends AbstractSmsService implements SmsService {

    public PinDuoDuoService() {
        super("拼多多", "https://imsapi.pinduoduo.com");
    }

    @Override
    public void send(String mobile, SendResult result) throws IOException {
        ApiService service = super.create(ApiService.class);
        Map<String, String> headers = Map.of(
                "origin", "https://ims.pinduoduo.com",
                "referer", "https://ims.pinduoduo.com/",
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"
        );
        Request request = new Request(mobile);
        Call<Result> call = service.send(headers, request);
        Response<Result> res = call.execute();
        result.setHttpStatus(res.code());
        result.setRes(res.body());
    }

    interface ApiService {
        @POST("/ims/entry/sendSms/sendMobileVerificationCode")
        Call<Result> send(
                @HeaderMap Map<String, String> headers,
                @Body Request request
        );
    }

    @Data
    static class Request {
        String appKey = "PDDFRONTLOGIN";
        String mobile;
        String crawlerInfo = "0anAfxnpUyGoY9TMzaksDoP8Cwy2TrOy6oYjzxYK3KTnkjPsOKDxmu1EKf-" +
                "Y3DjJy9r3U8oI6R9Zi9Wu9rANlEoqlQnAqXr4T4Upb0woSmun4yUtGRGPGv9V1m8TpJglEm__" +
                "tilY9QhFR3ridWRCFYd8j3swm66w3SkKzlswrnCUSFzR4av9uewgHKTjK2VNw6jdX_" +
                "jZzFIVUyHjqtSwWT0MEVfNGKKgf844CSNf96HSqp27U9Ok5G7YqzuAebhyCn0H3Vhg4It414XmFik4yZJ9ODTG2Aa7hIoC9Sj3-" +
                "_GtgFmF_CkIhwgI-o3aKuuIBctyH2ZEeUnQGQXt-bxMI9ArbxSdPF2smQ4sAAHjjM7m2Gs0GAE6juvW2tlHK_" +
                "Kv0ad0gSyauDo57hI51tWt3ZViD5_br6NUnRniRo1p8VoXpoma0acjInFjqUAc0-WMtDG3je6c4WshN8WAdboO9";
        String sign = "f8497abd0b4967bd66093f7d2d80af5f";

        Request(String mobile) {
            this.mobile = mobile;
        }
    }

    @Data
    static class Result implements SendResult.ApiResult {
        boolean success;
        Integer errorCode;
        String errorMsg;
        Object result;

        @Override
        public boolean sendOk() {
            return success;
        }
    }

}
