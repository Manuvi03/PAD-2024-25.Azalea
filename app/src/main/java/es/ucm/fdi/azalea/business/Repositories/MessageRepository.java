package es.ucm.fdi.azalea.business.Repositories;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.MessageModel;

public class MessageRepository implements Repository<MessageModel> {
    @Override
    public String create(MessageModel item) {
        return "";
    }

    @Override
    public MessageModel findById(String id) {
        return null;
    }

    @Override
    public String update(MessageModel item) {
        return "";
    }

    @Override
    public String delete(String id) {
        return "";
    }

    @Override
    public List<MessageModel> readAll() {
        return Collections.emptyList();
    }
}
