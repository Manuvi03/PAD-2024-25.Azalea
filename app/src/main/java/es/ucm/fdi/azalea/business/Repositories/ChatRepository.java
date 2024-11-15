package es.ucm.fdi.azalea.business.Repositories;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.ChatModel;

public class ChatRepository implements Repository<ChatModel>{
    @Override
    public String create(ChatModel item) {
        return "";
    }

    @Override
    public ChatModel findById(String id) {
        return null;
    }

    @Override
    public String update(ChatModel item) {
        return "";
    }

    @Override
    public String delete(String id) {
        return "";
    }

    @Override
    public List<ChatModel> readAll() {
        return Collections.emptyList();
    }
}
