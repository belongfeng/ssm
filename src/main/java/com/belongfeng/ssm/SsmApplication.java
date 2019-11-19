package com.belongfeng.ssm;

import com.belongfeng.ssm.read_file.MobileVo;
import com.belongfeng.ssm.read_file.ReadFile;
import com.belongfeng.ssm.sms_service.SendResult;
import com.belongfeng.ssm.sms_service.SmsService;
import com.belongfeng.ssm.sms_service.SmsServiceEnum;
import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class SsmApplication {


    public static void main(String[] args) {
        Variable.logLevel = HttpLoggingInterceptor.Level.BASIC;
        List<MobileVo> mobiles = ReadFile.mobileVos();
        LocalDate sign = LocalDate.now().minusDays(180);
        mobiles.stream()
                .filter(vo -> vo.getDate().isAfter(sign))
                .forEach(vo -> {
                    for (int i = 0; i < vo.getTimes(); i++)
                        send(vo.getMobile().replace("-", ""));
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        log.error("出错", e);
                    }
                });
    }

    static void send(String mobile) {
        Stream.of(SmsServiceEnum.values())
                .filter(serviceEnum -> !serviceEnum.deprecated())
                .parallel()
                .forEach(serviceEnum -> {
                    SmsService service = serviceEnum.service;
                    SendResult res = service.send(mobile);
                    log.info(
                            "domain: {}, mobile: {}, 是否成功：【{}】, res: {}",
                            service.domain(), mobile, res.sendOk(), res
                    );
                });
    }

}
