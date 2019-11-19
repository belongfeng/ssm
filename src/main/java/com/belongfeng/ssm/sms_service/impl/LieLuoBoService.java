package com.belongfeng.ssm.sms_service.impl;

import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.io.IOException;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
public class LieLuoBoService extends AbstractSmsService implements SmsService {

    public LieLuoBoService() {
        super("猎萝卜", "https://www.lieluobo.com");
    }

    @Override
    public void send(String mobile, SendResult result) throws IOException {
        ApiService service = super.create(ApiService.class);
        Request request = new Request(mobile);
        Call<Result> call = service.send(request, System.currentTimeMillis());
        Response<Result> res = call.execute();
        result.setHttpStatus(res.code());
        result.setRes(res.body());
    }

    interface ApiService {
        @POST("/api/biz/sms")
        Call<Result> send(@Body Request request, @Query("_") long ts);
    }

    @Data
    static class Request {
        String imgVerifyCode = "";
        String mobile;
        String uid = "";

        Request(String mobile) {
            this.mobile = mobile;
        }
    }

    @Data
    static class Result implements SendResult.ApiResult {
        boolean body;
        Integer code;
        String msg;

        @Override
        public boolean sendOk() {
            return body;
        }
    }

}
