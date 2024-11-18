package es.ucm.fdi.azalea.business.Repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface AuthRepository {

    public Task<AuthResult> login(String mail, String password);
    public Task<AuthResult> register(String mail, String password);
    public void logout();
}
