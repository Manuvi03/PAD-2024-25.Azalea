package es.ucm.fdi.azalea.presentation.parent;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.CreateEventUseCase;
import es.ucm.fdi.azalea.integration.GetEventsForDateUseCase;
import es.ucm.fdi.azalea.integration.GetEventsFromClassroomUseCase;

public class ParentViewModel extends ViewModel {

    private final GetEventsForDateUseCase getEventsForDateUseCase;
    private final GetEventsFromClassroomUseCase getEventsFromClassroom;
    private final MutableLiveData<List<EventModel>> eventsDate = new MutableLiveData<>();
    private final MutableLiveData<List<EventModel>> eventsClassroom = new MutableLiveData<>();
    private final MutableLiveData<EventModel> event = new MutableLiveData<>();
    public ParentViewModel() {
        this.getEventsForDateUseCase = new GetEventsForDateUseCase();
        this.getEventsFromClassroom = new GetEventsFromClassroomUseCase();
    }

    public LiveData<List<EventModel>> getEventsForDateLiveData() {
        return eventsDate;
    }
    public LiveData<List<EventModel>> getEventsForClassroomLiveData() {return eventsClassroom;}

    public void getEventsForDate(String date) {
        Log.i(this.getClass().getName(), "getEventsForDate: " + date);
        getEventsForDateUseCase.execute(date).observeForever(eventsDate::setValue);
    }

    public void getEventsForClassroom(String classroomId) {
        Log.i(this.getClass().getName(), "getEventsForClassroom: " + classroomId);
        getEventsFromClassroom.execute(classroomId).observeForever(eventsClassroom::setValue);
    }

    public LiveData<EventModel> createEventLiveData(){return event;}

    public void CreateEvent(EventModel em){
        CreateEventUseCase createEventUseCase = new CreateEventUseCase(em);
        createEventUseCase.execute(em);

    }
}

