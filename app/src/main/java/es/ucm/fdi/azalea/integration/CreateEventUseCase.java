package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class CreateEventUseCase {

    private String TAG = "CreateEventUseCase";

    public CreateEventUseCase() {
    }

    public void execute(EventModel em, CallBack<EventModel> cb) {
        getCurrentUser(em, cb);
    }

    private void getCurrentUser(EventModel em, CallBack<EventModel> cb) {
        try {
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Paso 1 completado. Usuario autenticado obtenido: " + success.getData().getUid());
                    getUserModel(em, success.getData().getUid(), cb);
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

    private void getUserModel(EventModel em, String id, CallBack<EventModel> cb) {
        Log.d(TAG, "Buscando usuario con id: " + id);
        try {
            BusinessFactory.getInstance().getUserRepository().findById(id, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Paso 2 completado. Usuario obtenido: " + success.getData().getClassId());
                    em.setIdClass(success.getData().getClassId());
                    createEvent(em, cb);
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
    // Paso 3: Crear el evento en cuestión en la clase asociada al usuario

    private void createEvent(EventModel em, CallBack<EventModel> cb){

        BusinessFactory.getInstance().getEventRepository().create(em, new CallBack<EventModel>() {
            @Override
            public void onSuccess(Event.Success<EventModel> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<EventModel> error) {
                cb.onError(error);
            }
        });
    }


}
