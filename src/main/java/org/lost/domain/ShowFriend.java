package org.lost.domain;

public class ShowFriend {
    private String id;
    private String userId;
    private String friendId;
    private String status;
    private String createTime;
    private String friendName;

    public ShowFriend(String id, String userId, String friendId, String status, String createTime) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
        this.createTime = createTime;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
