package com.example.febrian.ecomerce.Response;

import java.util.Date;

/**
 * Created by febrian on 23/03/18.
 */

public class ChatModel {
    public ChatModel(String messageText, String messageUser, String messageUserId, int messageUserType, String messageUserFriendId, String messageUserFriend) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageUserId = messageUserId;
        this.messageUserType = messageUserType;
        this.messageUserFriendId = messageUserFriendId;
        this.messageUserFriend = messageUserFriend;
        this.messageTime = new Date().getTime();
    }

    private String messageText;
    private String messageUser;

    public String getMessageUserFriend() {
        return messageUserFriend;
    }

    public void setMessageUserFriend(String messageUserFriend) {
        this.messageUserFriend = messageUserFriend;
    }

    public String getMessageUserFriendId() {
        return messageUserFriendId;
    }

    public void setMessageUserFriendId(String messageUserFriendId) {
        this.messageUserFriendId = messageUserFriendId;
    }

    private String messageUserFriend;
    private String messageUserId;
    private String messageUserFriendId;
    private int messageUserType;
    private long messageTime;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public int getMessageUserType() {
        return messageUserType;
    }

    public void setMessageUserType(int messageUserType) {
        this.messageUserType = messageUserType;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
