package es.ucm.fdi.azalea.repos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.credentials.webauthn.Cbor;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;

import es.ucm.fdi.azalea.business.Repositories.implementations.AuthRepositoryImp;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;



// el test no funciona, no hemos sido capaces de emular la funcion del repositorio al implementarse con callbacks
public class AuthUnitTest {

    @Mock
    private FirebaseAuth mockAuth;
    @Mock
    private FirebaseUser mockFirebaseUser;
    @Mock
    private Task<AuthResult> mockTask;

    private AuthRepositoryImp authRepository;
    @Captor
    private ArgumentCaptor<OnCompleteListener<AuthResult>> captor;
    @Captor
    private ArgumentCaptor<Event.Success<UserModel>> cbCaptor;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        authRepository = new AuthRepositoryImp(mockAuth);

    }

    @Test
    public void testRegister_Succesful() throws InterruptedException {
        String mail = "test@example.com";
        String password ="testPassword";
        String id = "1";
        UserModel expectedResult = new UserModel();
        expectedResult.setEmail(mail);
        expectedResult.setPassword(password);
        expectedResult.setId(id);
        when(mockTask.isSuccessful()).thenReturn(true); // Simula que la tarea fue exitosa
        when(mockAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(mockTask); // Simula la creación del usuario
        when(mockTask.getResult()).thenReturn(mock(AuthResult.class)); // Simula el resultado de la tarea
        when(mockTask.getResult().getUser()).thenReturn(mockFirebaseUser); // Simula el usuario autenticado
        when(mockFirebaseUser.getUid()).thenReturn(id); // Simula el ID del usuario




        // Llamar al método que estamos probando
        authRepository.register(mail, password, new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                UserModel user = success.getData();
                assertNotNull(user); // Asegurar que se devolvió un usuario
                assertEquals(expectedResult.getEmail(), user.getEmail());
                assertEquals(expectedResult.getPassword(), user.getPassword());
                assertEquals(expectedResult.getId(), user.getId());

            }

            @Override
            public void onError(Event.Error<UserModel> error) {


            }
        });

        OnCompleteListener<AuthResult> capturedListener = captor.getValue();
        capturedListener.onComplete(mockTask);



    }

}
