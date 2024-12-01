package es.ucm.fdi.azalea.integration;

import android.util.Log;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;

public class ReadClassRoomByIdUseCase {

    // caso de uso que busca la informacion de una clase por su id
    public void readClassRoomById(String classId, CallBack<ClassRoomModel> cb){
        BusinessFactory.getInstance().getClassRoomRepository().readById(classId, new CallBack<ClassRoomModel>(){

            @Override
            public void onSuccess(Event.Success<ClassRoomModel> success) {
                Log.d("ReadClassRoomByIdUseCase", "readClassRoomById: " + success.getData());
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<ClassRoomModel> error) {
                Log.d("ReadClassRoomByIdUseCase", "error");
                cb.onError(error);
            }
        });
    }
}
