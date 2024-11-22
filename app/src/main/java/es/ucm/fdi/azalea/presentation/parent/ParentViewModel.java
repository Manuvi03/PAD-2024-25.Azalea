package es.ucm.fdi.azalea.presentation.parent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.CreateEventUseCase;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.GetEventsForDateUseCase;

public class ParentViewModel extends ViewModel {

    private final GetEventsForDateUseCase getEventsForDateUseCase;
    private CreateEventUseCase createEventUseCase;
    private final MutableLiveData<List<EventModel>> events = new MutableLiveData<>();
    private final MutableLiveData<EventModel> event = new MutableLiveData<>();
    public ParentViewModel() {
        getEventsForDateUseCase = new GetEventsForDateUseCase();
    }

    public LiveData<List<EventModel>> getEventsForDateLiveData() {
        return events;
    }

    public void getEventsForDate(String date) {
        getEventsForDateUseCase.execute(date).observeForever(events::setValue);
    }

    public LiveData<EventModel> createEventLiveData(){return event;}

    public void CreateEvent(EventModel em){
        createEventUseCase = new CreateEventUseCase(em);
        createEventUseCase.execute(em);

    }
}

