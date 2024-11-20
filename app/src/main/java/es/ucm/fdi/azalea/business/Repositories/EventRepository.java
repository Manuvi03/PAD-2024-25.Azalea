package es.ucm.fdi.azalea.business.Repositories;

import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.CallBack;

import com.google.android.gms.tasks.Task;


public interface EventRepository {
    public void create(EventModel em, CallBack<List<EventModel>> cb);

}
