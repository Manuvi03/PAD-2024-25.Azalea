package es.ucm.fdi.azalea.business.Repositories;

import java.util.List;

import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface MessageRepository {
    public void create(MessageModel model, CallBack<MessageModel> cb);
    public void findById(String id, CallBack<MessageModel> cb);
    void readByChatId(String chatId, CallBack<List<MessageModel>> callBack);
}
