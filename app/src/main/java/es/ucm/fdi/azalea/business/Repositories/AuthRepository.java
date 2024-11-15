package es.ucm.fdi.azalea.business.Repositories;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.UserModel;

public class AuthRepository implements Repository<UserModel> {
    @Override
    public String create(UserModel item) {
        return "";
    }

    @Override
    public UserModel findById(String id) {
        return null;
    }

    @Override
    public String update(UserModel item) {
        return "";
    }

    @Override
    public String delete(String id) {
        return "";
    }

    @Override
    public List<UserModel> readAll() {
        return Collections.emptyList();
    }
}
