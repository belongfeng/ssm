package com.belongfeng.ssm.read_file;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zengxf on 2019/8/5.
 */
@Slf4j
public class ReadFile {

    private static final String
            FILE_NAME = "mobile-list.md";

    public static List<MobileVo> mobileVos() {
        List<String> list = mobiles();
        return list.stream()
                .filter(item -> !item.isBlank())
                .map(item -> item.trim().split("\\s+"))
                .filter(arr -> arr.length >= 2)
                .map(arr -> {
                    LocalDate date = LocalDate.parse("20" + arr[1]);
                    int times = arr.length == 2 ? 1 : Integer.parseInt(arr[2]);
                    times = times < 0 ? 1 : times;
                    return MobileVo.of(arr[0], date, times);
                })
                .collect(Collectors.toList());
    }

    public static List<String> mobiles() {
        try {
            URL url = ReadFile.class.getResource("/" + FILE_NAME);
            String file = url.getFile();
            log.info("file-url: [{}] \n file: [{}]", url, file);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            List<String> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("- ")) {
                    list.add(line.substring(2));
                }
            }
            return list;
        } catch (Exception e) {
            log.info("读取文件[{}]出错", FILE_NAME, e);
            return List.of();
        }
    }

}
