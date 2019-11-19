package com.belongfeng.ssm.sms_service.impl;

import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
@Deprecated
public class ChSiService extends AbstractSmsService implements SmsService {

    public ChSiService() {
        super("学信网", "https://account.chsi.com.cn");
    }

    @Override
    public void send(String mobile, SendResult result) throws IOException {
        ApiService service = super.create(ApiService.class);
        Map<String, String> headers = Map.of(
                "origin", "https://account.chsi.com.cn",
                "referer", "https://account.chsi.com.cn/account/reg/preregister.action?from=account-reghead",
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"
        );
        Map<String, String> fields = Map.of(
                "mobilePhone", mobile,
                "captch", "MgB" + new Random().nextInt(10000),
                "mphonearea", "0086",
                "optType", "REGISTER",
                "ignoremphone", "true"
        );
        CountDownLatch latch = new CountDownLatch(1);
        Call<String> call = service.send(headers, fields);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                result.setHttpStatus(response.code());
                if (response.isSuccessful()) {
                    String body = response.body();
                    log.info("body: {}", body);
                    Result res = new Gson().fromJson(body, Result.class);
                    result.setRes(res);
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log.error("请求失败", t);
                result.setSign(SendResult.Sign.ERROR);
                result.setErrorMsg(t.getMessage());
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }

    interface ApiService {
        @FormUrlEncoded
        @POST("/account/getgatmphonpincode.action")
        Call<String> send(
                @HeaderMap Map<String, String> headers,
                @FieldMap Map<String, String> fields
        );
    }

    @Data
    static class Result implements SendResult.ApiResult {
        String status;
        String tips;
        String tipsBtnValue;
        String tipsNote;

        @Override
        public boolean sendOk() {
            return Objects.equals("2", status);
        }
    }

}
