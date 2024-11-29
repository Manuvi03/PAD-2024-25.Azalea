package es.ucm.fdi.azalea.business.Repositories;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface UserRepository {
    public void findById(String id, CallBack<UserModel> cb);
    public void create(UserModel item,CallBack<UserModel> cb);
    public void checkUserExists(String mail,CallBack<Boolean> cb);
    public void update(String id, UserModel item, CallBack<Boolean> cb);
}
