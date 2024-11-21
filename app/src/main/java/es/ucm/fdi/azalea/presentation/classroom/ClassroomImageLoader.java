/*
package es.ucm.fdi.azalea.presentation.classroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ClassroomImageLoader extends AsyncTaskLoader<Bitmap> {

    // atributos
    // para completar la URL
    private final String queryString;

    // para almacenar el resultado
    private Bitmap result;

    public ClassroomImageLoader(@NonNull Context context, String queryString) {
        super(context);
        this.queryString = queryString;
    }

    @Override
    // comprueba datos de caché
    public void onStartLoading(){
        forceLoad();
    }

    @Nullable
    @Override
    // se cargan los datos de la API
    public Bitmap loadInBackground() {
        try {
            getProfileImage(queryString);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // obtiene de la API la imagen de perfil
    private void getProfileImage(String queryString) throws IOException{
        URL url = completeURL(queryString);

            try {
                // se crea la conexion HTTP
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // se establece la conexion
                urlConnection.setDoInput(true);
                urlConnection.connect();

                // se obtiene el input
                try (InputStream input = urlConnection.getInputStream()) {

                    // y se almacena en el resultado
                    result = BitmapFactory.decodeStream(input);

                } finally {
                    urlConnection.disconnect();
                }

            } catch(Exception e){
                // se maneja la excepcion
                e.printStackTrace();
            }
    }

    // completa la URL correctamente para obtener la imagen de perfil de UI Avatars API
    private URL completeURL(String queryString) throws IOException {
        // constantes para determinar la URL
        String URL_BASE = "https://ui-avatars.com/api/?";
        String URL_NAME = "name=";
        String URL_BACKGROUND_COLOR = "&background=random";

        URL url;

        // se crea la URL
        url = new URL(URL_BASE + URL_NAME + queryString + URL_BACKGROUND_COLOR);
        return url;
    }
}
*/