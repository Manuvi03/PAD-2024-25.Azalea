package es.ucm.fdi.azalea.business.model;

import java.sql.Timestamp;
import java.util.List;

public class ChatModel {
    private int id;
    private List<Integer> usersId;
    private Timestamp lastMessageTime;
    private int lastMessageUserId;

    public ChatModel() {
    }

    public ChatModel(int id, List<Integer> usersId, Timestamp lastMessageTime, int lastMessageUserId) {
        this.id = id;
        this.usersId = usersId;
        this.lastMessageTime = lastMessageTime;
        this.lastMessageUserId = lastMessageUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<Integer> usersId) {
        this.usersId = usersId;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getLastMessageUserId() {
        return lastMessageUserId;
    }

    public void setLastMessageUserId(int lastMessageUserId) {
        this.lastMessageUserId = lastMessageUserId;
    }
}
