package com.limou.forum.common;

/**
 * 全局状态码
 *
 * @author 小李哞哞
 * @date 2023/8/27
 */
public enum ResultCode {

    SUCCESS                         (0, "操作成功"),
    FAILED                          (1000, "操作失败"),
    FAILED_UNAUTHORIZED             (1001, "未授权"),
    FAILED_PARAMS_INVALIDATE          (1002, "参数校验失败"),
    FAILED_FORBIDDEN                (1003, "禁止访问"),
    FAILED_CREATE                   (1004, "新增失败"),
    FAILED_NOT_EXISTS               (1005, "资源不存在"),

    // 关于用户的错误描述=======================================================================
    FAILED_USERNAME_EXISTS          (1100, "用户名已存在"),
    FAILED_USER_EXISTS              (1101, "用户已存在"),
    FAILED_USER_NOT_EXISTS          (1102, "用户不存在"),
    FAILED_LOGIN                    (1103, "用户名或密码错误"),
    FAILED_USER_BANNED              (1104, "您已被禁言，请联系管理员，并重新登录"),
    FAILED_TWO_PWD_NOT_SAME         (1105, "两次输入的密码不一致"),
    FAILED_USER_ARTICLE_COUNT       (1106, "更新用户帖子数量失败"),
    FAILED_OLD_PWD_ERROR            (1107, "旧密码输入错误"),

    // 关于板块的错误描述=======================================================================
    FAILED_BOARD_ARTICLE_COUNT      (1201, "更新板块帖子数量失败"),
    FAILED_BOARD_BANNED             (1202, "板块状态异常"),
    FAILED_BOARD_NOT_EXISTS         (1203, "板块不存在"),

    // 关于帖子的错误描述=======================================================================
    FAILED_ARTICLE_NOT_EXISTS       (1301, "帖子不存在"),
    FAILED_ARTICLE_BANNED           (1302, "帖子状态异常"),

    // 关于站内信的错误描述=====================================================================
    FAILED_MESSAGE_SELF             (1401, "禁止给自己发私信"),
    FAILED_MESSAGE_NOT_EXISTS       (1402, "站内信不存在"),

    // 关于站内信的状态描述=====================================================================
    MESSAGE_UNREAD                  (1500, "未读"),
    MESSAGE_READ                    (1501, "已读"),
    MESSAGE_REPLIED                 (1502, "已回复"),

    // 其他错误描述============================================================================
    ERROR_SERVICES                  (2000, "服务器内部错误"),
    ERROR_IS_NULL                   (2001, "IS NULL");

    // 状态码
    int code;
    // 描述信息
    String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
