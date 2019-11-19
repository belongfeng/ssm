package com.belongfeng.ssm.sms_service.impl;

import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
@Deprecated
public class JDService extends AbstractSmsService implements SmsService {

    public JDService() {
        super("京东", "https://reg.jd.com");
    }

    @Override
    public void send(String mobile, SendResult result) throws IOException {
        ApiService service = super.create(ApiService.class);
        Map<String, String> headers = Map.of(
                "referer", "https://reg.jd.com/reg/person?ReturnUrl=https%3A//www.jd.com/",
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"
        );
        Map<String, String> queries = Map.of(
                "source", "main",
                "mobile", "+0086" + mobile,
                "mobileUnbind", "false",
                "authCodeStrategy", "1",
                "qwLRQPoZxy", "rNbTO",
                "imageAuthCodeToken", "66e5a30cf9ab448bad12a4a3e919913e",
                "_", "" + System.currentTimeMillis()
        );
        CountDownLatch latch = new CountDownLatch(1);
        Call<String> call = service.send(headers, queries);
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
        @GET("/p/sendMessage")
        Call<String> send(
                @HeaderMap Map<String, String> headers,
                @QueryMap Map<String, String> queries
        );
    }

    @Data
    static class Result implements SendResult.ApiResult {
        Integer resultCode;
        Integer smsTimes24;
        Integer smsTimes48;
        String resultMessage;
        String audioMessageType;
        String needCheckImageAuthCode;

        @Override
        public boolean sendOk() {
            return Objects.equals(1, resultCode);
        }
    }

}
