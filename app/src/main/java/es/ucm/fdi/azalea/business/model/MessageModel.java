package es.ucm.fdi.azalea.business.model;

public class MessageModel {
    private String message;
    private String id;
    private String senderId;
    private String chatId;

    public MessageModel() {}

    public MessageModel(String id, String message, String senderId, String chatId) {
        this.id = id;
        this.message = message;
        this.senderId = senderId;
        this.chatId = chatId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

}
