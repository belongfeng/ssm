package com.belongfeng.ssm.sms_service.impl;

import com.belongfeng.ssm.Variable;
import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
public abstract class AbstractSmsService implements SmsService {

    protected final String
            domain,
            baseUrl;

    protected Retrofit retrofit;
    protected Object service;

    public AbstractSmsService(String domain, String baseUrl) {
        this.domain = domain;
        this.baseUrl = baseUrl;
    }

    @Override
    public String domain() {
        return this.domain;
    }

    @Override
    public SendResult send(String mobile) {
        log.info("利用[{}]发送短信到[{}]", this.domain, mobile);
        this.buildRetrofit();
        SendResult result = new SendResult();
        try {
            this.send(mobile, result);
            result.setSign(SendResult.Sign.SUCCESS);
        } catch (IOException e) {
            log.error("猎萝卜发送短信失败，手机号[{}]", mobile, e);
            result.setSign(SendResult.Sign.ERROR);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    protected Retrofit buildRetrofit() {
        if (this.retrofit != null)
            return this.retrofit;
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor((msg) -> log.info(msg))
                                .setLevel(Variable.logLevel)
                );
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        return this.retrofit = retrofit;
    }

    protected <T> T create(final Class<T> service) {
        if (this.service != null)
            return (T) this.service;
        return (T) (this.service = this.retrofit.create(service));
    }

    protected abstract void send(String mobile, SendResult result) throws IOException;

}
