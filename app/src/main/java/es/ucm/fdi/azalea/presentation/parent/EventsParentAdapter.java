package es.ucm.fdi.azalea.presentation.parent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;

public class EventsParentAdapter extends RecyclerView.Adapter<EventsParentAdapter.ViewHolder> {

    private List<EventModel> mEventsData;
    private final LayoutInflater mInflater;

    public EventsParentAdapter(List<EventModel> EventsInfoArrayList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mEventsData = EventsInfoArrayList;
    }

    public void setEventsData(List<EventModel> events) {
        this.mEventsData = events;
    }


    @NonNull
    @Override
    public EventsParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.teacher_calendar_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsParentAdapter.ViewHolder holder, int position) {

        EventModel event = mEventsData.get(position);
        holder.title.setText(event.getTitle()!=null ? event.getTitle() : "");
        holder.date.setText(event.getDate()!=null ? event.getDate() : "");
        holder.time.setText(event.getTime()!=null ? event.getTime() : "");
        holder.location.setText(event.getLocation()!=null ? event.getLocation() : "");
        holder.description.setText(event.getDescription()!=null ? event.getDescription() : "");
    }

    @Override
    public int getItemCount() {
        return mEventsData != null ? mEventsData.size() : 0;
    }

    // Clase interna ViewHolder para el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, location, description, title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitleTextView);
            date = itemView.findViewById(R.id.eventDateTextView);
            time = itemView.findViewById(R.id.eventTimeTextView);
            location = itemView.findViewById(R.id.eventLocationTextView);
            description = itemView.findViewById(R.id.eventDescriptionTextView);
        }
    }
}
