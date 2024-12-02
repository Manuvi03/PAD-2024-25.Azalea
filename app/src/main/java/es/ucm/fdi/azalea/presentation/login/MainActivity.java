 package es.ucm.fdi.azalea.presentation.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jakewharton.threetenabp.AndroidThreeTen;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.presentation.teacher.TeacherActivity;

 public class MainActivity extends AppCompatActivity {

     private final static String TAG = "MainActivity";

     private boolean previousNetworkUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.d(TAG, "onCreate: ");
        AndroidThreeTen.init(this); //inicializacion de la libreria de fecha



        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        if(Settings.System.canWrite(this))
            setupNetwork();
        // restaura el fragmento que estaba activo antes de cambiar de configuracion
        if (savedInstanceState != null) {
            Fragment restoredFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            if (restoredFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment_container, restoredFragment)
                        .commit();
            }
        }

        else replaceFragment(LoginFragment.class);

        generateFCMTokenFirst();
    }
     @Override
     protected void onSaveInstanceState(@NonNull Bundle outState) {
         super.onSaveInstanceState(outState);
         // Guarda el fragmento actual (si usas fragmentos).
         if (!getSupportFragmentManager().getFragments().isEmpty()) {
             Fragment currentFragment = getSupportFragmentManager().getFragments().get(0);
             getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
         }
     }
     private void setupNetwork(){
         NetworkRequest networkRequest = new NetworkRequest.Builder().addCapability(
                 NetworkCapabilities.NET_CAPABILITY_INTERNET)
                         .build();
         ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
             @Override
             public void onAvailable(@NonNull Network network) {
                 super.onAvailable(network);
                 Log.d(TAG,"Hay conexion");
                 toggleNoInternetBar(true);

             }

             @Override
             public void onLost(@NonNull Network network) {
                 super.onLost(network);
                 Log.d(TAG,"no hay conexion");
                 toggleNoInternetBar(false);
             }

             @Override
             public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                 super.onCapabilitiesChanged(network, networkCapabilities);
                 final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
             }
         };

         ConnectivityManager connectivityManager =
                 (ConnectivityManager) getSystemService(ConnectivityManager.class);
         connectivityManager.requestNetwork(networkRequest, networkCallback);
     }

     private void toggleNoInternetBar(boolean display){
         if (display) {

             runOnUiThread(() -> {
                 // Actualiza la UI o realiza las acciones necesarias
                 if(!previousNetworkUpdate){
                     Toast.makeText(MainActivity.this, R.string.conexion_recuperada, Toast.LENGTH_SHORT).show();
                 }
                 previousNetworkUpdate = true;

             });

         } else {


             runOnUiThread(() -> {
                 Toast.makeText(this, R.string.conexion_perdida, Toast.LENGTH_SHORT).show();
                 previousNetworkUpdate = false;
             });
         }

     }


     private void replaceFragment(Class<? extends androidx.fragment.app.Fragment> c){
         getSupportFragmentManager().beginTransaction()
                 .setReorderingAllowed(true)
                 .replace(R.id.login_fragment_container, c, null).commit();

     }

    private void generateFCMTokenFirst() { //TODO si da tiempo preguntar aqui tambien por activar las notis
        //SharedPreferences preferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);

        //Devuelve true si ya existe lo q se esta buscando
        //boolean tokenGenerated = preferences.getBoolean("tokenGenerated", false);

        //if(!tokenGenerated){
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Obtener el token generado
                    String token = task.getResult();
                    Log.d("IDTOKEN", "Token generado: " + token);

                    // Aquí creamos la instancia del token en la RTDB
                    LoginViewModel loginViewModel = new LoginViewModel();

                    loginViewModel.createToken(token);

                });


    }

 }