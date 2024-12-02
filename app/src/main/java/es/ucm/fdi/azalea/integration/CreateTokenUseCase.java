package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.TokenModel;

public class CreateTokenUseCase {
    // Crear el Token en la RTDB

    public void createToken(String token, CallBack<TokenModel> cb){

        TokenModel tokenModel = new TokenModel(token);
        BusinessFactory.getInstance().getTokenRepository().create(tokenModel, new CallBack<TokenModel>() {
            @Override
            public void onSuccess(Event.Success<TokenModel> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<TokenModel> error) {
                cb.onError(error);
            }
        });
    }
}
