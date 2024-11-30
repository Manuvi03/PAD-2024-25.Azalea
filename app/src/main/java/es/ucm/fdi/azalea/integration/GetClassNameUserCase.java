package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class GetClassNameUserCase {
    private final String TAG = "GetClassNameUserCase";

    public void execute(CallBack<String> callBack) {
        getCurrentUser(callBack);
    }

    private void getCurrentUser(CallBack<String> cb){
        try{
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Se ha obtenido el usuario actual: " + success.getData().getUid());
                    getUserModel(cb,success.getData().getUid());
                }

                @Override
                public void onError(Event.Error<FirebaseUser> error) {
                    cb.onError(new Event.Error<>());
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    private void getUserModel(CallBack<String> cb, String uid){
        try{
            BusinessFactory.getInstance().getUserRepository().findById(uid, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Se ha obtenido el UserModel: " + success.getData().getClassId());
                    getClassName(cb, success.getData().getClassId());
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    cb.onError(new Event.Error<>());
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    private void getClassName(CallBack<String> cb, String classid){
        try {
            BusinessFactory.getInstance().getClassRoomRepository().readById(classid, new CallBack<ClassRoomModel>() {
                @Override
                public void onError(Event.Error<ClassRoomModel> error) {
                    cb.onError(new Event.Error<>());
                }

                @Override
                public void onSuccess(Event.Success<ClassRoomModel> success) {
                    Log.d(TAG, "Nombre de la clase: " + success.getData().getName());
                    cb.onSuccess(new Event.Success<>(success.getData().getName()));
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }


}
