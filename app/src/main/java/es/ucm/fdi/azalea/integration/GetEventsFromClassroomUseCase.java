package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class GetEventsFromClassroomUseCase {

    private final String TAG = "GetEventsFromClassroomUseCase";

    public GetEventsFromClassroomUseCase() {
    }

    public void execute(CallBack<List<EventModel>> cb) {
        getCurrentUser(cb);
    }

    private void getCurrentUser(CallBack<List<EventModel>> cb) {
        try {
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Paso 1 completado. Usuario autenticado obtenido: " + success.getData().getUid());
                    getUserModel(success.getData().getUid(), cb);
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

    private void getUserModel(String id, CallBack<List<EventModel>> cb) {
        Log.d(TAG, "Buscando usuario con id: " + id);
        try {
            BusinessFactory.getInstance().getUserRepository().findById(id, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Paso 2 completado. Usuario obtenido: " + success.getData().getClassId());
                    readClassRoomById(success.getData().getClassId(), cb);
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
    // Paso 3: Obtener la clase asociada al usuario

    private void readClassRoomById(String classId, CallBack<List<EventModel>> cb) {
        BusinessFactory.getInstance().getClassRoomRepository().readById(classId, new CallBack<ClassRoomModel>() {

            @Override
            public void onSuccess(Event.Success<ClassRoomModel> success) {
                Log.d(TAG, "Paso 3 completado. Clase asociada al usuario: " + success.getData());
                getEventsForClassroom(classId, cb);

            }

            @Override
            public void onError(Event.Error<ClassRoomModel> error) {
                Log.d("ReadClassRoomByIdUseCase", "error");
                cb.onError(new Event.Error<>());
            }
        });
    }

    //Paso 4: Recuperar los Eventos de esa clase
    public void getEventsForClassroom(String classroomId, CallBack<List<EventModel>> cb) {
        BusinessFactory.getInstance().getEventRepository().getEventsForClassroom(classroomId, new CallBack<List<EventModel>>() {
            @Override
            public void onSuccess(Event.Success<List<EventModel>> success) {
                Log.d(TAG, "Eventos obtenidos correctamente. Devuelvo: " + success.getData().size() + " resultados.");
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<List<EventModel>> error) {
                Log.e(TAG, "Error al obtener los eventos");
                cb.onError(error);

            }
        });
    }
    }
