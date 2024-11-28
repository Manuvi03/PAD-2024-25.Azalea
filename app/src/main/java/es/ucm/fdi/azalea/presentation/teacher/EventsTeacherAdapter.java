package es.ucm.fdi.azalea.presentation.teacher;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;

public class EventsTeacherAdapter extends RecyclerView.Adapter<EventsTeacherAdapter.ViewHolder> {

    private List<EventModel> mEventsData;
    private final LayoutInflater mInflater;
    private final OnEventButtonClickListener modifyButtonClickListener;


    public interface OnEventButtonClickListener {
        void onButtonClick(EventModel event);
    }

    public EventsTeacherAdapter(List<EventModel> EventsInfoArrayList, Context context, OnEventButtonClickListener buttonClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mEventsData = EventsInfoArrayList;
        this.modifyButtonClickListener = buttonClickListener;
    }

    public void setEventsData(List<EventModel> events) {
        this.mEventsData = events;
    }


    @NonNull
    @Override
    public EventsTeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.teacher_calendar_recyclerview_item, parent, false);
        return new EventsTeacherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsTeacherAdapter.ViewHolder holder, int position) {

        EventModel event = mEventsData.get(position);
        holder.title.setText(event.getTitle() != null ? event.getTitle() : "");
        holder.date.setText(event.getDate() != null ? event.getDate() : "");
        holder.time.setText(event.getTime() != null ? event.getTime() : "");
        holder.location.setText(event.getLocation() != null ? event.getLocation() : "");
        holder.description.setText(event.getDescription() != null ? event.getDescription() : "");
        Log.d("EventsTeacherAdapter", "Event: " + event);
        holder.modifyButton.setOnClickListener(v -> modifyButtonClickListener.onButtonClick(event));

    }

    @Override
    public int getItemCount() {
        return mEventsData != null ? mEventsData.size() : 0;
    }

    // Clase interna ViewHolder para el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, location, description, title;
        Button modifyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitleTextView);
            date = itemView.findViewById(R.id.eventDateTextView);
            time = itemView.findViewById(R.id.eventTimeTextView);
            location = itemView.findViewById(R.id.eventLocationTextView);
            description = itemView.findViewById(R.id.eventDescriptionTextView);
            modifyButton = itemView.findViewById(R.id.eventActionButton);
        }
    }
}
