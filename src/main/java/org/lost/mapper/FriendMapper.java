package org.lost.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lost.domain.Friend;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Select("select * from friend where userId = '${id}' or friendId = '${id}'")
    List<Friend> showFriend(String id);
}
