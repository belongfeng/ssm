package com.belongfeng.ssm.sms_service.impl;

import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
@Deprecated
public class FangService extends AbstractSmsService implements SmsService {

    public FangService() {
        super("房天下", "https://passport.fang.com");
    }

    @Override
    public void send(String mobile, SendResult result) throws IOException {
        ApiService service = super.create(ApiService.class);
        Map<String, String> headers = Map.of(
                "X-Requested-With", "XMLHttpRequest",
                "referer", "https://passport.fang.com/NewRegister.aspx?backurl=https://gz.fang.com/",
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"
        );
        Map<String, String> queries = Map.of(
                "MobilePhone", mobile,
                "Service", "soufun-passport-web",
                "MathCode", "",
                "Operatetype", "2"
        );
        Call<Result> call = service.send(headers, queries);
        Response<Result> response = call.execute();
        result.setHttpStatus(response.code());
        result.setRes(response.body());
    }

    interface ApiService {
        @GET("/loginsendmsm.api")
        Call<Result> send(
                @HeaderMap Map<String, String> headers,
                @QueryMap Map<String, String> queries
        );
    }

    @Data
    static class Result implements SendResult.ApiResult {
        String BackUrl;
        String IsSent;
        String IsShowMathCode;
        String Message;
        String Tip;

        @Override
        public boolean sendOk() {
            return Objects.equals("Success", Message);
        }
    }

}
