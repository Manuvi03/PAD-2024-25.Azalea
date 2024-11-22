package es.ucm.fdi.azalea.business.Repositories;

import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface MessageRepository {
    public void create(MessageModel model);
    public void findById(String id, CallBack<MessageModel> cb);
}
