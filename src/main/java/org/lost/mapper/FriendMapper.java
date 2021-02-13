package org.lost.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lost.domain.Friend;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Select("select * from friend where userId = '${id}' or friendId = '${id}'")
    List<Friend> showFriend(String id);

    @Select("select * from friend where (userId = '${id1}' and friendId = '${id2}') or (userId = '${id2}' and friendId = '${id1}')")
    Friend findFriend(String id1,String id2);
}
