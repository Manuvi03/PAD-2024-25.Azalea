package es.ucm.fdi.azalea.integration;

import android.util.Log;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.EventModel;

public class ModifyEventUseCase {

    private final String TAG = "ModifyEventUseCase";

    public ModifyEventUseCase(){}

    public void execute(EventModel em, CallBack<EventModel> cb) {
        modifyEvent(em, cb);
    }

    private void modifyEvent(EventModel em, CallBack<EventModel> cb){
        Log.d(TAG, "modifyEvent: " + em.getId());
        BusinessFactory.getInstance().getEventRepository().modify(em, new CallBack<EventModel>() {
            @Override
            public void onSuccess(Event.Success<EventModel> success) {
                Log.d(TAG, "modifyEvent: " + success.getData().getId());
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<EventModel> error) {
                Log.d(TAG, "modifyEvent: " + error.getErrorData());
                cb.onError(error);
            }
        });
    }
}
