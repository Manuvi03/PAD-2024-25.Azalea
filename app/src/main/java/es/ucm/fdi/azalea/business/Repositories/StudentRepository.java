package es.ucm.fdi.azalea.business.Repositories;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;

public class StudentRepository implements Repository<StudentModel> {
    @Override
    public String create(StudentModel item) {
        return "";
    }

    @Override
    public StudentModel findById(int id) {
        return null;
    }

    @Override
    public String update(StudentModel item) {
        return "";
    }

    @Override
    public String delete(int id) {
        return "";
    }

    @Override
    public List<StudentModel> readAll() {
        return Collections.emptyList();
    }
}
