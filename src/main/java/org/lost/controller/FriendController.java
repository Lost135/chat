package org.lost.controller;

import org.lost.domain.ShowFriend;
import org.lost.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @RequestMapping("/showFriend")
    @ResponseBody
    public List<ShowFriend> showFriend(@RequestParam("loginId") String id){
        List<ShowFriend> friends = friendService.showFriend(id);
        return friends;
    }

    @RequestMapping("/findFriend")
    @ResponseBody
    public ShowFriend findFriend(@RequestParam("loginId") String id,@RequestParam("friendName") String name){
        ShowFriend friend = friendService.findFriend(id,name);
        return friend;
    }
}
