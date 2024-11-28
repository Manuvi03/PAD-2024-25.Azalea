package es.ucm.fdi.azalea.business.Repositories;

import java.util.List;

import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface MarkRepository {
    public void create(MarkModel item, CallBack<MarkModel> cb);
    public void update(String id, MarkModel item, CallBack<MarkModel> cb);
    public void findById(String id, CallBack<MarkModel> cb);
    public void readByStudentId(String studentId, CallBack<List<MarkModel>> cb);
}
