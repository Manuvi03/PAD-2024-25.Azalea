package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.TokenModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class SendNotificationUseCase {

    private final String TAG = "SendNotificationUseCase";

    public void execute(String chatId, MessageModel model, CallBack<Boolean> cb){
        getCurrentUser(chatId, model, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                cb.onError(error);
            }
        });
    }

    public void getCurrentUser(String chatId,MessageModel model, CallBack<Boolean> cb) {
        try {
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Paso 1 completado. Usuario autenticado obtenido: " + success.getData().getUid());
                   getUserModel(chatId, success.getData().getUid(), model, cb);
                }

                @Override
                public void onError(Event.Error<FirebaseUser> error) {
                    Log.d(TAG, "Error obteniendo usuario autenticado: " + error);
                    cb.onError(new Event.Error<>());
                }
            });

        } catch (Exception e) {
            cb.onError(new Event.Error<>(e));
        }
    }

    public void getUserModel(String chatId, String id, MessageModel model, CallBack<Boolean> cb) {
        Log.d(TAG, "Buscando usuario con id: " + id);
        try {
            BusinessFactory.getInstance().getUserRepository().findById(id, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Paso 2 completado. Usuario obtenido: " + success.getData().getClassId());
                    if(success.getData().getParent())
                        getClassModel(success.getData().getClassId(), success.getData().getName(), model, cb);
                    else
                        sendMessageToParent(chatId, success.getData().getName(), model, cb);
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    Log.d(TAG, "Error obteniendo usuario: " + error);
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        } catch (Exception e) {
            cb.onError(new Event.Error<>(e));
        }
    }

    private void sendMessageToParent(String chatId, String name, MessageModel model, CallBack<Boolean> cb) {
        String[] IDs = chatId.split("\\+");
        sendMessageToId(IDs[1], name, model, cb);
    }

    private void getClassModel(String classId, String name, MessageModel model, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getClassRoomRepository().readById(classId, new CallBack<ClassRoomModel>() {
            @Override
            public void onSuccess(Event.Success<ClassRoomModel> success) {
                sendMessageToId(success.getData().getIdTeacher(), name, model, cb);
            }

            @Override
            public void onError(Event.Error<ClassRoomModel> error) {
                cb.onError(new Event.Error<>(error.getException()));
            }
        });

    }

    private void sendMessageToId(String idToSend, String name, MessageModel model, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getTokenRepository().readByUserId(idToSend, new CallBack<TokenModel>() {
            @Override
            public void onSuccess(Event.Success<TokenModel> success) {
                sendNotification(name, model, success.getData().getId(), cb);
            }

            @Override
            public void onError(Event.Error<TokenModel> error) {
                cb.onError(new Event.Error<>(error.getException()));
            }
        });
    }

    private void sendNotification(String name, MessageModel model, String token, CallBack<Boolean> cb) {
        //TODO logica de enviar un mensaje pero no consegui la serverKey
    }
}
