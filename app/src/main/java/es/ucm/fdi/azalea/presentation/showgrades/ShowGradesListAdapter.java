package es.ucm.fdi.azalea.presentation.showgrades;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.MarkModel;

public class ShowGradesListAdapter extends RecyclerView.Adapter<ShowGradesListAdapter.ViewHolder>{

    // constantes
    private final String TAG = "ShowGradesListAdapter";

    // atributos
    private List<MarkModel> mMarksData;           // todas las notas del estudiante
    private final LayoutInflater mInflater;
    private final Context context;

    public ShowGradesListAdapter(List<MarkModel> studentInfoList, Context context) {
        Log.d(TAG, "Se crea el ShowGradesListAdapter");
        this.mInflater = LayoutInflater.from(context);
        this.mMarksData = studentInfoList;
        this.context = context;
    }

    public void setStudentsData(List<MarkModel> students) {
        this.mMarksData = students;
    }

    @NonNull
    @Override
    public ShowGradesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout para cada elemento de la vista
        View view = mInflater.inflate(R.layout.show_grades_list_element, parent, false);
        return new ShowGradesListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShowGradesListAdapter.ViewHolder holder, int position) {
        // Configuración de la UI para cada elemento
        MarkModel markModel = mMarksData.get(position);

        // la asignatura
        holder.subject.setText(markModel.getSubject());

        // su calificacion
        if ((markModel.getMark() < 5)) holder.mark.setTextColor(ContextCompat.getColor(context, R.color.Imperial_Red));
        else holder.mark.setTextColor(ContextCompat.getColor(context, R.color.Persian_Green));

        holder.mark.setText(markModel.getMark() + "");
    }

    @Override
    public int getItemCount() {
        // devuelve el tamaño de la lista de estudiantes
        return mMarksData != null ? mMarksData.size() : 0;
    }

    // clase interna ViewHolder para el RecyclerView, se enlaza con la vista de cada elemento
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, mark;

        public ViewHolder(View view) {
            super(view);
            subject = view.findViewById(R.id.show_grades_subject);
            mark = view.findViewById(R.id.show_grades_mark);
        }
    }


}