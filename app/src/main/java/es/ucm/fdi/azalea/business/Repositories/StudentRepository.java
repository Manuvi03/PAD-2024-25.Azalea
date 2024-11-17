package es.ucm.fdi.azalea.business.Repositories;

import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;

public interface StudentRepository {
    public String create(StudentModel item);
    public String update(String id, StudentModel item);
    public String delete(String id);
    public List<StudentModel> readAll();
    public StudentModel readById(String id);
    public List<StudentModel> readByClassRoomId(String classroomId);
}
