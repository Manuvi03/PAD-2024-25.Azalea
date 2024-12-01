package es.ucm.fdi.azalea.presentation.addstudent;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.CreateStudentAndParentUseCase;
import es.ucm.fdi.azalea.presentation.profilepicture.RandomColor;

public class AddStudentViewModel extends ViewModel {

    private final String TAG = "AddStudentViewModel";
    private final MutableLiveData<Event<Boolean>> hState = new MutableLiveData<>();

    private final CreateStudentAndParentUseCase createStudentAndParentUseCaseUseCase;


    public AddStudentViewModel() {

        createStudentAndParentUseCaseUseCase = new CreateStudentAndParentUseCase();
    }

    public LiveData<Event<Boolean>> gethState(){return hState;}

    public void funcion(StudentModel student, UserModel parent, Context context){
        // se generan las fotos de perfil tanto de padre como de estudiante
        generateProfileImages(student, parent, context);

        // se crea el estudiante y el padre
        createStudentAndParentUseCaseUseCase.execute(student,parent, new CallBack<Boolean>() {

            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                Log.d(TAG, "onSuccess: Se ha creado el estudiante correctamente");
                hState.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.d(TAG, "onError: Error al crear el estudiante");
                hState.postValue(error);
            }
        });
    }

    private void generateProfileImages(StudentModel student, UserModel parent, Context context){
        // se genera la imagen de perfil del estudiante
        String color = RandomColor.generateAppRandomColor(context);
        String path = "https://ui-avatars.com/api/?name=" + student.getName() + "&background=" + color + "&length=1&rounded=true";
        student.setProfileImage(path);

        // se genera la imagen de perfil del padre
        color = RandomColor.generateAppRandomColor(context);
        path = "https://ui-avatars.com/api/?name=" + parent.getName() + "&background=" + color + "&length=1&rounded=true";
        parent.setProfileImage(path);
    }

}
