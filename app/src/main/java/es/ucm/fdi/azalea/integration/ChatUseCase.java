package es.ucm.fdi.azalea.integration;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.ChatRepositoryImp;
import es.ucm.fdi.azalea.business.model.ChatModel;

public class ChatUseCase {

    private final ChatRepositoryImp repositoryImp;

    public ChatUseCase() {
        this.repositoryImp = new ChatRepositoryImp();
    }

    public void getChat(String chatId, CallBack<ChatModel> cb){

        BusinessFactory.getInstance().getChatRepository().readById(chatId, new CallBack<ChatModel>() {
            @Override
            public void onSuccess(Event.Success<ChatModel> success) {cb.onSuccess(success);}

            @Override
            public void onError(Event.Error<ChatModel> error) {
                cb.onError(error);
            }
        });
    }
}
