package es.ucm.fdi.azalea.presentation.parent;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.GetEventsForDateUseCase;
import es.ucm.fdi.azalea.integration.GetEventsFromClassroomUseCase;
import es.ucm.fdi.azalea.integration.getCurrUserUseCase;

public class ParentHomeFragmentViewModel extends ViewModel{

    private final String TAG = "ParentHomeFragmentViewModel";
    private final GetEventsForDateUseCase getEventsForDateUseCase;
    private final GetEventsFromClassroomUseCase getEventsFromClassroom;
    private final getCurrUserUseCase getCurrUser;
    private final MutableLiveData<Event<List<EventModel>>> eventsDate = new MutableLiveData<>();
    private final MutableLiveData<Event<List<EventModel>>> eventsClassroom = new MutableLiveData<>();
    private final MutableLiveData<Event<UserModel>> parentClassroom = new MutableLiveData<>();

    public ParentHomeFragmentViewModel() {
        this.getEventsForDateUseCase = new GetEventsForDateUseCase();
        this.getEventsFromClassroom = new GetEventsFromClassroomUseCase();
        this.getCurrUser = new getCurrUserUseCase();
    }

    public LiveData<Event<UserModel>> getParentForClassroomLiveData(){return parentClassroom;}

    public void getParentForClassroom(){
        Log.i(TAG, "getParentForClassroom");
        getCurrUser.getCurrentUser(new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                parentClassroom.postValue(success);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                parentClassroom.postValue(error);
            }
        });
    }

    public LiveData<Event<List<EventModel>>>getEventsForDateLiveData() {
        return eventsDate;
    }
    public LiveData<Event<List<EventModel>>> getEventsForClassroomLiveData() {return eventsClassroom;}

    public void getEventsForDate(String date) {
        Log.i(TAG, "getEventsForDate: " + date);
        getEventsForDateUseCase.execute(date, new CallBack<List<EventModel>>() {

            @Override
            public void onSuccess(Event.Success<List<EventModel>> success) {
                eventsDate.postValue(success);
            }

            @Override
            public void onError(Event.Error<List<EventModel>> error) {
                eventsDate.postValue(error);
            }
        });
    }

    public void getEventsForClassroom() {
        Log.i(TAG, "getEventsForClassroom ");
        getEventsFromClassroom.execute(new CallBack<List<EventModel>>() {
            @Override
            public void onSuccess(Event.Success<List<EventModel>> success) {
                eventsClassroom.postValue(success);
            }

            @Override
            public void onError(Event.Error<List<EventModel>> error) {
                eventsClassroom.postValue(error);
            }
        });
    }
}
