package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.azalea.business.Repositories.TokenRepository;
import es.ucm.fdi.azalea.business.model.TokenModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class TokenRepositoryImp implements TokenRepository {

    private final static String TAG = "TokenRepository";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference tokensReference = database.getReference("tokens");

    @Override
    public void create(TokenModel token, CallBack<TokenModel> cb) {
        Log.i(TAG, "Create del token");
        try{
            tokensReference.child(token.getId()).setValue(token);
            Log.d(TAG, "TokenAssociated: " + token.getId());
            cb.onSuccess(new Event.Success<>(token));
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    @Override
    public void update(TokenModel tokenModel, CallBack<TokenModel> cb) {
        tokensReference.child(tokenModel.getId()).setValue(tokenModel).addOnSuccessListener(task->{
            Log.d(TAG, "Token " + tokenModel.getId() + " modificado correctamente");
            cb.onSuccess(new Event.Success<>(tokenModel));
        })      .addOnFailureListener(e->{
            Log.d(TAG, "Error al modificar el token", e);
            cb.onError(new Event.Error<>(e));
        });
    }
}
