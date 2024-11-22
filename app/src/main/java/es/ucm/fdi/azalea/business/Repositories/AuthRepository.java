package es.ucm.fdi.azalea.business.Repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface AuthRepository {

    public void login(String mail, String password, CallBack<UserModel> cb);
    public void register(String mail, String password, CallBack<UserModel> cb);
    public void logout();
}
