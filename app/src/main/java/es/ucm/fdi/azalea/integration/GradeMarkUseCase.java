package es.ucm.fdi.azalea.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class GradeMarkUseCase {

    // caso de uso que busca la informacion de una clase por su id
    public void gradeMark(MarkModel markModel, StudentModel studentModel, CallBack<StudentModel> cb){

        // lee las notas del estudiante de la base de datos
        BusinessFactory.getInstance().getMarkRepository().readByStudentId(studentModel.getId(), new CallBack<List<MarkModel>>(){

            @Override
            public void onSuccess(Event.Success<List<MarkModel>> success) {
                int i = 0;
                boolean encontrado = false;

                // si encuentra notas, busca la de la asignatura correspondiente
                while (i < success.getData().size() && !encontrado){
                    if(Objects.equals(success.getData().get(i).getSubject(), markModel.getSubject()))
                        encontrado = true;
                    else i++;
                }

                // si existia la nota, la actualiza, y si no, la crea
                if(!encontrado) addMark(markModel, studentModel, cb);
                else {
                    // a la nueva nota se le pone el id antiguo para realizar correctamente el update
                    markModel.setId(success.getData().get(i).getId());
                    updateMark(success.getData().get(i).getId(), markModel, studentModel, cb);
                }
            }

            @Override
            public void onError(Event.Error<List<MarkModel>> error) {
                addMarkToStudent(error.getException().toString(), studentModel, cb);
            }
        });
    }

    // crea la nueva nota si no existia
    private void addMark(MarkModel markModel, StudentModel studentModel, CallBack<StudentModel> cb){
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

    // actualiza la nota si es que existia
    private void updateMark(String id, MarkModel markModel, StudentModel studentModel, CallBack<StudentModel> cb){
        BusinessFactory.getInstance().getMarkRepository().update(id, markModel, new CallBack<MarkModel>(){

            @Override
            public void onSuccess(Event.Success<MarkModel> success) {
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
