package org.lost.mapper;

import org.apache.ibatis.annotations.*;
import org.lost.domain.Friend;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Select("select * from friend where userId = '${id}' or friendId = '${id}'")
    List<Friend> showFriend(String id);

    @Select("select * from friend where (userId = '${id1}' and friendId = '${id2}') or (userId = '${id2}' and friendId = '${id1}')")
    Friend findFriend(String id1,String id2);

    @Delete("DELETE FROM friend where userId = '${id1}' and friendId = '${id2}' ")
    void deleteFriend(String id1,String id2);

    @Update("UPDATE friend SET status = 'Y' WHERE userId = '${id1}' and friendId = '${id2}'")
    void confirmFriend(String id1,String id2);

    @Insert("INSERT INTO friend  VALUES (null, '${id1}', '${id2}', 'N', null )")
    void addFriend(String id1,String id2);
}
