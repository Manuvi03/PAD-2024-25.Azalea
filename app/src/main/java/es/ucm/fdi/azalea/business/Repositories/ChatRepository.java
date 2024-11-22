package es.ucm.fdi.azalea.business.Repositories;


import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface ChatRepository {
    public void getOrCreateChat(String chatRoomId, CallBack<ChatModel> cb);
    public void delete(String id);
    public void readById(String chatId, CallBack<ChatModel> cb);

}
