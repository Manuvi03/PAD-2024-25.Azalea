package es.ucm.fdi.azalea.presentation.parent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.GetEventsForDateUseCase;

public class ParentViewModel extends ViewModel {

    private final GetEventsForDateUseCase getEventsForDateUseCase;
    private final MutableLiveData<List<EventModel>> events = new MutableLiveData<>();

    public ParentViewModel() {
        getEventsForDateUseCase = new GetEventsForDateUseCase();
    }

    public LiveData<List<EventModel>> getEventsForDateLiveData() {
        return events;
    }

    public void getEventsForDate(String date) {
        getEventsForDateUseCase.execute(date).observeForever(events::setValue);
    }
}
