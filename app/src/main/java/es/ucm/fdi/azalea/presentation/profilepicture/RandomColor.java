package es.ucm.fdi.azalea.presentation.profilepicture;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.util.Random;

import es.ucm.fdi.azalea.R;

public class RandomColor {

    // la semilla random solamente se crea una vez para que sea la misma
    private static Random r;

    static private Random getRandom(){
        if(r == null) r = new Random(11122004);
        return r;
    }

    private enum AppColors {
        Cyclomen, French_Rose, Amaranth_Purple, Lapis_Lazuli
    }

    // genera un color aleatorio de los seleccionados entre los de la app
    static public String generateAppRandomColor(Context context){

        // se genera un numero aleatorio dentro del rango de colores de la app
        int n = Math.abs(getRandom().nextInt() % AppColors.values().length);

        // se devuelve el color en esa posicion
        return mapColorToHex(AppColors.values()[n], context);
    }

    // mapea un color a su valor hexadecimal
    static private String mapColorToHex(AppColors color, Context context){
        int colorHex;
        switch(color){
            case Cyclomen:
                colorHex = ContextCompat.getColor(context, R.color.Cyclomen);
                break;
            case French_Rose:
                colorHex = ContextCompat.getColor(context,R.color.French_Rose);
                break;
            case Amaranth_Purple:
                colorHex = ContextCompat.getColor(context,R.color.Amaranth_Purple);
                break;
            case Lapis_Lazuli:
                colorHex = ContextCompat.getColor(context,R.color.Lapis_Lazuli);
                break;
            default:
                colorHex = ContextCompat.getColor(context,R.color.Cyclomen);
                break;
        }
        return String.format("%06X", (0xFFFFFF & colorHex));
    }
}
