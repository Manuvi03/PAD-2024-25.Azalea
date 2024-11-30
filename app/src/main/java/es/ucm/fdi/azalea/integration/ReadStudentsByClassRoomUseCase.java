package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class ReadStudentsByClassRoomUseCase {

    private final String TAG= "ReadStudentsByClassRoomUseCase";

    public void execute(CallBack<List<StudentModel>> cb) {
        getCurrentUser(cb);
    }

    private void getCurrentUser(CallBack<List<StudentModel>> cb) {
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

    private void getUserModel(String id, CallBack<List<StudentModel>> cb) {
        Log.d(TAG, "Buscando usuario con id: " + id);
        try {
            BusinessFactory.getInstance().getUserRepository().findById(id, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Paso 2 completado. Usuario obtenido: " + success.getData().getClassId());
                    readStudentsByClassRoom(success.getData().getClassId(), cb);
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

    // caso de uso que, a partir de el id de una clase, busca a sus estudiantes
    private void readStudentsByClassRoom(String classroomId, CallBack<List<StudentModel>> cb){
        BusinessFactory.getInstance().getStudentRepository().readByClassRoomId(classroomId, new CallBack<List<StudentModel>>(){

            @Override
            public void onSuccess(Event.Success<List<StudentModel>> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<List<StudentModel>> error) {
                cb.onError(error);
            }
        });
    }
}
