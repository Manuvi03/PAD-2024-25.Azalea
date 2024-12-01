package es.ucm.fdi.azalea.business.Repositories;

import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface StudentRepository {
    void create(StudentModel item, CallBack<StudentModel> cb);
    void update(String id, StudentModel item, CallBack<StudentModel> cb);
    String delete(String id);
    List<StudentModel> readAll();
    void readById(String id, CallBack<StudentModel> cb);
    void readByClassRoomId(String classroomId, CallBack<List<StudentModel>> cb);
}
