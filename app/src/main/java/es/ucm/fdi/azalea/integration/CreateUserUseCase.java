package es.ucm.fdi.azalea.integration;

import android.util.Log;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;


public class CreateUserUseCase {

    private String TAG = "CreateUserUseCase";

    public void createUser(UserModel userdata, CallBack<UserModel> cb) {
        //1. Verifico que el padre a crear tiene un email distinto a todos los de la bbdd
        //2.En el caso en el que sea cierto, lo registramos en firebase
        register(userdata, cb);
        //3. En el caso en el que se registre correctamente, lo metemos en la realtiemdatabase como user
        //4. Updateamos el Student para que tenga el id del padre que se ha creado
    }

    //1. Verifico que el padre a crear tiene un email distinto a todos los de la bbdd
    public void checkUserExists(String email, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getUserRepository().checkUserExists(email, new CallBack<Boolean>() {
            // si checkUserExists devuelve TRUE significa que existe por lo tanto no podemos crear
            // el usuario pero si devuelve false no existe entonces podemos crear el usuario
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                Log.d(TAG, "Este usuario ya existe");
                cb.onError(new Event.Error<>());
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.d(TAG, "Este usuario no existe");
                cb.onSuccess(new Event.Success<>(false));
            }
        });
    }

    //2.En el caso en el que sea cierto, lo registramos en firebase
    private void register(UserModel userdata, CallBack<UserModel> cb) {
        BusinessFactory.getInstance().getAuthRepository().register(userdata.getEmail(), userdata.getPassword(), new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                Log.d(TAG, "createUser: " + success.getData());
                userdata.setId(success.getData().getId());
                registerUser(userdata, cb);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                cb.onError(new Event.Error<>(error.getException()));
            }
        });
    }

    private void registerUser(UserModel userdata, CallBack<UserModel> cb) {
        BusinessFactory.getInstance().getUserRepository().create(userdata, new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                Log.d(TAG, "createUser: " + success.getData());
                cb.onSuccess(new Event.Success<>(success.getData()));
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                Log.d(TAG, "createUser: " + error.getException());
                cb.onError(new Event.Error<>(error.getException()));
            }
        });
    }

    public void updateStudent(String id, StudentModel sm, CallBack<Boolean> cb) {
        BusinessFactory.getInstance().getStudentRepository().update(id, sm, new CallBack<StudentModel>() {


            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                Log.d(TAG, "updateStudent: " + success.getData());
                cb.onSuccess(new Event.Success<>(true));
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                Log.d(TAG, "updateStudent: " + error.getException());
            }
        });
    }
}
