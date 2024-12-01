package es.ucm.fdi.azalea.business.Repositories;


import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface ChatRepository {
    public void create(ChatModel chat, CallBack<ChatModel> cb);
    public void readById(String chatId, CallBack<ChatModel> cb);

}
