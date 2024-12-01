package es.ucm.fdi.azalea.presentation.chat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.ChatUseCase;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadMessagesByChatUseCase;
import es.ucm.fdi.azalea.integration.CreateMessageUseCase;

public class chatViewModel extends ViewModel {

    private final String TAG = "ChatViewModel";
    private MutableLiveData<Event<ChatModel>> chatState;
    private MutableLiveData<Event<MessageModel>> messageState;
    private MutableLiveData<Event<List<MessageModel>>> messagesListState;

    public chatViewModel(){
        this.chatState = new MutableLiveData<>();
        this.messageState = new MutableLiveData<>();
        this.messagesListState = new MutableLiveData<>();
    }

    public LiveData<Event<ChatModel>> getChatsState(){
        if(chatState == null)
            chatState = new MutableLiveData<>();
        return chatState;}

    public MutableLiveData<Event<MessageModel>> getMessageState() {
        if(messageState == null)
            messageState = new MutableLiveData<>();
        return messageState;}

    public void setMessageState(MutableLiveData<Event<MessageModel>> messageState) {
        this.messageState = messageState;}

    public MutableLiveData<Event<List<MessageModel>>> getMessagesListState() {
        if(messagesListState == null)
            messagesListState = new MutableLiveData<>();
        return messagesListState;}

    public void setMessagesListState(MutableLiveData<Event<List<MessageModel>>> messagesListState) {
        this.messagesListState = messagesListState;}

    public void getChat(String chatId){
        //Se asigna el estado a cargando.
        this.chatState.postValue(new Event.Loading<>());

        //Se crea el caso de uso para read.
        ChatUseCase chatUseCase = new ChatUseCase();

        //Se llama al caso de uso para intentar leer el chat en la DB
        chatUseCase.getChat(chatId, new CallBack<ChatModel>() {
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

    public void sendMessage(String chatId, String message){
        this.chatState.postValue(new Event.Loading<>());
        CreateMessageUseCase useCase = new CreateMessageUseCase();
        MessageModel model = new MessageModel();
        model.setMessage(message);
        model.setChatId(chatId);

        useCase.execute(model, new CallBack<MessageModel>() {
            @Override
            public void onSuccess(Event.Success<MessageModel> success) {
                messageState.postValue(success);
            }

            @Override
            public void onError(Event.Error<MessageModel> error) {
                messageState.postValue(error);
            }
        });

    }

    public String getChatId(String teacher, String parent){ //Construyo el id del chat con ambos ids
        return teacher + "+" + parent;
    }

    public void readMessagesByChat(String chatId) {
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        messagesListState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        ReadMessagesByChatUseCase useCase = new ReadMessagesByChatUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.execute(chatId, new CallBack<List<MessageModel>>() {
            @Override
            public void onSuccess(Event.Success<List<MessageModel>> success) {
                Log.d(TAG, "Los datos han llegado correctamente al ClassRoomViewModel en readStudentsByClassroom");
                messagesListState.postValue(success);
            }

            @Override
            public void onError(Event.Error<List<MessageModel>> error) {
                Log.d(TAG, "Los datos NO han llegado correctamente al ClassRoomViewModel en readStudentsByClassroom");
                messagesListState.postValue(error);
            }
        });
    }

    //TODO comunicacion entre el activity y el integration del chat
}

