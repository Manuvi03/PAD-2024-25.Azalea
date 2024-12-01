package es.ucm.fdi.azalea.integration;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.UserModel;


//esta clase tiene dos funciones una para autenticar al profesor y otra para crear la clase y el usuario en la base de datos
public class createTeacherUseCase {
    private static String TAG = "createTeacherUseCase";
    private final static List<String> asignaturas = new ArrayList<String>(List.of("Matemáticas","Física","Química","Lengua","Inglés","Biología"));
    public void createTeacher(UserModel data,String className, CallBack<Boolean> cb){
        //Esta funcion crea una cadena de llamadas, primero se registra el usuario en Auth de firebase, Segundo se crea la clase con el id de
        // firebaseAuth y Tercero se crea el Usuario en realtime database
        registerTeacherAuth(data,className,cb);
    }

    public void authenticateTeacher(String mail, String password, CallBack<Boolean> cb){
        try{
            // primero lo autenticamos con firebase authentication
            // si checkUserExists devuelve TRUE significa que existe por lo tanto no podemos crear
            // el usuario pero si devuelve false no existe entonces podemos crear el usuario
            BusinessFactory.getInstance().getUserRepository().checkUserExists(mail, new CallBack<Boolean>() {
                @Override
                public void onSuccess(Event.Success<Boolean> success) {
                    Log.d(TAG,"se autentica el usuario");
                    cb.onError(new Event.Error<>());
                }

                @Override
                public void onError(Event.Error<Boolean> error) {
                    cb.onSuccess(new Event.Success<>(true));
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    private void registerTeacherAuth(UserModel userdata,String className, CallBack<Boolean> cb){
        try{
            // primero lo autenticamos con firebase authentication
            BusinessFactory.getInstance().getAuthRepository().register(userdata.getEmail(), userdata.getPassword(), new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG,"se autentica el usuario");
                    userdata.setId(success.getData().getId());
                    registerClassRoom(userdata,className,cb);
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    private void registerClassRoom(UserModel data,String className, CallBack<Boolean> cb){
        try{
            ClassRoomModel new_class = new ClassRoomModel();
            new_class.setIdTeacher(data.getId());
            new_class.setName(className);
            new_class.setSubjects(asignaturas);
            BusinessFactory.getInstance().getClassRoomRepository().create(new_class, new CallBack<ClassRoomModel>() {
                @Override
                public void onSuccess(Event.Success<ClassRoomModel> success) {
                    Log.d(TAG,"se crea la clase correctamente");
                    data.setClassId(success.getData().getId());
                    registerTeacher(data,cb);
                }

                @Override
                public void onError(Event.Error<ClassRoomModel> error) {
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    private void registerTeacher(UserModel userdata, CallBack<Boolean> cb) {
        try{
            BusinessFactory.getInstance().getUserRepository().create(userdata, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG,"se crea el usuario en realtime correctamente");
                    cb.onSuccess(new Event.Success<Boolean>(true));
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    Log.d(TAG,"no se crea el usuario correctamente en realtime",error.getException());
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch(Exception e){
            Log.d(TAG,"no se crea el usuario correctamente en realtime",e);

            cb.onError(new Event.Error<>(e));
        }

    }
}
