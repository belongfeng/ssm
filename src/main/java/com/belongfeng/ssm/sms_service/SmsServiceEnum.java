package com.belongfeng.ssm.sms_service;

import com.belongfeng.ssm.sms_service.impl.*;
import lombok.AllArgsConstructor;

/**
 * 发送服务类枚举总成
 * <p>
 * Created by zengxf on 2019/8/5.
 */
@AllArgsConstructor
public enum SmsServiceEnum {

    LieLuoBo(new LieLuoBoService()),
    HunterPlus(new HunterPlusService()),
    SinaCnMail(new SinaCnMailService()),
    PinDuoDuo(new PinDuoDuoService()),
    @Deprecated JD(new JDService()),
    @Deprecated QQ(new QQService()),
    @Deprecated ChSi(new ChSiService()),
    @Deprecated Fang(new FangService()),
    ;

    public final SmsService service;


    public boolean deprecated() {
        try {
            Deprecated deprecated = SmsServiceEnum.class
                    .getDeclaredField(this.name())
                    .getAnnotation(Deprecated.class);
            return deprecated != null;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

}
