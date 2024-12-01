package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class CreateMessageUseCase {
    private String TAG = "CreateMessageUseCase";

    public CreateMessageUseCase() {}

    public void execute(MessageModel model, CallBack<MessageModel> cb) {
        getCurrentUser(model, cb);
    }

    private void getCurrentUser(MessageModel model, CallBack<MessageModel> cb) {
        try {
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Paso 1 completado. Usuario autenticado obtenido: " + success.getData().getUid());
                    getUserModel(model, success.getData().getUid(), cb);
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
    // Paso 2: Obtener el modelo de usuario asociado

    private void getUserModel(MessageModel model, String id, CallBack<MessageModel> cb) {
        Log.d(TAG, "Buscando usuario con id: " + id);
        try {
            BusinessFactory.getInstance().getUserRepository().findById(id, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Paso 2 completado. Usuario obtenido: " + success.getData().getClassId());
                    model.setSenderId(success.getData().getId());
                    createMessage(model, cb);
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    Log.d(TAG, "Error obteniendo usuario: " + error);
                    cb.onError(new Event.Error<>());
                }
            });
        } catch (Exception e) {
            cb.onError(new Event.Error<>(e));
        }
    }
    // Paso 3: Crear el mensaje en el chat asociado al senderId

    private void createMessage(MessageModel model, CallBack<MessageModel> cb){

        BusinessFactory.getInstance().getMessageRepository().create(model, new CallBack<MessageModel>() {
            @Override
            public void onSuccess(Event.Success<MessageModel> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<MessageModel> error) {
                cb.onError(error);
            }
        });
    }
}
