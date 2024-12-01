package es.ucm.fdi.azalea.business.Repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface AuthRepository {

    public void login(String mail, String password, CallBack<UserModel> cb);
    public void register(String mail, String password, CallBack<UserModel> cb);
    public void logout();
    public void updateCurrUserMail(String mail,CallBack<Boolean> cb);
    public void updateCurrUserPassword(String password,CallBack<Boolean> cb);
    public void sendUpdatePasswordMail(String mail, CallBack<Boolean> cb);
    public void getCurrUser(CallBack<FirebaseUser> cb);
}
