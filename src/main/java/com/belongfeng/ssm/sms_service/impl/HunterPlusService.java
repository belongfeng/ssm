package com.belongfeng.ssm.sms_service.impl;

import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
public class HunterPlusService extends AbstractSmsService implements SmsService {

    public HunterPlusService() {
        super("猎萌", "https://www.hunterplus.net");
    }

    @Override
    public void send(String mobile, SendResult result) throws IOException {
        ApiService service = super.create(ApiService.class);
        Request request = new Request(mobile);
        Call<String> call = service.send(request);
        Response<String> res = call.execute();
        result.setHttpStatus(res.code());
        result.setRes(new Result(res.body()));
    }

    interface ApiService {
        @POST("/api/code/send")
        Call<String> send(@Body Request request);
    }

    @Data
    static class Request {
        String mobile;

        Request(String mobile) {
            this.mobile = mobile;
        }
    }

    @Data
    static class Result implements SendResult.ApiResult {
        String msg;

        Result(String msg) {
            this.msg = msg;
        }

        @Override
        public boolean sendOk() {
            return "\"SUCCESS\"".equals(msg);
        }
    }

}
