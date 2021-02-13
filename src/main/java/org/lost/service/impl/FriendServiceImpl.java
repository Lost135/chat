package org.lost.service.impl;

import org.lost.domain.Friend;
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
}
