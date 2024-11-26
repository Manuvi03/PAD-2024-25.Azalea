package es.ucm.fdi.azalea.business.model;

import java.sql.Timestamp;
import java.util.List;

public class ChatModel {
    private String id;
//    private List<String> usersId;
//    private Timestamp lastMessageTime;
//    private int lastMessageUserId;

    public ChatModel() {}

    public ChatModel(String id) {
        this.id = id;
        //this.usersId = usersId;
        //this.lastMessageTime = lastMessageTime; //TODO mirarlo con la oopcion de INDEXOf de dani
        //this.lastMessageUserId = lastMessageUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
