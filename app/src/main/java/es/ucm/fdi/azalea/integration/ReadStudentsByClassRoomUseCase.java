package es.ucm.fdi.azalea.integration;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class ReadStudentsByClassRoomUseCase {

    // caso de uso que, a partir de el id de una clase, busca a sus estudiantes
    public Event<List<StudentModel>> readStudentsByClassRoomUseCase(String classroomId){
        try{
            // se obtiene la lista de estudiantes
            List<StudentModel> list;
            list = BusinessFactory.getInstance().getStudentRepository().readByClassRoomId(classroomId);
            Log.d("ReadStudentsByClassRoomUseCase", "Alumnos de la clase " + classroomId + " leidos correctamente");
            return new Event.Success<>(list);
        }catch (Exception e){
            Log.w("ReadStudentsByClassRoomUseCase", "Excepcion al leer los alumnos de la clase " + classroomId);
            return new Event.Error<>(e);
        }
    }
}
