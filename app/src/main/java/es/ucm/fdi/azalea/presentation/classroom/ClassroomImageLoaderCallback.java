/*
package es.ucm.fdi.azalea.presentation.classroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import es.ucm.fdi.azalea.R;

public class ClassroomImageLoaderCallback implements LoaderManager.LoaderCallbacks<Bitmap>{

    private final Context context;
    public static final String EXTRA_QUERY = "queryString";

    public ClassroomImageLoaderCallback(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    // crea el Loader para el nombre dado
    public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
        assert args != null;
        return new ClassroomImageLoader(context, args.getString(EXTRA_QUERY));
    }

    @Override
    // devuelve la imagen cargada tras realizar la busqueda
    public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
        // a través del Context, se envía la información al classroom fragment
        if (data != null) {
            Log.i("JSON", data.toString());
            FragmentActivity activity = (FragmentActivity) context;
            ClassroomFragment cf = (ClassroomFragment) activity.getSupportFragmentManager()
                    .findFragmentById(R.id.classroom_fragment);
            cf.updateProfileImage(data);
        } else {
            Log.e("BookLoaderCallbacks", "Error: data is null in onLoadFinished");
            FragmentActivity activity = (FragmentActivity) context;
            ClassroomFragment cf = (ClassroomFragment) activity.getSupportFragmentManager()
                    .findFragmentById(R.id.classroom_fragment);
            cf.updateProfileImage(null);
        }
    }

    @Override
    // vacío
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {}
}
*/