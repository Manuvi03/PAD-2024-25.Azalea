package es.ucm.fdi.azalea.integration;

import com.google.android.gms.tasks.Task;

import java.util.List;

import es.ucm.fdi.azalea.business.Repositories.implementations.EventRepositoryImp;
import es.ucm.fdi.azalea.business.model.EventModel;

public class GetEventsForDateUseCase{

    EventRepositoryImp eventRepositoryImp;

    public GetEventsForDateUseCase(){
        eventRepositoryImp = new EventRepositoryImp();
    }

    public void getEventsForDate(String date, CallBack<List<EventModel>> cb){
        try{
            Task task = eventRepositoryImp.getEventsForDate(date);
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }
}
