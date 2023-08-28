package com.limou.forum.utils;

import java.util.UUID;

/**
 * @author 小李哞哞
 * @date 2023/8/28
 */
public class UUIDUtil {
    /**
     * 生成一个36位的随机字符串，带下划线
     * @return
     */
    public static String UUID_36() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成一个32位的随机字符串
     * @return
     */
    public static String UUID_32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
