package com.limou.forum.dao;

import com.limou.forum.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    int insert(Message row);

    int insertSelective(Message row);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message row);

    int updateByPrimaryKey(Message row);

    /**
     * 根据接收方id查询未读站内信数量
     *
     * @param receiveUserId 接收方id
     * @return 站内信数量
     */
    Integer selectUnreadCount(@Param("receiveUserId") Long receiveUserId);

    /**
     * 根据接收者用户id查询用户所有站内信信息，包含发送者信息
     *
     * @param receiveUserId 接收者用户id
     * @return 站内信列表
     */
    List<Message> selectByReceiveUserId(@Param("receiveUserId") Long receiveUserId);
}