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
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadClassRoomByIdUseCase;
import es.ucm.fdi.azalea.integration.getCurrUserUseCase;

public class AddStudentViewModel extends ViewModel {

    private final String TAG = "AddStudentViewModel";
    private final MutableLiveData<StudentModel> studentState = new MutableLiveData<>();
    private final MutableLiveData<Event<FirebaseUser>> userfirebaseState = new MutableLiveData<>();
    private final MutableLiveData<Event<UserModel>> userState = new MutableLiveData<>();
    private final MutableLiveData<Event<ClassRoomModel>> classState = new MutableLiveData<>();
    private final CreateStudentUseCase createStudentUseCase;
    private final ReadClassRoomByIdUseCase readClassRoomByIdUseCase;
    private final getCurrUserUseCase getCurrUserUseCase;

    public AddStudentViewModel() {
        createStudentUseCase = new CreateStudentUseCase();
        getCurrUserUseCase = new getCurrUserUseCase();
        readClassRoomByIdUseCase = new ReadClassRoomByIdUseCase();
    }

    public LiveData<StudentModel> getStudentState() {return studentState;}

    public void createStudent(StudentModel sm) {
        Log.i(TAG, "createStudent: " + sm);
        createStudentUseCase.execute(sm).observeForever(studentState::setValue);
    }

    public LiveData<Event<FirebaseUser>> getCurrUserFirebaseLD() {return userfirebaseState;}

    public void getCurrUserFirebase() {
        userState.setValue(new Event.Loading<>());
        getCurrUserUseCase.getCurrentUser(new CallBack<FirebaseUser>() {


            @Override
            public void onSuccess(Event.Success<FirebaseUser> success) {
                userfirebaseState.postValue(success);
            }

            @Override
            public void onError(Event.Error<FirebaseUser> error) {
                userfirebaseState.postValue(error);
            }
        });
    }

    public LiveData<Event<UserModel>> getCurrUserLD() {return userState;}

    public void getCurrUser(String id) {
        userState.setValue(new Event.Loading<>());
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

    public void getClass(String classId) {
        classState.setValue(new Event.Loading<>());
        readClassRoomByIdUseCase.readClassRoomById(classId, new CallBack<ClassRoomModel>() {
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
}
