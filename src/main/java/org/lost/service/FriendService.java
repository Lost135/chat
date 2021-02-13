package org.lost.service;

import org.lost.domain.ShowFriend;

import java.util.List;

public interface FriendService {
    List<ShowFriend> showFriend(String id);

    ShowFriend findFriend(String id, String name);
}
