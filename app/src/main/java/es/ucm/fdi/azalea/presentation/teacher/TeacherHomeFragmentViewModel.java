package es.ucm.fdi.azalea.presentation.teacher;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.CreateEventUseCase;
import es.ucm.fdi.azalea.integration.GetEventsForDateUseCase;
import es.ucm.fdi.azalea.integration.GetEventsFromClassroomUseCase;
import es.ucm.fdi.azalea.integration.ModifyEventUseCase;

public class TeacherHomeFragmentViewModel extends ViewModel {
    private final GetEventsForDateUseCase getEventsForDateUseCase;
    private final GetEventsFromClassroomUseCase getEventsFromClassroom;
    private final MutableLiveData<List<EventModel>> eventsDate = new MutableLiveData<>();
    private final MutableLiveData<List<EventModel>> eventsClassroom = new MutableLiveData<>();
    private final MutableLiveData<EventModel> event = new MutableLiveData<>();

    private final String TAG = "TeacherHomeFragmentViewModel";
    public TeacherHomeFragmentViewModel(){
        this.getEventsForDateUseCase = new GetEventsForDateUseCase();
        this.getEventsFromClassroom = new GetEventsFromClassroomUseCase();
    }

    public LiveData<List<EventModel>> getEventsForDateLiveData() {
        return eventsDate;
    }
    public LiveData<List<EventModel>> getEventsForClassroomLiveData() {return eventsClassroom;}

    public void getEventsForDate(String date) {
        Log.i(TAG, "getEventsForDate: " + date);
        getEventsForDateUseCase.execute(date).observeForever(eventsDate::setValue);
    }

    public void getEventsForClassroom(String classroomId) {
        Log.i(TAG, "getEventsForClassroom: " + classroomId);
        getEventsFromClassroom.execute(classroomId).observeForever(eventsClassroom::setValue);
    }

    public LiveData<EventModel> createEventLiveData(){return event;}

    public void CreateEvent(EventModel em){
        CreateEventUseCase createEventUseCase = new CreateEventUseCase(em);
        createEventUseCase.execute(em).observeForever(event::setValue);
    }

    public LiveData<EventModel> modifyEventLiveData(){return event;}

    public void ModifyEvent(EventModel em){
        ModifyEventUseCase modifyEventUseCase = new ModifyEventUseCase();
        modifyEventUseCase.execute(em).observeForever(event::setValue);
    }
}
