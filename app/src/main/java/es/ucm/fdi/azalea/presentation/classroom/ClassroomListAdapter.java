package es.ucm.fdi.azalea.presentation.classroom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class ClassroomListAdapter extends RecyclerView.Adapter<ClassroomListAdapter.ViewHolder>{

    // atributps
    private List<StudentModel> mStudentsData;
    private final LayoutInflater mInflater;

    public ClassroomListAdapter(List<StudentModel> studentInfoList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mStudentsData = studentInfoList;
    }

    public void setStudentsData(List<StudentModel> students) {
        this.mStudentsData = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout para cada elemento de la vista
        View view = mInflater.inflate(R.layout.teacher_classroom_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Configuración de la UI para cada elemento
        StudentModel studentModel = mStudentsData.get(position);
        holder.name.setText(studentModel.getName() + " " + studentModel.getSurnames());

        /*
        PARA QUITAR EL ERROR, poner en strings:
        <string name="student_name">%1$s %2$s</string>

        y luego aqui
        getString(R.string.student_name, studentModel.getName(), studentModel.getSurnames());
         */

        // CONFIGURAR LA IMAGEN DE ALGUNA FORMA QUE SEA LA PRIMERA LETRA DEL NOMBRE

        /*
        // listener para acceder a la informacion de cada alumno, abriendo el nuevo Fragment
        holder.itemView.setOnClickListener(view -> {
            findNavController().navigate(R.id.action...);
            view.getContext().startActivity(intent);
        });*/
    }

    @Override
    public int getItemCount() {
        // devuelve el tamaño de la lista de estudiantes
        return mStudentsData != null ? mStudentsData.size() : 0;
    }

    // clase interna ViewHolder para el RecyclerView, se enlaza con la vista de cada elemento
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView name;

        public ViewHolder(View view) {
            super(view);
            profileImage = view.findViewById(R.id.teacher_classroom_student_image);
            name = view.findViewById(R.id.teacher_classroom_student_name);
        }
    }
}
