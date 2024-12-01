package es.ucm.fdi.azalea.presentation.profilepicture;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import es.ucm.fdi.azalea.R;

public class PicassoSetter {
    static public void setProfilePicture(String path, ImageView imageView){
        Picasso.get()
                .load(path)
                .placeholder(R.drawable.teacher_classroom_student_image)
                .error(R.drawable.teacher_classroom_student_image_error)
                .into(imageView);
    }
}
