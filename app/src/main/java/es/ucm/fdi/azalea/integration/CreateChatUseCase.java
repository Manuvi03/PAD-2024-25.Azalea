package es.ucm.fdi.azalea.integration;

import androidx.lifecycle.MutableLiveData;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.ChatRepositoryImp;
import es.ucm.fdi.azalea.business.model.ChatModel;

public class CreateChatUseCase {

    private ChatRepositoryImp chatRepositoryImp;

    public CreateChatUseCase(){
        chatRepositoryImp = new ChatRepositoryImp();
    }

    public void execute(ChatModel chatModel){
        MutableLiveData<ChatModel> result = new MutableLiveData<>();

        BusinessFactory.getInstance().getChatRepository().create(chatModel, new CallBack<ChatModel>(){

            @Override
            public void onSuccess(Event.Success<ChatModel> success) {

            }

            @Override
            public void onError(Event.Error<ChatModel> error) {

            }
        });
    }
}
