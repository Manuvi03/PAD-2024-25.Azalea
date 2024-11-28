package es.ucm.fdi.azalea.business.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ChatModel {
    private String id;
    private List<String> messagesId;

    public ChatModel() {}

    public ChatModel(String id, List<String> messagesId) {
        this.id = id;
        this.messagesId = messagesId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public List<String> getMessagesId(){ return this.messagesId; }

    public  void setMessagesId(List<String> messagesId){ this.messagesId = messagesId;}
}
