package com.fyp.cli;

public class Friend {
    private int friendId;
    private String friendName;  // ✅ Add friend's name

    public Friend(int friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {  // ✅ Getter for friend's name
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
