package es.ucm.fdi.azalea.integration;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.AuthRepository;
import es.ucm.fdi.azalea.business.model.UserModel;

public class loginUseCase {
    private final AuthRepository authRepository;
    private Event<Boolean> evento;
    public loginUseCase(){
        authRepository = new AuthRepository();
    }
    // la clase task de la api firebase ya gestiona ttodo lo que es acceso al repositorio
    public Event<Boolean> logIn(String mail, String password){

        try{
            Task<AuthResult> task =  authRepository.login(mail, password);
            // la lambda result es un objeto de tipo OnCompleteListener que espera un objeto  de tipo Task<TResult>
            //vamos a distinguir si consigue hacer el logIn o no lo consigue
            task.addOnCompleteListener(result ->{
                if (result.isSuccessful()) {
                    evento = new Event.Success<Boolean>(true) ;

                } else {
                    //cogemos la excepcion de Task
                    evento = new Event.Error<>(result.getException());

                }
            });
        }catch(Exception e){
            return new Event.Error<>(e);
        }
        //mientras autentifica el usuario devolvemos un evento que indica que esta cargando
        return new Event.Loading<>();

    }

}
