package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class updateStudentUseCase {
    private static final String TAG = "updateStudentUseCase";

    public void updateStudent(StudentModel student, CallBack<Boolean> cb){
        try{
            BusinessFactory.getInstance().getStudentRepository().update(student.getId(), student, new CallBack<StudentModel>() {
                @Override
                public void onSuccess(Event.Success<StudentModel> success) {
                    cb.onSuccess(new Event.Success<>(true));
                }

                @Override
                public void onError(Event.Error<StudentModel> error) {
                    Log.d(TAG,"Error al updatear el student en la bd",error.getException());
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch(Exception e){
            Log.d(TAG,"Error al updatear el student en useCase",e);
            cb.onError(new Event.Error<>(e));
        }

    }
}
