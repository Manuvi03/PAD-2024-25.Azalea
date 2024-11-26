package es.ucm.fdi.azalea.business.Repositories;

import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface ClassRoomRepository {
    public void readById(String id, CallBack<ClassRoomModel> cb);
}
