package es.ucm.fdi.azalea.presentation.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.ChatUseCase;
import es.ucm.fdi.azalea.integration.Event;

public class chatViewModel extends ViewModel {

    private final ChatUseCase chatUseCase;
    private MutableLiveData<Event<ChatModel>> chatState;

    public chatViewModel(){
        this.chatUseCase = new ChatUseCase();
        this.chatState = new MutableLiveData<>();
    }

    public LiveData<Event<ChatModel>> getChatsState(){
        return chatState;
    }

    public void getOrCreateChat(String chatRoomId) {
        this.chatState.setValue(new Event.Loading<>());

        chatUseCase.getOrCreateChat(chatRoomId, new CallBack<ChatModel>() {
            @Override
            public void onSuccess(Event.Success<ChatModel> success) {
                chatState.postValue(success);
            }

            @Override
            public void onError(Event.Error<ChatModel> error) {
                chatState.postValue(error);
            }
        });
    }

    public void createChat(){

    }

    public String getChat(){
        return null;
    }

    //TODO comunicacion entre el activity y el integration del chat
}

