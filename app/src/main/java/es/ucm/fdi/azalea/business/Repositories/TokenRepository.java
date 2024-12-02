package es.ucm.fdi.azalea.business.Repositories;

import es.ucm.fdi.azalea.business.model.TokenModel;
import es.ucm.fdi.azalea.integration.CallBack;

public interface TokenRepository {
    public void create(TokenModel token, CallBack<TokenModel> callBack);
    public void update(TokenModel tokenModel, CallBack<TokenModel> callBack);
    public void readByUserId(String idToSend, CallBack<TokenModel> tokenModelCallBack);
}
