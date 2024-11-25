package es.ucm.fdi.azalea.presentation.chat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.ChatUseCase;
import es.ucm.fdi.azalea.integration.Event;

public class chatViewModel extends ViewModel {

    private final String TAG = "ChatViewModel";
    private MutableLiveData<Event<ChatModel>> chatState;

    public chatViewModel(){
        this.chatState = new MutableLiveData<>();
    }

    public LiveData<Event<ChatModel>> getChatsState(){
        return chatState;
    }

    public void getChat(String teacherId, String parentId){
        //Se asigna el estado a cargando.
        this.chatState.postValue(new Event.Loading<>());

        //Se crea el caso de uso para read.
        ChatUseCase chatUseCase = new ChatUseCase();

        //Se llama al caso de uso para intentar leer el chat en la DB
        chatUseCase.getChat(teacherId, parentId, new CallBack<ChatModel>() {
            @Override
            public void onSuccess(Event.Success<ChatModel> success) {
                Log.d(TAG, "Los datos del chat se han devuelto correctamente al ChatViewModel");
                chatState.postValue(success);
            }

            @Override
            public void onError(Event.Error<ChatModel> error) {
                Log.e(TAG, "Los datos no se han devuelto correctamente");
                chatState.postValue(error);
            }
        });

    }

    //TODO comunicacion entre el activity y el integration del chat
}

