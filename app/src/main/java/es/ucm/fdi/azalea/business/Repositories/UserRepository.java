package es.ucm.fdi.azalea.business.Repositories;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface UserRepository {
    void findById(String id, CallBack<UserModel> cb);
    void create(UserModel item, CallBack<UserModel> cb);
    void checkUserExists(String mail, CallBack<Boolean> cb);
    //void findByIdWithQuery(String id, CallBack<UserModel> cb);
}
