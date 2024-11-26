package es.ucm.fdi.azalea.business.Repositories;

import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface MarkRepository {
    public void create(MarkModel item, CallBack<MarkModel> cb);
}
