package es.ucm.fdi.azalea.integration;

import com.google.android.gms.tasks.Task;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.ChatRepositoryImp;
import es.ucm.fdi.azalea.business.model.ChatModel;

public class ChatUseCase {

    private final ChatRepositoryImp repositoryImp;

    public ChatUseCase() {
        this.repositoryImp = new ChatRepositoryImp();
    }

    private String getChatRoomId(String user1, String user2){ //TODO intentar que siempre este el del profe delante.
        if(user1.hashCode() < user2.hashCode())
            return user1 + "+" + user2;
        return user2 + "+" + user1;
     }

    public void getOrCreateChat(String chatId, CallBack<ChatModel> callBack) {
        repositoryImp.getOrCreateChat(chatId, callBack);

    }

    private void handleChat(String chatId, CallBack<ChatModel> cb){
        BusinessFactory.getInstance().getChatRepository().readById(chatId, new CallBack<ChatModel>() {
            @Override
            public void onSuccess(Event.Success<ChatModel> success) {
                if (success.get)
            }

            @Override
            public void onError(Event.Error<ChatModel> error) {
                cb.onError(error);
            }
        });
    }
}
