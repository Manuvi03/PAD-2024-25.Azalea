package es.ucm.fdi.azalea.integration;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class GradeMarkUseCase {

    // caso de uso que busca la informacion de una clase por su id
    public void gradeMark(MarkModel markModel, StudentModel studentModel, CallBack<StudentModel> cb){
        BusinessFactory.getInstance().getMarkRepository().create(markModel, new CallBack<MarkModel>(){

            @Override
            public void onSuccess(Event.Success<MarkModel> success) {

                // se anyade la nota al estudiante
                List<String> marks = studentModel.getMarksId();
                if(marks == null) marks = new ArrayList<String>();
                marks.add(markModel.getId());
                studentModel.setMarksId(marks);

                addMarkToStudent(success.getData().getId(), studentModel, cb);
            }

            @Override
            public void onError(Event.Error<MarkModel> error) {
                addMarkToStudent(error.getException().toString(), studentModel, cb);
            }
        });
    }

    // anyade al estudiante la nueva nota creada
    private void addMarkToStudent(String markId, StudentModel studentModel, CallBack<StudentModel> cb){
        BusinessFactory.getInstance().getStudentRepository().update(studentModel.getId(), studentModel, new CallBack<StudentModel>(){

            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                cb.onError(error);
            }
        });
    }

}
