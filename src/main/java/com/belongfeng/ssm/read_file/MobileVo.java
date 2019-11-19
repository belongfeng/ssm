package com.belongfeng.ssm.read_file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Created by zengxf on 2019/8/8.
 */
@Getter
@ToString
@AllArgsConstructor(staticName = "of")
public class MobileVo {

    String mobile;
    LocalDate date;
    int times;

}
