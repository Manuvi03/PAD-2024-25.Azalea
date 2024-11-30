package es.ucm.fdi.azalea.presentation.addstudent;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.CreateStudentUseCase;
import es.ucm.fdi.azalea.integration.CreateUserUseCase;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadClassRoomByIdUseCase;
import es.ucm.fdi.azalea.integration.getCurrUserUseCase;

public class AddStudentViewModel extends ViewModel {

    private final String TAG = "AddStudentViewModel";
    private final MutableLiveData<StudentModel> studentState = new MutableLiveData<>();
    private final MutableLiveData<Event<FirebaseUser>> userfirebaseState = new MutableLiveData<>();
    private final MutableLiveData<Event<UserModel>> userState = new MutableLiveData<>();
    private final MutableLiveData<Event<ClassRoomModel>> classState = new MutableLiveData<>();
    private final MutableLiveData<Event<UserModel>> parentState = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> checkParentState = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> updateStudentState = new MutableLiveData<>();

    private final CreateStudentUseCase createStudentUseCase;
    private final ReadClassRoomByIdUseCase readClassRoomByIdUseCase;
    private final getCurrUserUseCase getCurrUserUseCase;
    private final CreateUserUseCase createUserUseCase;

    public AddStudentViewModel() {
        createStudentUseCase = new CreateStudentUseCase();
        getCurrUserUseCase = new getCurrUserUseCase();
        readClassRoomByIdUseCase = new ReadClassRoomByIdUseCase();
        createUserUseCase = new CreateUserUseCase();
    }

    public LiveData<Event<Boolean>> getCheckParentState() {return checkParentState;}

    public void checkParent(String email) {
        createUserUseCase.checkUserExists(email, new CallBack<Boolean>() {


            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                checkParentState.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                checkParentState.postValue(error);
            }
        });
    }


    public LiveData<Event<UserModel>> getParentState(){return parentState;}

    public void createParent(UserModel um) {
        createUserUseCase.createUser(um, new CallBack<UserModel>() {

            @Override
            public void onSuccess(Event.Success<UserModel> success) {

            }

            @Override
            public void onError(Event.Error<UserModel> error) {

            }
        });
    }

    public LiveData<StudentModel> getStudentState() {return studentState;}

    public void createStudent(StudentModel sm) {
        Log.i(TAG, "createStudent: " + sm);
        createStudentUseCase.execute(sm).observeForever(studentState::postValue);
    }

    public LiveData<Event<FirebaseUser>> getCurrUserFirebaseLD() {return userfirebaseState;}

    public void getCurrUserFirebase() {
        getCurrUserUseCase.getCurrentUser(new CallBack<FirebaseUser>() {


            @Override
            public void onSuccess(Event.Success<FirebaseUser> success) {
                Log.d(TAG, "Usuario autenticado obtenido: " + success.getData().getEmail());
                userfirebaseState.postValue(success);
            }

            @Override
            public void onError(Event.Error<FirebaseUser> error) {
                Log.d(TAG, "Error obteniendo usuario autenticado: " + error);
                userfirebaseState.postValue(error);
            }
        });
    }

    public LiveData<Event<UserModel>> getCurrUserLD() {return userState;}

    public void getCurrUser(String id) {
        getCurrUserUseCase.getUserModel(id,new CallBack<UserModel>() {


            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                userState.postValue(success);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                userState.postValue(error);
            }
        });
    }

    public LiveData<Event<ClassRoomModel>> getClassState() {return classState;}

    public void getClass(String id) {
        readClassRoomByIdUseCase.readClassRoomById(id, new CallBack<ClassRoomModel>() {
            @Override
            public void onSuccess(Event.Success<ClassRoomModel> success) {
                classState.postValue(success);
            }

            @Override
            public void onError(Event.Error<ClassRoomModel> error) {
                classState.postValue(error);
            }
        }
        );
    }

    public LiveData<Event<Boolean>> getUpdateStudentState() {return updateStudentState;}

    public void updateStudent(String id, StudentModel sm) {
        createUserUseCase.updateStudent(id, sm, new CallBack<Boolean>(){

            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                Log.i(TAG, "El Estudiante se ha updateado correctamente");
                updateStudentState.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.i(TAG, "Error al actualizar el estudiante");
                updateStudentState.postValue(error);
            }
        });
    }
}
