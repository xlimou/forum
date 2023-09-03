package com.limou.forum.model;

import lombok.ToString;

import java.util.Date;

@ToString
public class Message {
    private Long id;

    private Long postUserId;

    private Long receiveUserId;

    private String content;

    /**
     * 状态（0未读，1已读，2已回复）
     */
    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

    /**
     * 关联消息发送者
     */
    private User postUser;

    public User getPostUser() {
        return postUser;
    }

    public void setPostUser(User postUser) {
        this.postUser = postUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(Long postUserId) {
        this.postUserId = postUserId;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(Byte deleteState) {
        this.deleteState = deleteState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}