package es.ucm.fdi.azalea.business.Repositories;

import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface StudentRepository {
    public String create(StudentModel item);
    public void update(String id, StudentModel item, CallBack<StudentModel> cb);
    public String delete(String id);
    public List<StudentModel> readAll();
    public void readById(String id, CallBack<StudentModel> cb);
    public void readByClassRoomId(String classroomId, CallBack<List<StudentModel>> cb);
}
