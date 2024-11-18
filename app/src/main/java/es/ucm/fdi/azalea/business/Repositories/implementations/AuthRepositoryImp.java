package es.ucm.fdi.azalea.business.Repositories.implementations;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.ucm.fdi.azalea.business.Repositories.AuthRepository;

public class AuthRepositoryImp implements AuthRepository {


    public Task<AuthResult> login(String mail, String password){
       return  FirebaseAuth.getInstance().signInWithEmailAndPassword(mail,password);
    }

    public Task<AuthResult> register(String mail, String password){
        return  FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,password);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }
}
