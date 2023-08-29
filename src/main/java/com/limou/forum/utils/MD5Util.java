package com.limou.forum.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 用于MD5加密的工具类
 *
 * @author 小李哞哞
 * @date 2023/8/28
 */
public class MD5Util {

    /**
     * 对字符串进行MD5加密
     *
     * @param str 明文
     * @return 密文
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     * 原密码进行MD5加密，得到的密文 + 盐再进行加密
     *
     * @param str  密码明文
     * @param salt 扰动字符(盐)
     * @return 密文
     */
    public static String md5Salt(String str, String salt) {
        return md5(md5(str) + salt);
    }

    /**
     * 校验密码
     *
     * @param currentPwd 被校验的密码
     * @param actualPwd  数据库中实际的密码
     * @param salt       数据库中的盐
     * @return 校验结果
     */
    public static boolean checkPwd(String currentPwd, String actualPwd, String salt) {
        currentPwd = md5Salt(currentPwd, salt);
        return StrUtil.equals(currentPwd, actualPwd);
    }
}
