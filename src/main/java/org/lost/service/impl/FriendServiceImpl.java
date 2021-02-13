package org.lost.service.impl;

import org.lost.domain.Friend;
import org.lost.domain.ResultInfo;
import org.lost.domain.ShowFriend;
import org.lost.domain.User;
import org.lost.mapper.FriendMapper;
import org.lost.mapper.UserMapper;
import org.lost.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ShowFriend> showFriend(String id) {
        List<Friend> friends = friendMapper.showFriend(id);
        List<ShowFriend> f = new ArrayList<>();
        for(Friend friend : friends){
            ShowFriend showFriend = new ShowFriend(friend.getId(),friend.getUserId(),friend.getFriendId(),friend.getStatus(),friend.getCreateTime());
            if(friend.getUserId().equals(id)){
                showFriend.setFriendName(userMapper.findNameById(friend.getFriendId()));
            } else if (friend.getFriendId().equals(id)) {
                showFriend.setFriendName(userMapper.findNameById(friend.getUserId()));
            }
            f.add(showFriend);
        }
        return f;
    }

    @Override
    public ShowFriend findFriend(String id, String name) {
        User user = userMapper.findByName(name);
        ShowFriend friend = new ShowFriend();
        if(user == null){
            friend.setStatus("nouser");
            return friend;
        }else {
            Friend f = friendMapper.findFriend(id,user.getId());
            if (f == null){
                friend.setFriendId(user.getId());
                friend.setFriendName(name);
                friend.setStatus("notadd");
                return friend;
            }else {
                friend.setStatus("add");
                return friend;
            }
        }
    }

    @Override
    public ResultInfo handleFriend(String userId, String friendId) {
        String userName = userMapper.findNameById(userId);
        String friendName = userMapper.findNameById(friendId);
        try {
            if((userName == null || userName.equals("")) || (friendName == null || friendName.equals(""))){
                return new ResultInfo(false,null,"用户错误");
            }else {
                Friend friend = friendMapper.findFriend(userId,friendId);
                if(friend == null || friend.getId()==null || friend.getId().equals("")) {
                    //请求添加好友
                    try {
                        friendMapper.addFriend(userId, friendId);
                        return new ResultInfo(true, null, "添加成功，等待确认");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResultInfo(false, null, "用户错误");
                    }
                }else if (friend.getUserId().equals(userId) && friend.getStatus().equals("Y")){
                    //好友已添加，进行删除
                    try {
                        friendMapper.deleteFriend(userId,friendId);
                        return new ResultInfo(true,null,"删除成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResultInfo(false,null,"删除户错误");
                    }
                }else if (friend.getUserId().equals(userId) && friend.getStatus().equals("N")){
                    //好友已申请，等待对方进行确认
                    return new ResultInfo(true,null,"等待对方进行确认");
                }else if (friend.getFriendId().equals(userId) && friend.getStatus().equals("Y")){
                    //好友已添加，进行删除
                    try {
                        friendMapper.deleteFriend(friendId,userId);
                        return new ResultInfo(true,null,"删除成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResultInfo(false,null,"删除错误");
                    }
                }else if (friend.getFriendId().equals(userId) && friend.getStatus().equals("N")){
                    //好友已申请，进行确认
                    try {
                        friendMapper.confirmFriend(friendId,userId);
                        return new ResultInfo(true,null,"确认请求");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResultInfo(false,null,"确认错误");
                    }
                } else return new ResultInfo(false,null,"未知错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultInfo(false,null,"未知错误");
        }
    }
}
