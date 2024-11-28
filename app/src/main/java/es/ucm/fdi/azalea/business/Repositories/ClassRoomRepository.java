package es.ucm.fdi.azalea.business.Repositories;

import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.integration.CallBack;

import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface ClassRoomRepository {
    public void create(ClassRoomModel data, CallBack<ClassRoomModel> cb);
    public void read(String id, CallBack<ClassRoomModel> cb);
    public void update(ClassRoomModel data, CallBack<ClassRoomModel> cb);
    public void delete(String id, CallBack<ClassRoomModel> cb);
    public void readByIdTeacher(String idTeacher, CallBack<ClassRoomModel> cb);

    public void readById(String id, CallBack<ClassRoomModel> cb);
}
