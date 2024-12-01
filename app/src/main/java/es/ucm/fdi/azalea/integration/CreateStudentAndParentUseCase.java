package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.presentation.addstudent.ChatIdGenerate;

public class CreateStudentAndParentUseCase {

    public String TAG = "CreateStudentAndParentUseCase";

    public CreateStudentAndParentUseCase() {
    }

    public void execute(StudentModel sm, UserModel parent, ChatModel chat, CallBack<Boolean> cb) {
        getCurrentUser(sm, parent, chat, new CallBack<Boolean>() {
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

    // Paso 1: Obtener el usuario actual
    public void getCurrentUser(StudentModel student, UserModel parent, ChatModel chat, CallBack<Boolean> cb) {
        try {
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Paso 1 completado. Usuario autenticado obtenido: " + success.getData().getUid());
                    getUserModel(success.getData().getUid(), student, parent, chat, cb);
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

    public void getUserModel(String id, StudentModel student, UserModel parent, ChatModel chat, CallBack<Boolean> cb) {
        Log.d(TAG, "Buscando usuario con id: " + id);
        try {
            BusinessFactory.getInstance().getUserRepository().findById(id, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Paso 2 completado. Usuario obtenido: " + success.getData().getClassId());
                    readClassRoomById(success.getData().getClassId(), student, parent, chat, cb);
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

    public void readClassRoomById(String classId, StudentModel student, UserModel parent, ChatModel chat, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getClassRoomRepository().readById(classId, new CallBack<ClassRoomModel>() {

            @Override
            public void onSuccess(Event.Success<ClassRoomModel> success) {
                Log.d(TAG, "Paso 3 completado. Clase asociada al usuario: " + success.getData());
                student.setClassroomId(success.getData().getId());
                student.setSubjects(success.getData().getSubjects());
                parent.setClassId(success.getData().getId());
                checkUserExists(student, parent, chat, cb);
            }

            @Override
            public void onError(Event.Error<ClassRoomModel> error) {
                Log.d("ReadClassRoomByIdUseCase", "error");
                cb.onError(new Event.Error<>());
            }
        });
    }
    // Paso 4: Verificar si el padre existe

    public void checkUserExists(StudentModel student, UserModel parent, ChatModel chat, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getUserRepository().checkUserExists(parent.getEmail(), new CallBack<Boolean>() {
            // si checkUserExists devuelve TRUE significa que existe por lo tanto no podemos crear
            // el usuario pero si devuelve false no existe entonces podemos crear el usuario
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                Log.d(TAG, "Este usuario ya existe");
                cb.onError(new Event.Error<>());
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.d(TAG, "Paso 5 completado. Padre no existe: " + error);
                createStudent(student, parent, chat, cb);
            }
        });
    }
    // Paso 5: Crear el estudiante

    public void createStudent(StudentModel student, UserModel parent, ChatModel chat, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getStudentRepository().create(student, new CallBack<StudentModel>() {
            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                Log.d(TAG, "Paso 4 completado. Estudiante creado: " + success.getData());
                parent.setStudentId(success.getData().getId());
                register(student, parent, chat, cb);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                cb.onError(new Event.Error<>());
            }
        });
    }

    // Paso 6: Crear el padre si no existe
    private void register(StudentModel student, UserModel parent, ChatModel chat, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getAuthRepository().register(parent.getEmail(), parent.getPassword(), new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                Log.d(TAG, "Paso 6 completado. createParent: " + success.getData());
                parent.setId(success.getData().getId());
                registerUser(student, parent, chat, cb);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                cb.onError(new Event.Error<>(error.getException()));
            }
        });
    }

    //Paso 7: Crear el padre en Realtime Database
    private void registerUser(StudentModel student, UserModel userdata, ChatModel chat, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getUserRepository().create(userdata, new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                Log.d(TAG, "Paso 7 completado. createUser: " + success.getData());
                student.setParentId(success.getData().getId());
                updateStudent(student.getId(), student, chat, cb);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                Log.d(TAG, "createUser: " + error.getException());
                cb.onError(new Event.Error<>(error.getException()));
            }
        });
    }
    // Paso 8: Actualizar el estudiante con el id del padre

    public void updateStudent(String id, StudentModel sm, ChatModel chat, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getStudentRepository().update(id, sm, new CallBack<StudentModel>() {


            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                Log.d(TAG, "Paso 8 completado. updateStudent: " + success.getData());
                createChat(sm.getParentId(), sm.getClassroomId(), chat, cb);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                Log.d(TAG, "updateStudent: " + error.getException());
            }
        });
    }

    // Paso 9: Crear el chat con el profesor
    public void createChat(String parentId, String classId, ChatModel chat, CallBack<Boolean> cb){
        chat.setId(ChatIdGenerate.createChatId(classId, parentId));
        chat.setMessagesId(new ArrayList<>());
        BusinessFactory.getInstance().getChatRepository().create(chat, new CallBack<ChatModel>() {
            @Override
            public void onSuccess(Event.Success<ChatModel> success) {
                Log.d(TAG, "Paso 9 completado. createChat: " + success.getData());
                cb.onSuccess(new Event.Success<>(true));
            }

            @Override
            public void onError(Event.Error<ChatModel> error) {
                Log.e(TAG, "createChat: " + error.getException());
            }
        });
    }


}
