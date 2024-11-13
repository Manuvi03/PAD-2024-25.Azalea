package es.ucm.fdi.azalea.business.Repositories;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class ClassRoomRepository implements Repository<ClassRoomModel>{
    @Override
    public String create(ClassRoomModel item) {
        return "";
    }

    @Override
    public ClassRoomModel findById(int id) {
        return null;
    }

    @Override
    public String update(ClassRoomModel item) {
        return "";
    }

    @Override
    public String delete(int id) {
        return "";
    }

    @Override
    public List<ClassRoomModel> readAll() {
        return Collections.emptyList();
    }
}
