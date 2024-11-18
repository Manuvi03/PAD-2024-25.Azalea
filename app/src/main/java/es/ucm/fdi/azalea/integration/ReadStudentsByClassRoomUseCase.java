package es.ucm.fdi.azalea.integration;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class ReadStudentsByClassRoomUseCase {

    public <T> Event<T> readStudentsByClassRoomUseCase(){
        try{
            List<StudentModel> list = new ArrayList<>();
            //list = BusinessFactory.getInstance().getStudentRepository().readByClassRoom();
            return new Event.Success<>(null);
        }catch (Exception e){
            return new Event.Error<>(e);
        }
    }
}
